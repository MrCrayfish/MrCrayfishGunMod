package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class ModelStandard implements IOverrideModel
{
    private final Item item;
    private IBakedModel base;

    public ModelStandard(Item item)
    {
        this.item = item;
    }

    @Override
    public void init()
    {
        base = RenderUtil.getModel(item, 0);
    }

    @Override
    public void tick(EntityLivingBase entity) {}

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, EntityLivingBase entity)
    {
        RenderUtil.renderModel(base, transformType, stack);
    }
}
