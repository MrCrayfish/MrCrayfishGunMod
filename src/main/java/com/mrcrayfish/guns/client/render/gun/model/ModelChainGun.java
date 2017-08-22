package com.mrcrayfish.guns.client.render.gun.model;

import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
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
        if(init) return;
        base = RenderUtil.getModel(new ResourceLocation("cgm:gun_part"), 0);
        barrel = RenderUtil.getModel(new ResourceLocation("cgm:gun_part"), 1);
        init = true;
    }

    @Override
    public void tick()
    {
        if(Mouse.isButtonDown(1))
        {
            lastRotation = rotation;
            rotation += 20;
        }
    }

    @Override
    public void render(float partialTicks)
    {
        RenderUtil.renderModel(base);

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.22, 0.1675, 0);
        if(Mouse.isButtonDown(1))
        {
            GlStateManager.rotate(lastRotation + (rotation - lastRotation) * partialTicks, 0, 0, -1);
        }
        else
        {
            GlStateManager.rotate(rotation, 0, 0, -1);
        }
        GlStateManager.translate(0.22, -0.1675, 0);
        RenderUtil.renderModel(barrel);
        GlStateManager.popMatrix();
    }
}
