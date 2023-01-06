package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.GunModel;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class BazookaModel extends SimpleModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/effect/red_dot_reticle.png");
    private static final ResourceLocation RED_DOT_RETICLE_GLOW = new ResourceLocation(Reference.MOD_ID, "textures/effect/red_dot_reticle_glow.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    public BazookaModel(Supplier<BakedModel> modelSupplier)
    {
        super(modelSupplier);
    }

    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, @Nullable LivingEntity entity, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay)
    {
        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.NONE, false, poseStack, buffer, light, overlay, GunModel.wrap(this.modelSupplier.get()));

        if(transformType.firstPerson() && entity != null && entity.equals(Minecraft.getInstance().player))
        {
            poseStack.pushPose();
            {
                Matrix4f matrix = poseStack.last().pose();
                Matrix3f normal = poseStack.last().normal();

                double size = 1.2 / 16.0;
                poseStack.translate(-size / 2 - 3.5 * 0.0625, -3.7 * 0.0625 - size / 2, -7 * 0.0625);

                VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(VIGNETTE));
                builder.vertex(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 1.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) size, 0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 1.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) size, (float) size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(0, 0).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, 0, (float) size, 0).color(1.0F, 1.0F, 1.0F, 1.0F).uv(1.0F, 0).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                poseStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 6.0;
                poseStack.translate(size / 2, size / 2, 0);
                poseStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                poseStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, 0);
                CompoundTag tag = stack.getTag();
                if(tag != null && tag.contains("ReticleColor", Tag.TAG_INT))
                {
                    reticleGlowColor = tag.getInt("ReticleColor");
                }

                float red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                float green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                float blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) (1.0F * AimingHandler.get().getNormalisedAdsProgress());

                builder = buffer.getBuffer(RenderType.entityTranslucent(RED_DOT_RETICLE_GLOW));
                builder.vertex(matrix, 0, (float) (size / scale), 0).color(red, green, blue, alpha).uv(0.0F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, 0, 0, 0).color(red, green, blue, alpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), 0, 0).color(red, green, blue, alpha).uv(0.9375F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, alpha).uv(0.9375F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

                alpha = (float) (0.75F * AimingHandler.get().getNormalisedAdsProgress());

                builder = buffer.getBuffer(RenderType.entityTranslucent(RED_DOT_RETICLE));
                builder.vertex(matrix, 0, (float) (size / scale), 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, 0, 0, 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), 0, 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.9375F, 0.0F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.vertex(matrix, (float) (size / scale), (float) (size / scale), 0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.9375F, 0.9375F).overlayCoords(overlay).uv2(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            poseStack.popPose();
        }
    }
}
