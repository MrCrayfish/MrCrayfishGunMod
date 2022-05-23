package com.tac.guns.client.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.obfuscate.client.event.PlayerModelEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.GunRenderType;
import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.render.animation.GunAnimationController;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GrenadeItem;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.TransitionalTypes.ITimelessAnimated;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.IBarrel;
import com.tac.guns.item.attachment.impl.Barrel;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.OptifineHelper;
import com.tac.guns.util.math.easing.QuadEaseOut;
import com.tac.guns.util.math.easing.SineEaseInOut;
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
import net.minecraft.util.math.vector.Matrix3f;
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
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;

public class GunRenderingHandler {
    private static GunRenderingHandler instance;

    public static GunRenderingHandler get() {
        if (instance == null) {
            instance = new GunRenderingHandler();
        }
        return instance;
    }

    public static final ResourceLocation MUZZLE_FLASH_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_flash.png");
    public static final ResourceLocation MUZZLE_SMOKE_TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/effect/muzzle_smoke.png");

    private Random random = new Random();
    private Set<Integer> entityIdForMuzzleFlash = new HashSet<>();
    private Set<Integer> entityIdForDrawnMuzzleFlash = new HashSet<>();
    private Map<Integer, Float> entityIdToRandomValue = new HashMap<>();

    private int sprintTransition;
    private int prevSprintTransition;
    private int sprintCooldown;

    private float offhandTranslate;
    private float prevOffhandTranslate;

    public float muzzleExtraOnEnch = 0f;

    private Field equippedProgressMainHandField;
    private Field prevEquippedProgressMainHandField;

    private GunRenderingHandler() {
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        this.updateSprinting();
        this.updateMuzzleFlash();
        this.updateOffhandTranslate();
    }

    private void updateSprinting() {
        this.prevSprintTransition = this.sprintTransition;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.player.isSprinting() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.SHOOTING) && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING) && !AimingHandler.get().isAiming() && this.sprintCooldown == 0) {
            if (this.sprintTransition < 5) {
                this.sprintTransition++;
            }
        } else if (this.sprintTransition > 0) {
            this.sprintTransition--;
        }

        if (this.sprintCooldown > 0) {
            this.sprintCooldown--;
        }
    }

    private void updateMuzzleFlash() {
        this.entityIdForMuzzleFlash.removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdToRandomValue.keySet().removeAll(this.entityIdForDrawnMuzzleFlash);
        this.entityIdForDrawnMuzzleFlash.clear();
        this.entityIdForDrawnMuzzleFlash.addAll(this.entityIdForMuzzleFlash);
    }

    private void updateOffhandTranslate() {
        this.prevOffhandTranslate = this.offhandTranslate;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        boolean down = false;
        ItemStack heldItem = mc.player.getHeldItemMainhand();
        if (heldItem.getItem() instanceof GunItem) {
            Gun modifiedGun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            down = !modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem();
        }

        float direction = down ? -0.3F : 0.3F;
        this.offhandTranslate = MathHelper.clamp(this.offhandTranslate + direction, 0.0F, 1.0F);
    }

    @SubscribeEvent
    public void onGunReload(GunReloadEvent.Post event) {
        GunAnimationController controller = GunAnimationController.fromItem(event.getStack().getItem());
        if(controller!=null) controller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Post event) {
        if (!event.isClient())
            return;
        Item item = event.getStack().getItem();
        if(item instanceof ITimelessAnimated){
            ITimelessAnimated animated = (ITimelessAnimated) item;
            animated.playAnimation("fire",event.getStack(),true);
        }
        this.sprintTransition = 0;
        this.sprintCooldown = 8;

        ItemStack heldItem = event.getStack();
        GunItem gunItem = (GunItem) heldItem.getItem();
        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        if (modifiedGun.getDisplay().getFlash() != null) {
            this.showMuzzleFlashForPlayer(Minecraft.getInstance().player.getEntityId());
        }
    }

    public void showMuzzleFlashForPlayer(int entityId) {
        this.entityIdForMuzzleFlash.add(entityId);
        this.entityIdToRandomValue.put(entityId, this.random.nextFloat());
    }

    @SubscribeEvent
    public void onAnimatedGunReload(GunReloadEvent.Pre event){
        Item item = event.getStack().getItem();
        if(item instanceof ITimelessAnimated){
            ITimelessAnimated animated = (ITimelessAnimated) item;
            animated.playAnimation("reload",event.getStack(),false);
        }
    }

/*    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) // TEST FROM CGM
    {
        *//*if(!Config.CLIENT.experimental.immersiveCamera.get())
            return;*//*

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        ItemStack heldItem = mc.player.getHeldItemMainhand();
        float targetAngle = heldItem.getItem() instanceof GunItem ? mc.player.movementInput.moveStrafe * 1F: 0F;
        this.immersiveRoll = MathHelper.approach(this.immersiveRoll, targetAngle, 0.4F);
        event.setRoll(-this.immersiveRoll);
    }*/

    public float immersiveWeaponRoll;

    public float walkingDistance;
    public float walkingCrouch;
    public float walkingCameraYaw;
    public float zoomProgressInv;

    public double xOffset = 0.0;
    public double yOffset = 0.0;
    public double zOffset = 0.0;

    public double opticMovement;

    @SubscribeEvent
    public void onRenderOverlay(RenderHandEvent event)
    {
        boolean isAnimated = GunAnimationController.fromItem(event.getItemStack().getItem()) != null;
        MatrixStack matrixStack = event.getMatrixStack();

        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHand() == Hand.MAIN_HAND : event.getHand() == Hand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if (!(heldItem.getItem() instanceof GunItem)) {
            return;
        }

        /* Cancel it because we are doing our own custom render */
        event.setCanceled(true);

        ItemStack overrideModel = ItemStack.EMPTY;
        if (heldItem.getTag() != null) {
            if (heldItem.getTag().contains("Model", Constants.NBT.TAG_COMPOUND)) {
                overrideModel = ItemStack.read(heldItem.getTag().getCompound("Model"));
            }
        }
        Minecraft mc = Minecraft.getInstance();
        GunItem gunItem = (GunItem) heldItem.getItem();
        if (mc.gameSettings.viewBobbing && mc.getRenderViewEntity() instanceof PlayerEntity)
        {
            PlayerEntity playerentity = (PlayerEntity) mc.getRenderViewEntity();
            float deltaDistanceWalked = playerentity.distanceWalkedModified - playerentity.prevDistanceWalkedModified;
            float distanceWalked = -(playerentity.distanceWalkedModified + deltaDistanceWalked * event.getPartialTicks());
            float cameraYaw = MathHelper.lerp(event.getPartialTicks(), playerentity.prevCameraYaw, playerentity.cameraYaw);

            /* Reverses the original bobbing rotations and translations so it can be controlled */

            matrixStack.rotate(Vector3f.XP.rotationDegrees(-(Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F)));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 3.0F)));
            matrixStack.translate((double) -(MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F), (double) -(-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)), 0.0D);

            // Walking bobbing
            boolean aimed = false;
            /* The new controlled bobbing */
            if(AimingHandler.get().isAiming())
                aimed = true;

            //double invertZoomProgress = aimed ? 0.0575 : 0.468; //0.135 : 0.44;//0.94;//aimed ? 1.0 - AimingHandler.get().getNormalisedAdsProgress() : ;
            double invertZoomProgress = aimed ? (Gun.getScope(heldItem) != null ? 0.0575 : 0.0725) : 0.468;
            float crouch = mc.player.isCrouching() ? 148f : 1f;

            this.walkingDistance = distanceWalked;
            this.walkingCrouch = crouch;
            this.walkingCameraYaw = cameraYaw;
            this.zoomProgressInv = (float)invertZoomProgress;

            //matrixStack.translate((double) (MathHelper.sin(distanceWalked * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, ((double) (-Math.abs(MathHelper.cos(distanceWalked * (float) Math.PI) * cameraYaw)) * invertZoomProgress) * (1.285 * crouch), 0.0D);
            matrixStack.translate((double) (MathHelper.sin(distanceWalked*crouch * (float) Math.PI) * cameraYaw * 0.5F) * invertZoomProgress, ((double) (-Math.abs(MathHelper.cos(distanceWalked*crouch * (float) Math.PI) * cameraYaw)) * invertZoomProgress) * 1.140, 0.0D);// * 1.140, 0.0D);
            //matrixStack.translate((double) (Math.asin(-MathHelper.sin(distanceWalked*crouch * (float) Math.PI) * cameraYaw * 0.5F)) * invertZoomProgress, ((double) (Math.asin((-Math.abs(-MathHelper.cos(distanceWalked*crouch * (float) Math.PI) * cameraYaw))) * invertZoomProgress)) * 1.140, 0.0D);// * 1.140, 0.0D);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees((MathHelper.sin(distanceWalked*crouch * (float) Math.PI) * cameraYaw * 3.0F) * (float) invertZoomProgress));
            matrixStack.rotate(Vector3f.XP.rotationDegrees((Math.abs(MathHelper.cos(distanceWalked*crouch * (float) Math.PI - 0.2F) * cameraYaw) * 5.0F) * (float) invertZoomProgress));

            // Weapon movement clanting
            float rollingForceCrouch = mc.player.isCrouching() ? 4f : 1f;
            float rollingForceAim = AimingHandler.get().isAiming() ? 0.425f : 1f;
            /*
                Pretty much from CGM, was going to build something very similar for 0.3, movement update comes early I guess,
                all credit to Mr.Crayfish who developed this weapon roll code for CGM,
                all I added was scaling for other game actions and adjusted rolling values
            */
            float targetAngle = heldItem.getItem() instanceof GunItem ? mc.player.movementInput.moveStrafe * (6.65F * rollingForceCrouch * rollingForceAim) : 0F;
            this.immersiveWeaponRoll = MathHelper.approach(this.immersiveWeaponRoll, targetAngle, 0.335F);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(this.immersiveWeaponRoll));
        }

        if (event.getHand() == Hand.OFF_HAND)
        {
            if (heldItem.getItem() instanceof GunItem) {
                event.setCanceled(true);
                return;
            }

            float offhand = 1.0F - MathHelper.lerp(event.getPartialTicks(), this.prevOffhandTranslate, this.offhandTranslate);
            matrixStack.translate(0, offhand * -0.6F, 0);

            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && player.getHeldItemMainhand().getItem() instanceof GunItem) {
                Gun modifiedGun = ((GunItem) player.getHeldItemMainhand().getItem()).getModifiedGun(player.getHeldItemMainhand());
                if (!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem()) {
                    return;
                }
            }

            /* Makes the off hand item move out of view */
            matrixStack.translate(0, -2 * AimingHandler.get().getNormalisedAdsProgress(), 0);
        }

        LivingEntity entity = Minecraft.getInstance().player;
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(overrideModel.isEmpty() ? heldItem : overrideModel, entity.world, entity);
        float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
        float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
        float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
        float translateX = model.getItemCameraTransforms().firstperson_right.translation.getX();
        float translateY = model.getItemCameraTransforms().firstperson_right.translation.getY();
        float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ();

        matrixStack.push();

        Gun modifiedGun = gunItem.getModifiedGun(heldItem);
        int gunZoom = heldItem.getTag().getInt("currentZoom");

        if (AimingHandler.get().getNormalisedAdsProgress() > 0 && modifiedGun.canAimDownSight())
        {
            if (event.getHand() == Hand.MAIN_HAND) {
                this.xOffset = 0.0;
                this.yOffset = 0.0;
                this.zOffset = 0.0;
                Scope scope = Gun.getScope(heldItem);
                boolean isScopeOffsetType = Config.COMMON.gameplay.gameplayEnchancedScopeOffset.get();
                boolean isScopeRenderType = Config.COMMON.gameplay.scopeDoubleRender.get();
                /* Creates the required offsets to position the scope into the middle of the screen. */
                if (modifiedGun.canAttachType(IAttachment.Type.SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset();
                    if(scope.getAdditionalZoom() > 0)
                        viewFinderOffset = isScopeRenderType ? (isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset()) : (isScopeOffsetType || OptifineHelper.isShadersEnabled() ? scope.getViewFinderOffsetSpecialDR() : scope.getViewFinderOffsetDR()); // switches between either, but either must be populated

                    //if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    //if (isScopeRenderType) viewFinderOffset *= 0.735;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getScope();
                    xOffset = -translateX + -scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();

                }
                else if (modifiedGun.canAttachType(IAttachment.Type.OLD_SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || isScopeRenderType ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset(); // switches between either, but either must be populated
                    if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getOldScope();
                    xOffset = -translateX + -scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();

                }
                else if (modifiedGun.canAttachType(IAttachment.Type.PISTOL_SCOPE) && scope != null) {
                    double viewFinderOffset = isScopeOffsetType || isScopeRenderType ? scope.getViewFinderOffsetSpecial() : scope.getViewFinderOffset(); // switches between either, but either must be populated
                    if (OptifineHelper.isShadersEnabled()) viewFinderOffset *= 0.735;
                    Gun.ScaledPositioned scaledPos = modifiedGun.getModules().getAttachments().getPistolScope();
                    xOffset = -translateX + -scaledPos.getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - scaledPos.getYOffset()) * 0.0625 * scaleY - scope.getCenterOffset() * scaleY * 0.0625 * scaledPos.getScale();
                    zOffset = -translateZ - scaledPos.getZOffset() * 0.0625 * scaleZ + 0.72 - viewFinderOffset * scaleZ * scaledPos.getScale();
                }
                else if (!ArrayUtils.isEmpty(modifiedGun.getModules().getZoom()))
                {
                    xOffset = -translateX + modifiedGun.getModules().getZoom()[gunZoom].getXOffset() * 0.0625 * scaleX;
                    yOffset = -translateY + (8 - modifiedGun.getModules().getZoom()[gunZoom].getYOffset()) * 0.0625 * scaleY;
                    zOffset = -translateZ + modifiedGun.getModules().getZoom()[gunZoom].getZOffset() * 0.0625 * scaleZ;
                } 


                /* Controls the direction of the following translations, changes depending on the main hand. */
                float side = right ? 1.0F : -1.0F;
                //double transition = 1.0 - Math.pow(1.0 - AimingHandler.get().getNormalisedAdsProgress(), 2);

                QuadEaseOut easeOut = new QuadEaseOut();
                double transition = new SineEaseInOut().evaluate((float) AimingHandler.get().getNormalisedAdsProgress(), 0, 1);
                double transitionY = easeOut.evaluate(1.0f - (float) Math.abs(0.5-AimingHandler.get().getNormalisedAdsProgress())*2,0,1);

                /* Reverses the original first person translations */
                //matrixStack.translate(-0.56 * side * transition, 0.52 * transition, 0);
                matrixStack.translate(-0.56 * side * transition, 0.52 * (transition- 0.1*transitionY), 0);

                /* Reverses the first person translations of the item in order to position it in the center of the screen */
                matrixStack.translate(xOffset * side * transition, yOffset * transition, zOffset * transition);

                if(Config.COMMON.gameplay.realisticAimedBreathing.get()) {
                    /* Apply scope jitter*/
                    double scopeJitterOffset = 0.8;
                    if (entity.isCrouching())
                        scopeJitterOffset *= 0.30;
                    if (entity.isSprinting())
                        scopeJitterOffset *= 4;
                    if (entity.getMotion().getX() != 0.0 || entity.getMotion().getY() != 0.0 || entity.getMotion().getZ() != 0.0)
                        scopeJitterOffset *= 6.5;
                    if (scope == null)
                        scopeJitterOffset *= modifiedGun.getModules().getZoom()[gunZoom].getStabilityOffset();
                    else
                        scopeJitterOffset *= scope.getStabilityOffset();

                    double yOffsetRatio = ScopeJitterHandler.getInstance().getYOffsetRatio() * (0.0125 * 0.75 * scopeJitterOffset);
                    double xOffsetRatio = ScopeJitterHandler.getInstance().getXOffsetRatio() * (0.0085 * 0.875 * scopeJitterOffset);
                    Objects.requireNonNull(Minecraft.getInstance().player).rotationPitch += yOffsetRatio;
                    Objects.requireNonNull(Minecraft.getInstance().player).rotationYaw += xOffsetRatio;
                }
            }
        }

        float equipProgress = this.getEquipProgress(event.getPartialTicks());
        matrixStack.rotate(Vector3f.XP.rotationDegrees(equipProgress * -50F));

        HandSide hand = right ? HandSide.RIGHT : HandSide.LEFT;
        Objects.requireNonNull(entity);
        int blockLight = entity.isBurning() ? 15 : entity.world.getLightFor(LightType.BLOCK, new BlockPos(entity.getEyePosition(event.getPartialTicks())));
        blockLight += (this.entityIdForMuzzleFlash.contains(entity.getEntityId()) ? 3 : 0); // 3
        int packedLight = LightTexture.packLight(blockLight, entity.world.getLightFor(LightType.SKY, new BlockPos(entity.getEyePosition(event.getPartialTicks()))));

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        //this.renderReloadArm(matrixStack, event.getBuffers(), event.getLight(), modifiedGun, heldItem, hand);

        /* Translate the item position based on the hand side */
        int offset = right ? 1 : -1;
        matrixStack.translate(0.56 * offset, -0.52, -0.72);

        this.applySprintingTransforms(gunItem, hand, matrixStack, event.getPartialTicks());
        /* Applies recoil and reload rotations */
        this.applyRecoilTransforms(matrixStack, heldItem, modifiedGun);
        if(!isAnimated) this.applyReloadTransforms(matrixStack, hand, event.getPartialTicks(), heldItem);

        /* Renders the first persons arms from the grip type of the weapon */
        matrixStack.push();
        IHeldAnimation pose = modifiedGun.getGeneral().getGripType().getHeldAnimation();
        if(pose!=null) {
            if(!isAnimated) matrixStack.translate(-0.56, 0.52, 0.72);
            pose.renderFirstPersonArms(Minecraft.getInstance().player, hand, heldItem, matrixStack, event.getBuffers(), event.getLight(), event.getPartialTicks());
        }
        matrixStack.pop();


        /* Renders the weapon */
        ItemCameraTransforms.TransformType transformType = right ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
        this.renderWeapon(Minecraft.getInstance().player, heldItem, transformType, event.getMatrixStack(), event.getBuffers(), packedLight, event.getPartialTicks());
        matrixStack.pop();
    }
    private void applySprintingTransforms(GunItem modifiedGun, HandSide hand, MatrixStack matrixStack, float partialTicks)
    {
        GunAnimationController controller = GunAnimationController.fromItem(modifiedGun.getItem());
        if(controller == null)
        {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;
            float transition = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
            transition = (float) Math.sin((transition * Math.PI) / 2);
            matrixStack.translate(-0.25 * leftHanded * transition, -0.1 * transition, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45F * leftHanded * transition));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-25F * transition));
        }
        else if (modifiedGun.getGun().getGeneral().getGripType().getHeldAnimation().canApplySprintingAnimation() && !controller.isAnimationRunning()) {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;
            float transition = (this.prevSprintTransition + (this.sprintTransition - this.prevSprintTransition) * partialTicks) / 5F;
            transition = (float) Math.sin((transition * Math.PI) / 2);
            matrixStack.translate(-0.25 * leftHanded * transition, -0.1 * transition, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45F * leftHanded * transition));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-25F * transition));
        }
    }

    private void applyReloadTransforms(MatrixStack matrixStack, HandSide hand, float partialTicks, ItemStack modifiedGun) {
        /*float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks, stack);
        matrixStack.translate(0, 0.35 * reloadProgress, 0);
        matrixStack.translate(0, 0, -0.1 * reloadProgress);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(45F * reloadProgress));*/

        float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks,  modifiedGun);

        if(reloadProgress > 0) {
            float leftHanded = hand == HandSide.LEFT ? -1 : 1;

            matrixStack.translate(-0.25 * leftHanded, -0.1, 0);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(45F * leftHanded));
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-25F));
        }
    }

    public float kickReduction = 0;
    public float recoilReduction = 0;
    public double kick = 0;
    public float recoilLift = 0;
    public float recoilSwayAmount = 0;
    public float recoilSway = 0;
    public float weaponsHorizontalAngle = 0;

    private void applyRecoilTransforms(MatrixStack matrixStack, ItemStack item, Gun gun)
    {
        Minecraft mc = Minecraft.getInstance();
        double recoilNormal = RecoilHandler.get().getGunRecoilNormal();
        if (Gun.hasAttachmentEquipped(item, gun, IAttachment.Type.SCOPE)) {
            recoilNormal -= recoilNormal * (0.25 * AimingHandler.get().getNormalisedAdsProgress());
        }
        this.kickReduction = 1.0F - GunModifierHelper.getKickReduction(item);
        this.recoilReduction = 1.0F - GunModifierHelper.getRecoilModifier(item);
        this.kick = gun.getGeneral().getRecoilKick() * 0.0625 * recoilNormal * RecoilHandler.get().getAdsRecoilReduction(gun);
        //this.recoilLift = ((float) (gun.getGeneral().getRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        this.recoilLift = ((float) (RecoilHandler.get().getGunRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        this.recoilSwayAmount = ((float) (2F + 1F * (1.0 - AimingHandler.get().getNormalisedAdsProgress())));// * 1.5f;
        this.recoilSway = ((float) ((RecoilHandler.get().getGunRecoilRandom() * this.recoilSwayAmount - this.recoilSwayAmount / 2F) * recoilNormal)) / 2;
        if (item.getTag().getInt("CurrentFireMode") == 1)
            this.recoilSway *= 0.375;
        if (mc.player.isCrouching()) {
            this.recoilSway *= 0.75;
            this.recoilLift *= 0.875;
        }
    //    this.weaponsHorizontalAngle = ((float) (gun.getGeneral().getHorizontalRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));
        this.weaponsHorizontalAngle = ((float) (RecoilHandler.get().getGunHorizontalRecoilAngle() * recoilNormal) * (float) RecoilHandler.get().getAdsRecoilReduction(gun));

        matrixStack.translate(0, 0, this.kick * this.kickReduction);
        matrixStack.translate(0, 0, 0.35);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(this.recoilSway * this.recoilReduction * this.weaponsHorizontalAngle));
        matrixStack.rotate(Vector3f.ZN.rotationDegrees(this.recoilSway * this.weaponsHorizontalAngle * this.recoilReduction)); // seems to be interesting to increase the force of
        //matrixStack.rotate(Vector3f.ZP.rotationDegrees(recoilSway * 2.5f * recoilReduction)); // seems to be interesting to increase the force of
        if(gun.getGeneral().getWeaponRecoilDuration() != 0)
            matrixStack.rotate(Vector3f.XP.rotationDegrees(this.recoilLift * this.recoilReduction));
        matrixStack.translate(0, 0, -0.35);
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START))
            return;

        Minecraft mc = Minecraft.getInstance();
        if (!mc.isGameFocused())
            return;

        PlayerEntity player = mc.player;
        if (player == null)
            return;

        if (Minecraft.getInstance().gameSettings.getPointOfView() != PointOfView.FIRST_PERSON)
            return;

        ItemStack heldItem = player.getHeldItem(Hand.MAIN_HAND);
        if (heldItem.isEmpty())
            return;

        if (player.isHandActive() && player.getActiveHand() == Hand.MAIN_HAND && heldItem.getItem() instanceof GrenadeItem)
        {
            if (!((GrenadeItem) heldItem.getItem()).canCook())
                return;

            int duration = player.getItemInUseMaxCount();
            if (duration >= 10) {
                float cookTime = 1.0F - ((float) (duration - 10) / (float) (player.getActiveItemStack().getUseDuration() - 10));
                if (cookTime > 0.0F) {
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

        /*if (heldItem.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) heldItem.getItem()).getGun(); // Cooldown stuffs, i should look into this
            if (!gun.getGeneral().isAuto()) {
                float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
                if (coolDown > 0.0F) {
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
        }*/
    }

    @SubscribeEvent
    public void onRenderHeldItem(RenderItemEvent.Held.Pre event) {
        Hand hand = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? event.getHandSide() == HandSide.RIGHT ? Hand.MAIN_HAND : Hand.OFF_HAND : event.getHandSide() == HandSide.LEFT ? Hand.MAIN_HAND : Hand.OFF_HAND;
        LivingEntity entity = event.getEntity();
        ItemStack heldItem = entity.getHeldItem(hand);

        if (hand == Hand.OFF_HAND) {
            if (heldItem.getItem() instanceof GunItem) {
                event.setCanceled(true);
                return;
            }

            if (entity.getHeldItemMainhand().getItem() instanceof GunItem) {
                Gun modifiedGun = ((GunItem) entity.getHeldItemMainhand().getItem()).getModifiedGun(entity.getHeldItemMainhand());
                if (!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem()) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if (heldItem.getItem() instanceof GunItem) {
            event.setCanceled(true);

            if (heldItem.getTag() != null) {
                CompoundNBT compound = heldItem.getTag();
                if (compound.contains("Scale", Constants.NBT.TAG_FLOAT)) {
                    float scale = compound.getFloat("Scale");
                    event.getMatrixStack().scale(scale, scale, scale);
                }
            }

            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if (entity instanceof PlayerEntity) {
                gun.getGeneral().getGripType().getHeldAnimation().applyHeldItemTransforms((PlayerEntity) entity, hand, AimingHandler.get().getAimProgress((PlayerEntity) entity, event.getPartialTicks()), event.getMatrixStack(), event.getRenderTypeBuffer());
            }
            this.renderWeapon(entity, heldItem, event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onSetupAngles(PlayerModelEvent.SetupAngles.Post event) {
        // Dirty hack to reject first person arms
        if (event.getAgeInTicks() == 0F) {
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
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            PlayerModel model = event.getModelPlayer();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerModelRotation(player, model, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialTicks()));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
        }
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest) {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerPreRender(player, Hand.MAIN_HAND, AimingHandler.get().getAimProgress((PlayerEntity) event.getEntity(), event.getPartialRenderTick()), event.getMatrixStack(), event.getBuffers());
        }
    }

    @SubscribeEvent
    public void onModelRender(PlayerModelEvent.Render.Pre event) {
        PlayerEntity player = event.getPlayer();
        ItemStack offHandStack = player.getHeldItemOffhand();
        if (offHandStack.getItem() instanceof GunItem) {
            switch (player.getPrimaryHand().opposite()) {
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
    public void onRenderPlayer(PlayerModelEvent.Render.Post event) {
        MatrixStack matrixStack = event.getMatrixStack();
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItemOffhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem) {
            matrixStack.push();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if (gun.getGeneral().getGripType().getHeldAnimation().applyOffhandTransforms(player, event.getModelPlayer(), heldItem, matrixStack, event.getPartialTicks())) {
                IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, matrixStack, buffer, event.getLight(), event.getPartialTicks());
            }
            matrixStack.pop();
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderItemFrame(RenderItemEvent.ItemFrame.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getMatrixStack(), event.getRenderTypeBuffer(), event.getLight(), event.getPartialTicks()));
    }

    public boolean renderWeapon(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof GunItem) {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;
            if (stack.getTag() != null) {
                if (stack.getTag().contains("Model", Constants.NBT.TAG_COMPOUND)) {
                    model = ItemStack.read(stack.getTag().getCompound("Model"));
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType, entity);

            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);
            this.renderMuzzleFlash(entity, matrixStack, renderTypeBuffer, stack, transformType);

            matrixStack.pop();
            return true;
        }
        return false;
    }

    public boolean renderScope(LivingEntity entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof ScopeItem) {
            matrixStack.push();

            ItemStack model = ItemStack.EMPTY;

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, matrixStack, transformType, entity);

            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, matrixStack, renderTypeBuffer, light, partialTicks);//matrixStack, renderTypeBuffer, light, partialTicks);
            matrixStack.pop();
            return true;
        }
        return false;
    }
    /*public boolean renderColored(LivingEntity entity, IBakedModel model, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {

            matrixStack.push();

            RenderUtil.applyTransformTypeIB(model, matrixStack, transformType, entity);

            this.renderColoredModel(entity, transformType, model, matrixStack, renderTypeBuffer, light, partialTicks);//matrixStack, renderTypeBuffer, light, partialTicks);
            //this.renderAttachments(entity, transformType, stack, matrixStack, renderTypeBuffer, light, partialTicks);
            //this.renderMuzzleFlash(entity, matrixStack, renderTypeBuffer, stack, transformType);

            matrixStack.pop();
            return true;

        //return false;
    }*/
    private void renderGun(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        /*if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI))
            return;*/
        if(stack.getItem() instanceof ITimelessAnimated) RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        if (ModelOverrides.hasModel(stack)) {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if (model != null) {
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
            }
        } else {
            RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        }
    }
    /*private void renderColoredModel(LivingEntity entity, ItemCameraTransforms.TransformType transformType, IBakedModel model, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks)
    {
        //if(stack.getItem() instanceof ITimelessAnimated) RenderUtil.renderModel(stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY, entity);
        IOverrideModel model = ModelOverrides.getModel(stack);
        if (model != null) {
            model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
        }
    }*/

    private void renderAttachments(LivingEntity entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, float partialTicks) {
        if (stack.getItem() instanceof GunItem) {
            Gun gun = ((GunItem) stack.getItem()).getModifiedGun(stack);
            CompoundNBT gunTag = stack.getOrCreateTag();
            CompoundNBT attachments = gunTag.getCompound("Attachments");
            for (String tagKey : attachments.keySet()) {
                IAttachment.Type type = IAttachment.Type.byTagKey(tagKey);
                if (gun.canAttachType(type)) {
                    ItemStack attachmentStack = Gun.getAttachment(type, stack);
                    if (!attachmentStack.isEmpty()) {
                        Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                        if (positioned != null) {
                            matrixStack.push();
                            GunAnimationController controller = GunAnimationController.fromItem(stack.getItem());
                            if(controller!=null) controller.applyAttachmentsTransform(stack, transformType, entity, matrixStack);
                            double displayX = positioned.getXOffset() * 0.0625;
                            double displayY = positioned.getYOffset() * 0.0625;
                            double displayZ = positioned.getZOffset() * 0.0625;
                            matrixStack.translate(displayX, displayY, displayZ);
                            matrixStack.translate(0, -0.5, 0);
                            matrixStack.scale((float) positioned.getScale(), (float) positioned.getScale(), (float) positioned.getScale());

                            IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                            if (model != null) {
                                model.render(partialTicks, transformType, attachmentStack, stack, entity, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            } else {
                                RenderUtil.renderModel(attachmentStack, stack, matrixStack, renderTypeBuffer, light, OverlayTexture.NO_OVERLAY);
                            }

                            matrixStack.pop();
                        }
                    }
                }
            }
        }
    }

    private void renderMuzzleFlash(LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack weapon, ItemCameraTransforms.TransformType transformType) {
        Gun modifiedGun = ((GunItem) weapon.getItem()).getModifiedGun(weapon);
        if (modifiedGun.getDisplay().getFlash() == null) {
            return;
        }
        if (modifiedGun.canAttachType(IAttachment.Type.BARREL) && GunModifierHelper.isSilencedFire(weapon)) return;

        if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            if (this.entityIdForMuzzleFlash.contains(entity.getEntityId())) {
                float randomValue = this.entityIdToRandomValue.get(entity.getEntityId());
                this.drawMuzzleFlash(weapon, modifiedGun, randomValue, randomValue >= 0.5F, matrixStack, buffer);
            }
        }
    }

    private void drawMuzzleFlash(ItemStack weapon, Gun modifiedGun, float random, boolean flip, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        matrixStack.push();

        Gun.Positioned muzzleFlash = modifiedGun.getDisplay().getFlash();
        if (muzzleFlash == null)
            return;

        double displayX = muzzleFlash.getXOffset() * 0.0625;
        double displayY = muzzleFlash.getYOffset() * 0.0625;
        double displayZ = (muzzleFlash.getZOffset()+this.muzzleExtraOnEnch) * 0.0625;

        if(GunRenderingHandler.get().muzzleExtraOnEnch != 0)
            this.muzzleExtraOnEnch = 0;

        matrixStack.translate(displayX, displayY, displayZ);
        matrixStack.translate(0, -0.5, 0);

        ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, weapon);
        if (!barrelStack.isEmpty() && barrelStack.getItem() instanceof IBarrel) {
            Barrel barrel = ((IBarrel) barrelStack.getItem()).getProperties();
            Gun.ScaledPositioned positioned = modifiedGun.getModules().getAttachments().getBarrel();
            if (positioned != null) {
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

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        IVertexBuilder builder = buffer.getBuffer(GunRenderType.getMuzzleFlash());
        builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, size, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
        builder.pos(matrix, 0, size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();

        /*float smokeSize = (float) modifiedGun.getDisplay().getFlash().getSmokeSize();
        builder = buffer.getBuffer(GunRenderType.getMuzzleSmoke());
        matrixStack.translate(size / 2,size / 2,0);
        matrixStack.translate(-smokeSize / 2, -smokeSize / 2, 0);
        builder.pos(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, smokeSize, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 1.0F).lightmap(15728880).endVertex();
        builder.pos(matrix, smokeSize, smokeSize, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(0, 0).lightmap(15728880).endVertex();
        builder.pos(matrix, 0, smokeSize, 0).color(1.0F, 1.0F, 1.0F, 1.0F).tex(1.0F, 0).lightmap(15728880).endVertex();
*/
        matrixStack.pop();
    }

    /*private void renderReloadArm(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, Gun modifiedGun, ItemStack stack, HandSide hand) {
        *//*if (stack.getItem() instanceof IAnimatable && stack.getItem() instanceof GunItem) {

        }*//*
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.player.ticksExisted < ReloadHandler.get().getStartReloadTick() || ReloadHandler.get().getReloadTimer() != 5)
            return;

        Item item = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if (item == null)
            return;

        matrixStack.push();

        float interval = GunEnchantmentHelper.getReloadInterval(stack);
        float reload = ((mc.player.ticksExisted - ReloadHandler.get().getStartReloadTick() + mc.getRenderPartialTicks()) % interval) / interval;
        float percent = 1.0F - reload;
        if (percent >= 0.5F) {
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

        if (reload < 0.5F) {
            matrixStack.push();
            matrixStack.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(180F));
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            ItemStack ammo = new ItemStack(item, modifiedGun.getReloads().getReloadAmount());
            IBakedModel model = RenderUtil.getModel(ammo);
            boolean isModel = model.isGui3d();
            this.random.setSeed(Item.getIdFromItem(item));
            int count = Math.min(modifiedGun.getReloads().getReloadAmount(), 5);
            for (int i = 0; i < count; ++i) {
                matrixStack.push();
                if (i > 0) {
                    if (isModel) {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        float z = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
                        matrixStack.translate(x, y, z);
                    } else {
                        float x = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        float y = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                        matrixStack.translate(x, y, 0);
                    }
                }

                RenderUtil.renderModel(ammo, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, matrixStack, buffer, light, OverlayTexture.NO_OVERLAY, null);
                matrixStack.pop();

                if (!isModel) {
                    matrixStack.translate(0.0, 0.0, 0.09375F);
                }
            }
            matrixStack.pop();
        }
        matrixStack.pop();
    }*/

    /**
     * A temporary hack to get the equip progress until Forge fixes the issue.
     *
     * @return
     */
    private float getEquipProgress(float partialTicks) {
        if (this.equippedProgressMainHandField == null) {
            this.equippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187469_f");
            this.equippedProgressMainHandField.setAccessible(true);
        }
        if (this.prevEquippedProgressMainHandField == null) {
            this.prevEquippedProgressMainHandField = ObfuscationReflectionHelper.findField(FirstPersonRenderer.class, "field_187470_g");
            this.prevEquippedProgressMainHandField.setAccessible(true);
        }
        FirstPersonRenderer firstPersonRenderer = Minecraft.getInstance().getFirstPersonRenderer();
        try {
            float equippedProgressMainHand = (float) this.equippedProgressMainHandField.get(firstPersonRenderer);
            float prevEquippedProgressMainHand = (float) this.prevEquippedProgressMainHandField.get(firstPersonRenderer);
            return 1.0F - MathHelper.lerp(partialTicks, prevEquippedProgressMainHand, equippedProgressMainHand);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0.0F;
    }
}
