package com.mrcrayfish.guns.client.render.gun;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IOverrideModel
{
    void init();

    void tick(LivingEntity entity);

    void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity);
}
