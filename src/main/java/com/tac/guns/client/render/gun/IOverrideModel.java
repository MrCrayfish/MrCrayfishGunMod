package com.tac.guns.client.render.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * A simple interface to render custom models. This can only be used for overriding the models of
 * weapons or attachments.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IOverrideModel
{
    /**
     * Called on every game tick for each player if they are holding an item with an overridden
     * model. Useful for creating animations.
     *
     * @param entity the player holding the
     */
    default void tick(PlayerEntity entity)
    {
    }

    /**
     * Renders the overridden model.
     *
     * @param partialTicks  the current partial ticks
     * @param transformType the camera transform type
     * @param stack         the itemstack of the item that has the overridden model
     * @param parent        if an attachment, the parent is the weapon this attachment is attached to otherwise it's an empty stack.
     * @param entity        the entity holding the item
     * @param matrixStack   the current matrix stack
     * @param buffer        a render type buffer get
     * @param light         the combined light for the item
     * @param overlay       the overlay texture for the item
     */
    void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay);
}
