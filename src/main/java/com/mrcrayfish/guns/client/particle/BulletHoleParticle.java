package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class BulletHoleParticle extends SpriteTexturedParticle
{
    private Direction direction;
    private BlockPos pos;

    public BulletHoleParticle(World world, double x, double y, double z, Direction direction, BlockPos pos)
    {
        super(world, x, y, z);
        this.setSprite(Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(GunMod.BULLET_HOLE_TEXTURE));
        this.direction = direction;
        this.pos = pos;
        this.maxAge = 200;
        this.canCollide = false;
        this.particleGravity = 0.0F;
        this.particleScale = 0.04F;
    }

    @Override
    public void tick()
    {
        super.tick();
        Minecraft minecraft = Minecraft.getInstance();
        World world = minecraft.world;
        if(world != null)
        {
            if(world.getBlockState(this.pos).isAir(world, this.pos))
            {
                this.setExpired();
            }
        }
    }

    @Override
    public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo renderInfo, float partialTicks)
    {
        Vec3d view = renderInfo.getProjectedView();
        float particleX = (float) (MathHelper.lerp((double) partialTicks, this.prevPosX, this.posX) - view.getX());
        float particleY = (float) (MathHelper.lerp((double) partialTicks, this.prevPosY, this.posY) - view.getY());
        float particleZ = (float) (MathHelper.lerp((double) partialTicks, this.prevPosZ, this.posZ) - view.getZ());
        Quaternion quaternion = this.direction.getRotation();
        Vector3f[] points = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0F), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};
        float scale = this.getScale(partialTicks);

        for(int i = 0; i < 4; ++i)
        {
            Vector3f vector3f = points[i];
            vector3f.transform(quaternion);
            vector3f.mul(scale);
            vector3f.add(particleX, particleY, particleZ);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int j = this.getBrightnessForRender(partialTicks);
        buffer.pos((double) points[0].getX(), (double) points[0].getY(), (double) points[0].getZ()).tex(f8, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double) points[1].getX(), (double) points[1].getY(), (double) points[1].getZ()).tex(f8, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double) points[2].getX(), (double) points[2].getY(), (double) points[2].getZ()).tex(f7, f5).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
        buffer.pos((double) points[3].getX(), (double) points[3].getY(), (double) points[3].getZ()).tex(f7, f6).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j).endVertex();
    }

    @Override
    public IParticleRenderType getRenderType()
    {
        return IParticleRenderType.TERRAIN_SHEET;
    }
}
