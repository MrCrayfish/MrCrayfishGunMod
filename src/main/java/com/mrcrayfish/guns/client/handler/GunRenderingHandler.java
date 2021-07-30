package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GrenadeItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.item.attachment.IBarrel;
import com.mrcrayfish.guns.item.attachment.impl.Barrel;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.OptifineHelper;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.LightType;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class GunRenderingHandler
{
    private static GunRenderingHandler instance;

    public static GunRenderingHandler get()
    {
        if(instance == null)
        {
            instance = new GunRenderingHandler();
        }
        return instance;
    }
    
    public static final ResourceLocation MUZZLE_FLASH_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_flash.png");

    private Random random = new Random();
    private Set<Integer> entityIdForMuzzleFlash = new HashSet<>();
    private Set<Integer> entityIdForDrawnMuzzleFlash = new HashSet<>();
    private Map<Integer, Float> entityIdToRandomValue = new HashMap<>();

    private int sprintTransition;
    private int prevSprintTransition;
    private int sprintCooldown;

    private float offhandTranslate;
    private float prevOffhandTranslate;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;
    
    private GunRenderingHandler() {}

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        this.updateSprinting();
        this.updateMuzzleFlash();
        this.updateOffhandTranslate();
    }

    private void updateSprinting()
    {
        this.prevSprintTransition = this.sprintTransition;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.player.isSprinting() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.SHOOTING) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING) && !AimingHandler.get().isAiming() && this.sprintCooldown == 0)
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
    }

    private void updateMuzzleFlash()
    {
        this.entityIdForMuzzleFlash.removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdToRandomValue.keySet().removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdForDrawnMuzzleFlash.clear();
        this.entityIdForDrawnMuzzleFlash.addAll(this.entityIdForMuzzleFlash);
    }

    private void updateOffhandTranslate()
    {
        this.prevOffhandTranslate = this.offhandTranslate;
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        boolean down = false;
        ItemStack heldItem = mc.player.getMainHandItem();
        if(heldItem.getItem() instanceof GunItem)
        {
            Gun modifiedGun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            down = !modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem();
        }

        float direction = down ? -0.3F : 0.3F;
        this.offhandTranslate = MathHelper.clamp(this.offhandTranslate + direction, 0.0F, 1.0F);
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event)
    {
        if(!event.isClient())
            return;

        this.sprintTransition = 0;
        this.sprintCooldown = 8;

        ItemStack heldItem = event.getStack();
        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        if(modifiedGun.getDisplay().getFlash() != null)
        {
            this.showMuzzleFlashForPlayer(Minecraft.getInstance().player.getId());
        }
    }

    public void showMuzzleFlashForPlayer(int entityId)
    {
        this.entityIdForMuzzleFlash.add(entityId);
        this.entityIdToRandomValue.put(entityId, this.random.nextFloat());
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        MatrixStack matrixStack = event.getMatrixStack();
        if(mc.options.bobView && mc.getCameraEntity() instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) mc.getCameraEntity();
            float deltaDistanceWalked = playerentity.walkDist - playerentity.walkDistO;
            float distanceWalked = -(playerentity.walkDist + deltaDistanceWalked * event.getPartialTicks());
            float cameraYaw = MathHelper.lerp(event.getPartialTicks(), playerentity.oBob, playerentity.bob);

            /* Reverses the original bobbing rotations and translations so it can be controlled */
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-(Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F)));
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F)));
            matrixStack.translate((double) -(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F), (double) -(-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)), 0.0D);

            /* The new controlled bobbing */
            double invertZoomProgress = 1.0 - AimingHandler.get().getNormalisedAdsProgress();
            matrixStack.translate((double) (MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, (double) (-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)) * invertZoomProgress, 0.0D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees((MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F) * (float) invertZoomProgress));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees((Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F) * (float) invertZoomProgress));
        }

        boolean right = Minecraft.getInstance().options.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if(event.getHand() == Hand.OFF_HAND)
        {
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setCanceled(true);
                return;
            }

            float offhand = 1.0F - MathHelper.lerp(event.getPartialTicks(), this.prevOffhandTranslate, this.offhandTranslate);
            matrixStack.translate(0, offhand * -0.6F, 0);

            PlayerEntity player = Minecraft.getInstance().player;
            if(player != null && player.getMainHandItem().getItem() instanceof GunItem)
            {
                Gun modifiedGun = ((GunItem) player.getMainHandItem().getItem()).getModifiedGun(player.getMainHandItem());
                if(!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem())
                {
                    return;
                }
            }

            /* Makes the off hand item move out of view */
            matrixStack.translate(0, -1 * AimingHandler.get().getNormalisedAdsProgress(), 0);
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
                overrideModel = ItemStack.of(heldItem.getTag().getCompound("Model"));
            }
        }

        LivingEntity entity = Minecraft.getInstance().player;
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(overrideModel.isEmpty() ? heldItem : overrideModel, entity.level, entity);
        float scaleX = model.getTransforms().firstPersonRightHand.scale.x();
        float scaleY = model.getTransforms().firstPersonRightHand.scale.y();
        float scaleZ = model.getTransforms().firstPersonRightHand.scale.z();
        float translateX = model.getTransforms().firstPersonRightHand.translation.x();
        float translateY = model.getTransforms().firstPersonRightHand.translation.y();
        float translateZ = model.getTransforms().firstPersonRightHand.translation.z();

        matrixStack.pushPose();

        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);

        if(AimingHandler.get().getNormalisedAdsProgress() > 0 && modifiedGun.canAimDownSight())
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
                    double viewFinderOffset = scope.getViewFinderOffset();
                    if(OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.75;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getScope();
                    xOffset = -translateX + scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();
                }
                else if(modifiedGun.getModules().getZoom() != null)
                {
                    xOffset = -translateX + modifiedGun.getModules().getZoom().getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - modifiedGun.getModules().getZoom().getYOffset()) * 0.0625 * scaleY;
                    zOffset = -translateZ + modifiedGun.getModules().getZoom().getZOffset() * 0.0625 * scaleZ;
                }

                /* Controls the direction of the following translations, changes depending on the main hand. */
                float side = right ? 1.0F : -1.0F;
                double transition = 1.0 - Math.pow(1.0 - AimingHandler.get().getNormalisedAdsProgress(), 2);

                /* Reverses the original first person translations */
                matrixStack.translate(-0.56 * side * transition, 0.52 * transition, 0);

                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                matrixStack.translate(xOffset * side * transition, yOffset * transition, zOffset * transition);
            }
        }

        /* Applies equip progress animation translations */
        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        //matrixStack.translate(0, equipProgress * -0.6F, 0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(equipProgress * -50F));

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;
        Objects.requireNonNull(entity);
        int blockLight = entity.isOnFire() ? 15 : entity.level.getBrightness(LightType.BLOCK, new BlockPos(entity.getEyePosition(event.getPartialTicks())));
        blockLight += (this.entityIdForMuzzleFlash.contains(entity.getId()) ? 3 : 0);
        int packedLight = LightTexture.pack(blockLight, entity.level.getBrightness(LightType.SKY, new BlockPos(entity.getEyePosition(event.getPartialTicks()))));

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        this.renderReloadArm(matrixStack, event.getBuffers(), event.getLight(), modifiedGun, heldItem, hand);

        /* Translate the item position based on the hand side */
        int offset = right ? 1 : -1;
        matrixStack.translate(0.56 * offset, -0.52, -0.72);

        /* Applies recoil and reload rotations */
        this.applySprintingTransforms(modifiedGun, hand, matrixStack, event.getPartialTicks());
        this.applyRecoilTransforms(matrixStack, heldItem, modifiedGun);
        this.applyReloadTransforms(matrixStack, event.getPartialTicks());

        /* Renders the first persons arms from the grip type of the weapon */
        matrixStack.pushPose();
        matrixStack.translate(-0.56 * offset, 0.52, 0.72);
        modifiedGun.getGeneral().getGripType().getHeldAnimation().renderFirstPersonArms(Minecraft.getInstance().player, hand, heldItem, matrixStack, event.getBuffers(), event.getLight(), event.getPartialTicks());
        matrixStack.popPose();

        /* Renders the weapon */
        ItemCameraTransforms.TransformType transformType = right ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        this.renderWeapon(Minecraft.getInstance().player, heldItem, transformType, event.getMatrixStack(), event.getBuffers(), packedLight, event.getPartialTicks());

        matrixStack.popPose();
    }

    private void applySprintingTransforms(Gun modifiedGun, HandSide hand, MatrixStack matrixStack, float partialTicks)
    {
        if(modifiedGun.getGeneral().getGripType().getHeldAnimation().canApplySprintingAnimation())
        {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;
            float transition = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
            transition = (float) Math.sin((transition * Math.PI) / 2);
            matrixStack.translate(-0.25 * leftHanded * transition, -0.1 * transition, 0);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(45F * leftHanded * transition));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-25F * transition));
        }
    }

    private void applyReloadTransforms(MatrixStack matrixStack, float partialTicks)
    {
        float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks);
        matrixStack.translate(0, 0.35 * reloadProgress, 0);
        matrixStack.translate(0, 0, -0.1 * reloadProgress);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45F * reloadProgress));
    }

    private void applyRecoilTransforms(MatrixStack matrixStack, ItemStack item, Gun gun)
    {
        double recoilNormal = RecoilHandler.get().getGunRecoilNormal();
        if(Gun.hasAttachmentEquipped(item, gun, IAttachment.Type.SCOPE))
        {
            recoilNormal -= recoilNormal * (0.5 * AimingHandler.get().getNormalisedAdsProgress());
        }
        float kickReduction = 1.0F - GunModifierHelper.getKickReduction(item);
        float recoilReduction = 1.0F - GunModifierHelper.getRecoilModifier(item);
        double kick = gun.getGeneral().getRecoilKick() * 0.0625 * recoilNormal * RecoilHandler.get().getAdsRecoilReduction(gun);
        float recoilLift = (float) (gun.getGeneral().getRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun);
        float recoilSwayAmount = (float) (2F + 1F * (1.0 - AimingHandler.get().getNormalisedAdsProgress()));
        float recoilSway = (float) ((RecoilHandler.get().getGunRecoilRandom() * recoilSwayAmount - recoilSwayAmount / 2F) * recoilNormal);
        matrixStack.translate(0, 0, kick * kickReduction);
        matrixStack.translate(0, 0, 0.35);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(recoilSway * recoilReduction));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(recoilSway * recoilReduction));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(recoilLift * recoilReduction));
        matrixStack.translate(0, 0, -0.35);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase.equals(TickEvent.Phase.START))
            return;

        Minecraft mc = Minecraft.getInstance();
        if(!mc.isWindowActive())
            return;

        PlayerEntity player = mc.player;
        if(player == null)
            return;

        if(Minecraft.getInstance().options.getCameraType() != PointOfView.FIRST_PERSON)
            return;

        ItemStack heldItem = player.getItemInHand(Hand.MAIN_HAND);
        if(heldItem.isEmpty())
            return;

        if(player.isUsingItem() && player.getUsedItemHand() == Hand.MAIN_HAND && heldItem.getItem() instanceof GrenadeItem)
        {
            if(!((GrenadeItem) heldItem.getItem()).canCook())
                return;

            int duration = player.getTicksUsingItem();
            if(duration >= 10)
            {
                float cookTime = 1.0F - ((float) (duration - 10) / (float) (player.getUseItem().getUseDuration() - 10));
                if(cookTime > 0.0F)
                {
                    double scale = 3;
                    MainWindow window = mc.getWindow();
                    int i = (int) ((window.getGuiScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getGuiScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

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
                float coolDown = player.getCooldowns().getCooldownPercent(heldItem.getItem(), event.renderTickTime);
                if(coolDown > 0.0F)
                {
                    double scale = 3;
                    MainWindow window = mc.getWindow();
                    int i = (int) ((window.getGuiScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getGuiScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);

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
        Hand hand = Minecraft.getInstance().options.mainHand == HandSide.RIGHT ? event.getHandSide() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND : event.getHandSide() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        LivingEntity entity = event.getEntity();
        ItemStack heldItem = entity.getItemInHand(hand);

        if(hand == Hand.OFF_HAND)
        {
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setCanceled(true);
                return;
            }

            if(entity.getMainHandItem().getItem() instanceof GunItem)
            {
                Gun modifiedGun = ((GunItem) entity.getMainHandItem().getItem()).getModifiedGun(entity.getMainHandItem());
                if(!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem())
                {
                    event.setCanceled(true);
                    return;
                }
            }
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
            if(entity instanceof PlayerEntity)
            {
                gun.getGeneral().getGripType().getHeldAnimation().applyHeldItemTransforms((PlayerEntity) entity, hand, AimingHandler.get().getAimProgress((PlayerEntity) entity, event.getPartialTicks()), event.getMatrixStack(), event.getRenderTypeBuffer());
            }
            this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event)
    {
        // Dirty hack to reject first person arms
        if(event.getAgeInTicks() == 0F)
        {
            event.getModelPlayer().rightArm.xRot = 0;
            event.getModelPlayer().rightArm.yRot = 0;
            event.getModelPlayer().rightArm.zRot = 0;
            event.getModelPlayer().leftArm.xRot = 0;
            event.getModelPlayer().leftArm.yRot = 0;
            event.getModelPlayer().leftArm.zRot = 0;
            return;
        }

        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getMainHandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerModelRotation(player, model, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.rightArm, model.rightSleeve);
            copyModelAngles(model.leftArm, model.leftSleeve);
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.xRot = source.xRot;
        dest.yRot = source.yRot;
        dest.zRot = source.zRot;
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getMainHandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
        }
    }

    @SubscribeEvent
    public void onModelRender(PlayerModelEvent.Render.Pre event)
    {
        PlayerEntity player = event.getPlayer();
        ItemStack offHandStack = player.getOffhandItem();
        if(offHandStack.getItem() instanceof GunItem)
        {
            switch(player.getMainArm().getOpposite())
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
        ItemStack heldItem = player.getOffhandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            matrixStack.pushPose();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if(gun.getGeneral().getGripType().getHeldAnimation().applyOffhandTransforms(player, event.getModelPlayer(), heldItem, matrixStack, event.getPartialTicks()))
            {
                IRenderTypeBuffer buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());
            }
            matrixStack.popPose();
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
            matrixStack.pushPose();

            ItemStack model = ItemStack.EMPTY;
            if(stack.getTag() != null)
            {
                if(stack.getTag().contains("Model", Constants.NBT.TAG_COMPOUND))
                {
                    model = ItemStack.of(stack.getTag().getCompound("Model"));
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType, entity);

            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderMuzzleFlash(entity, matrixStack, renderTypeBuffer, stack, transformType);

            matrixStack.popPose();
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
            RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        }
    }

    private void renderAttachments(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
            CompoundNBT gunTag = stack.getOrCreateTag();
            CompoundNBT attachments = gunTag.getCompound("Attachments");
            for(String tagKey : attachments.getAllKeys())
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
                            matrixStack.pushPose();
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

                            matrixStack.popPose();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack weapon, ItemCameraTransforms.TransformType transformType)
    {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if(modifiedGun.getDisplay().getFlash() == null)
        {
            return;
        }

        if(transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
        {
            if(this.entityIdForMuzzleFlash.contains(entity.getId()))
            {
                float randomValue = this.entityIdToRandomValue.get(entity.getId());
                this.drawMuzzleFlash(weapon, modifiedGun, randomValue, randomValue >= 0.5F, matrixStack, buffer);
            }
        }
    }

    private void drawMuzzleFlash(ItemStack weapon, Gun modifiedGun, float random, boolean flip, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        matrixStack.pushPose();

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
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(360F * random));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(flip ? 180F : 0F));
        matrixStack.translate(-size / 2, -size / 2, 0);

        Matrix4f matrix = matrixStack.last().pose();
        IVertexBuilder builder = buffer.getBuffer(GunRenderType.getMuzzleFlash());
        builder.vertex(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).uv2(15728880).endVertex();
        builder.vertex(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 1.0F).uv2(15728880).endVertex();
        builder.vertex(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 0).uv2(15728880).endVertex();
        builder.vertex(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0).uv2(15728880).endVertex();

        matrixStack.popPose();
    }

    private void renderReloadArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, Gun modifiedGun, ItemStack stack, HandSide hand)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || mc.player.tickCount < ReloadHandler.get().getStartReloadTick() || ReloadHandler.get().getReloadTimer() != 5)
            return;

        Item item = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(item == null)
            return;

        matrixStack.pushPose();

        float interval = GunEnchantmentHelper.getReloadInterval(stack);
        float reload = ((mc.player.tickCount - ReloadHandler.get().getStartReloadTick() + mc.getFrameTime()) % interval) / interval;
        float percent = 1.0F - reload;
        if(percent >= 0.5F)
        {
            percent = 1.0F - percent;
        }
        percent *= 2F;
        percent = percent < 0.5 ? 2 * percent * percent : -1 + (4 - 2 * percent) * percent;

        int side = hand.getOpposite() == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(-2.75 * side * 0.0625, -0.5625, -0.5625);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        matrixStack.translate(0, -0.35 * (1.0 - percent), 0);
        matrixStack.translate(side * 0.0625, 0, 0);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(35F * -side));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-75F * percent));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(mc.player, hand.getOpposite(), matrixStack, buffer, light);

        if(reload < 0.5F)
        {
            matrixStack.pushPose();
            matrixStack.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(180F));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            ItemStack ammo = new ItemStack(item, modifiedGun.getGeneral().getReloadAmount());
            IBakedModel model = RenderUtil.getModel(ammo);
            boolean isModel = model.isGui3d();
            this.random.setSeed(Item.getId(item));
            int count = Math.min(modifiedGun.getGeneral().getReloadAmount(), 5);
            for(int i = 0; i < count; ++i)
            {
                matrixStack.pushPose();
                if(i > 0)
                {
                    if(isModel)
                    {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        matrixStack.translate(x, y, z);
                    }
                    else
                    {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        matrixStack.translate(x, y, 0);
                    }
                }

                RenderUtil.renderModel(ammo, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, null);
                matrixStack.popPose();

                if(!isModel)
                {
                    matrixStack.translate(0.0, 0.0, 0.09375F);
                }
            }
            matrixStack.popPose();
        }
        matrixStack.popPose();
    }

    /**
     * A temporary hack to get the equip progress until Forge fixes the issue.
     * @return
     */
    private float getEquipProgress(float partialTicks)
    {
        if(this.equippedProgressMainHandField == null)
        {
            this.equippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "mainHandHeight");
            this.equippedProgressMainHandField.setAccessible(true);
        }
        if(this.prevEquippedProgressMainHandField == null)
        {
            this.prevEquippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "oMainHandHeight");
            this.prevEquippedProgressMainHandField.setAccessible(true);
        }
        FirstPersonRenderer firstPersonRenderer = Minecraft.getInstance().getItemInHandRenderer();
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
}
