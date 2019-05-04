package com.mrcrayfish.guns.client.render.gun;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IGunModel
{
    void registerPieces();

    void tick(EntityLivingBase entity);

    void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, EntityLivingBase entity);
}
