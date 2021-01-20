package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.common.GripType;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public class MiniGunPose extends WeaponPose
{
    @Override
    protected AimPose getUpPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(45F).setItemRotation(new Vector3f(10F, 0F, 0F)).setRightArm(new LimbPose().setRotationAngleX(-100F).setRotationAngleY(-45F).setRotationAngleZ(0F).setRotationPointY(2)).setLeftArm(new LimbPose().setRotationAngleX(-150F).setRotationAngleY(40F).setRotationAngleZ(-10F).setRotationPointY(1));
        return pose;
    }

    @Override
    protected AimPose getForwardPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(45F).setRightArm(new LimbPose().setRotationAngleX(-15F).setRotationAngleY(-45F).setRotationAngleZ(0F).setRotationPointY(2)).setLeftArm(new LimbPose().setRotationAngleX(-45F).setRotationAngleY(30F).setRotationAngleZ(0F).setRotationPointY(2));
        return pose;
    }

    @Override
    protected AimPose getDownPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(45F).setItemRotation(new Vector3f(-50F, 0F, 0F)).setItemTranslate(new Vector3f(0F, 0F, 1F)).setRightArm(new LimbPose().setRotationAngleX(0F).setRotationAngleY(-45F).setRotationAngleZ(0F).setRotationPointY(1)).setLeftArm(new LimbPose().setRotationAngleX(-25F).setRotationAngleY(30F).setRotationAngleZ(15F).setRotationPointY(4));
        return pose;
    }

    @Override
    protected boolean hasAimPose()
    {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        super.applyHeldItemTransforms(player,hand, aimProgress, matrixStack, buffer);
    }

    @Override
    public boolean applyOffhandTransforms(PlayerEntity player, PlayerModel model, ItemStack stack, MatrixStack matrixStack, float partialTicks)
    {
        return GripType.applyBackTransforms(player, matrixStack);
    }

    @Override
    public boolean canApplySprintingAnimation()
    {
        return false;
    }
}
