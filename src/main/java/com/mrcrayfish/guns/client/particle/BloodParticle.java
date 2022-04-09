package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
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
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class BloodParticle extends SpriteTexturedParticle
{
    public BloodParticle(ClientWorld world, double x, double y, double z)
    {
        super(world, x, y, z, 0.1, 0.1, 0.1);
        this.gravity = 1.5F;
        this.quadSize = 0.0625F;
        this.lifetime = (int)(12.0F / (this.random.nextFloat() * 0.9F + 0.1F));
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
            this.xd = 0;
            this.zd = 0;
            this.quadSize *= 0.95F;
        }
    }

    @Override
    public void render(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        Vector3d projectedView = renderInfo.getPosition();
        float x = (float) (MathHelper.lerp((double) partialTicks, this.xo, this.x) - projectedView.x());
        float y = (float) (MathHelper.lerp((double) partialTicks, this.yo, this.y) - projectedView.y());
        float z = (float) (MathHelper.lerp((double) partialTicks, this.zo, this.z) - projectedView.z());

        if(this.onGround)
        {
            y += 0.01;
        }

        Quaternion rotation = Direction.NORTH.getRotation();
        if(this.roll == 0.0F)
        {
            if(!this.onGround)
            {
                rotation = renderInfo.rotation();
            }
        }
        else
        {
            rotation = new Quaternion(renderInfo.rotation());
            float angle = MathHelper.lerp(partialTicks, this.oRoll, this.roll);
            rotation.mul(Vector3f.ZP.rotation(angle));
        }

        Vector3f[] vertices = new Vector3f[] {
            new Vector3f(-1.0F, -1.0F, 0.0F),
            new Vector3f(-1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, 1.0F, 0.0F),
            new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float scale = this.getQuadSize(partialTicks);
        for(int i = 0; i < 4; ++i)
        {
            Vector3f vertex = vertices[i];
            vertex.transform(rotation);
            vertex.mul(scale);
            vertex.add(x, y, z);
        }

        float minU = this.getU0();
        float maxU = this.getU1();
        float minV = this.getV0();
        float maxV = this.getV1();
        int light = this.getLightColor(partialTicks);
        buffer.vertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).uv(maxU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).uv(maxU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).uv(minU, minV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
        buffer.vertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).uv(minU, maxV).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(light).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<BasicParticleType>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BloodParticle particle = new BloodParticle(worldIn, x, y, z);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}
