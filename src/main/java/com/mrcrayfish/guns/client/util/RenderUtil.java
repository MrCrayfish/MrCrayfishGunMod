package com.mrcrayfish.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class RenderUtil
{
    public static IBakedModel getModel(Item item, int meta)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(new ItemStack(item));
    }

    public static void rotateZ(float xOffset, float yOffset, float rotation)
    {
        /*GlStateManager.translate(xOffset, yOffset, 0);
        GlStateManager.rotate(rotation, 0, 0, -1);
        GlStateManager.translate(-xOffset, -yOffset, 0);*/
    }

    public static void renderModel(ItemStack stack)
    {
        renderModel(stack, ItemCameraTransforms.TransformType.NONE);
    }

    public static void renderModel(ItemStack child, ItemStack parent)
    {
        //IBakedModel model = Minecraft.getInstance().getRenderItem().getItemModelMesher().getItemModel(child);
        //renderModel(model, ItemCameraTransforms.TransformType.NONE, null, child, parent);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType)
    {
        //IBakedModel model = Minecraft.getInstance().getRenderItem().getItemModelMesher().getItemModel(stack);
        //renderModel(model, transformType, stack);
    }

    public static void renderModel(IBakedModel model, ItemStack stack)
    {
        renderModel(model, ItemCameraTransforms.TransformType.NONE, stack);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, ItemStack stack)
    {
        renderModel(model, transformType, null, stack, ItemStack.EMPTY);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, @Nullable Transform transform, ItemStack stack, ItemStack parent)
    {
        /*GlStateManager.pushMatrix();
        {
            Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, transformType, false);

            GlStateManager.pushMatrix();
            {
                RenderUtil.renderModel(model, transform, stack, parent);
            }
            GlStateManager.popMatrix();

            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();

            Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
        GlStateManager.popMatrix();*/
    }

    private static void renderModel(IBakedModel model, @Nullable Transform transform, ItemStack stack, ItemStack parent)
    {
        /*GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        if(transform != null) transform.apply();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.ITEM);
        for(EnumFacing enumfacing : EnumFacing.values())
        {
            renderQuads(buffer, model.getQuads(null, enumfacing, 0L), stack, parent);
        }
        renderQuads(buffer, model.getQuads(null, null, 0L), stack, parent);
        tessellator.draw();*/
    }

    private static void renderQuads(BufferBuilder buffer, List<BakedQuad> quads, ItemStack stack, ItemStack parent)
    {
        /*int i = 0;
        for (int j = quads.size(); i < j; ++i)
        {
            BakedQuad bakedQuad = quads.get(i);
            int color = -1;
            if (bakedQuad.hasTintIndex())
            {
                color = getItemStackColor(stack, parent, bakedQuad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    color = TextureUtil.anaglyphColor(color);
                }

                color = color | -16777216;
            }
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quads.get(i), color);
        }*/
    }

    private static int getItemStackColor(ItemStack stack, ItemStack parent, int tintIndex)
    {
        /*int color = Minecraft.getInstance().getItemColors().colorMultiplier(stack, tintIndex);
        if(color == -1)
        {
            if(!parent.isEmpty())
            {
                return getItemStackColor(parent, ItemStack.EMPTY, tintIndex);
            }
        }
        return color;*/
        return 0;
    }

    public static void applyTransformType(ItemStack stack, MatrixStack matrixStack, ItemCameraTransforms.TransformType transformType)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
        ItemTransformVec3f transformVec3f = model.getItemCameraTransforms().getTransform(transformType);
        matrixStack.translate(transformVec3f.translation.getX(), transformVec3f.translation.getY(), transformVec3f.translation.getZ());
        matrixStack.rotate(Vector3f.XP.rotationDegrees(transformVec3f.rotation.getX()));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(transformVec3f.rotation.getY()));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(transformVec3f.rotation.getZ()));
        matrixStack.scale(transformVec3f.scale.getX(), transformVec3f.scale.getY(), transformVec3f.scale.getZ());
    }

    public interface Transform
    {
        void apply();
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}