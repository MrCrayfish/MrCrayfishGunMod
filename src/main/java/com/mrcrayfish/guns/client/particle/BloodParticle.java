package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

/**
 * Author: MrCrayfish
 */
public class BloodParticle extends Particle
{
    public BloodParticle(ClientWorld world, double x, double y, double z)
    {
        super(world, x, y, z, 0.1, 0.1, 0.1);
        this.particleRed = 0.541F;
        this.particleGreen = 0.027F;
        this.particleBlue = 0.027F;
        this.particleGravity = 1.0F;
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        Vector3d view = renderInfo.getProjectedView();
        float posX = (float) (MathHelper.lerp((double) partialTicks, this.prevPosX, this.posX) - view.getX());
        float posY = (float) (MathHelper.lerp((double) partialTicks, this.prevPosY, this.posY) - view.getY());
        float posZ = (float) (MathHelper.lerp((double) partialTicks, this.prevPosZ, this.posZ) - view.getZ());

        Vector3f[] positions = new Vector3f[] {
            new Vector3f(-1.0F, -1.0F, 0.0F),
            new Vector3f(-1.0F,  1.0F, 0.0F),
            new Vector3f( 1.0F,  1.0F, 0.0F),
            new Vector3f( 1.0F, -1.0F, 0.0F)
        };

        Quaternion quaternion = renderInfo.getRotation();
        float scale = 0.05F;
        for(int i = 0; i < 4; ++i)
        {
            Vector3f vector3f = positions[i];
            vector3f.transform(quaternion);
            vector3f.mul(scale);
            vector3f.add(posX, posY, posZ);
        }

        RenderSystem.disableTexture();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_LIGHTMAP);

        int light = this.getBrightnessForRender(partialTicks);
        builder.pos(positions[0].getX(), positions[0].getY(), positions[0].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        builder.pos(positions[1].getX(), positions[1].getY(), positions[1].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        builder.pos(positions[2].getX(), positions[2].getY(), positions[2].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        builder.pos(positions[3].getX(), positions[3].getY(), positions[3].getZ()).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();

        tessellator.draw();

        RenderSystem.enableTexture();
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.CUSTOM;
    }
}
