package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class ModelStandard implements IGunModel
{
    private final Item item;
    private IBakedModel base;

    public ModelStandard(Item item)
    {
        this.item = item;
    }

    @Override
    public void registerPieces()
    {
        base = RenderUtil.getModel(item, 0);
    }

    @Override
    public void tick() {}

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack)
    {
        RenderUtil.renderModel(base, transformType, stack);
    }
}
