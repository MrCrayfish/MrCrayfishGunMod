package com.mrcrayfish.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RenderUtil
{
    public static void scissor(int x, int y, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        int scale = (int) mc.getMainWindow().getGuiScaleFactor();
        GL11.glScissor(x * scale, mc.getMainWindow().getHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
    }

    public static IBakedModel getModel(Item item)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(new ItemStack(item));
    }

    public static IBakedModel getModel(ItemStack item)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(item);
    }

    public static void rotateZ(MatrixStack matrixStack, float xOffset, float yOffset, float rotation)
    {
        matrixStack.translate(xOffset, yOffset, 0);
        matrixStack.rotate(Vector3f.ZN.rotationDegrees(rotation));
        matrixStack.translate(-xOffset, -yOffset, 0);
    }

    public static void renderModel(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack child, ItemStack parent, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(child);
        renderModel(model, ItemCameraTransforms.TransformType.NONE, null, child, parent, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
        renderModel(model, transformType, stack, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(IBakedModel model, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        renderModel(model, ItemCameraTransforms.TransformType.NONE, stack, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        renderModel(model, transformType, null, stack, ItemStack.EMPTY, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(IBakedModel model, ItemCameraTransforms.TransformType transformType, @Nullable Transform transform, ItemStack stack, ItemStack parent, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if(!stack.isEmpty())
        {
            matrixStack.push();

            boolean isGui = transformType == ItemCameraTransforms.TransformType.GUI;
            boolean tridentFlag = isGui || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;
            if(stack.getItem() == Items.TRIDENT && tridentFlag)
            {
                model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
            }

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, false);
            matrixStack.translate(-0.5, -0.5, -0.5);
            if(!model.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT || tridentFlag))
            {
                RenderType renderType = RenderTypeLookup.getRenderType(stack);
                if(isGui && Objects.equals(renderType, Atlases.getTranslucentBlockType()))
                {
                    renderType = Atlases.getTranslucentCullBlockType();
                }
                IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(buffer, renderType, true, stack.hasEffect());
                renderModel(model, stack, parent, transform, matrixStack, vertexBuilder, light, overlay);
            }
            else
            {
                stack.getItem().getItemStackTileEntityRenderer().render(stack, matrixStack, buffer, light, overlay);
            }

            matrixStack.pop();
        }
    }

    /**
     *
     * @param model
     * @param stack
     * @param parent
     * @param transform
     * @param matrixStack
     * @param buffer
     * @param light
     * @param overlay
     */
    private static void renderModel(IBakedModel model, ItemStack stack, ItemStack parent, @Nullable Transform transform, MatrixStack matrixStack, IVertexBuilder buffer, int light, int overlay)
    {
        if(transform != null)
        {
            transform.apply();
        }
        Random random = new Random();
        for(Direction direction : Direction.values())
        {
            random.setSeed(42l);
            renderQuads(matrixStack, buffer, model.getQuads(null, direction, random), stack, parent, light, overlay);
        }
        random.setSeed(42L);
        renderQuads(matrixStack, buffer, model.getQuads(null, null, random), stack, parent, light, overlay);
    }

    /**
     *
     * @param matrixStack
     * @param buffer
     * @param quads
     * @param stack
     * @param parent
     * @param light
     * @param overlay
     */
    private static void renderQuads(MatrixStack matrixStack, IVertexBuilder buffer, List<BakedQuad> quads, ItemStack stack, ItemStack parent, int light, int overlay)
    {
        MatrixStack.Entry entry = matrixStack.getLast();
        for(BakedQuad quad : quads)
        {
            int color = -1;
            if(quad.hasTintIndex())
            {
                color = getItemStackColor(stack, parent, quad.getTintIndex());
            }
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            buffer.addVertexData(entry, quad, red, green, blue, light, overlay, true);
        }
    }

    public static int getItemStackColor(ItemStack stack, ItemStack parent, int tintIndex)
    {
        int color = Minecraft.getInstance().getItemColors().getColor(stack, tintIndex);
        if(color == -1)
        {
            if(!parent.isEmpty())
            {
                return getItemStackColor(parent, ItemStack.EMPTY, tintIndex);
            }
        }
        return color;
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