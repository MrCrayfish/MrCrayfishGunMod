package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.render.HeldAnimation;
import com.mrcrayfish.guns.object.GripType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public class MiniGunPose implements HeldAnimation
{
    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        player.prevRenderYawOffset = player.prevRotationYaw + 45F * (right ? 1F : -1F);
        player.renderYawOffset = player.rotationYaw + 45F * (right ? 1F : -1F);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        if(hand == Hand.OFF_HAND)
        {
            matrixStack.translate(0, -10 * 0.0625F, 0);
            matrixStack.translate(0, 0, -2 * 0.0625F);
        }
    }

    @Override
    public boolean applyOffhandTransforms(PlayerEntity player, PlayerModel model, ItemStack stack, MatrixStack matrixStack, float partialTicks)
    {
        return GripType.applyBackTransforms(player, matrixStack);
    }
}
