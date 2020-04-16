package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mrcrayfish.guns.object.Bullet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class BulletHandler
{
    private List<Bullet> bullets = new ArrayList<>();

    @SubscribeEvent
    public void onRenderBullets(RenderWorldLastEvent event)
    {
        for(Bullet bullet : bullets)
        {
            this.renderBullet(bullet, event.getPartialTicks());
        }
    }

    private void renderBullet(Bullet bullet, float partialTicks)
    {
        Entity entity = Minecraft.getInstance().getRenderViewEntity();
        if(entity == null || bullet.isFinished() || bullet.getProjectile() == null)
            return;

        GlStateManager.pushMatrix();

        double doubleX = MathHelper.lerp(partialTicks, entity.lastTickPosX, entity.getPosX());
        double doubleY = MathHelper.lerp(partialTicks, entity.lastTickPosY, entity.getPosY());
        double doubleZ = MathHelper.lerp(partialTicks, entity.lastTickPosZ, entity.getPosZ());

        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

        double bulletX = bullet.getPosX() + bullet.getMotionX() * partialTicks;
        double bulletY = bullet.getPosY() + bullet.getMotionY() * partialTicks;
        double bulletZ = bullet.getPosZ() + bullet.getMotionZ() * partialTicks;
        GlStateManager.translate(bulletX, bulletY, bulletZ);

        GlStateManager.rotate(bullet.getRotationYaw(), 0, 1, 0);
        GlStateManager.rotate(-bullet.getRotationPitch() + 90, 1, 0, 0);

        {
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            Minecraft.getInstance().getTextureManager().bindTexture(WHITE_GRADIENT);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            Vec3d motionVec = new Vec3d(bullet.getMotionX(), bullet.getMotionY(), bullet.getMotionZ());
            double length = (motionVec.length() / 3.0) * bullet.getTrailLengthMultiplier();

            int trailColor = bullet.getTrailColor();
            float r = (float)(trailColor >> 16 & 255) / 255.0F;
            float g = (float)(trailColor >> 8 & 255) / 255.0F;
            float b = (float)(trailColor & 255) / 255.0F;
            float a = 1.0F;

            buffer.pos(0, 0, -0.035).tex(0, 0).color(r, g, b, a).endVertex();
            buffer.pos(0, 0, 0.035).tex(1, 0).color(r, g, b, a).endVertex();
            buffer.pos(0, -length, 0.035).tex(1, 1).color(r, g, b, a).endVertex();
            buffer.pos(0, -length, -0.035).tex(0, 1).color(r, g, b, a).endVertex();

            buffer.pos(-0.035, 0, 0).tex(0, 0).color(r, g, b, a).endVertex();
            buffer.pos(0.035, 0, 0).tex(1, 0).color(r, g, b, a).endVertex();
            buffer.pos(0.035, -length, 0).tex(1, 1).color(r, g, b, a).endVertex();
            buffer.pos(-0.035, -length, 0).tex(0, 1).color(r, g, b, a).endVertex();

            tessellator.draw();

            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.popAttrib();
        }

        // No point rendering item if empty, so return
        if(bullet.getProjectile().getItem().isEmpty())
        {
            GlStateManager.popMatrix();
            return;
        }

        GlStateManager.rotate((bullet.getProjectile().ticksExisted + partialTicks) * (float) 50, 0, 1, 0);

        GlStateManager.scale(0.275, 0.275, 0.275);

        Minecraft mc = Minecraft.getInstance();
        mc.entityRenderer.enableLightmap();
        int brightness = 0;
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(MathHelper.floor(doubleX), 0, MathHelper.floor(doubleZ));
        if (mc.world.isBlockLoaded(blockPos))
        {
            blockPos.setY(MathHelper.floor(doubleY));
            brightness = mc.world.getCombinedLight(blockPos, 0);
        }
        int x = brightness % 65536;
        int y = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)x, (float)y);
        Minecraft.getInstance().getRenderItem().renderItem(bullet.getProjectile().getItem(), ItemCameraTransforms.TransformType.NONE);
        mc.entityRenderer.disableLightmap();

        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onTickBullets(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            bullets.forEach(bullet -> bullet.tick(Minecraft.getInstance().world));
            bullets.removeIf(Bullet::isFinished);
        }
    }

    public void addBullet(Bullet bullet)
    {
        this.bullets.add(bullet);
    }
}
