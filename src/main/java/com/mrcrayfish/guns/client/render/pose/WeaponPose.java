package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.render.IHeldAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * A simple class that handles interpolating between different poses depending on the rotation pitch
 * of the player. Used for pointing the weapon in the same direction the playing is looking.
 * <p>
 * Author: MrCrayfish
 */
public abstract class WeaponPose implements IHeldAnimation
{
    private AimPose upPose;
    private AimPose forwardPose;
    private AimPose downPose;

    public WeaponPose()
    {
        this.upPose = this.getUpPose();
        this.forwardPose = this.getForwardPose();
        this.downPose = this.getDownPose();
    }

    /**
     * Gets the pose of the player when looking directly up
     */
    protected abstract AimPose getUpPose();

    /**
     * Gets the pose of the player when looking directly forward
     */
    protected abstract AimPose getForwardPose();

    /**
     * Gets the pose of the player when looking directly down
     */
    protected abstract AimPose getDownPose();

    /**
     * If this weapon pose has an aim pose
     */
    protected boolean hasAimPose()
    {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerModelRotation(Player player, ModelPart rightArm, ModelPart leftArm, ModelPart head, InteractionHand hand, float aimProgress)
    {
        Minecraft mc = Minecraft.getInstance();
        boolean right = mc.options.mainHand == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
        ModelPart mainArm = right ? rightArm : leftArm;
        ModelPart secondaryArm = right ? leftArm : rightArm;

        float angle = this.getPlayerPitch(player);
        float angleAbs = Math.abs(angle);
        float zoom = this.hasAimPose() ? aimProgress : 0F;
        AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
        this.applyAimPose(targetPose, mainArm, secondaryArm, angleAbs, zoom, right ? 1 : -1, player.isCrouching());
    }

    /**
     * Gets the pitch of the player with an except when a screen is open. If a screen is open, the
     * pitch will always be zero.
     *
     * @param player the player the pose is being applied to
     * @return the current pitch of the player
     */
    protected float getPlayerPitch(Player player)
    {
        if(Minecraft.getInstance().getCameraEntity() == player && Minecraft.getInstance().screen != null)
        {
            return 0F;
        }
        return Mth.lerp(Minecraft.getInstance().getFrameTime(), player.xRotO, player.getXRot()) / 90F;
    }

    private void applyAimPose(AimPose targetPose, ModelPart rightArm, ModelPart leftArm, float partial, float zoom, float offhand, boolean sneaking)
    {
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getRightArm(), targetPose.getAiming().getRightArm(), this.forwardPose.getIdle().getRightArm(), this.forwardPose.getAiming().getRightArm(), rightArm, partial, zoom, offhand, sneaking);
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getLeftArm(), targetPose.getAiming().getLeftArm(), this.forwardPose.getIdle().getLeftArm(), this.forwardPose.getAiming().getLeftArm(), leftArm, partial, zoom, offhand, sneaking);
    }

    private void applyLimbPoseToModelRenderer(LimbPose targetIdlePose, LimbPose targetAimingPose, LimbPose idlePose, LimbPose aimingPose, ModelPart renderer, float partial, float zoom, float leftHanded, boolean sneaking)
    {
        renderer.xRot = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleX(), targetAimingPose.getRotationAngleX(), idlePose.getRotationAngleX(), aimingPose.getRotationAngleX(), renderer.xRot, partial, zoom, 1F));
        renderer.yRot = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleY(), targetAimingPose.getRotationAngleY(), idlePose.getRotationAngleY(), aimingPose.getRotationAngleY(), renderer.yRot, partial, zoom, leftHanded));
        renderer.zRot = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleZ(), targetAimingPose.getRotationAngleZ(), idlePose.getRotationAngleZ(), aimingPose.getRotationAngleZ(), renderer.zRot, partial, zoom, leftHanded));
        renderer.x = this.getValue(targetIdlePose.getRotationPointX(), targetAimingPose.getRotationPointX(), idlePose.getRotationPointX(), aimingPose.getRotationPointX(), renderer.x, partial, zoom, leftHanded);
        renderer.y = this.getValue(targetIdlePose.getRotationPointY(), targetAimingPose.getRotationPointY(), idlePose.getRotationPointY(), aimingPose.getRotationPointY(), renderer.y, partial, zoom, 1F) + (sneaking ? 2F : 0F);
        renderer.z = this.getValue(targetIdlePose.getRotationPointZ(), targetAimingPose.getRotationPointZ(), idlePose.getRotationPointZ(), aimingPose.getRotationPointZ(), renderer.z, partial, zoom, 1F);
    }

    private float getValue(@Nullable Float t1, @Nullable Float t2, Float s1, Float s2, Float def, float partial, float zoom, float leftHanded)
    {
        float start = t1 != null && s1 != null ? (s1 + (t1 - s1) * partial) * leftHanded : (s1 != null ? s1 * leftHanded : def);
        float end = t2 != null && s2 != null ? (s2 + (t2 - s2) * partial) * leftHanded : (s2 != null ? s2 * leftHanded : def);
        return Mth.lerp(zoom, start, end);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerPreRender(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer)
    {
        boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
        float angle = this.getPlayerPitch(player);
        float angleAbs = Math.abs(angle);
        float zoom = this.hasAimPose() ? aimProgress : 0F;
        AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
        float rightOffset = this.getValue(targetPose.getIdle().getRenderYawOffset(), targetPose.getAiming().getRenderYawOffset(), this.forwardPose.getIdle().getRenderYawOffset(), this.forwardPose.getAiming().getRenderYawOffset(), 0F, angleAbs, zoom, right ? 1 : -1);
        player.yBodyRotO = player.yRotO + rightOffset;
        player.yBodyRot = player.getYRot() + rightOffset;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer)
    {
        if(hand == InteractionHand.MAIN_HAND)
        {
            boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT;
            float leftHanded = right ? 1 : -1;
            poseStack.translate(0, 0, 0.05);

            float angle = this.getPlayerPitch(player);
            float angleAbs = Math.abs(angle);
            float zoom = this.hasAimPose() ? aimProgress : 0F;
            AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;

            float translateX = this.getValue(targetPose.getIdle().getItemTranslate().x(), targetPose.getAiming().getItemTranslate().x(), this.forwardPose.getIdle().getItemTranslate().x(), this.forwardPose.getAiming().getItemTranslate().x(), 0F, angleAbs, zoom, 1F);
            float translateY = this.getValue(targetPose.getIdle().getItemTranslate().y(), targetPose.getAiming().getItemTranslate().y(), this.forwardPose.getIdle().getItemTranslate().y(), this.forwardPose.getAiming().getItemTranslate().y(), 0F, angleAbs, zoom, 1F);
            float translateZ = this.getValue(targetPose.getIdle().getItemTranslate().z(), targetPose.getAiming().getItemTranslate().z(), this.forwardPose.getIdle().getItemTranslate().z(), this.forwardPose.getAiming().getItemTranslate().z(), 0F, angleAbs, zoom, 1F);
            poseStack.translate(translateX * 0.0625 * leftHanded, translateY * 0.0625, translateZ * 0.0625);

            float rotateX = this.getValue(targetPose.getIdle().getItemRotation().x(), targetPose.getAiming().getItemRotation().x(), this.forwardPose.getIdle().getItemRotation().x(), this.forwardPose.getAiming().getItemRotation().x(), 0F, angleAbs, zoom, 1F);
            float rotateY = this.getValue(targetPose.getIdle().getItemRotation().y(), targetPose.getAiming().getItemRotation().y(), this.forwardPose.getIdle().getItemRotation().y(), this.forwardPose.getAiming().getItemRotation().y(), 0F, angleAbs, zoom, 1F);
            float rotateZ = this.getValue(targetPose.getIdle().getItemRotation().z(), targetPose.getAiming().getItemRotation().z(), this.forwardPose.getIdle().getItemRotation().z(), this.forwardPose.getAiming().getItemRotation().z(), 0F, angleAbs, zoom, 1F);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(rotateX));
            poseStack.mulPose(Vector3f.YP.rotationDegrees(rotateY * leftHanded));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(rotateZ * leftHanded));
        }
    }
}
