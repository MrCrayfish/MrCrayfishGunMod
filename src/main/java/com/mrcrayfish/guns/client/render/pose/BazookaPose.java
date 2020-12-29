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

/**
 * Author: MrCrayfish
 */
public class BazookaPose implements HeldAnimation
{
    @Override
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

        mainArm.rotateAngleX = (float) Math.toRadians(-90F);
        mainArm.rotateAngleY = (float) Math.toRadians(-35F) * (right ? 1F : -1F);
        mainArm.rotateAngleZ = (float) Math.toRadians(0F);

        secondaryArm.rotateAngleX = (float) Math.toRadians(-91F);
        secondaryArm.rotateAngleY = (float) Math.toRadians(45F) * (right ? 1F : -1F);
        secondaryArm.rotateAngleZ = (float) Math.toRadians(0F);
    }

    @Override
    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        player.prevRenderYawOffset = player.prevRotationYaw + 35F * (right ? 1F : -1F);
        player.renderYawOffset = player.rotationYaw + 35F * (right ? 1F : -1F);
    }

    @Override
    public boolean applyOffhandTransforms(PlayerEntity player, PlayerModel model, ItemStack stack, MatrixStack matrixStack, float partialTicks)
    {
        return GripType.applyBackTransforms(player, matrixStack);
    }
}
