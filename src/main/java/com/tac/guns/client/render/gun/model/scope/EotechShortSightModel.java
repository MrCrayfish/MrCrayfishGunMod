package com.tac.guns.client.render.gun.model.scope;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.HUDRenderingHandler;
import com.tac.guns.client.handler.command.ScopeEditor;
import com.tac.guns.client.handler.command.data.ScopeData;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class EotechShortSightModel implements IOverrideModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/eotech_cqb_reticle.png");
    private static final ResourceLocation HIT_MARKER = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/hit_marker/eotech_cqb_reticle.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay) {
        matrixStack.push();
        if (Config.CLIENT.display.redDotSquishUpdate.get() && transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
            double prog = 0;
            if(AimingHandler.get().getNormalisedAdsProgress() > 0.725) {
                prog = (AimingHandler.get().getNormalisedAdsProgress() - 0.725) * 3.63;
            }
                double transition = 1.0D - Math.pow(1.0D - prog, 2.0D);
                double zScale = 0.05D + 0.95D * (1.0D - transition);
                matrixStack.scale(1.0F, 1.0F, (float) zScale);

        }
        else if (transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
            double prog = 0;
            if(AimingHandler.get().getNormalisedAdsProgress() > 0.725) {
                prog = (AimingHandler.get().getNormalisedAdsProgress() - 0.725) * 1.1875;
                double transition = 1.0D - Math.pow(1.0D - prog, 2.0D);
                double zScale = 0.05D + 0.95D * (1.0D - transition);
                matrixStack.scale(1.0F, 1.0F, (float) zScale);
            }
            else {
                double transition = 1.0D - Math.pow(1.0D - prog, 2.0D);
                double zScale = 0.05D + 0.95D * (1.0D - transition);
                matrixStack.scale(1.0F, 1.0F, (float) zScale);
            }

        }
        int glassGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_GLASS_COLOR, 2);
        float red = ((glassGlowColor >> 16) & 0xFF) / 255F;
        float green = ((glassGlowColor >> 8) & 0xFF) / 255F;
        float blue = ((glassGlowColor >> 0) & 0xFF) / 255F;

        matrixStack.translate(0, 0.055, -0.1375);

        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);

        matrixStack.translate(0, -0.049, 0);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(0, 0.006, -0.1375);
        if(transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player))
        {
            ScopeData scopeData = ScopeEditor.get().getScopeData() == null || ScopeEditor.get().getScopeData().getTagName() != "eotechshort" ? new ScopeData("") : ScopeEditor.get().getScopeData();
            matrixStack.push();
            {
                // Walking bobbing
                boolean aimed = false;
                if(AimingHandler.get().isAiming())
                    aimed = true;

                double invertZoomProgress = aimed ? 0.0575 : 0.468;//double invertZoomProgress = aimed ? 0.135 : 0.94;//aimed ? 1.0 - AimingHandler.get().getNormalisedAdsProgress() : ;

                double invertProgress = (1.0 - AimingHandler.get().getNormalisedAdsProgress());
                Matrix4f matrix = matrixStack.getLast().getMatrix();
                Matrix3f normal = matrixStack.getLast().getNormal();

                //matrixStack.translate(0, 0, -0.2);
                float size = 1.4F / 16.0F;
                matrixStack.translate(((-size / 2) -0.002015 + scopeData.getReticleXMod()), (1.38 + 0.03308125 + scopeData.getReticleYMod()+0.47275) * 0.0625, (0.075 + scopeData.getReticleZMod() + (!Config.CLIENT.display.redDotSquishUpdate.get() ? 1.2625 : 0)) * 0.0625);
                IVertexBuilder builder;
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 3.75 -0.49499965 -2.3025033 + 0.38249975 + scopeData.getReticleSizeMod();
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);

                int reticleGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_RETICLE_COLOR, 1);

                red = ((reticleGlowColor >> 16) & 0xFF) / 255F;
                green = ((reticleGlowColor >> 8) & 0xFF) / 255F;
                blue = ((reticleGlowColor >> 0) & 0xFF) / 255F;
                float alpha = (float) AimingHandler.get().getNormalisedAdsProgress();

                alpha = AimingHandler.get().getNormalisedAdsProgress() != 0 &&  AimingHandler.get().getNormalisedAdsProgress() != 1 ? (float) (0.5F * AimingHandler.get().getNormalisedAdsProgress()) : (float) AimingHandler.get().getNormalisedAdsProgress();

                builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(RED_DOT_RETICLE));

                GunRenderingHandler.get().applyBobbingTransforms(matrixStack,true, 1.0f);
                GunRenderingHandler.get().applyNoiseMovementTransform(matrixStack, -1.5f);
                GunRenderingHandler.get().applyJumpingTransforms(matrixStack, partialTicks,-0.8f);

                matrixStack.translate(0, 0, -0.35);
                matrixStack.rotate(Vector3f.YP.rotationDegrees(GunRenderingHandler.get().newSwayYaw*0.5f));
                matrixStack.rotate(Vector3f.ZN.rotationDegrees(GunRenderingHandler.get().newSwayPitch*0.5f));
                matrixStack.rotate(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift * GunRenderingHandler.get().recoilReduction) * 0.85F));
                matrixStack.translate(0, 0, 0.35);

                builder.pos(matrix, 0, (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();

                if(HUDRenderingHandler.get().hitMarkerTracker > 0)
                {
                    builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(HIT_MARKER));

                    if(HUDRenderingHandler.get().hitMarkerHeadshot)
                    {
                        green = 0;
                        blue = 0;
                    }
                    float opac = Math.max(Math.min(HUDRenderingHandler.get().hitMarkerTracker / HUDRenderingHandler.hitMarkerRatio, 100f), 0.25f);
                    opac *= (float) AimingHandler.get().getNormalisedAdsProgress();
                    builder.pos(matrix, 0, (float) (size / scale), 0).color(red, green, blue, opac).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, 0, 0, 0).color(red, green, blue, opac).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, (float) (size / scale), 0, 0).color(red, green, blue, opac).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    builder.pos(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, opac).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                    HUDRenderingHandler.get().hitMarkerTracker--;
                }
            }
            matrixStack.pop();
        }
        matrixStack.pop();
    }
}
