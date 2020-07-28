package com.mrcrayfish.guns.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.entity.ThrowableGrenadeEntity;
import com.mrcrayfish.guns.entity.ThrowableStunGrenadeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ThrowableGrenadeRenderer extends EntityRenderer<ThrowableGrenadeEntity>
{
    public ThrowableGrenadeRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(ThrowableGrenadeEntity entity)
    {
        return null;
    }

    @Override
    public void render(ThrowableGrenadeEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light)
    {
        matrixStack.push();

        /* Makes the grenade face in the direction of travel */
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(entityYaw));

        /* Offsets to the center of the grenade before applying rotation */
        float rotation = entity.prevRotation + (entity.rotation - entity.prevRotation) * partialTicks;
        matrixStack.translate(0, 0.15, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-rotation));
        matrixStack.translate(0, -0.15, 0);

        if(entity instanceof ThrowableStunGrenadeEntity)
        {
            matrixStack.translate(0, entity.getSize(Pose.STANDING).height / 2, 0);
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(-90F));
            matrixStack.translate(0, -entity.getSize(Pose.STANDING).height / 2, 0);
        }

        /* */
        matrixStack.translate(0.0, 0.5, 0.0);

        Minecraft.getInstance().getItemRenderer().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.NONE, light, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);

        matrixStack.pop();
    }
}
