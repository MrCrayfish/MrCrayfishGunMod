package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.object.Bullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class BulletRenderer
{
    private static final ResourceLocation WHITE_GRADIENT = new ResourceLocation(Reference.MOD_ID, "textures/effect/white_gradient.png");

    private List<Bullet> bullets = new ArrayList<>();

    /**
     *
     * @param bullet
     */
    public void addBullet(Bullet bullet)
    {
        this.bullets.add(bullet);
    }

    @SubscribeEvent
    public void onTickBullets(TickEvent.ClientTickEvent event)
    {
        if(Minecraft.getInstance().world != null && event.phase == TickEvent.Phase.END)
        {
            this.bullets.forEach(bullet -> bullet.tick(Minecraft.getInstance().world));
            this.bullets.removeIf(Bullet::isFinished);
        }
    }

    @SubscribeEvent
    public void onRenderBullets(RenderWorldLastEvent event)
    {
        for(Bullet bullet : this.bullets)
        {
            this.renderBullet(bullet, event.getMatrixStack(), event.getPartialTicks());
        }
    }

    /**
     *
     * @param bullet
     * @param matrixStack
     * @param partialTicks
     */
    private void renderBullet(Bullet bullet, MatrixStack matrixStack, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getRenderViewEntity();
        if(entity == null || bullet.isFinished() || bullet.getProjectile() == null)
            return;

        matrixStack.push();

        double doubleX = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
        double doubleY = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
        double doubleZ = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());
        matrixStack.translate(-doubleX, -doubleY, -doubleZ);

        double bulletX = bullet.getPosX() + bullet.getMotionX() * partialTicks;
        double bulletY = bullet.getPosY() + bullet.getMotionY() * partialTicks;
        double bulletZ = bullet.getPosZ() + bullet.getMotionZ() * partialTicks;
        matrixStack.translate(bulletX, bulletY, bulletZ);

        matrixStack.rotate(Vector3f.YP.rotationDegrees(bullet.getRotationYaw()));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-bullet.getRotationPitch() + 90));

        Vec3d motionVec = new Vec3d(bullet.getMotionX(), bullet.getMotionY(), bullet.getMotionZ());
        float trailLength = (float) ((motionVec.length() / 3.0F) * bullet.getTrailLengthMultiplier());
        float red = (float)(bullet.getTrailColor() >> 16 & 255) / 255.0F;
        float green = (float)(bullet.getTrailColor() >> 8 & 255) / 255.0F;
        float blue = (float)(bullet.getTrailColor() & 255) / 255.0F;
        float alpha = 1.0F;

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        IRenderTypeBuffer.Impl renderTypeBuffer = mc.getRenderTypeBuffers().getBufferSource();
        IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.getEntityTranslucent(WHITE_GRADIENT)); //TODO probably will crash
        builder.pos(matrix4f, 0, 0, -0.035F).tex(0, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, 0, 0, 0.035F).tex(1, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, 0, -trailLength, 0.035F).tex(1, 1).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, 0, -trailLength, -0.035F).tex(0, 1).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, -0.035F, 0, 0).tex(0, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, 0.035F, 0, 0).tex(1, 0).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, 0.035F, -trailLength, 0).tex(1, 1).color(red, green, blue, alpha).endVertex();
        builder.pos(matrix4f, -0.035F, -trailLength, 0).tex(0, 1).color(red, green, blue, alpha).endVertex();

        // No point rendering item if empty, so return
        if(bullet.getProjectile().getItem().isEmpty())
        {
            matrixStack.pop();
            return;
        }

        matrixStack.rotate(Vector3f.YP.rotationDegrees((bullet.getProjectile().ticksExisted + partialTicks) * (float) 50));
        matrixStack.scale(0.275F, 0.275F, 0.275F);

        int combinedLight = WorldRenderer.getCombinedLight(entity.world, entity.getPosition());
        mc.getItemRenderer().renderItem(bullet.getProjectile().getItem(), ItemCameraTransforms.TransformType.NONE, combinedLight, OverlayTexture.NO_OVERLAY, matrixStack, renderTypeBuffer);

        matrixStack.pop();
    }
}
