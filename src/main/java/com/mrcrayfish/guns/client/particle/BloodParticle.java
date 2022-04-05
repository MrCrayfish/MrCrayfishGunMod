package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.math.Quaternion;
import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class BloodParticle extends TextureSheetParticle
{
    public BloodParticle(ClientLevel world, double x, double y, double z)
    {
        super(world, x, y, z, 0.1, 0.1, 0.1);
        this.setColor(0.541F, 0.027F, 0.027F);
        this.gravity = 1.5F;
        this.quadSize = 0.0625F;
        this.lifetime = (int)(12.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        Vec3 projectedView = renderInfo.getPosition();
        float x = (float) (Mth.lerp((double) partialTicks, this.xo, this.x) - projectedView.x());
        float y = (float) (Mth.lerp((double) partialTicks, this.yo, this.y) - projectedView.y());
        float z = (float) (Mth.lerp((double) partialTicks, this.zo, this.z) - projectedView.z());

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
            float angle = Mth.lerp(partialTicks, this.oRoll, this.roll);
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
    public static class Factory implements ParticleProvider<SimpleParticleType>
    {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            BloodParticle particle = new BloodParticle(worldIn, x, y, z);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}
