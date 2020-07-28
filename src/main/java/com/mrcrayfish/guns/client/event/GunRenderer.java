package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
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
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.IBarrel;
import com.mrcrayfish.guns.object.Barrel;
import com.mrcrayfish.guns.object.GripType;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Scope;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Random;

public class GunRenderer
{
    private static final ResourceLocation MUZZLE_FLASH = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_flash.png");
    private static final double ZOOM_TICKS = 4;
    public static int screenTextureId = -1;

    private Random random = new Random();
    private boolean hasDrawnMuzzleFlash = false;
    private boolean drawMuzzleFlash = false;
    private double muzzleFlashSize;
    private float muzzleFlashRoll;
    private int muzzleFlashYaw;
    private double zoomProgress;
    private double lastZoomProgress;
    public double normalZoomProgress;
    public double recoilNormal;
    public double recoilAngle;

    private int startReloadTick;
    private int reloadTimer;
    private int prevReloadTimer;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.thirdPersonView == 0)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if(this.isZooming(Minecraft.getInstance().player) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.modules.zoom != null)
                    {
                        float newFov = modifiedGun.modules.zoom.fovModifier;
                        Scope scope = Gun.getScope(heldItem);
                        if(scope != null)
                        {
                            newFov -= scope.getAdditionalZoom();
                        }
                        event.setNewfov(newFov + (1.0F - newFov) * (1.0F - ((float) zoomProgress / (float) ZOOM_TICKS)));
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

        this.lastZoomProgress = this.zoomProgress;
        if(this.hasDrawnMuzzleFlash)
        {
            this.drawMuzzleFlash = false;
        }

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
        {
            return;
        }

        if(isZooming(player) && !SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.zoomProgress < ZOOM_TICKS)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.zoomProgress += speed;
                if(this.zoomProgress > ZOOM_TICKS)
                {
                    this.zoomProgress = (int) ZOOM_TICKS;
                }
            }
        }
        else
        {
            if(this.zoomProgress > 0)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.zoomProgress -= speed;
                if(this.zoomProgress < 0)
                {
                    this.zoomProgress = 0;
                }
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END) return;

        this.prevReloadTimer = this.reloadTimer;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player != null)
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
                    this.reloadTimer -= 1;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        normalZoomProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0.0 : event.getPartialTicks())) / ZOOM_TICKS;
        if(normalZoomProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
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

            /* The new controlled bo if(scopeStack.getItem() instanceof ScopeItem)bbing */
            double invertZoomProgress = 1.0 - this.normalZoomProgress;
            matrixStack.translate((double) (MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, (double) (-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)) * invertZoomProgress, 0.0D);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees((MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F) * (float) invertZoomProgress));
            matrixStack.rotate(Vector3f.XP.rotationDegrees((Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F) * (float) invertZoomProgress));
        }

        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if(event.getHand() == Hand.OFF_HAND)
        {
            ItemStack mainHandStack = Minecraft.getInstance().player.getHeldItemMainhand();
            if(mainHandStack.getItem() instanceof GunItem)
            {
                if(((GunItem) mainHandStack.getItem()).getModifiedGun(mainHandStack).general.gripType != GripType.ONE_HANDED)
                {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if(!(heldItem.getItem() instanceof GunItem))
        {
            return;
        }

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        /* Ignores rendering the gun if the grip type doesn't allow it to be render in the offhand */
        if(event.getHand() == Hand.OFF_HAND)
        {
            return;
        }

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

        if(this.normalZoomProgress > 0 && modifiedGun.canAimDownSight())
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
                    Gun.ScaledPositioned scaledPos = modifiedGun.modules.attachments.scope;
                    xOffset = -translateX + scaledPos.xOffset * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.yOffset) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.scale;
                    zOffset = -translateZ - scaledPos.zOffset * 0.0625 * scaleZ + 0.72 - scope.getViewFinderOffset() * scaleZ * scaledPos.scale;
                }
                else if(modifiedGun.modules.zoom != null)
                {
                    xOffset = -translateX + modifiedGun.modules.zoom.xOffset * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - modifiedGun.modules.zoom.yOffset) * 0.0625 * scaleY;
                    zOffset = -translateZ + modifiedGun.modules.zoom.zOffset * 0.0625 * scaleZ;
                }

                /* Controls the direction of the following translations, changes depending on the main hand. */
                float side = right ? 1.0F : -1.0F;

                /* Reverses the original first person translations */
                matrixStack.translate(-0.56 * side * this.normalZoomProgress, 0.52 * this.normalZoomProgress, 0);

                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                matrixStack.translate(xOffset * side * this.normalZoomProgress, yOffset * this.normalZoomProgress, zOffset * normalZoomProgress);
            }
            else
            {
                /* Makes the off hand item move out of view */
                matrixStack.translate(0, -1 * this.normalZoomProgress, 0);
            }
        }

        /* Applies equip progress animation translations */
        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        matrixStack.translate(0, equipProgress * -0.6F, 0);

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        this.renderReloadArm(matrixStack, event.getBuffers(), event.getLight(), heldItem, hand);

        /* Translate the item position based on the hand side */
        matrixStack.translate(0.56, -0.52, -0.72);

        /* Applies recoil and reload rotations */
        this.applyRecoil(matrixStack, heldItem, modifiedGun);
        this.applyReload(matrixStack, event.getPartialTicks());

        /* Render offhand arm so it is holding the weapon. Only applies if it's a two handed weapon */
        matrixStack.push();
        matrixStack.translate(-(0.56F - (right ? 0.0F : 0.72F)), 0.52F, 0.72F);
        this.renderHeldArm(matrixStack, event.getBuffers(), event.getLight(), Minecraft.getInstance().player, heldItem, hand, event.getPartialTicks());
        matrixStack.pop();

        /* Renders the weapon */
        this.renderWeapon(Minecraft.getInstance().player, heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, event.getMatrixStack(), event.getBuffers(), event.getLight(), event.getPartialTicks());

        matrixStack.pop();
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
        cooldown = cooldown >= gun.general.recoilDurationOffset ? (cooldown - gun.general.recoilDurationOffset) / (1.0F - gun.general.recoilDurationOffset) : 0.0F;

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

        this.recoilAngle = gun.general.recoilAngle;

        float kickReduction = 1.0F - GunModifierHelper.getKickReduction(item);
        float recoilReduction = 1.0F - GunModifierHelper.getRecoilModifier(item);
        double kick = gun.general.recoilKick * 0.0625 * this.recoilNormal * (float) (1.0 - (gun.general.recoilAdsReduction * this.normalZoomProgress));
        float recoil = (float) (gun.general.recoilAngle * this.recoilNormal) * (float) (1.0 - (gun.general.recoilAdsReduction * this.normalZoomProgress));
        matrixStack.translate(0, 0, kick * kickReduction);
        matrixStack.translate(0, 0, 0.35);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(recoil * recoilReduction));
        matrixStack.translate(0, 0, -0.35);
    }

    private boolean isZooming(PlayerEntity player)
    {
        if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
        {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof GunItem)
            {
                Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
                return gun.modules != null && gun.modules.zoom != null && ClientHandler.isAiming();
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

        if(Minecraft.getInstance().gameSettings.thirdPersonView != 0)
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
            if(!gun.general.auto)
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
            gun.general.gripType.getHeldAnimation().applyHeldItemTransforms(hand, entity instanceof PlayerEntity ? AimTracker.getAimProgress((PlayerEntity) entity, event.getPartialTicks()) : 0.0F, event.getMatrixStack(), event.getRenderTypeBuffer());
            if(hand == Hand.MAIN_HAND)
            {
                this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
            }
        }

        if(hand == Hand.OFF_HAND)
        {
            ItemStack mainHandStack = entity.getHeldItemMainhand();
            if(!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof GunItem)
            {
                Gun mainHandGun = ((GunItem) mainHandStack.getItem()).getModifiedGun(mainHandStack);
                if(!mainHandGun.general.gripType.canRenderOffhand())
                {
                    event.setCanceled(true);
                }
                else if(heldItem.getItem() instanceof GunItem)
                {
                    Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
                    if(gun.general.gripType.canRenderOffhand())
                    {
                        this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.general.gripType.getHeldAnimation().applyPlayerModelRotation(model, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
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
            gun.general.gripType.getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimTracker.getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
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
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if(!gun.general.gripType.canRenderOffhand())
            {
                matrixStack.push();

                matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
                if(player.isCrouching())
                {
                    matrixStack.translate(0 * 0.0625, -7 * 0.0625, -5 * 0.0625);
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(30F));
                }
                else
                {
                    matrixStack.translate(0 * 0.0625, -5 * 0.0625, -2.75 * 0.0625);
                }
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(-45F));
                matrixStack.scale(0.5F, 0.5F, 0.5F);

                IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());

                matrixStack.pop();
            }
            else
            {
                matrixStack.push();
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
                if(player.isCrouching())
                {
                    matrixStack.translate(-4.5 * 0.0625, -15 * 0.0625, -4 * 0.0625);
                }
                else
                {
                    matrixStack.translate(-4.5 * 0.0625, -13 * 0.0625, 1 * 0.0625);
                }
                matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees(75F));
                matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (Math.toDegrees(event.getModelPlayer().bipedRightLeg.rotateAngleX) / 10F)));
                matrixStack.scale(0.5F, 0.5F, 0.5F);
                IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());
                matrixStack.pop();
            }
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

            if(transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            {
                this.renderMuzzleFlash(matrixStack, stack);
            }

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
                            double displayX = positioned.xOffset * 0.0625;
                            double displayY = positioned.yOffset * 0.0625;
                            double displayZ = positioned.zOffset * 0.0625;
                            matrixStack.translate(displayX, displayY, displayZ);
                            matrixStack.translate(0, -0.5, 0);
                            matrixStack.scale((float) positioned.scale, (float) positioned.scale, (float) positioned.scale);

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

    private void renderMuzzleFlash(MatrixStack matrixStack, ItemStack weapon)
    {
        if(this.drawMuzzleFlash)
        {
            matrixStack.push();

            Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
            if(modifiedGun.display.flash == null)
            {
                this.drawMuzzleFlash = false;
                matrixStack.pop();
                return;
            }

            this.hasDrawnMuzzleFlash = true;

            Gun.Positioned muzzleFlash = modifiedGun.display.flash;
            double displayX = muzzleFlash.xOffset * 0.0625;
            double displayY = muzzleFlash.yOffset * 0.0625;
            double displayZ = muzzleFlash.zOffset * 0.0625;
            matrixStack.translate(displayX, displayY, displayZ);
            matrixStack.translate(0, -0.5, 0);

            ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, weapon);
            if(!barrelStack.isEmpty() && barrelStack.getItem() instanceof IBarrel)
            {
                Barrel barrel = ((IBarrel) barrelStack.getItem()).getProperties();
                Gun.ScaledPositioned positioned = modifiedGun.getAttachmentPosition(IAttachment.Type.BARREL);
                if(positioned != null)
                {
                    matrixStack.translate(0, 0, -barrel.getLength() * 0.0625 * positioned.scale);
                }
            }

            matrixStack.scale(0.5F, 0.5F, 0.0F);

            RenderSystem.pushMatrix();
            {
                RenderSystem.multMatrix(matrixStack.getLast().getMatrix());

                RenderSystem.enableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();

                double partialSize = modifiedGun.display.flash.size / 5.0;
                double size = modifiedGun.display.flash.size - partialSize + partialSize * this.muzzleFlashSize;
                size = GunModifierHelper.getMuzzleFlashSize(weapon, size);
                RenderSystem.rotatef(360F * this.muzzleFlashRoll, 0, 0, 1);
                RenderSystem.rotatef(180F * this.muzzleFlashYaw, 1, 0, 0);
                RenderSystem.translated(-size / 2, -size / 2, 0);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                Minecraft.getInstance().getTextureManager().bindTexture(MUZZLE_FLASH);
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP);
                buffer.pos(0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
                buffer.pos(size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
                buffer.pos(size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
                buffer.pos(0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();
                tessellator.draw();

                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
            }
            RenderSystem.popMatrix();

            matrixStack.pop();
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    private void renderHeldArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, ClientPlayerEntity player, ItemStack stack, HandSide hand, float partialTicks)
    {
        matrixStack.push();

        Gun modifiedGun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        if(modifiedGun.general.gripType == GripType.TWO_HANDED)
        {
            matrixStack.translate(0, 0, -1);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

            float reloadProgress = (prevReloadTimer + (reloadTimer - prevReloadTimer) * partialTicks) / 5F;
            matrixStack.translate(0, -reloadProgress * 2, 0);

            int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
            matrixStack.translate(6 * side * 0.0625, -0.585, -0.5);

            if(Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT)
            {
                matrixStack.translate(0.03125F * -side, 0, 0);
            }

            matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * -side));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(player.getLocationSkin()));
            this.renderArm(matrixStack, builder, light, hand.opposite());
        }
        else if(modifiedGun.general.gripType == GripType.ONE_HANDED)
        {
            matrixStack.translate(0, 0, -1);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

            double centerOffset = 2.5;
            if(Minecraft.getInstance().player.getSkinType().equals("slim"))
            {
                centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
            }
            centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
            matrixStack.translate(centerOffset * 0.0625, -0.45, -1.0);

            matrixStack.rotate(Vector3f.XP.rotationDegrees(75F));
            matrixStack.scale(0.5F, 0.5F, 0.5F);

            IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(player.getLocationSkin()));
            this.renderArm(matrixStack, builder, light, hand);
        }

        matrixStack.pop();
    }

    private void renderReloadArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, ItemStack stack, HandSide hand)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || mc.player.ticksExisted < startReloadTick || reloadTimer != 5)
        {
            return;
        }

        Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
        Item item = ForgeRegistries.ITEMS.getValue(gun.projectile.item);
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
        matrixStack.translate(side * 1 * 0.0625, 0, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(35F * -side));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-75F * percent));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        IVertexBuilder builder = buffer.getBuffer(RenderType.getEntitySolid(mc.player.getLocationSkin()));
        this.renderArm(matrixStack, builder, light, hand.opposite());

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

    private void renderArm(MatrixStack matrixStack, IVertexBuilder builder, int light, HandSide hand)
    {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        Minecraft.getInstance().getTextureManager().bindTexture(clientPlayerEntity.getLocationSkin());
        PlayerRenderer renderPlayer = (PlayerRenderer) Minecraft.getInstance().getRenderManager().getRenderer(clientPlayerEntity);

        if(hand == HandSide.RIGHT)
        {
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleX = 0F;
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleY = 0F;
            renderPlayer.getEntityModel().bipedRightArm.rotateAngleZ = 0F;
            renderPlayer.getEntityModel().bipedRightArm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        }
        else
        {
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleX = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleY = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.rotateAngleZ = 0F;
            renderPlayer.getEntityModel().bipedLeftArm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void blindPlayer(TickEvent.RenderTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
        {
            return;
        }

        if(Minecraft.getInstance().player == null)
        {
            return;
        }

        EffectInstance effect = Minecraft.getInstance().player.getActivePotionEffect(ModEffects.BLINDED.get());
        if(effect != null)
        {
            // Render white screen-filling overlay at full alpha effect when duration is above threshold
            // When below threshold, fade to full transparency as duration approaches 0
            float percent = Math.min((effect.getDuration() / (float) Config.SERVER.alphaFadeThreshold.get()), 1);
            MainWindow window = Minecraft.getInstance().getMainWindow();
            MatrixStack matrixStack = new MatrixStack();
            Screen.fill(matrixStack, 0, 0, window.getWidth(), window.getHeight(), ((int) (percent * Config.SERVER.alphaOverlay.get() + 0.5) << 24) | 16777215);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderLastWorld(RenderWorldLastEvent event)
    {
        if(screenTextureId == -1)
        {
            screenTextureId = GlStateManager.genTexture();
        }
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.bindTexture(screenTextureId);
        GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight(), 0);
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
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.thirdPersonView == 0)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if(this.isZooming(Minecraft.getInstance().player) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.modules.zoom != null)
                    {
                        float newFov = modifiedGun.modules.zoom.fovModifier;
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
        return sensitivity * (1.0 - (1.0 - GunMod.getOptions().getAdsSensitivity()) * this.normalZoomProgress) * additionalAdsSensitivity;
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

    public void showMuzzleFlash()
    {
        this.drawMuzzleFlash = true;
        this.muzzleFlashSize = this.random.nextDouble();
        this.muzzleFlashRoll = this.random.nextFloat();
        this.muzzleFlashYaw = this.random.nextInt(2);
        this.hasDrawnMuzzleFlash = false;
    }
}
