package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.common.GripType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;
            mainArm.rotateAngleX = (float) Math.toRadians(-15F);
            mainArm.rotateAngleY = (float) Math.toRadians(-45F) * (right ? 1F : -1F);
            mainArm.rotateAngleZ = (float) Math.toRadians(0F);
            secondaryArm.rotateAngleX = (float) Math.toRadians(-45F);
            secondaryArm.rotateAngleY = (float) Math.toRadians(30F) * (right ? 1F : -1F);
            secondaryArm.rotateAngleZ = (float) Math.toRadians(0F);
        }
        else
        {
            super.applyPlayerModelRotation(player, model, hand, aimProgress);
        }
    }

    @Override
    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + 45F * (right ? 1F : -1F);
            player.renderYawOffset = player.rotationYaw + 45F * (right ? 1F : -1F);
        }
        else
        {
            super.applyPlayerPreRender(player, hand, aimProgress, matrixStack, buffer);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        if(Config.CLIENT.display.oldAnimations.get())
        {
            if(hand == Hand.OFF_HAND)
            {
                matrixStack.translate(0, -10 * 0.0625F, 0);
                matrixStack.translate(0, 0, -2 * 0.0625F);
            }
        }
        else
        {
            super.applyHeldItemTransforms(player, hand, aimProgress, matrixStack, buffer);
        }
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
