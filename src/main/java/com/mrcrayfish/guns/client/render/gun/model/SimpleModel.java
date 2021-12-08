package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class SimpleModel implements IOverrideModel
{
    public final Supplier<BakedModel> modelSupplier;

    public SimpleModel(Supplier<BakedModel> modelSupplier)
    {
        this.modelSupplier = modelSupplier;
    }

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        RenderUtil.renderItemWithoutTransforms(this.modelSupplier.get(), stack, parent, poseStack, buffer, light, overlay);
    }
}
