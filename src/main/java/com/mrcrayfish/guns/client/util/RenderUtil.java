package com.mrcrayfish.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RenderUtil
{
    public static void scissor(int x, int y, int width, int height)
    {
        Minecraft mc = Minecraft.getInstance();
        int scale = (int) mc.getWindow().getGuiScale();
        GL11.glScissor(x * scale, mc.getWindow().getScreenHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
    }

    public static IBakedModel getModel(Item item)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(new ItemStack(item));
    }

    public static IBakedModel getModel(ItemStack item)
    {
        return Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(item);
    }

    public static void rotateZ(MatrixStack matrixStack, float xOffset, float yOffset, float rotation)
    {
        matrixStack.translate(xOffset, yOffset, 0);
        matrixStack.mulPose(Vector3f.ZN.rotationDegrees(rotation));
        matrixStack.translate(-xOffset, -yOffset, 0);
    }

    public static void renderModel(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable LivingEntity entity)
    {
        renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, buffer, light, overlay, entity);
    }

    public static void renderModel(ItemStack child, ItemStack parent, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(child);
        renderModel(model, ItemCameraTransforms.TransformType.NONE, null, child, parent, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        if(entity != null)
        {
            model = Minecraft.getInstance().getItemRenderer().getModel(stack, entity.level, entity);
        }
        renderModel(model, transformType, stack, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable World world, @Nullable LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, entity);
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
            matrixStack.pushPose();
            boolean flag = transformType == ItemCameraTransforms.TransformType.GUI || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;
            if(stack.getItem() == Items.TRIDENT && flag)
            {
                model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
            }

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, false);
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            if(!model.isCustomRenderer() && (stack.getItem() != Items.TRIDENT || flag))
            {
                boolean entity = true;
                if(transformType != ItemCameraTransforms.TransformType.GUI && !transformType.firstPerson() && stack.getItem() instanceof BlockItem)
                {
                    Block block = ((BlockItem) stack.getItem()).getBlock();
                    entity = !(block instanceof BreakableBlock) && !(block instanceof StainedGlassPaneBlock);
                }

                if(model.isLayered())
                {
                    net.minecraftforge.client.ForgeHooksClient.drawItemLayered(Minecraft.getInstance().getItemRenderer(), model, stack, matrixStack, buffer, light, overlay, entity);
                }
                else
                {
                    RenderType renderType = getRenderType(stack, entity);
                    IVertexBuilder builder;
                    if(stack.getItem() == Items.COMPASS && stack.hasFoil())
                    {
                        matrixStack.pushPose();
                        MatrixStack.Entry entry = matrixStack.last();
                        if(transformType == ItemCameraTransforms.TransformType.GUI)
                        {
                            entry.pose().multiply(0.5F);
                        }
                        else if(transformType.firstPerson())
                        {
                            entry.pose().multiply(0.75F);
                        }

                        if(entity)
                        {
                            builder = ItemRenderer.getCompassFoilBufferDirect(buffer, renderType, entry);
                        }
                        else
                        {
                            builder = ItemRenderer.getCompassFoilBuffer(buffer, renderType, entry);
                        }

                        matrixStack.popPose();
                    }
                    else if(entity)
                    {
                        builder = ItemRenderer.getFoilBufferDirect(buffer, renderType, true, stack.hasFoil() || parent.hasFoil());
                    }
                    else
                    {
                        builder = ItemRenderer.getFoilBuffer(buffer, renderType, true, stack.hasFoil() || parent.hasFoil());
                    }
                    renderModel(model, stack, parent, transform, matrixStack, builder, light, overlay);
                }
            }
            else
            {
                stack.getItem().getItemStackTileEntityRenderer().renderByItem(stack, transformType, matrixStack, buffer, light, overlay);
            }

            matrixStack.popPose();
        }
    }

    /**
     * @param model
     * @param stack
     * @param parent
     * @param transform
     * @param matrixStack
     * @param buffer
     * @param light
     * @param overlay
     */
    public static void renderModel(IBakedModel model, ItemStack stack, ItemStack parent, @Nullable Transform transform, MatrixStack matrixStack, IVertexBuilder buffer, int light, int overlay)
    {
        if(transform != null)
        {
            transform.apply();
        }
        Random random = new Random();
        for(Direction direction : Direction.values())
        {
            random.setSeed(42L);
            renderQuads(matrixStack, buffer, model.getQuads(null, direction, random), stack, parent, light, overlay);
        }
        random.setSeed(42L);
        renderQuads(matrixStack, buffer, model.getQuads(null, null, random), stack, parent, light, overlay);
    }

    /**
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
        MatrixStack.Entry entry = matrixStack.last();
        for(BakedQuad quad : quads)
        {
            int color = -1;
            if(quad.isTinted())
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

    public static void applyTransformType(ItemStack stack, MatrixStack matrixStack, ItemCameraTransforms.TransformType transformType, LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, entity.level, entity);
        boolean leftHanded = transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);

        /* Flips the model and normals if left handed. */
        if(leftHanded)
        {
            Matrix4f scale = Matrix4f.createScaleMatrix(-1, 1, 1);
            Matrix3f normal = new Matrix3f(scale);
            matrixStack.last().pose().multiply(scale);
            matrixStack.last().normal().mul(normal);
        }
    }

    public interface Transform
    {
        void apply();
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public static void renderFirstPersonArm(ClientPlayerEntity player, HandSide hand, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight)
    {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererManager renderManager = mc.getEntityRenderDispatcher();
        PlayerRenderer renderer = (PlayerRenderer) renderManager.getRenderer(player);
        mc.getTextureManager().bind(player.getSkinTextureLocation());
        if(hand == HandSide.RIGHT)
        {
            renderer.renderRightHand(matrixStack, buffer, combinedLight, player);
        }
        else
        {
            renderer.renderLeftHand(matrixStack, buffer, combinedLight, player);
        }
    }

    private static RenderType getRenderType(ItemStack stack, boolean entity)
    {
        Item item = stack.getItem();
        if(item instanceof BlockItem)
        {
            Block block = ((BlockItem) item).getBlock();
            return RenderTypeLookup.getRenderType(block.defaultBlockState(), !entity);
        }
        return entity ? Atlases.translucentItemSheet() : RenderType.entityTranslucent(PlayerContainer.BLOCK_ATLAS);
    }
}