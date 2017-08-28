package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class ModelStandard implements IGunModel
{
    private final ResourceLocation resource;
    private IBakedModel base;

    public ModelStandard(ResourceLocation resource)
    {
        this.resource = resource;
    }

    @Override
    public void registerPieces()
    {
        base = RenderUtil.getModel(resource, 0);
    }

    @Override
    public void tick() {}

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType)
    {
        RenderUtil.renderModel(base, transformType);
    }
}
