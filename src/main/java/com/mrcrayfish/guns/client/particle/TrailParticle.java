package com.mrcrayfish.guns.client.particle;

import com.mrcrayfish.guns.particles.TrailData;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
@OnlyIn(Dist.CLIENT)
public class TrailParticle extends RisingParticle
{
    protected TrailParticle(ClientWorld world, double x, double y, double z, float scale, float red, float green, float blue, IAnimatedSprite spriteWithAge)
    {
        super(world, x, y, z, 0.0F, 0.0F, 0.0F, 0.0, 0.0, 0.0, scale, spriteWithAge, 0.2F, 0, 0.0D, false);
        this.maxAge = 4;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleAlpha = 0.25F;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<TrailData>
    {
        private final IAnimatedSprite spriteSet;

        public Factory(IAnimatedSprite spriteSet)
        {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle makeParticle(TrailData data, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
        {
            float red = data.isEnchanted() ? 0.611F : 0.5F;
            float green = data.isEnchanted() ? 0.443F : 0.5F;
            float blue = data.isEnchanted() ? 1.0F : 0.5F;
            return new TrailParticle(worldIn, x, y, z, 1.0F, red, green, blue, this.spriteSet);
        }
    }
}
