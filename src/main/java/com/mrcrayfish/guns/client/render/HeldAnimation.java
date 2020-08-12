package com.mrcrayfish.guns.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public interface HeldAnimation
{
    /**
     * Allows for modifications of the player model. This is where arms should be aligned to hold
     * the weapon correctly. This also gives you access to the current aiming progress.
     *
     * @param model an instance of the player model
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     */
    @OnlyIn(Dist.CLIENT)
    default void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress) {}

    /**
     * Allows for transformations of the player model. This is where the entire player model can
     * be rotated and translated to suit the current weapon.
     *
     * @param player the player holding the weapon
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     * @param matrixStack the current matrix stack
     * @param buffer a render type buffer instance
     */
    @OnlyIn(Dist.CLIENT)
    default void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer) {}

    /**
     * Allows for transformations of the weapon before rendering. This is where rotations can be
     * applied to the item for animating aiming.
     *
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     * @param matrixStack the current matrix stack
     * @param buffer a render type buffer instance
     */
    @OnlyIn(Dist.CLIENT)
    default void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer) {}

    /**
     *
     * @param player
     * @param hand
     * @param stack
     * @param matrixStack
     * @param buffer
     * @param light
     * @param partialTicks
     */
    default void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks) {}
}
