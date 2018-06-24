package com.mrcrayfish.guns.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
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

    public static void renderModel(ItemStack stack)
    {
        renderModel(stack, ItemCameraTransforms.TransformType.NONE);
    }

    public static void renderModel(ItemStack child, ItemStack parent)
    {
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(child);
        renderModel(model, ItemCameraTransforms.TransformType.NONE, parent);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType)
    {
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        renderModel(model, transformType, stack);
    }

    public static void renderModel(IBakedModel model, ItemStack stack)
    {
        renderModel(model, ItemCameraTransforms.TransformType.NONE, stack);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, ItemStack stack)
    {
        renderModel(model, transformType, null, stack);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, @Nullable Transform transform, ItemStack stack)
    {
        GlStateManager.pushMatrix();
        {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, transformType, false);

            GlStateManager.pushMatrix();
            {
                RenderUtil.renderModel(model, transform, stack);
            }
            GlStateManager.popMatrix();

            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
        GlStateManager.popMatrix();
    }

    private static void renderModel(IBakedModel model, @Nullable Transform transform, ItemStack stack)
    {
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        if(transform != null) transform.apply();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.ITEM);
        for(EnumFacing enumfacing : EnumFacing.values())
        {
            renderQuads(buffer, model.getQuads(null, enumfacing, 0L), stack);
        }
        renderQuads(buffer, model.getQuads(null, null, 0L), stack);
        tessellator.draw();
    }

    private static void renderQuads(BufferBuilder buffer, List<BakedQuad> quads, ItemStack stack)
    {
        int i = 0;
        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedquad = quads.get(i);
            int color = -1;
            if (bakedquad.hasTintIndex())
            {
                color = Minecraft.getMinecraft().getItemColors().colorMultiplier(stack, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    color = TextureUtil.anaglyphColor(color);
                }

                color = color | -16777216;
            }
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quads.get(i), color);
        }
    }

    public static void applyTransformType(ItemStack stack, ItemCameraTransforms.TransformType transformType)
    {
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
        ItemTransformVec3f transformVec3f = model.getItemCameraTransforms().getTransform(transformType);
        GlStateManager.translate(transformVec3f.translation.getX(), transformVec3f.translation.getY(), transformVec3f.translation.getZ());
        GlStateManager.rotate(transformVec3f.rotation.getX(), 1, 0, 0);
        GlStateManager.rotate(transformVec3f.rotation.getY(), 0, 1, 0);
        GlStateManager.rotate(transformVec3f.rotation.getZ(), 0, 0, 1);
        GlStateManager.scale(transformVec3f.scale.getX(), transformVec3f.scale.getY(), transformVec3f.scale.getZ());
    }

    public interface Transform
    {
        void apply();
    }
}