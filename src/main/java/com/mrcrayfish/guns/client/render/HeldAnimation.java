package com.mrcrayfish.guns.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(PlayerModel model, Hand hand, float aimProgress) {}

    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer) {}

    public void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer) {}
}
