package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;

public class ProjectileRenderer extends EntityRenderer<ProjectileEntity>
{
    public ProjectileRenderer(EntityRendererProvider.Context context)
    {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileEntity entity)
    {
        return null;
    }

    @Override
    public void render(ProjectileEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource renderTypeBuffer, int light)
    {
        if(!entity.getProjectile().isVisible() || entity.tickCount <= 1)
        {
            return;
        }

        poseStack.pushPose();

        if(!RenderUtil.getModel(entity.getItem()).isGui3d())
        {
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.GROUND, light, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, entity.level(), 0);
        }
        else
        {
            poseStack.mulPose(Axis.YP.rotationDegrees(180F));
            poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.NONE, light, OverlayTexture.NO_OVERLAY, poseStack, renderTypeBuffer, entity.level(), 0);
        }

        poseStack.popPose();
    }
}
