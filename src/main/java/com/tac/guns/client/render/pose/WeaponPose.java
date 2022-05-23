package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.IHeldAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * A simple class that handles interpolating between different poses depending on the rotation pitch
 * of the player. Used for pointing the weapon in the same direction the playing is looking.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
    {
        Minecraft mc = Minecraft.getInstance();
        boolean right = mc.gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

        float angle = this.getPlayerPitch(player);
        float angleAbs = Math.abs(angle);
        float zoom = this.hasAimPose() ? aimProgress : 0F;
        AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
        this.applyAimPose(targetPose, mainArm, secondaryArm, angleAbs, zoom, right ? 1 : -1, model.isSneak);
    }

    /**
     * Gets the pitch of the player with an except when a screen is open. If a screen is open, the
     * pitch will always be zero.
     *
     * @param player the player the pose is being applied to
     * @return the current pitch of the player
     */
    protected float getPlayerPitch(PlayerEntity player)
    {
        if(Minecraft.getInstance().getRenderViewEntity() == player && Minecraft.getInstance().currentScreen != null)
        {
            return 0F;
        }
        return MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), player.prevRotationPitch, player.rotationPitch) / 90F;
    }

    private void applyAimPose(AimPose targetPose, ModelRenderer rightArm, ModelRenderer leftArm, float partial, float zoom, float offhand, boolean sneaking)
    {
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getRightArm(), targetPose.getAiming().getRightArm(), this.forwardPose.getIdle().getRightArm(), this.forwardPose.getAiming().getRightArm(), rightArm, partial, zoom, offhand, sneaking);
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getLeftArm(), targetPose.getAiming().getLeftArm(), this.forwardPose.getIdle().getLeftArm(), this.forwardPose.getAiming().getLeftArm(), leftArm, partial, zoom, offhand, sneaking);
    }

    private void applyLimbPoseToModelRenderer(LimbPose targetIdlePose, LimbPose targetAimingPose, LimbPose idlePose, LimbPose aimingPose, ModelRenderer renderer, float partial, float zoom, float leftHanded, boolean sneaking)
    {
        renderer.rotateAngleX = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleX(), targetAimingPose.getRotationAngleX(), idlePose.getRotationAngleX(), aimingPose.getRotationAngleX(), renderer.rotateAngleX, partial, zoom, 1F));
        renderer.rotateAngleY = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleY(), targetAimingPose.getRotationAngleY(), idlePose.getRotationAngleY(), aimingPose.getRotationAngleY(), renderer.rotateAngleY, partial, zoom, leftHanded));
        renderer.rotateAngleZ = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleZ(), targetAimingPose.getRotationAngleZ(), idlePose.getRotationAngleZ(), aimingPose.getRotationAngleZ(), renderer.rotateAngleZ, partial, zoom, leftHanded));
        renderer.rotationPointX = this.getValue(targetIdlePose.getRotationPointX(), targetAimingPose.getRotationPointX(), idlePose.getRotationPointX(), aimingPose.getRotationPointX(), renderer.rotationPointX, partial, zoom, leftHanded);
        renderer.rotationPointY = this.getValue(targetIdlePose.getRotationPointY(), targetAimingPose.getRotationPointY(), idlePose.getRotationPointY(), aimingPose.getRotationPointY(), renderer.rotationPointY, partial, zoom, 1F) + (sneaking ? 2F : 0F);
        renderer.rotationPointZ = this.getValue(targetIdlePose.getRotationPointZ(), targetAimingPose.getRotationPointZ(), idlePose.getRotationPointZ(), aimingPose.getRotationPointZ(), renderer.rotationPointZ, partial, zoom, 1F);
    }

    private float getValue(@Nullable Float t1, @Nullable Float t2, Float s1, Float s2, Float def, float partial, float zoom, float leftHanded)
    {
        float start = t1 != null && s1 != null ? (s1 + (t1 - s1) * partial) * leftHanded : (s1 != null ? s1 * leftHanded : def);
        float end = t2 != null && s2 != null ? (s2 + (t2 - s2) * partial) * leftHanded : (s2 != null ? s2 * leftHanded : def);
        return MathHelper.lerp(zoom, start, end);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        float angle = this.getPlayerPitch(player);
        float angleAbs = Math.abs(angle);
        float zoom = this.hasAimPose() ? aimProgress : 0F;
        AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
        float rightOffset = this.getValue(targetPose.getIdle().getRenderYawOffset(), targetPose.getAiming().getRenderYawOffset(), this.forwardPose.getIdle().getRenderYawOffset(), this.forwardPose.getAiming().getRenderYawOffset(), 0F, angleAbs, zoom, right ? 1 : -1);
        player.prevRenderYawOffset = player.prevRotationYaw + rightOffset;
        player.renderYawOffset = player.rotationYaw + rightOffset;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        if(hand == Hand.MAIN_HAND)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT;
            float leftHanded = right ? 1 : -1;
            matrixStack.translate(0, 0, 0.05);

            float angle = this.getPlayerPitch(player);
            float angleAbs = Math.abs(angle);
            float zoom = this.hasAimPose() ? aimProgress : 0F;
            AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;

            float translateX = this.getValue(targetPose.getIdle().getItemTranslate().getX(), targetPose.getAiming().getItemTranslate().getX(), this.forwardPose.getIdle().getItemTranslate().getX(), this.forwardPose.getAiming().getItemTranslate().getX(), 0F, angleAbs, zoom, 1F);
            float translateY = this.getValue(targetPose.getIdle().getItemTranslate().getY(), targetPose.getAiming().getItemTranslate().getY(), this.forwardPose.getIdle().getItemTranslate().getY(), this.forwardPose.getAiming().getItemTranslate().getY(), 0F, angleAbs, zoom, 1F);
            float translateZ = this.getValue(targetPose.getIdle().getItemTranslate().getZ(), targetPose.getAiming().getItemTranslate().getZ(), this.forwardPose.getIdle().getItemTranslate().getZ(), this.forwardPose.getAiming().getItemTranslate().getZ(), 0F, angleAbs, zoom, 1F);
            matrixStack.translate(translateX * 0.0625 * leftHanded, translateY * 0.0625, translateZ * 0.0625);

            float mulPoseX = this.getValue(targetPose.getIdle().getItemRotation().getX(), targetPose.getAiming().getItemRotation().getX(), this.forwardPose.getIdle().getItemRotation().getX(), this.forwardPose.getAiming().getItemRotation().getX(), 0F, angleAbs, zoom, 1F);
            float mulPoseY = this.getValue(targetPose.getIdle().getItemRotation().getY(), targetPose.getAiming().getItemRotation().getY(), this.forwardPose.getIdle().getItemRotation().getY(), this.forwardPose.getAiming().getItemRotation().getY(), 0F, angleAbs, zoom, 1F);
            float mulPoseZ = this.getValue(targetPose.getIdle().getItemRotation().getZ(), targetPose.getAiming().getItemRotation().getZ(), this.forwardPose.getIdle().getItemRotation().getZ(), this.forwardPose.getAiming().getItemRotation().getZ(), 0F, angleAbs, zoom, 1F);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(mulPoseX));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(mulPoseY * leftHanded));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(mulPoseZ * leftHanded));
        }
    }
}
