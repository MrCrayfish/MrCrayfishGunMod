package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.Config;
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
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
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

    private final Random random = new Random();
    private final Set<Integer> entityIdForMuzzleFlash = new HashSet<>();
    private final Set<Integer> entityIdForDrawnMuzzleFlash = new HashSet<>();
    private final Map<Integer, Float> entityIdToRandomValue = new HashMap<>();

    private int sprintTransition;
    private int prevSprintTransition;
    private int sprintCooldown;
    private float sprintIntensity;

    private float offhandTranslate;
    private float prevOffhandTranslate;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;

    private float immersiveRoll;
    private float prevImmersiveRoll;
    private float fallSway;
    private float prevFallSway;

    @Nullable
    private ItemStack renderingWeapon;

    private GunRenderingHandler() {}

    @Nullable
    public ItemStack getRenderingWeapon()
    {
        return this.renderingWeapon;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        this.updateSprinting();
        this.updateMuzzleFlash();
        this.updateOffhandTranslate();
        this.updateImmersiveCamera();
    }

    private void updateSprinting()
    {
        this.prevSprintTransition = this.sprintTransition;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && mc.player.isSprinting() && !ModSyncedDataKeys.SHOOTING.getValue(mc.player) && !ModSyncedDataKeys.RELOADING.getValue(mc.player) && !AimingHandler.get().isAiming() && this.sprintCooldown == 0)
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
        this.offhandTranslate = Mth.clamp(this.offhandTranslate + direction, 0.0F, 1.0F);
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event)
    {
        if(!event.isClient())
            return;

        this.sprintTransition = 0;
        this.sprintCooldown = 20; //TODO make a config option

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
        PoseStack poseStack = event.getPoseStack();

        boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT ? event.getHand() == InteractionHand.MAIN_HAND : event.getHand() == InteractionHand.OFF_HAND;
        HumanoidArm hand = right ? HumanoidArm.RIGHT : HumanoidArm.LEFT;

        ItemStack heldItem = event.getItemStack();
        if(event.getHand() == InteractionHand.OFF_HAND)
        {
            if(heldItem.getItem() instanceof GunItem)
            {
                event.setCanceled(true);
                return;
            }

            float offhand = 1.0F - Mth.lerp(event.getPartialTicks(), this.prevOffhandTranslate, this.offhandTranslate);
            poseStack.translate(0, offhand * -0.6F, 0);

            Player player = Minecraft.getInstance().player;
            if(player != null && player.getMainHandItem().getItem() instanceof GunItem)
            {
                Gun modifiedGun = ((GunItem) player.getMainHandItem().getItem()).getModifiedGun(player.getMainHandItem());
                if(!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem())
                {
                    return;
                }
            }

            /* Makes the off hand item move out of view */
            poseStack.translate(0, -1 * AimingHandler.get().getNormalisedAdsProgress(), 0);
        }

        if(!(heldItem.getItem() instanceof GunItem gunItem))
        {
            return;
        }

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        ItemStack overrideModel = ItemStack.EMPTY;
        if(heldItem.getTag() != null)
        {
            if(heldItem.getTag().contains("Model", Tag.TAG_COMPOUND))
            {
                overrideModel = ItemStack.of(heldItem.getTag().getCompound("Model"));
            }
        }

        LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(overrideModel.isEmpty() ? heldItem : overrideModel, player.level, player, 0);
        float scaleX = model.getTransforms().firstPersonRightHand.scale.x();
        float scaleY = model.getTransforms().firstPersonRightHand.scale.y();
        float scaleZ = model.getTransforms().firstPersonRightHand.scale.z();
        float translateX = model.getTransforms().firstPersonRightHand.translation.x();
        float translateY = model.getTransforms().firstPersonRightHand.translation.y();
        float translateZ = model.getTransforms().firstPersonRightHand.translation.z();

        poseStack.pushPose();

        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        if(AimingHandler.get().getNormalisedAdsProgress() > 0 && modifiedGun.canAimDownSight())
        {
            if(event.getHand() == InteractionHand.MAIN_HAND)
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
                    xOffset = -translateX - scaledPos.getXOffset() * 0.0625 * scaleX;
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
                poseStack.translate(-0.56 * side * transition, 0.52 * transition, 0);

                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                poseStack.translate(xOffset * side * transition, yOffset * transition, zOffset * transition);
            }
        }

        /* Applies custom bobbing animations */
        this.applyBobbingTransforms(poseStack, event.getPartialTicks());

        /* Applies equip progress animation translations */
        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        //poseStack.translate(0, equipProgress * -0.6F, 0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(equipProgress * -50F));

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        this.renderReloadArm(poseStack, event.getMultiBufferSource(), event.getPackedLight(), modifiedGun, heldItem, hand, translateX);

        // Values are based on vanilla translations for first person
        int offset = right ? 1 : -1;
        poseStack.translate(0.56 * offset, -0.52, -0.72);

        /* Applies recoil and reload rotations */
        this.applyAimingTransforms(poseStack, translateX, translateY, translateZ, offset);
        this.applySwayTransforms(poseStack, modifiedGun, player, translateX, translateY, translateZ, event.getPartialTicks());
        this.applySprintingTransforms(modifiedGun, hand, poseStack, event.getPartialTicks());
        this.applyRecoilTransforms(poseStack, heldItem, modifiedGun);
        this.applyReloadTransforms(poseStack, event.getPartialTicks());

        int blockLight = player.isOnFire() ? 15 : player.level.getBrightness(LightLayer.BLOCK, new BlockPos(player.getEyePosition(event.getPartialTicks())));
        blockLight += (this.entityIdForMuzzleFlash.contains(player.getId()) ? 3 : 0);
        blockLight = Math.min(blockLight, 15);
        int packedLight = LightTexture.pack(blockLight, player.level.getBrightness(LightLayer.SKY, new BlockPos(player.getEyePosition(event.getPartialTicks()))));

        /* Renders the first persons arms from the grip type of the weapon */
        poseStack.pushPose();
        modifiedGun.getGeneral().getGripType().getHeldAnimation().renderFirstPersonArms(Minecraft.getInstance().player, hand, heldItem, poseStack, event.getMultiBufferSource(), packedLight, event.getPartialTicks());
        poseStack.popPose();

        /* Renders the weapon */
        ItemTransforms.TransformType transformType = right ? ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        this.renderWeapon(Minecraft.getInstance().player, heldItem, transformType, event.getPoseStack(), event.getMultiBufferSource(), packedLight, event.getPartialTicks());

        poseStack.popPose();
    }

    private void applyBobbingTransforms(PoseStack poseStack, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.options.bobView && mc.getCameraEntity() instanceof Player player)
        {
            float deltaDistanceWalked = player.walkDist - player.walkDistO;
            float distanceWalked = -(player.walkDist + deltaDistanceWalked * partialTicks);
            float bobbing = Mth.lerp(partialTicks, player.oBob, player.bob);

            /* Reverses the original bobbing rotations and translations so it can be controlled */
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-(Math.abs(Mth.cos(distanceWalked * (float) Math.PI - 0.2F) * bobbing) * 5.0F)));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(-(Mth.sin(distanceWalked * (float) Math.PI) * bobbing * 3.0F)));
            poseStack.translate(-(Mth.sin(distanceWalked * (float) Math.PI) * bobbing * 0.5F), -(-Math.abs(Mth.cos(distanceWalked * (float) Math.PI) * bobbing)), 0.0D);

            /* Slows down the bob by half */
            bobbing *= player.isSprinting() ? 8.0 : 4.0;
            bobbing *= Config.CLIENT.display.bobbingIntensity.get();

            /* The new controlled bobbing */
            double invertZoomProgress = 1.0 - AimingHandler.get().getNormalisedAdsProgress() * this.sprintIntensity;
            //poseStack.translate((double) (Mth.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, (double) (-Math.abs(Mth.cos(distanceWalked * (float) Math.PI) * cameraYaw)) * invertZoomProgress, 0.0D);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees((Mth.sin(distanceWalked * (float) Math.PI) * bobbing * 3.0F) * (float) invertZoomProgress));
            poseStack.mulPose(Vector3f.XP.rotationDegrees((Math.abs(Mth.cos(distanceWalked * (float) Math.PI - 0.2F) * bobbing) * 5.0F) * (float) invertZoomProgress));
        }
    }

    private void applyAimingTransforms(PoseStack poseStack, float x, float y, float z, int offset)
    {
        if(!Config.CLIENT.display.oldAnimations.get())
        {
            poseStack.translate(x * offset, y, z);
            poseStack.translate(0, -0.25, 0.25);
            float aiming = (float) Math.sin(Math.toRadians(AimingHandler.get().getNormalisedAdsProgress() * 180F));
            aiming = (float) (1 - Math.cos((aiming * Math.PI) / 2.0));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(aiming * 10F * offset));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(aiming * 5F));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(aiming * 5F * offset));
            poseStack.translate(0, 0.25, -0.25);
            poseStack.translate(-x * offset, -y, -z);
        }
    }

    private void applySwayTransforms(PoseStack poseStack, Gun modifiedGun, LocalPlayer player, float x, float y, float z, float partialTicks)
    {
        if(Config.CLIENT.display.weaponSway.get() && player != null)
        {
            poseStack.translate(x, y, z);

            double zOffset = modifiedGun.getGeneral().getGripType().getHeldAnimation().getFallSwayZOffset();
            poseStack.translate(0, -0.25, zOffset);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.lerp(partialTicks, this.prevFallSway, this.fallSway)));
            poseStack.translate(0, 0.25, -zOffset);

            float bobPitch = Mth.rotLerp(partialTicks, player.xBobO, player.xBob);
            float headPitch = Mth.rotLerp(partialTicks, player.xRotO, player.getXRot());
            float swayPitch = headPitch - bobPitch;
            swayPitch *= 1.0 - 0.5 * AimingHandler.get().getNormalisedAdsProgress();
            poseStack.mulPose(Config.CLIENT.display.swayType.get().getPitchRotation().rotationDegrees(swayPitch * Config.CLIENT.display.swaySensitivity.get().floatValue()));

            float bobYaw = Mth.rotLerp(partialTicks, player.yBobO, player.yBob);
            float headYaw = Mth.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
            float swayYaw = headYaw - bobYaw;
            swayYaw *= 1.0 - 0.5 * AimingHandler.get().getNormalisedAdsProgress();
            poseStack.mulPose(Config.CLIENT.display.swayType.get().getYawRotation().rotationDegrees(swayYaw * Config.CLIENT.display.swaySensitivity.get().floatValue()));

            poseStack.translate(-x, -y, -z);
        }
    }

    private void applySprintingTransforms(Gun modifiedGun, HumanoidArm hand, PoseStack poseStack, float partialTicks)
    {
        if(Config.CLIENT.display.sprintAnimation.get() && modifiedGun.getGeneral().getGripType().getHeldAnimation().canApplySprintingAnimation())
        {
            float leftHanded = hand == HumanoidArm.LEFT ? -1 : 1;
            float transition = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
            transition = (float) Math.sin((transition * Math.PI) / 2);
            poseStack.translate(-0.25 * leftHanded * transition, -0.1 * transition, 0);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(45F * leftHanded * transition));
            poseStack.mulPose(Vector3f.XP.rotationDegrees(-25F * transition));
        }
    }

    private void applyReloadTransforms(PoseStack poseStack, float partialTicks)
    {
        float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks);
        poseStack.translate(0, 0.35 * reloadProgress, 0);
        poseStack.translate(0, 0, -0.1 * reloadProgress);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(45F * reloadProgress));
    }

    private void applyRecoilTransforms(PoseStack poseStack, ItemStack item, Gun gun)
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
        poseStack.translate(0, 0, kick * kickReduction);
        poseStack.translate(0, 0, 0.15);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(recoilSway * recoilReduction));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(recoilSway * recoilReduction));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(recoilLift * recoilReduction));
        poseStack.translate(0, 0, -0.15);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase.equals(TickEvent.Phase.START))
            return;

        Minecraft mc = Minecraft.getInstance();
        if(!mc.isWindowActive())
            return;

        Player player = mc.player;
        if(player == null)
            return;

        if(Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON)
            return;

        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(heldItem.isEmpty())
            return;

        if(player.isUsingItem() && player.getUsedItemHand() == InteractionHand.MAIN_HAND && heldItem.getItem() instanceof GrenadeItem)
        {
            if(!((GrenadeItem) heldItem.getItem()).canCook())
                return;

            int duration = player.getTicksUsingItem();
            if(duration >= 10)
            {
                float cookTime = 1.0F - ((float) (duration - 10) / (float) (player.getUseItem().getUseDuration() - 10));
                if(cookTime > 0.0F)
                {
                    float scale = 3;
                    Window window = mc.getWindow();
                    int i = (int) ((window.getGuiScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getGuiScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);

                    PoseStack stack = new PoseStack();
                    stack.scale(scale, scale, scale);
                    int progress = (int) Math.ceil((cookTime) * 17.0F) - 1;
                    Screen.blit(stack, j, i, 36, 94, 16, 4, 256, 256);
                    Screen.blit(stack, j, i, 52, 94, progress, 4, 256, 256);

                    RenderSystem.disableBlend();
                }
            }
            return;
        }

        if(Config.CLIENT.display.cooldownIndicator.get() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getGun();
            if(!gun.getGeneral().isAuto())
            {
                float coolDown = player.getCooldowns().getCooldownPercent(heldItem.getItem(), event.renderTickTime);
                if(coolDown > 0.0F)
                {
                    float scale = 3;
                    Window window = mc.getWindow();
                    int i = (int) ((window.getGuiScaledHeight() / 2 - 7 - 60) / scale);
                    int j = (int) Math.ceil((window.getGuiScaledWidth() / 2 - 8 * scale) / scale);

                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);

                    PoseStack stack = new PoseStack();
                    stack.scale(scale, scale, scale);
                    int progress = (int) Math.ceil((coolDown + 0.05) * 17.0F) - 1;
                    Screen.blit(stack, j, i, 36, 94, 16, 4, 256, 256);
                    Screen.blit(stack, j, i, 52, 94, progress, 4, 256, 256);

                    RenderSystem.disableBlend();
                }
            }
        }
    }

    public void applyWeaponScale(ItemStack heldItem, PoseStack stack)
    {
        if(heldItem.getTag() != null)
        {
            CompoundTag compound = heldItem.getTag();
            if(compound.contains("Scale", Tag.TAG_FLOAT))
            {
                float scale = compound.getFloat("Scale");
                stack.scale(scale, scale, scale);
            }
        }
    }

    public boolean renderWeapon(@Nullable LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            poseStack.pushPose();

            ItemStack model = ItemStack.EMPTY;
            if(stack.getTag() != null)
            {
                if(stack.getTag().contains("Model", Tag.TAG_COMPOUND))
                {
                    model = ItemStack.of(stack.getTag().getCompound("Model"));
                }
            }

            RenderUtil.applyTransformType(stack, poseStack, transformType, entity);

            this.renderingWeapon = stack;
            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, poseStack, renderTypeBuffer, light, partialTicks);
            this.renderAttachments(entity, transformType, stack, poseStack, renderTypeBuffer, light, partialTicks);
            this.renderMuzzleFlash(entity, poseStack, renderTypeBuffer, stack, transformType, partialTicks);
            this.renderingWeapon = null;

            poseStack.popPose();
            return true;
        }
        return false;
    }

    private void renderGun(@Nullable LivingEntity entity, ItemTransforms.TransformType transformType, ItemStack stack, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light, float partialTicks)
    {
        if(ModelOverrides.hasModel(stack))
        {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if(model != null)
            {
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, poseStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
            }
        }
        else
        {
            BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
            Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, poseStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, bakedModel);
        }
    }

    private void renderAttachments(@Nullable LivingEntity entity, ItemTransforms.TransformType transformType, ItemStack stack, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light, float partialTicks)
    {
        if(stack.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
            CompoundTag gunTag = stack.getOrCreateTag();
            CompoundTag attachments = gunTag.getCompound("Attachments");
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
                            poseStack.pushPose();
                            double displayX = positioned.getXOffset() * 0.0625;
                            double displayY = positioned.getYOffset() * 0.0625;
                            double displayZ = positioned.getZOffset() * 0.0625;
                            poseStack.translate(displayX, displayY, displayZ);
                            poseStack.translate(0, -0.5, 0);
                            poseStack.scale((float) positioned.getScale(), (float) positioned.getScale(), (float) positioned.getScale());

                            IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                            if(model != null)
                            {
                                model.render(partialTicks, transformType, attachmentStack, stack, entity, poseStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            }
                            else
                            {
                                BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(attachmentStack);
                                Minecraft.getInstance().getItemRenderer().render(attachmentStack, ItemTransforms.TransformType.NONE, false, poseStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, bakedModel);
                            }

                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(@Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, ItemStack weapon, ItemTransforms.TransformType transformType, float partialTicks)
    {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if(modifiedGun.getDisplay().getFlash() == null)
            return;

        if(transformType != ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND && transformType != ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND && transformType != ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND && transformType != ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
            return;

        if(entity == null || !this.entityIdForMuzzleFlash.contains(entity.getId()))
            return;

        float randomValue = this.entityIdToRandomValue.get(entity.getId());
        this.drawMuzzleFlash(weapon, modifiedGun, randomValue, randomValue >= 0.5F, poseStack, buffer, partialTicks);
    }

    private void drawMuzzleFlash(ItemStack weapon, Gun modifiedGun, float random, boolean flip, PoseStack poseStack, MultiBufferSource buffer, float partialTicks)
    {
        poseStack.pushPose();

        Gun.Positioned muzzleFlash = modifiedGun.getDisplay().getFlash();
        if(muzzleFlash == null)
            return;

        double displayX = muzzleFlash.getXOffset() * 0.0625;
        double displayY = muzzleFlash.getYOffset() * 0.0625;
        double displayZ = muzzleFlash.getZOffset() * 0.0625;
        poseStack.translate(displayX, displayY, displayZ);
        poseStack.translate(0, -0.5, 0);

        ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, weapon);
        if(!barrelStack.isEmpty() && barrelStack.getItem() instanceof IBarrel)
        {
            Barrel barrel = ((IBarrel) barrelStack.getItem()).getProperties();
            Gun.ScaledPositioned positioned = modifiedGun.getModules().getAttachments().getBarrel();
            if(positioned != null)
            {
                poseStack.translate(0, 0, -barrel.getLength() * 0.0625 * positioned.getScale());
            }
        }

        poseStack.scale(0.5F, 0.5F, 0.0F);

        float scale = 0.5F + 0.5F * (1.0F - partialTicks);
        poseStack.scale(scale, scale, 1.0F);

        double partialSize = modifiedGun.getDisplay().getFlash().getSize() / 5.0;
        float size = (float) (modifiedGun.getDisplay().getFlash().getSize() - partialSize + partialSize * random);
        size = (float) GunModifierHelper.getMuzzleFlashSize(weapon, size);
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(360F * random));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(flip ? 180F : 0F));
        poseStack.translate(-size / 2, -size / 2, 0);

        float minU = weapon.isEnchanted() ? 0.5F : 0.0F;
        float maxU = weapon.isEnchanted() ? 1.0F : 0.5F;
        Matrix4f matrix = poseStack.last().pose();
        VertexConsumer builder = buffer.getBuffer(GunRenderType.getMuzzleFlash());
        builder.vertex(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(maxU, 1.0F).uv2(15728880).endVertex();
        builder.vertex(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(minU, 1.0F).uv2(15728880).endVertex();
        builder.vertex(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(minU, 0).uv2(15728880).endVertex();
        builder.vertex(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(maxU, 0).uv2(15728880).endVertex();

        poseStack.popPose();
    }

    private void renderReloadArm(PoseStack poseStack, MultiBufferSource buffer, int light, Gun modifiedGun, ItemStack stack, HumanoidArm hand, float translateX)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null || mc.player.tickCount < ReloadHandler.get().getStartReloadTick() || ReloadHandler.get().getReloadTimer() != 5)
            return;

        Item item = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if(item == null)
            return;

        poseStack.pushPose();

        int side = hand.getOpposite() == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(translateX * side, 0, 0);

        float interval = GunEnchantmentHelper.getReloadInterval(stack);
        float reload = ((mc.player.tickCount - ReloadHandler.get().getStartReloadTick() + mc.getFrameTime()) % interval) / interval;
        float percent = 1.0F - reload;
        if(percent >= 0.5F)
        {
            percent = 1.0F - percent;
        }
        percent *= 2F;
        percent = percent < 0.5 ? 2 * percent * percent : -1 + (4 - 2 * percent) * percent;

        poseStack.translate(3.5 * side * 0.0625, -0.5625, -0.5625);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        poseStack.translate(0, -0.35 * (1.0 - percent), 0);
        poseStack.translate(side * 0.0625, 0, 0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(35F * -side));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-75F * percent));
        poseStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(mc.player, hand.getOpposite(), poseStack, buffer, light);

        if(reload < 0.5F)
        {
            poseStack.pushPose();
            poseStack.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(180F));
            poseStack.scale(0.75F, 0.75F, 0.75F);
            ItemStack ammo = new ItemStack(item, modifiedGun.getGeneral().getReloadAmount());
            BakedModel model = RenderUtil.getModel(ammo);
            boolean isModel = model.isGui3d();
            this.random.setSeed(Item.getId(item));
            int count = Math.min(modifiedGun.getGeneral().getReloadAmount(), 5);
            for(int i = 0; i < count; ++i)
            {
                poseStack.pushPose();
                if(i > 0)
                {
                    if(isModel)
                    {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        poseStack.translate(x, y, z);
                    }
                    else
                    {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        poseStack.translate(x, y, 0);
                    }
                }

                RenderUtil.renderModel(ammo, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, poseStack, buffer, light, OverlayTexture.NO_OVERLAY, null);
                poseStack.popPose();

                if(!isModel)
                {
                    poseStack.translate(0.0, 0.0, 0.09375F);
                }
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /**
     * A temporary hack to get the equip progress until Forge fixes the issue.
     * @return
     */
    private float getEquipProgress(float partialTicks)
    {
        if(this.equippedProgressMainHandField == null)
        {
            this.equippedProgressMainHandField = ObfuscationReflectionHelper.findField(ItemInHandRenderer.class, "f_109302_");
            this.equippedProgressMainHandField.setAccessible(true);
        }
        if(this.prevEquippedProgressMainHandField == null)
        {
            this.prevEquippedProgressMainHandField = ObfuscationReflectionHelper.findField(ItemInHandRenderer.class, "f_109303_");
            this.prevEquippedProgressMainHandField.setAccessible(true);
        }
        ItemInHandRenderer firstPersonRenderer = Minecraft.getInstance().getItemInHandRenderer();
        try
        {
            float equippedProgressMainHand = (float) this.equippedProgressMainHandField.get(firstPersonRenderer);
            float prevEquippedProgressMainHand = (float) this.prevEquippedProgressMainHandField.get(firstPersonRenderer);
            return 1.0F - Mth.lerp(partialTicks, prevEquippedProgressMainHand, equippedProgressMainHand);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return 0.0F;
    }

    private void updateImmersiveCamera()
    {
        this.prevImmersiveRoll = this.immersiveRoll;
        this.prevFallSway = this.fallSway;

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getMainHandItem();
        float targetAngle = heldItem.getItem() instanceof GunItem ? mc.player.input.leftImpulse: 0F;
        float speed = mc.player.input.leftImpulse != 0 ? 0.1F : 0.15F;
        this.immersiveRoll = Mth.lerp(speed, this.immersiveRoll, targetAngle);

        float deltaY = (float) Mth.clamp((mc.player.yo - mc.player.getY()), -1.0, 1.0);
        deltaY *= 1.0 - AimingHandler.get().getNormalisedAdsProgress();
        deltaY *= 1.0 - (Mth.abs(mc.player.getXRot()) / 90.0F);
        this.fallSway = Mth.approach(this.fallSway, deltaY * 60F * Config.CLIENT.display.swaySensitivity.get().floatValue(), 10.0F);

        float intensity = mc.player.isSprinting() ? 0.75F : 1.0F;
        this.sprintIntensity = Mth.approach(this.sprintIntensity, intensity, 0.1F);
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
        if(Config.CLIENT.display.cameraRollEffect.get())
        {
            float roll = (float) Mth.lerp(event.getPartialTicks(), this.prevImmersiveRoll, this.immersiveRoll);
            roll = (float) Math.sin((roll * Math.PI) / 2.0);
            roll *= Config.CLIENT.display.cameraRollAngle.get().floatValue();
            event.setRoll(-roll);
        }
    }
}
