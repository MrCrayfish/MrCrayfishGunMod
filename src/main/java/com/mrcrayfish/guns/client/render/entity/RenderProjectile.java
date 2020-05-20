package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderProjectile extends EntityRenderer<EntityProjectile>
{
    public RenderProjectile(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityProjectile entity)
    {
        return null;
    }

    @Override
    public void render(EntityProjectile entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
        if(!entity.getProjectile().visible)
        {
            return;
        }

        matrixStack.push();
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(entityYaw));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch));
        float distancePercent = (entity.ticksExisted + partialTicks) / (entity.getProjectile().life / 2.0F);
        double translate = -0.25 - (0.25 * distancePercent);
        matrixStack.translate(0, 0, 0);
        Minecraft.getInstance().getItemRenderer().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);
        matrixStack.pop();
    }
}
