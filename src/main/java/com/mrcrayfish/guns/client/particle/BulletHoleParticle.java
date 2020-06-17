package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.GunMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    private int uOffset;
    private int vOffset;
    private float textureDensity;

    public BulletHoleParticle(World world, double x, double y, double z, Direction direction, BlockPos pos)
    {
        super(world, x, y, z);
        this.setSprite(this.getSprite(pos));
        this.direction = direction;
        this.pos = pos;
        this.maxAge = Config.CLIENT.particle.bulletHoleLife.get();
        this.canCollide = false;
        this.particleGravity = 0.0F;
        this.particleScale = 0.05F;

        /* Expire the particle straight away if the block is air */
        BlockState state = world.getBlockState(pos);
        if(world.getBlockState(pos).isAir(world, pos))
        {
            this.setExpired();
        }

        int color = this.getBlockColor(state, world, pos, direction);
        this.particleRed = ((float)(color >> 16 & 255) / 255.0F) / 3.0F;
        this.particleGreen = ((float)(color >> 8 & 255) / 255.0F) / 3.0F;
        this.particleBlue = ((float)(color & 255) / 255.0F) / 3.0F;
        this.particleAlpha = 0.9F;
    }

    private int getBlockColor(BlockState state, World world, BlockPos pos, Direction direction)
    {
        //Add an exception for grass blocks
        if(state.getBlock() == Blocks.GRASS_BLOCK)
        {
            return Integer.MAX_VALUE;
        }
        return Minecraft.getInstance().getBlockColors().getColor(state, world, pos, 0);
    }

    @Override
    protected void setSprite(TextureAtlasSprite sprite)
    {
        super.setSprite(sprite);
        this.uOffset = this.rand.nextInt(16);
        this.vOffset = this.rand.nextInt(16);
        this.textureDensity = (sprite.getMaxU() - sprite.getMinU()) / 16.0F; //Assuming texture is a square
    }

    private TextureAtlasSprite getSprite(BlockPos pos)
    {
        Minecraft minecraft = Minecraft.getInstance();
        World world = minecraft.world;
        if(world != null)
        {
            BlockState state = world.getBlockState(pos);
            return Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        }
        return Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(GunMod.BULLET_HOLE_TEXTURE);
    }

    @Override
    protected float getMinU()
    {
        return this.sprite.getMinU() + this.uOffset * this.textureDensity;
    }

    @Override
    protected float getMinV()
    {
        return this.sprite.getMinV() + this.vOffset * this.textureDensity;
    }

    @Override
    protected float getMaxU()
    {
        return this.getMinU() + this.textureDensity;
    }

    @Override
    protected float getMaxV()
    {
        return this.getMinV() + this.textureDensity;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.world.getBlockState(this.pos).isAir(this.world, this.pos))
        {
            this.setExpired();
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
