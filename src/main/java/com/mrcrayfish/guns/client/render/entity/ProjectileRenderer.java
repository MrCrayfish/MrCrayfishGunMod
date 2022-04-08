package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ProjectileRenderer extends EntityRenderer<ProjectileEntity>
{
    public ProjectileRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileEntity entity)
    {
        return null;
    }

    @Override
    public void render(ProjectileEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
        if(!entity.getProjectile().isVisible() || entity.tickCount <= 1)
        {
            return;
        }

        matrixStack.pushPose();

        if(!RenderUtil.getModel(entity.getItem()).isGui3d())
        {
            matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemCameraTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        }
        else
        {
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(entityYaw));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        }

        matrixStack.popPose();
    }
}
