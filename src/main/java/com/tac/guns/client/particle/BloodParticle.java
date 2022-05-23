package com.tac.guns.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@OnlyIn(Dist.CLIENT)
public class BloodParticle extends SpriteTexturedParticle
{
    public BloodParticle(ClientWorld world, double x, double y, double z)
    {
        super(world, x, y, z, 0.1, 0.1, 0.1);
        this.setColor(0.541F, 0.027F, 0.027F);
        this.particleGravity = 1.25F;
        this.particleScale = 0.125F;
        this.maxAge = (int)(12.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.onGround)
        {
            this.motionX = 0;
            this.motionZ = 0;
            this.particleScale *= 0.95F;
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        Vector3d projectedView = renderInfo.getProjectedView();
        float x = (float) (MathHelper.lerp((double) partialTicks, this.prevPosX, this.posX) - projectedView.getX());
        float y = (float) (MathHelper.lerp((double) partialTicks, this.prevPosY, this.posY) - projectedView.getY());
        float z = (float) (MathHelper.lerp((double) partialTicks, this.prevPosZ, this.posZ) - projectedView.getZ());

        if(this.onGround)
        {
            y += 0.01;
        }

        Quaternion rotation = Direction.NORTH.getRotation();
        if(this.particleAngle == 0.0F)
        {
            if(!this.onGround)
            {
                rotation = renderInfo.getRotation();
            }
        }
        else
        {
            rotation = new Quaternion(renderInfo.getRotation());
            float angle = MathHelper.lerp(partialTicks, this.prevParticleAngle, this.particleAngle);
            rotation.multiply(Vector3f.ZP.rotation(angle));
        }

        Vector3f[] vertices = new Vector3f[] {
            new Vector3f(-1.0F, -1.0F, 0.0F),
            new Vector3f(-1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float scale = this.getScale(partialTicks);
        for(int i = 0; i < 4; ++i)
        {
            Vector3f vertex = vertices[i];
            vertex.transform(rotation);
            vertex.mul(scale);
            vertex.add(x, y, z);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int light = this.getBrightnessForRender(partialTicks);
        buffer.pos(vertices[0].getX(), vertices[0].getY(), vertices[0].getZ()).tex(maxU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        buffer.pos(vertices[1].getX(), vertices[1].getY(), vertices[1].getZ()).tex(maxU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        buffer.pos(vertices[2].getX(), vertices[2].getY(), vertices[2].getZ()).tex(minU, minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
        buffer.pos(vertices[3].getX(), vertices[3].getY(), vertices[3].getZ()).tex(minU, maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(light).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BloodParticle particle = new BloodParticle(worldIn, x, y, z);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}
