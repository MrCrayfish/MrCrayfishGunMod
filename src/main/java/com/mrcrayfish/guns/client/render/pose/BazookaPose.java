package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.common.GripType;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: MrCrayfish
 */
public class BazookaPose extends WeaponPose
{
    @Override
    protected AimPose getUpPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(35F).setItemRotation(new Vector3f(10F, 0F, 0F)).setRightArm(new LimbPose().setRotationAngleX(-170F).setRotationAngleY(-35F).setRotationAngleZ(0F).setRotationPointY(4).setRotationPointZ(-2)).setLeftArm(new LimbPose().setRotationAngleX(-130F).setRotationAngleY(65F).setRotationAngleZ(0F).setRotationPointX(3).setRotationPointZ(1));
        return pose;
    }

    @Override
    protected AimPose getForwardPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(35F).setRightArm(new LimbPose().setRotationAngleX(-90F).setRotationAngleY(-35F).setRotationAngleZ(0F).setRotationPointY(2).setRotationPointZ(0)).setLeftArm(new LimbPose().setRotationAngleX(-91F).setRotationAngleY(35F).setRotationAngleZ(0F).setRotationPointX(4).setRotationPointZ(0));
        return pose;
    }

    @Override
    protected AimPose getDownPose()
    {
        AimPose pose = new AimPose();
        pose.getIdle().setRenderYawOffset(35F).setRightArm(new LimbPose().setRotationAngleX(-10F).setRotationAngleY(-35F).setRotationAngleZ(0F).setRotationPointY(2).setRotationPointZ(0)).setLeftArm(new LimbPose().setRotationAngleX(-10F).setRotationAngleY(15F).setRotationAngleZ(30F).setRotationPointX(4).setRotationPointZ(0));
        return pose;
    }

    @Override
    protected boolean hasAimPose()
    {
        return false;
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
