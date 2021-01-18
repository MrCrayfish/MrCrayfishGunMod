package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.client.RenderTypes;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.object.Bullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class BulletRenderer
{
    private Map<Integer, Bullet> bullets = new HashMap<>();

    /**
     * Adds a bullet to render into the world
     *
     * @param bullet the bullet instance
     */
    public void addBullet(Bullet bullet)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            this.bullets.put(bullet.getEntityId(), bullet);
        }
    }

    /**
     * Removes the bullet for the given entity id.
     *
     * @param entityId the entity id of the bullet
     */
    public void remove(int entityId)
    {
        this.bullets.remove(entityId);
    }

    @SubscribeEvent
    public void onTickBullets(TickEvent.ClientTickEvent event)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            if(event.phase == TickEvent.Phase.END)
            {
                this.bullets.values().forEach(Bullet::tick);
                this.bullets.values().removeIf(Bullet::isDead);
            }
        }
        else if(!this.bullets.isEmpty())
        {
            this.bullets.clear();
        }
    }

    @SubscribeEvent
    public void onRenderBullets(RenderWorldLastEvent event)
    {
        for(Bullet bullet : this.bullets.values())
        {
            this.renderBullet(bullet, event.getMatrixStack(), event.getPartialTicks());
        }
    }

    /**
     * @param bullet
     * @param matrixStack
     * @param partialTicks
     */
    private void renderBullet(Bullet bullet, MatrixStack matrixStack, float partialTicks)
    {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getRenderViewEntity();
        if(entity == null || bullet.isDead()) return;

        matrixStack.push();

        Vector3d view = mc.gameRenderer.getActiveRenderInfo().getProjectedView();
        Vector3d position = bullet.getPosition();
        Vector3d motion = bullet.getMotion();
        double bulletX = position.x + motion.x * partialTicks;
        double bulletY = position.y + motion.y * partialTicks;
        double bulletZ = position.z + motion.z * partialTicks;
        matrixStack.translate(bulletX - view.getX(), bulletY - view.getY(), bulletZ - view.getZ());

        matrixStack.rotate(Vector3f.YP.rotationDegrees(bullet.getYaw()));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-bullet.getPitch() + 90));

        Vector3d motionVec = new Vector3d(motion.x, motion.y, motion.z);
        float trailLength = (float) ((motionVec.length() / 3.0F) * bullet.getTrailLengthMultiplier());
        float red = (float) (bullet.getTrailColor() >> 16 & 255) / 255.0F;
        float green = (float) (bullet.getTrailColor() >> 8 & 255) / 255.0F;
        float blue = (float) (bullet.getTrailColor() & 255) / 255.0F;
        float alpha = 0.3F;

        Matrix4f matrix4f = matrixStack.getLast().getMatrix();
        IRenderTypeBuffer.Impl renderTypeBuffer = mc.getRenderTypeBuffers().getBufferSource();

        if(bullet.isTrailVisible())
        {
            RenderType bulletType = RenderTypes.getBulletTrail();
            IVertexBuilder builder = renderTypeBuffer.getBuffer(bulletType);
            builder.pos(matrix4f, 0, 0, -0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, 0, 0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, -trailLength, 0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0, -trailLength, -0.035F).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, -0.035F, 0, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0.035F, 0, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, 0.035F, -trailLength, 0).color(red, green, blue, alpha).endVertex();
            builder.pos(matrix4f, -0.035F, -trailLength, 0).color(red, green, blue, alpha).endVertex();
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(bulletType);
        }

        if(!bullet.getItem().isEmpty())
        {
            matrixStack.rotate(Vector3f.YP.rotationDegrees((bullet.getAge() + partialTicks) * (float) 50));
            matrixStack.scale(0.275F, 0.275F, 0.275F);

            int combinedLight = WorldRenderer.getCombinedLight(entity.world, new BlockPos(entity.getPositionVec()));
            ItemStack stack = bullet.getItem();
            RenderType renderType = RenderTypeLookup.func_239219_a_(stack, false);
            RenderUtil.renderModel(stack, ItemCameraTransforms.TransformType.NONE, matrixStack, renderTypeBuffer, combinedLight, OverlayTexture.NO_OVERLAY);
            Minecraft.getInstance().getRenderTypeBuffers().getBufferSource().finish(renderType);
        }

        matrixStack.pop();
    }
}
