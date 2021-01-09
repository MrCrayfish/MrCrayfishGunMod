package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.AimTracker;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModEffects;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GrenadeItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.item.attachment.IBarrel;
import com.mrcrayfish.guns.item.attachment.impl.Barrel;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class GunRenderer
{
    private static final ResourceLocation MUZZLE_FLASH_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_flash.png");
    private static final double ZOOM_TICKS = 4;

    private Random random = new Random();
    private Set<Integer> entityIdForMuzzleFlash = new HashSet<>();
    private Set<Integer> entityIdForDrawnMuzzleFlash = new HashSet<>();
    private Map<Integer, Float> entityIdToRandomValue = new HashMap<>();

    private double aimProgress;
    private double lastAimProgress;
    private double normalisedAimProgress;
    private double recoilNormal;
    private double recoilAngle;
    private float recoilRandom;

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;

    private int sprintTransition;
    private int prevSprintTransition;
    private int sprintCooldown;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if(this.isZooming(Minecraft.getInstance().player) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.getModules().getZoom() != null)
                    {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if(scope != null)
                        {
                            newFov -= scope.getAdditionalZoom();
                        }
                        event.setNewfov(newFov + (1.0F - newFov) * (1.0F - ((float) aimProgress / (float) ZOOM_TICKS)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        this.prevSprintTransition = this.sprintTransition;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.player.isSprinting() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.SHOOTING) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING) && !ClientHandler.isAiming() && this.sprintCooldown == 0)
        {
            if(this.sprintTransition < 5)
            {
                this.sprintTransition++;
            }
        }
        else if(this.sprintTransition > 0)
        {
            this.sprintTransition--;
        }

        if(this.sprintCooldown > 0)
        {
            this.sprintCooldown--;
        }

        this.updateAimProgress();
    }

    private void updateAimProgress()
    {
        this.lastAimProgress = this.aimProgress;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
        {
            return;
        }

        if(this.isZooming(player) && !SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.aimProgress < ZOOM_TICKS)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.aimProgress += speed;
                if(this.aimProgress > ZOOM_TICKS)
                {
                    this.aimProgress = (int) ZOOM_TICKS;
                }
            }
        }
        else
        {
            if(this.aimProgress > 0)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.aimProgress -= speed;
                if(this.aimProgress < 0)
                {
                    this.aimProgress = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        this.prevReloadTimer = this.reloadTimer;

        this.updateMuzzleFlash();

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
        {
            this.updateReloadTimer(player);
        }
    }

    @SubscribeEvent
    public void onClientPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && event.side == LogicalSide.CLIENT)
        {
            this.tickOverrideModel(event.player);
        }
    }

    private void updateMuzzleFlash()
    {
        this.entityIdForMuzzleFlash.removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdToRandomValue.keySet().removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdForDrawnMuzzleFlash.clear();
        this.entityIdForDrawnMuzzleFlash.addAll(this.entityIdForMuzzleFlash);
    }

    private void updateReloadTimer(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.startReloadTick == -1)
            {
                this.startReloadTick = player.ticksExisted + 5;
            }
            if(this.reloadTimer < 5)
            {
                this.reloadTimer++;
            }
        }
        else
        {
            if(this.startReloadTick != -1)
            {
                this.startReloadTick = -1;
            }
            if(this.reloadTimer > 0)
            {
                this.reloadTimer--;
            }
        }
    }

    private void tickOverrideModel(PlayerEntity player)
    {
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            IOverrideModel model = ModelOverrides.getModel(heldItem);
            if(model != null)
            {
                model.tick(player);
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        this.normalisedAimProgress = (this.lastAimProgress + (this.aimProgress - this.lastAimProgress) * (this.lastAimProgress == 0 || this.lastAimProgress == ZOOM_TICKS ? 0.0 : event.getPartialTicks())) / ZOOM_TICKS;
        if(this.normalisedAimProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        MatrixStack matrixStack = event.getMatrixStack();
        if(mc.gameSettings.viewBobbing && mc.getRenderViewEntity() instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) mc.getRenderViewEntity();
            float deltaDistanceWalked = playerentity.distanceWalkedModified - playerentity.prevDistanceWalkedModified;
            float distanceWalked = -(playerentity.distanceWalkedModified + deltaDistanceWalked * event.getPartialTicks());
            float cameraYaw = MathHelper.lerp(event.getPartialTicks(), playerentity.prevCameraYaw, playerentity.cameraYaw);

            /* Reverses the original bobbing rotations and translations so it can be controlled */
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-(Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F)));
            matrixStack.translate((double) -(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F), (double) -(-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)), 0.0D);

            /* The new controlled bobbing */
            double invertZoomProgress = 1.0 - this.normalisedAimProgress;
            matrixStack.translate((double) (MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, (double) (-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)) * invertZoomProgress, 0.0D);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees((MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F) * (float) invertZoomProgress));
            matrixStack.rotate(Vector3f.XP.rotationDegrees((Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F) * (float) invertZoomProgress));
        }

        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if(event.getHand() == Hand.OFF_HAND)
        {
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setCanceled(true);
                return;
            }
        }

        if(!(heldItem.getItem() instanceof GunItem))
        {
            return;
        }

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        ItemStack overrideModel = ItemStack.EMPTY;
        if(heldItem.getTag() != null)
        {
            if(heldItem.getTag().contains("Model", Constants.NBT.TAG_COMPOUND))
            {
                overrideModel = ItemStack.read(heldItem.getTag().getCompound("Model"));
            }
        }

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(overrideModel.isEmpty() ? heldItem : overrideModel);
        float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
        float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
        float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
        float translateX = model.getItemCameraTransforms().firstperson_right.translation.getX();
        float translateY = model.getItemCameraTransforms().firstperson_right.translation.getY();
        float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ();

        matrixStack.push();

        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);

        if(this.normalisedAimProgress > 0 && modifiedGun.canAimDownSight())
        {
            if(event.getHand() == Hand.MAIN_HAND)
            {
                double xOffset = 0.0;
                double yOffset = 0.0;
                double zOffset = 0.0;
                Scope scope = Gun.getScope(heldItem);

                /* Creates the required offsets to position the scope into the middle of the screen. */
                if(modifiedGun.canAttachType(IAttachment.Type.SCOPE) && scope != null)
                {
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getScope();
                    xOffset = -translateX + scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - scope.getViewFinderOffset() * scaleZ * scaledPos.getScale();
                }
                else if(modifiedGun.getModules().getZoom() != null)
                {
                    xOffset = -translateX + modifiedGun.getModules().getZoom().getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - modifiedGun.getModules().getZoom().getYOffset()) * 0.0625 * scaleY;
                    zOffset = -translateZ + modifiedGun.getModules().getZoom().getZOffset() * 0.0625 * scaleZ;
                }

                /* Controls the direction of the following translations, changes depending on the main hand. */
                float side = right ? 1.0F : -1.0F;
                double transition = 1.0 - Math.pow(1.0 - this.normalisedAimProgress, 2);

                /* Reverses the original first person translations */
                matrixStack.translate(-0.56 * side * transition, 0.52 * transition, 0);

                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                matrixStack.translate(xOffset * side * transition, yOffset * transition, zOffset * transition);
            }
            else
            {
                /* Makes the off hand item move out of view */
                matrixStack.translate(0, -1 * this.normalisedAimProgress, 0);
            }
        }

        /* Applies equip progress animation translations */
        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        matrixStack.translate(0, equipProgress * -0.6F, 0);

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;
        Entity entity = Minecraft.getInstance().player;
        Objects.requireNonNull(entity);
        int blockLight = entity.isBurning() ? 15 : entity.world.getLightFor(LightType.BLOCK, new BlockPos(entity.getEyePosition(event.getPartialTicks())));
        blockLight += (this.entityIdForMuzzleFlash.contains(entity.getEntityId()) ? 3 : 0);
        int packedLight = LightTexture.packLight(blockLight, entity.world.getLightFor(LightType.SKY, new BlockPos(entity.getEyePosition(event.getPartialTicks()))));

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        this.renderReloadArm(matrixStack, event.getBuffers(), event.getLight(), heldItem, hand);

        /* Translate the item position based on the hand side */
        int offset = right ? 1 : -1;
        matrixStack.translate(0.56 * offset, -0.52, -0.72);

        /* Applies recoil and reload rotations */
        this.applySprinting(modifiedGun, hand, matrixStack, event.getPartialTicks());
        this.applyRecoil(matrixStack, heldItem, modifiedGun);
        this.applyReload(matrixStack, event.getPartialTicks());

        /* Renders the first persons arms from the grip type of the weapon */
        matrixStack.push();
        matrixStack.translate(-0.56 * offset, 0.52, 0.72);
        modifiedGun.getGeneral().getGripType().getHeldAnimation().renderFirstPersonArms(Minecraft.getInstance().player, hand, heldItem, matrixStack, event.getBuffers(), event.getLight(), event.getPartialTicks());
        matrixStack.pop();

        /* Renders the weapon */
        ItemCameraTransforms.TransformType transformType = right ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        this.renderWeapon(Minecraft.getInstance().player, heldItem, transformType, event.getMatrixStack(), event.getBuffers(), packedLight, event.getPartialTicks());

        matrixStack.pop();
    }

    private void applySprinting(Gun modifiedGun, HandSide hand, MatrixStack matrixStack, float partialTicks)
    {
        if(modifiedGun.getGeneral().getGripType().getHeldAnimation().canApplySprintingAnimation())
        {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;
            float transition = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
            transition = (float) Math.sin((transition * Math.PI) / 2);
            matrixStack.translate(-0.25 * leftHanded * transition, -0.1 * transition, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45F * leftHanded * transition));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-25F * transition));
        }
    }

    private void applyReload(MatrixStack matrixStack, float partialTicks)
    {
        float reloadProgress = (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5.0F;
        matrixStack.translate(0, 0.35 * reloadProgress, 0);
        matrixStack.translate(0, 0, -0.1 * reloadProgress);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(45F * reloadProgress));
    }

    private void applyRecoil(MatrixStack matrixStack, ItemStack item, Gun gun)
    {
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldown = tracker.getCooldown(item.getItem(), Minecraft.getInstance().getRenderPartialTicks());
        cooldown = cooldown >= gun.getGeneral().getRecoilDurationOffset() ? (cooldown - gun.getGeneral().getRecoilDurationOffset()) / (1.0F - gun.getGeneral().getRecoilDurationOffset()) : 0.0F;
        if(cooldown >= 0.8)
        {
            float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            this.recoilNormal = 1 - (--amount) * amount * amount * amount;
        }
        else
        {
            float amount = (cooldown / 0.8F);
            this.recoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
        }

        this.recoilAngle = gun.getGeneral().getRecoilAngle();

        float kickReduction = 1.0F - GunModifierHelper.getKickReduction(item);
        float recoilReduction = 1.0F - GunModifierHelper.getRecoilModifier(item);
        double kick = gun.getGeneral().getRecoilKick() * 0.0625 * this.recoilNormal * this.getAdsRecoilReduction(gun);
        float recoilLift = (float) (gun.getGeneral().getRecoilAngle() * this.recoilNormal) * (float) this.getAdsRecoilReduction(gun);
        float recoilSwayAmount = (float) (2F + 1F * (1.0 - this.normalisedAimProgress));
        float recoilSway = (float) ((this.recoilRandom * recoilSwayAmount - recoilSwayAmount / 2F) * this.recoilNormal);
        matrixStack.translate(0, 0, kick * kickReduction);
        matrixStack.translate(0, 0, 0.35);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(recoilSway * recoilReduction));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(recoilSway * recoilReduction));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(recoilLift * recoilReduction));
        matrixStack.translate(0, 0, -0.35);
    }

    public double getAdsRecoilReduction(Gun gun)
    {
        return 1.0 - gun.getGeneral().getRecoilAdsReduction() * this.normalisedAimProgress;
    }

    private boolean isZooming(PlayerEntity player)
    {
        if(this.aimProgress == 0 && ClientHandler.isLookingAtInteractableBlock())
        {
            return false;
        }
        if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
        {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem)
            {
                Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                return gun.getModules() != null && gun.getModules().getZoom() != null && ClientHandler.isAiming();
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase.equals(TickEvent.Phase.START))
        {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused())
        {
            return;
        }

        PlayerEntity player = mc.player;
        if(player == null)
        {
            return;
        }

        if(Minecraft.getInstance().gameSettings.getPointOfView() != PointOfView.FIRST_PERSON)
        {
            return;
        }

        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        if(heldItem.isEmpty())
        {
            return;
        }

        if(player.isHandActive() && player.getActiveHand() == Hand.MAIN_HAND && heldItem.getItem() instanceof GrenadeItem)
        {
            if(!((GrenadeItem) heldItem.getItem()).canCook())
            {
                return;
            }

            int duration = player.getItemInUseMaxCount();
            if(duration >= 10)
            {
                float cookTime = 1.0F - ((float) (duration - 10) / (float) (player.getActiveItemStack().getUseDuration() - 10));
                if(cookTime > 0.0F)
                {
                    double scale = 3;
                    MainWindow window = mc.getMainWindow();
                    int i = (int) ((window.getScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

                    RenderSystem.pushMatrix();
                    {
                        RenderSystem.scaled(scale, scale, scale);
                        int progress = (int) Math.ceil((cookTime) * 17.0F) - 1;
                        MatrixStack matrixStack = new MatrixStack();
                        Screen.blit(matrixStack, j, i, 36, 94, 16, 4, 256, 256);
                        Screen.blit(matrixStack, j, i, 52, 94, progress, 4, 256, 256);
                    }
                    RenderSystem.popMatrix();

                    RenderSystem.disableBlend();
                }
            }
            return;
        }

        if(heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            if(!gun.getGeneral().isAuto())
            {
                float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
                if(coolDown > 0.0F)
                {
                    double scale = 3;
                    MainWindow window = mc.getMainWindow();
                    int i = (int) ((window.getScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);

                    RenderSystem.pushMatrix();
                    {
                        RenderSystem.scaled(scale, scale, scale);
                        int progress = (int) Math.ceil((coolDown + 0.05) * 17.0F) - 1;
                        MatrixStack matrixStack = new MatrixStack();
                        Screen.blit(matrixStack, j, i, 36, 94, 16, 4, 256, 256);
                        Screen.blit(matrixStack, j, i, 52, 94, progress, 4, 256, 256);
                    }
                    RenderSystem.popMatrix();

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderHeldItem(RenderItemEvent.Held.Pre event)
    {
        Hand hand = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHandSide() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND : event.getHandSide() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        LivingEntity entity = event.getEntity();
        ItemStack heldItem = entity.getHeldItem(hand);

        if(hand == Hand.OFF_HAND)
        {
            event.setCanceled(true);
            return;
        }

        if(heldItem.getItem() instanceof GunItem)
        {
            event.setCanceled(true);

            if(heldItem.getTag() != null)
            {
                CompoundNBT compound = heldItem.getTag();
                if(compound.contains("Scale", Constants.NBT.TAG_FLOAT))
                {
                    float scale = compound.getFloat("Scale");
                    event.getMatrixStack().scale(scale, scale, scale);
                }
            }

            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyHeldItemTransforms((PlayerEntity) entity, hand, AimTracker.getAimProgress((PlayerEntity) entity, event.getPartialTicks()), event.getMatrixStack(), event.getRenderTypeBuffer());
            this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event)
    {
        // Dirty hack to reject first person arms
        if(event.getAgeInTicks() == 0F)
        {
            event.getModelPlayer().bipedRightArm.rotateAngleX = 0;
            event.getModelPlayer().bipedRightArm.rotateAngleY = 0;
            event.getModelPlayer().bipedRightArm.rotateAngleZ = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleX = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleY = 0;
            event.getModelPlayer().bipedLeftArm.rotateAngleZ = 0;
            return;
        }

        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerModelRotation(player, model, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
        }
    }

    @SubscribeEvent
    public void onModelRender(PlayerModelEvent.Render.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack offHandStack = player.getHeldItemOffhand();
        if(offHandStack.getItem() instanceof GunItem)
        {
            switch(player.getPrimaryHand().opposite())
            {
                case LEFT:
                    event.getModelPlayer().leftArmPose = BipedModel.ArmPose.EMPTY;
                    break;
                case RIGHT:
                    event.getModelPlayer().rightArmPose = BipedModel.ArmPose.EMPTY;
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(PlayerModelEvent.Render.Post event)
    {
        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemOffhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            matrixStack.push();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if(gun.getGeneral().getGripType().getHeldAnimation().applyOffhandTransforms(player, event.getModelPlayer(), heldItem, matrixStack, event.getPartialTicks()))
            {
                IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());
            }
            matrixStack.pop();
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderItemFrame(RenderItemEvent.ItemFrame.Pre event)
    {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    public boolean renderWeapon(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;
            if(stack.getTag() != null)
            {
                if(stack.getTag().contains("Model", Constants.NBT.TAG_COMPOUND))
                {
                    model = ItemStack.read(stack.getTag().getCompound("Model"));
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType);

            this.renderMuzzleFlash(entity, matrixStack, stack, transformType);
            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);

            matrixStack.pop();
            return true;
        }
        return false;
    }

    private void renderGun(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(ModelOverrides.hasModel(stack))
        {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if(model != null)
            {
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
            }
        }
        else
        {
            RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
        }
    }

    private void renderAttachments(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
            CompoundNBT gunTag = ItemStackUtil.createTagCompound(stack);
            CompoundNBT attachments = gunTag.getCompound("Attachments");
            for(String tagKey : attachments.keySet())
            {
                IAttachment.Type type = IAttachment.Type.byTagKey(tagKey);
                if(gun.canAttachType(type))
                {
                    ItemStack attachmentStack = Gun.getAttachment(type, stack);
                    if(!attachmentStack.isEmpty())
                    {
                        Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                        if(positioned != null)
                        {
                            matrixStack.push();
                            double displayX = positioned.getXOffset() * 0.0625;
                            double displayY = positioned.getYOffset() * 0.0625;
                            double displayZ = positioned.getZOffset() * 0.0625;
                            matrixStack.translate(displayX, displayY, displayZ);
                            matrixStack.translate(0, -0.5, 0);
                            matrixStack.scale((float) positioned.getScale(), (float) positioned.getScale(), (float) positioned.getScale());

                            IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                            if(model != null)
                            {
                                model.render(partialTicks, transformType, attachmentStack, stack, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            }
                            else
                            {
                                RenderUtil.renderModel(attachmentStack, stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            }

                            matrixStack.pop();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(LivingEntity entity, MatrixStack matrixStack, ItemStack weapon, ItemCameraTransforms.TransformType transformType)
    {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if(modifiedGun.getDisplay().getFlash() == null)
        {
            return;
        }

        if(transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
        {
            if(this.entityIdForMuzzleFlash.contains(entity.getEntityId()))
            {
                float randomValue = this.entityIdToRandomValue.get(entity.getEntityId());
                this.drawMuzzleFlash(matrixStack, weapon, modifiedGun, randomValue, randomValue >= 0.5F);
            }
        }
    }

    private void drawMuzzleFlash(MatrixStack matrixStack, ItemStack weapon, Gun modifiedGun, float random, boolean flip)
    {
        matrixStack.push();

        Gun.Positioned muzzleFlash = modifiedGun.getDisplay().getFlash();
        if(muzzleFlash == null)
            return;

        double displayX = muzzleFlash.getXOffset() * 0.0625;
        double displayY = muzzleFlash.getYOffset() * 0.0625;
        double displayZ = muzzleFlash.getZOffset() * 0.0625;
        matrixStack.translate(displayX, displayY, displayZ);
        matrixStack.translate(0, -0.5, 0);

        ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, weapon);
        if(!barrelStack.isEmpty() && barrelStack.getItem() instanceof IBarrel)
        {
            Barrel barrel = ((IBarrel) barrelStack.getItem()).getProperties();
            Gun.ScaledPositioned positioned = modifiedGun.getModules().getAttachments().getBarrel();
            if(positioned != null)
            {
                matrixStack.translate(0, 0, -barrel.getLength() * 0.0625 * positioned.getScale());
            }
        }

        matrixStack.scale(0.5F, 0.5F, 0.0F);

        double partialSize = modifiedGun.getDisplay().getFlash().getSize() / 5.0;
        float size = (float) (modifiedGun.getDisplay().getFlash().getSize() - partialSize + partialSize * random);
        size = (float) GunModifierHelper.getMuzzleFlashSize(weapon, size);
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(360F * random));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(flip ? 180F : 0F));
        matrixStack.translate(-size / 2, -size / 2, 0);

        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.disableCull();
        Minecraft.getInstance().getTextureManager().bindTexture(MUZZLE_FLASH_TEXTURE);

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
        buffer.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
        buffer.pos(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
        buffer.pos(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
        buffer.pos(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();
        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.defaultAlphaFunc();

        matrixStack.pop();
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    private void renderReloadArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, ItemStack stack, HandSide hand)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || mc.player.ticksExisted < this.startReloadTick || this.reloadTimer != 5)
        {
            return;
        }

        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        Item item = ForgeRegistries.ITEMS.getValue(gun.getProjectile().getItem());
        if(item == null)
        {
            return;
        }

        matrixStack.push();

        float interval = GunEnchantmentHelper.getReloadInterval(stack);
        float reload = ((mc.player.ticksExisted - this.startReloadTick + mc.getRenderPartialTicks()) % interval) / interval;
        float percent = 1.0F - reload;
        if(percent >= 0.5F)
        {
            percent = 1.0F - percent;
        }
        percent *= 2F;
        percent = percent < 0.5 ? 2 * percent * percent : -1 + (4 - 2 * percent) * percent;

        int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(-2.75 * side * 0.0625, -0.5625, -0.5625);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStack.translate(0, -0.35 * (1.0 - percent), 0);
        matrixStack.translate(side * 0.0625, 0, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(35F * -side));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-75F * percent));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(mc.player, hand.opposite(), matrixStack, buffer, light);

        if(reload < 0.5F)
        {
            matrixStack.push();
            matrixStack.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            RenderUtil.renderModel(new ItemStack(item), ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY);
            matrixStack.pop();
        }

        matrixStack.pop();
    }

    /**
     *
     * @param sensitivity
     * @return
     */
    public double applyZoomSensitivity(double sensitivity)
    {
        float additionalAdsSensitivity = 1.0F;
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if(this.isZooming(Minecraft.getInstance().player) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.getModules().getZoom() != null)
                    {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if(scope != null)
                        {
                            newFov -= scope.getAdditionalZoom();
                        }
                        additionalAdsSensitivity = MathHelper.clamp(1.0F - (1.0F / newFov) / 10F, 0.0F, 1.0F);
                    }
                }
            }
        }
        return sensitivity * (1.0 - (1.0 - GunMod.getOptions().getAdsSensitivity()) * this.normalisedAimProgress) * additionalAdsSensitivity;
    }

    /**
     * A temporary hack to get the equip progress until Forge fixes the issue.
     * @return
     */
    private float getEquipProgress(float partialTicks)
    {
        if(this.equippedProgressMainHandField == null)
        {
            this.equippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187469_f");
            this.equippedProgressMainHandField.setAccessible(true);
        }
        if(this.prevEquippedProgressMainHandField == null)
        {
            this.prevEquippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187470_g");
            this.prevEquippedProgressMainHandField.setAccessible(true);
        }
        FirstPersonRenderer firstPersonRenderer = Minecraft.getInstance().getFirstPersonRenderer();
        try
        {
            float equippedProgressMainHand = (float) this.equippedProgressMainHandField.get(firstPersonRenderer);
            float prevEquippedProgressMainHand = (float) this.prevEquippedProgressMainHandField.get(firstPersonRenderer);
            return 1.0F - MathHelper.lerp(partialTicks, prevEquippedProgressMainHand, equippedProgressMainHand);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return 0.0F;
    }

    public void shoot()
    {
        this.recoilRandom = this.random.nextFloat();
        this.sprintTransition = 0;
        this.sprintCooldown = 5;
    }

    public void showMuzzleFlash()
    {
        this.showMuzzleFlashForPlayer(Minecraft.getInstance().player.getEntityId());
    }

    public void showMuzzleFlashForPlayer(int entityId)
    {
        this.entityIdForMuzzleFlash.add(entityId);
        this.entityIdToRandomValue.put(entityId, this.random.nextFloat());
    }

    public double getNormalisedAimProgress()
    {
        return this.normalisedAimProgress;
    }

    public double getRecoilNormal()
    {
        return this.recoilNormal;
    }

    public double getRecoilAngle()
    {
        return this.recoilAngle;
    }

    public float getReloadProgress(float partialTicks)
    {
        return (this.prevReloadTimer + (this.reloadTimer - this.prevReloadTimer) * partialTicks) / 5F;
    }
}
