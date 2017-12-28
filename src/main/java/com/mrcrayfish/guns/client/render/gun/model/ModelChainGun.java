package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

/**
 * Author: MrCrayfish
 */
public class ModelChainGun implements IGunModel
{
    private boolean init = false;
    private IBakedModel base;
    private IBakedModel barrel;

    private int lastRotation;
    private int rotation;

    @Override
    public void registerPieces()
    {
        //if(init) return;
        base = RenderUtil.getModel(new ResourceLocation("cgm:part"), 0);
        barrel = RenderUtil.getModel(new ResourceLocation("cgm:part"), 1);
        //init = true;
    }

    @Override
    public void tick()
    {
        lastRotation = rotation;
        if(Mouse.isButtonDown(1))
        {
            rotation += 20;
        }
        else
        {
            rotation += 1;
        }
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType)
    {
        RenderUtil.renderModel(base, transformType);
        RenderUtil.renderModel(barrel, transformType, () -> RenderUtil.rotateZ(0.5F, 0.125F, lastRotation + (rotation - lastRotation) * partialTicks));
    }
}
