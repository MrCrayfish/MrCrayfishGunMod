package com.mrcrayfish.guns.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class RenderUtil
{
    public static IBakedModel getModel(ResourceLocation resource, int meta)
    {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Item.getByNameOrId(resource.toString()), 1, meta));
    }

    public static void rotateZ(float xOffset, float yOffset, float rotation)
    {
        GlStateManager.translate(xOffset, yOffset, 0);
        GlStateManager.rotate(rotation, 0, 0, -1);
        GlStateManager.translate(-xOffset, -yOffset, 0);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType)
    {
        renderModel(model, transformType, null);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, @Nullable Transform transform)
    {
        GlStateManager.pushMatrix();
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();

            net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, transformType, false);

            GlStateManager.pushMatrix();
            {
                GlStateManager.translate(-0.5F, -0.5F, -0.5F);

                if(transform != null)
                {
                    transform.apply();
                }

                Tessellator tessellator = Tessellator.getInstance();
                VertexBuffer vertexbuffer = tessellator.getBuffer();
                vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
                for(EnumFacing enumfacing : EnumFacing.values())
                {
                    renderQuads(vertexbuffer, model.getQuads(null, enumfacing, 0L));
                }
                renderQuads(vertexbuffer, model.getQuads(null, null, 0L));
                tessellator.draw();
            }
            GlStateManager.popMatrix();

            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
        }
        GlStateManager.popMatrix();
    }

    private static void renderQuads(VertexBuffer renderer, List<BakedQuad> quads)
    {
        int i = 0;
        for (int j = quads.size(); i < j; ++i)
        {
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer, quads.get(i), -1);
        }
    }

    public interface Transform
    {
        void apply();
    }
}