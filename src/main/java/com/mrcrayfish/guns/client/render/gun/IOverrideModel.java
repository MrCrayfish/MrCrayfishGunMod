package com.mrcrayfish.guns.client.render.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IOverrideModel
{
    void tick(LivingEntity entity);

    void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay);
}
