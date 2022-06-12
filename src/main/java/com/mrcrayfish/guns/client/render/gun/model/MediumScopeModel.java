package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.util.OptifineHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class MediumScopeModel implements IOverrideModel
{
    private static final ResourceLocation HOLO_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/effect/holo_reticle.png");
    private static final ResourceLocation HOLO_RETICLE_GLOW = new ResourceLocation(Reference.MOD_ID, "textures/effect/holo_reticle_glow.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        if(OptifineHelper.isShadersEnabled())
        {
            double transition = 1.0 - Math.pow(1.0 - AimingHandler.get().getNormalisedAdsProgress(), 2);
            double zScale = 0.05 + 0.95 * (1.0 - transition);
            matrixStack.scale(1.0F, 1.0F, (float) zScale);
        }

        IBakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(stack);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, light, overlay, bakedModel);

        if(transformType.firstPerson() && entity.equals(Minecraft.getInstance().player))
        {
            if(entity.getMainArm() == HandSide.LEFT)
            {
                matrixStack.scale(-1, 1, 1);
            }

            float scopeSize = 1.325F;
            float size = scopeSize / 16.0F;
            float crop = 0.4F;
            Minecraft mc = Minecraft.getInstance();
            MainWindow window = mc.getWindow();
            float texU = ((window.getScreenWidth() - window.getScreenHeight() + window.getScreenHeight() * crop * 2.0F) / 2.0F) / window.getScreenWidth();

            matrixStack.pushPose();
            {
                Matrix4f matrix = matrixStack.last().pose();
                Matrix3f normal = matrixStack.last().normal();

                matrixStack.translate(-size / 2, 0.06, 1.5 * 0.0625);

                float color = (float) AimingHandler.get().getNormalisedAdsProgress() * 0.8F + 0.2F;

                IVertexBuilder builder;

                if(!OptifineHelper.isShadersEnabled())
                {
                    builder = renderTypeBuffer.getBuffer(GunRenderType.getScreen());
                    builder.vertex(matrix, 0, size, 0).color(color, color, color, 1.0F).uv(texU, 1.0F - crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, 0, 0, 0).color(color, color, color, 1.0F).uv(texU, crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, 0, 0).color(color, color, color, 1.0F).uv(1.0F - texU, crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, size, 0).color(color, color, color, 1.0F).uv(1.0F - texU, 1.0F - crop).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                }

                matrixStack.translate(0, 0, 0.0001);

                if(!OptifineHelper.isShadersEnabled())
                {
                    builder = renderTypeBuffer.getBuffer(RenderType.entityTranslucent(VIGNETTE));
                    builder.vertex(matrix, 0, size, 0).color(color, color, color, 1.0F).uv(0, 1).overlayCoords(overlay).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, 0, 0, 0).color(color, color, color, 1.0F).uv(0, 0).overlayCoords(overlay).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, 0, 0).color(color, color, color, 1.0F).uv(1, 0).overlayCoords(overlay).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, size, size, 0).color(color, color, color, 1.0F).uv(1, 1).overlayCoords(overlay).uv2(light).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                }

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 8.0;
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, 0);
                CompoundNBT tag = stack.getTag();
                if(tag != null && tag.contains("ReticleColor", Constants.NBT.TAG_INT))
                {
                    reticleGlowColor = tag.getInt("ReticleColor");
                }

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) (1.0F * AimingHandler.get().getNormalisedAdsProgress());

                if(!OptifineHelper.isShadersEnabled())
                {
                    builder = renderTypeBuffer.getBuffer(RenderType.entityTranslucent(HOLO_RETICLE_GLOW));
                    builder.vertex(matrix, 0, (float) (size / scale), 0).color(red, green, blue, alpha).uv(0.0F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, 0, 0, 0).color(red, green, blue, alpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, (float) (size / scale), 0, 0).color(red, green, blue, alpha).uv(0.9375F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.vertex(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, alpha).uv(0.9375F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                }

                alpha = (float) (0.75F * AimingHandler.get().getNormalisedAdsProgress());

                builder = renderTypeBuffer.getBuffer(RenderType.entityTranslucent(HOLO_RETICLE));
                builder.vertex(matrix, 0, (float) (size / scale), 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), 0, 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.9375F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), (float) (size / scale), 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.9375F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            matrixStack.popPose();
        }
    }
}
