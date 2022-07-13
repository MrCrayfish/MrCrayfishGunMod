package com.mrcrayfish.guns.client.render.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

/**
 * A simple interface to render custom models. This can only be used for overriding the models of
 * weapons or attachments.
 * <p>
 * Author: MrCrayfish
 */
public interface IOverrideModel
{
    /**
     * Called on every game tick for each player if they are holding an item with an overridden
     * model. Useful for creating animations.
     *
     * @param entity the player holding the
     */
    default void tick(Player entity)
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
     * @param poseStack   the current matrix stack
     * @param buffer        a render type buffer get
     * @param light         the combined light for the item
     * @param overlay       the overlay texture for the item
     */
    void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay);
}
