package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.GripType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import com.mojang.math.Vector3f;
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
    public void applyPlayerModelRotation(Player player, ModelPart rightArm, ModelPart leftArm, ModelPart head, InteractionHand hand, float aimProgress)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
            ModelPart mainArm = right ? rightArm : leftArm;
            ModelPart secondaryArm = right ? leftArm : rightArm;
            mainArm.xRot = (float) Math.toRadians(-15F);
            mainArm.yRot = (float) Math.toRadians(-45F) * (right ? 1F : -1F);
            mainArm.zRot = (float) Math.toRadians(0F);
            secondaryArm.xRot = (float) Math.toRadians(-45F);
            secondaryArm.yRot = (float) Math.toRadians(30F) * (right ? 1F : -1F);
            secondaryArm.zRot = (float) Math.toRadians(0F);
        }
        else
        {
            super.applyPlayerModelRotation(player, rightArm, leftArm, head, hand, aimProgress);
        }
    }

    @Override
    public void applyPlayerPreRender(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            boolean right = Minecraft.getInstance().options.mainHand == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
            player.yBodyRotO = player.yRotO + 45F * (right ? 1F : -1F);
            player.yBodyRot = player.getYRot() + 45F * (right ? 1F : -1F);
        }
        else
        {
            super.applyPlayerPreRender(player, hand, aimProgress, poseStack, buffer);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            if(hand == InteractionHand.OFF_HAND)
            {
                poseStack.translate(0, -10 * 0.0625F, 0);
                poseStack.translate(0, 0, -2 * 0.0625F);
            }
        }
        else
        {
            super.applyHeldItemTransforms(player, hand, aimProgress, poseStack, buffer);
        }
    }

    @Override
    public boolean applyOffhandTransforms(Player player, PlayerModel model, ItemStack stack, PoseStack poseStack, float partialTicks)
    {
        return GripType.applyBackTransforms(player, poseStack);
    }

    @Override
    public boolean canApplySprintingAnimation()
    {
        return false;
    }
}
