package com.tac.guns.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.common.Gun;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.block.Block;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.StainedGlassPaneBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.*;
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

    public static void renderModel(ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable LivingEntity entity)
    {
        renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, buffer, light, overlay, entity);
    }

    public static void renderModel(ItemStack child, ItemStack parent, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(child);
        renderModel(model, ItemCameraTransforms.TransformType.NONE, null, child, parent, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(stack);
        if(entity != null)
        {
            model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity);
        }
        renderModel(model, transformType, stack, matrixStack, buffer, light, overlay);
    }

    public static void renderModel(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, int overlay, @Nullable World world, @Nullable LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, world, entity);
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
            boolean flag = transformType == ItemCameraTransforms.TransformType.GUI || transformType == ItemCameraTransforms.TransformType.GROUND || transformType == ItemCameraTransforms.TransformType.FIXED;
            if(stack.getItem() == Items.TRIDENT && flag)
            {
                model = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:trident#inventory"));
            }

            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, false);
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            if(!model.isBuiltInRenderer() && (stack.getItem() != Items.TRIDENT || flag))
            {
                boolean flag1;
                if(transformType != ItemCameraTransforms.TransformType.GUI && !transformType.isFirstPerson() && stack.getItem() instanceof BlockItem)
                {
                    Block block = ((BlockItem) stack.getItem()).getBlock();
                    flag1 = !(block instanceof BreakableBlock) && !(block instanceof StainedGlassPaneBlock);
                }
                else
                {
                    flag1 = true;
                }

                if(model.isLayered())
                {
                    net.minecraftforge.client.ForgeHooksClient.drawItemLayered(Minecraft.getInstance().getItemRenderer(), model, stack, matrixStack, buffer, light, overlay, flag1);
                }
                else
                {
                    RenderType renderType = getRenderType(stack, !flag1);
                    IVertexBuilder builder;
                    if(stack.getItem() == Items.COMPASS && stack.hasEffect())
                    {
                        matrixStack.push();
                        MatrixStack.Entry entry = matrixStack.getLast();
                        if(transformType == ItemCameraTransforms.TransformType.GUI)
                        {
                            entry.getMatrix().mul(0.5F);
                        }
                        else if(transformType.isFirstPerson())
                        {
                            entry.getMatrix().mul(0.75F);
                        }

                        if(flag1)
                        {
                            builder = ItemRenderer.getDirectGlintVertexBuilder(buffer, renderType, entry);
                        }
                        else
                        {
                            builder = ItemRenderer.getGlintVertexBuilder(buffer, renderType, entry);
                        }

                        matrixStack.pop();
                    }
                    else if(flag1)
                    {
                        builder = ItemRenderer.getEntityGlintVertexBuilder(buffer, renderType, true, stack.hasEffect() || parent.hasEffect());
                    }
                    else
                    {
                        builder = ItemRenderer.getBuffer(buffer, renderType, true, stack.hasEffect() || parent.hasEffect());
                    }

                    renderModel(model, stack, parent, transform, matrixStack, builder, light, overlay);
                }
            }
            else
            {
                stack.getItem().getItemStackTileEntityRenderer().func_239207_a_(stack, transformType, matrixStack, buffer, light, overlay);
            }

            matrixStack.pop();
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
    private static void renderModel(IBakedModel model, ItemStack stack, ItemStack parent, @Nullable Transform transform, MatrixStack matrixStack, IVertexBuilder buffer, int light, int overlay)
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

    /*
    private static void renderQuads(MatrixStack matrixStack, IVertexBuilder buffer, List<BakedQuad> quads, ItemStack stack, ItemStack parent, int light, int overlay)
    {
        MatrixStack.Entry entry = matrixStack.getLast();
        for(BakedQuad quad : quads)
        {
            boolean keepColor = true;
            int color = -1;
            if(quad.hasTintIndex())
            {
                //if(getItemStackColor(stack, parent, IAttachment.Type.SCOPE_BODY_COLOR, quad.getTintIndex()) != 0)
                    color = getItemStackColor(stack, parent, IAttachment.Type.SCOPE_BODY_COLOR, quad.getTintIndex());

                if(stack.getItem() instanceof ScopeItem && getItemStackColor(stack, parent, IAttachment.Type.SCOPE_GLASS_COLOR, quad.getTintIndex()) != 0) {
                    color = getItemStackColor(stack, parent, IAttachment.Type.SCOPE_GLASS_COLOR, quad.getTintIndex());
                    overlay=1;
                    keepColor = false;
                }
            }
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;
            buffer.addVertexData(entry, quad, red, green, blue, light, overlay, keepColor);
        }
    }
     */

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
        MatrixStack.Entry entry = matrixStack.getLast();
        for(BakedQuad quad : quads)
        {
            float alpha = 1f;
            boolean keepColor = true;
            int color = -1;
            if(quad.hasTintIndex())
            {
                color = getItemStackColor(stack, parent, IAttachment.Type.SCOPE_BODY_COLOR, quad.getTintIndex());
                keepColor = true;
            }
            if(quad.hasTintIndex() && stack.getItem() instanceof ScopeItem && quad.getTintIndex() == 1)
            {
                color = getItemStackColor(stack, parent, IAttachment.Type.SCOPE_GLASS_COLOR, quad.getTintIndex());
                alpha = 2f;
                keepColor = false;
            }
            float red = (float) (color >> 16 & 255) / 255.0F;
            float green = (float) (color >> 8 & 255) / 255.0F;
            float blue = (float) (color & 255) / 255.0F;

            buffer.addVertexData(entry, quad, red, green, blue, alpha, light, overlay, keepColor);
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
        if(stack != null && !Gun.getAttachment(IAttachment.Type.SCOPE_BODY_COLOR, stack).isEmpty())
        {
            color = ((DyeItem)Gun.getAttachment(IAttachment.Type.SCOPE_BODY_COLOR, stack).getItem()).getDyeColor().getColorValue();
        }
        return color;
    }
    public static int getItemStackColor(ItemStack stack, ItemStack parent, IAttachment.Type attachmentType, int tintIndex)
    {
        int color = getItemStackColor(stack,parent,tintIndex);

        if(stack != null && !Gun.getAttachment(attachmentType, stack).isEmpty())
        {
            if(Gun.getAttachment(attachmentType, stack).getItem() instanceof DyeItem)
                color = ((DyeItem)Gun.getAttachment(attachmentType, stack).getItem()).getDyeColor().getColorValue();
        }
        return color;
    }

    public static void applyTransformType(ItemStack stack, MatrixStack matrixStack, ItemCameraTransforms.TransformType transformType, LivingEntity entity)
    {
        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity);
        boolean leftHanded = transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);

        /* Flips the model and normals if left handed. */
        if(leftHanded)
        {
            Matrix4f scale = Matrix4f.makeScale(-1, 1, 1);
            Matrix3f normal = new Matrix3f(scale);
            matrixStack.getLast().getMatrix().mul(scale);
            matrixStack.getLast().getNormal().mul(normal);
        }
    }

    public static void applyTransformTypeIB(IBakedModel model, MatrixStack matrixStack, ItemCameraTransforms.TransformType transformType, LivingEntity entity)
    {
        /*IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, entity.world, entity);*/
        boolean leftHanded = transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
        ForgeHooksClient.handleCameraTransforms(matrixStack, model, transformType, leftHanded);

        /* Flips the model and normals if left handed. */
        if(leftHanded)
        {
            Matrix4f scale = Matrix4f.makeScale(-1, 1, 1);
            Matrix3f normal = new Matrix3f(scale);
            matrixStack.getLast().getMatrix().mul(scale);
            matrixStack.getLast().getNormal().mul(normal);
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
        EntityRendererManager renderManager = mc.getRenderManager();
        PlayerRenderer renderer = (PlayerRenderer) renderManager.getRenderer(player);
        mc.getTextureManager().bindTexture(player.getLocationSkin());
        if(hand == HandSide.RIGHT)
        {
            renderer.renderRightArm(matrixStack, buffer, combinedLight, player);
        }
        else
        {
            renderer.renderLeftArm(matrixStack, buffer, combinedLight, player);
        }
    }

    public static RenderType getRenderType(ItemStack stack, boolean entity)
    {
        Item item = stack.getItem();
        if(item instanceof BlockItem)
        {
            Block block = ((BlockItem) item).getBlock();
            return RenderTypeLookup.func_239220_a_(block.getDefaultState(), !entity);
        }
        return entity ? Atlases.getItemEntityTranslucentCullType() : RenderType.getEntityTranslucent(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
    }
}