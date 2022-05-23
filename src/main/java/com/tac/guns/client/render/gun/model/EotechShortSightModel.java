package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.RecoilHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.OptifineHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class EotechShortSightModel implements IOverrideModel
{
    private static final ResourceLocation RED_DOT_RETICLE = new ResourceLocation(Reference.MOD_ID, "textures/items/timeless_scopes/eotech_cqb_reticle.png");
    //private static final ResourceLocation RED_DOT_RETICLE_GLOW = new ResourceLocation(Reference.MOD_ID, "textures/effect/red_dot_reticle_glow.png");
    //private static final ResourceLocation VIGNETTE = new ResourceLocation(Reference.MOD_ID, "textures/effect/scope_vignette.png");

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay) {
        matrixStack.push();
        if (Config.COMMON.gameplay.redDotSquish2D.get() && transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player)) {
            double transition = 1.0D - Math.pow(1.0D - AimingHandler.get().getNormalisedAdsProgress(), 2.0D);
            double zScale = 0.05D + 0.95D * (1.0D - transition);
            matrixStack.scale(1.0F, 1.0F, (float)zScale);
        }

        int glassGlowColor = RenderUtil.getItemStackColor(stack, parent, IAttachment.Type.SCOPE_GLASS_COLOR, 2);
        float red = ((glassGlowColor >> 16) & 0xFF) / 255F;
        float green = ((glassGlowColor >> 8) & 0xFF) / 255F;
        float blue = ((glassGlowColor >> 0) & 0xFF) / 255F;

        matrixStack.translate(0, 0.055, 0);

        RenderUtil.renderModel(stack, parent, matrixStack, renderTypeBuffer, light, overlay);

        matrixStack.translate(0, -0.049, 0);
        matrixStack.pop();
        matrixStack.translate(0, 0.006, 0);
        if(transformType.isFirstPerson() && entity.equals(Minecraft.getInstance().player))
        {
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
                matrixStack.translate(-size / 2, 1.38 * 0.0625, 0.075 * 0.0625);

                IVertexBuilder builder;
                matrixStack.translate(-0.04 * invertProgress, 0.01 * invertProgress, 0);

                double scale = 3.75;
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
                /*
                //matrixStack.translate((double) (MathHelper.sin(GunRenderingHandler.get().walkingDistance * GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 0.5F) * GunRenderingHandler.get().zoomProgressInv, ((double) (-Math.abs(MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw)) * GunRenderingHandler.get().zoomProgressInv) * 1.140, 0.0D);// * 1.140, 0.0D);
                //matrixStack.translate((double) (Math.asin(-MathHelper.sin(distanceWalked*crouch * (float) Math.PI) * cameraYaw * 0.5F)) * invertZoomProgress, ((double) (Math.asin((-Math.abs(-MathHelper.cos(distanceWalked*crouch * (float) Math.PI) * cameraYaw))) * invertZoomProgress)) * 1.140, 0.0D);// * 1.140, 0.0D);

                //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-(MathHelper.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 3.0F) * (float) GunRenderingHandler.get().zoomProgressInv));
                //matrixStack.rotate(Vector3f.XP.rotationDegrees(-(Math.abs(MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI - 0.2F) * GunRenderingHandler.get().walkingCameraYaw) * 5.0F) * (float) GunRenderingHandler.get().zoomProgressInv));
                //matrixStack.rotate(Vector3f.ZP.rotationDegrees(-GunRenderingHandler.get().immersiveWeaponRoll));

                matrixStack.translate(0, gun.getModules().getAttachments().getScope().getYOffset(), -gun.getModules().getAttachments().getScope().getZOffset());

                matrixStack.translate( -1 * (double) (Math.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 0.5F) * invertZoomProgress,
                        ((double) (Math.abs(MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw)) * invertZoomProgress) * 1.140,0);//(Math.asin((double) (Math.abs(-MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw))) * invertZoomProgress * 1.140), 0.0D);// * 1.140, 0.0D);
                *//*matrixStack.translate( -1 * (double) (Math.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 0.5F) * invertZoomProgress,
                        (Math.asin((double) (Math.abs(-MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw))) * invertZoomProgress),0);//(Math.asin((double) (Math.abs(-MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw))) * invertZoomProgress * 1.140), 0.0D);// * 1.140, 0.0D);
                *//*matrixStack.rotate(Vector3f.ZP.rotationDegrees(( 1.125f*( MathHelper.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 3.0F) * (float) invertZoomProgress)) );
                matrixStack.rotate(Vector3f.XP.rotationDegrees(-1f*(Math.abs(MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI - 0.2F) * GunRenderingHandler.get().walkingCameraYaw) * 5.0F) * (float) invertZoomProgress));

                matrixStack.translate(0, -gun.getModules().getAttachments().getScope().getYOffset(), gun.getModules().getAttachments().getScope().getZOffset());

                matrixStack.translate(0, 0, -0.75f*(GunRenderingHandler.get().kick * GunRenderingHandler.get().kickReduction));
                matrixStack.translate(0, 0, -0.35);
                matrixStack.rotate(Vector3f.YP.rotationDegrees((GunRenderingHandler.get().recoilSway * GunRenderingHandler.get().recoilReduction)*0.625f));
                matrixStack.rotate(Vector3f.ZN.rotationDegrees((GunRenderingHandler.get().recoilSway * RecoilHandler.get().horizontalCameraRecoil)*0.625f)); // seems to be interesting to increase the force of
                //matrixStack.rotate(Vector3f.ZP.rotationDegrees(recoilSway * 2.5f * recoilReduction)); // seems to be interesting to increase the force of
                matrixStack.rotate(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift * GunRenderingHandler.get().recoilReduction)*0.625f));
                matrixStack.translate(0, 0, 0.35);

                matrixStack.translate(0, 0, 0.010);
                double scale = 3.75;
                matrixStack.translate(size / 2, size / 2, 0);
                matrixStack.translate(-(size / scale) / 2, -(size / scale) / 2, 0);
                matrixStack.translate(0, 0, 0.0001);
*/
                //double invertZoomProgress = aimed ? 0.0575 : 0.468;//double invertZoomProgress = aimed ? 0.135 : 0.94;//aimed ? 1.0 - AimingHandler.get().getNormalisedAdsProgress() : ;
                matrixStack.translate(-1.00*Math.asin(((double) (MathHelper.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI)) * GunRenderingHandler.get().walkingCameraYaw * 0.5F) * invertZoomProgress), 1.00*(Math.asin((double) (Math.abs(-MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw))) * invertZoomProgress * 1.140),0);//(Math.asin((double) (Math.abs(-MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw))) * invertZoomProgress * 1.140), 0.0D);// * 1.140, 0.0D);
                matrixStack.rotate(Vector3f.ZN.rotationDegrees((float)(MathHelper.sin(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI) * GunRenderingHandler.get().walkingCameraYaw * 3.0F) * (float) invertZoomProgress));
                matrixStack.rotate(Vector3f.XN.rotationDegrees((float)(Math.abs(MathHelper.cos(GunRenderingHandler.get().walkingDistance*GunRenderingHandler.get().walkingCrouch * (float) Math.PI - 0.2F) * GunRenderingHandler.get().walkingCameraYaw) * 5.0F) * (float) invertZoomProgress));

                //matrixStack.translate(0, 0, (GunRenderingHandler.get().kick * -GunRenderingHandler.get().kickReduction)*0.75);
                matrixStack.translate(0, 0, -0.35);
                matrixStack.rotate(Vector3f.YN.rotationDegrees((GunRenderingHandler.get().recoilSway * GunRenderingHandler.get().recoilReduction)*0.5F));
                matrixStack.rotate(Vector3f.ZN.rotationDegrees((GunRenderingHandler.get().recoilSway * GunRenderingHandler.get().weaponsHorizontalAngle * 0.65f * GunRenderingHandler.get().recoilReduction)*0.5F)); // seems to be interesting to increase the force of
                //matrixStack.rotate(Vector3f.ZP.rotationDegrees(recoilSway * 2.5f * recoilReduction)); // seems to be interesting to increase the force of
                matrixStack.rotate(Vector3f.XP.rotationDegrees((GunRenderingHandler.get().recoilLift * GunRenderingHandler.get().recoilReduction) * 0.75F));
                matrixStack.translate(0, 0, 0.35);

                builder.pos(matrix, 0, (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.0F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, 0, 0, 0).color(red, green, blue, alpha).tex(0.0F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), 0, 0).color(red, green, blue, alpha).tex(0.9375F, 0.0F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
                builder.pos(matrix, (float) (size / scale), (float) (size / scale), 0).color(red, green, blue, alpha).tex(0.9375F, 0.9375F).overlay(overlay).lightmap(15728880).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
            }
            matrixStack.pop();
        }
    }
}
