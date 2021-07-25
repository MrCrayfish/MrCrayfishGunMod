package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.entity.MissileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: MrCrayfish
 */
public class MissileRenderer extends EntityRenderer<MissileEntity>
{
    public MissileRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(MissileEntity entity)
    {
        return null;
    }

    @Override
    public void render(MissileEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
        if(!entity.getProjectile().isVisible() || entity.tickCount <= 1)
        {
            return;
        }

        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(entityYaw));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot - 90));
        Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemCameraTransforms.TransformType.NONE, 15728880, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        matrixStack.translate(0, -1, 0);
        RenderUtil.renderModel(SpecialModels.FLAME.getModel(), entity.getItem(), matrixStack, renderTypeBuffer, 15728880, OverlayTexture.NO_OVERLAY);
        matrixStack.popPose();
    }
}
