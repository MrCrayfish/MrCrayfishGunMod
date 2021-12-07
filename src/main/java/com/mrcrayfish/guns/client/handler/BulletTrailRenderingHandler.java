package com.mrcrayfish.guns.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrcrayfish.guns.client.BulletTrail;
import com.mrcrayfish.guns.client.GunRenderType;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.util.OptifineHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import com.mojang.math.Matrix4f;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class BulletTrailRenderingHandler
{
    private static BulletTrailRenderingHandler instance;

    public static BulletTrailRenderingHandler get()
    {
        if(instance == null)
        {
            instance = new BulletTrailRenderingHandler();
        }
        return instance;
    }

    private Map<Integer, BulletTrail> bullets = new HashMap<>();

    private BulletTrailRenderingHandler() {}

    /**
     * Adds a bullet trail to render into the world
     *
     * @param trail the bullet trail get
     */
    public void add(BulletTrail trail)
    {
        // Prevents trails being added when not in a world
        Level world = Minecraft.getInstance().level;
        if(world != null)
        {
            this.bullets.put(trail.getEntityId(), trail);
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
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Level world = Minecraft.getInstance().level;
        if(world != null)
        {
            if(event.phase == TickEvent.Phase.END)
            {
                this.bullets.values().forEach(BulletTrail::tick);
                this.bullets.values().removeIf(BulletTrail::isDead);
            }
        }
        else if(!this.bullets.isEmpty())
        {
            this.bullets.clear();
        }
    }

    public void render(PoseStack stack, float partialSticks)
    {
        for(BulletTrail bulletTrail : this.bullets.values())
        {
            this.renderBulletTrail(bulletTrail, stack, partialSticks);
        }
    }

    @SubscribeEvent
    public void onRespawn(ClientPlayerNetworkEvent.RespawnEvent event)
    {
        this.bullets.clear();
    }

    @SubscribeEvent
    public void onLoggedOut(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.bullets.clear();
    }

    private void renderBulletTrail(BulletTrail trail, PoseStack poseStack, float deltaTicks)
    {
        if(OptifineHelper.isShadersEnabled())
            return;

        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.getCameraEntity();
        if(entity == null || trail.isDead())
            return;

        poseStack.pushPose();

        Vec3 view = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 position = trail.getPosition();
        Vec3 motion = trail.getMotion();
        double bulletX = position.x + motion.x * deltaTicks;
        double bulletY = position.y + motion.y * deltaTicks;
        double bulletZ = position.z + motion.z * deltaTicks;
        poseStack.translate(bulletX - view.x(), bulletY - view.y(), bulletZ - view.z());

        poseStack.mulPose(Vector3f.YP.rotationDegrees(trail.getYaw()));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-trail.getPitch() + 90));

        Vec3 motionVec = new Vec3(motion.x, motion.y, motion.z);
        float trailLength = (float) (motionVec.length() * trail.getTrailLengthMultiplier());
        float red = (float) (trail.getTrailColor() >> 16 & 255) / 255.0F;
        float green = (float) (trail.getTrailColor() >> 8 & 255) / 255.0F;
        float blue = (float) (trail.getTrailColor() & 255) / 255.0F;
        float alpha = 0.3F;

        // Prevents the trail length from being longer than the distance to shooter
        Entity shooter = trail.getShooter();
        if(shooter != null)
        {
            trailLength = (float) Math.min(trailLength, shooter.getEyePosition(deltaTicks).distanceTo(new Vec3(bulletX,bulletY, bulletZ)));
        }

        Matrix4f matrix4f = poseStack.last().pose();
        MultiBufferSource.BufferSource renderTypeBuffer = mc.renderBuffers().bufferSource();

        if(trail.isTrailVisible())
        {
            RenderType bulletType = GunRenderType.getBulletTrail();
            VertexConsumer builder = renderTypeBuffer.getBuffer(bulletType);
            builder.vertex(matrix4f, 0, 0, -0.035F).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0, 0, 0.035F).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0, -trailLength, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0, -trailLength, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, -0.035F, 0, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0.035F, 0, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0, -trailLength, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            builder.vertex(matrix4f, 0, -trailLength, 0).color(red, green, blue, alpha).uv2(15728880).endVertex();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch(bulletType);
        }

        if(!trail.getItem().isEmpty())
        {
            poseStack.mulPose(Vector3f.YP.rotationDegrees((trail.getAge() + deltaTicks) * (float) 50));
            poseStack.scale(0.275F, 0.275F, 0.275F);

            int combinedLight = LevelRenderer.getLightColor(entity.level, new BlockPos(entity.position()));
            ItemStack stack = trail.getItem();
            RenderUtil.renderModel(stack, ItemTransforms.TransformType.NONE, poseStack, renderTypeBuffer, combinedLight, OverlayTexture.NO_OVERLAY, null, null);
        }

        poseStack.popPose();
    }
}
