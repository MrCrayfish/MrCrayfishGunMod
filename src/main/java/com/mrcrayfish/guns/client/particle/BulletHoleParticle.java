package com.mrcrayfish.guns.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.Config;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Author: MrCrayfish
 */
public class BulletHoleParticle extends TextureSheetParticle
{
    private final Direction direction;
    private final BlockPos pos;
    private int uOffset;
    private int vOffset;
    private float textureDensity;

    public BulletHoleParticle(ClientLevel world, double x, double y, double z, Direction direction, BlockPos pos)
    {
        super(world, x, y, z);
        this.setSprite(this.getSprite(pos));
        this.direction = direction;
        this.pos = pos;
        this.lifetime = (int) (Config.CLIENT.particle.bulletHoleLifeMin.get() + world.random.nextFloat() * (Config.CLIENT.particle.bulletHoleLifeMax.get() - Config.CLIENT.particle.bulletHoleLifeMin.get()));
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.quadSize = 0.05F;

        /* Expire the particle straight away if the block is air */
        BlockState state = world.getBlockState(pos);
        if (world.getBlockState(pos).isAir())
            this.remove();

        int color = this.getBlockColor(state, world, pos, direction);
        this.rCol = ((float) (color >> 16 & 255) / 255.0F) / 3.0F;
        this.gCol = ((float) (color >> 8 & 255) / 255.0F) / 3.0F;
        this.bCol = ((float) (color & 255) / 255.0F) / 3.0F;
        this.alpha = 0.9F;
    }

    private int getBlockColor(BlockState state, Level world, BlockPos pos, Direction direction)
    {
        //Add an exception for grass blocks
        if (state.getBlock() == Blocks.GRASS_BLOCK)
            return Integer.MAX_VALUE;
        return Minecraft.getInstance().getBlockColors().getColor(state, world, pos, 0);
    }

    @Override
    protected void setSprite(TextureAtlasSprite sprite)
    {
        super.setSprite(sprite);
        this.uOffset = this.random.nextInt(16);
        this.vOffset = this.random.nextInt(16);
        this.textureDensity = (sprite.getU1() - sprite.getU0()) / 16.0F; //Assuming texture is a square
    }

    private TextureAtlasSprite getSprite(BlockPos pos)
    {
        Minecraft minecraft = Minecraft.getInstance();
        Level world = minecraft.level;
        if (world != null)
        {
            BlockState state = world.getBlockState(pos);
            return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state);
        }
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(MissingTextureAtlasSprite.getLocation());
    }

    @Override
    protected float getU0()
    {
        return this.sprite.getU0() + this.uOffset * this.textureDensity;
    }

    @Override
    protected float getV0()
    {
        return this.sprite.getV0() + this.vOffset * this.textureDensity;
    }

    @Override
    protected float getU1()
    {
        return this.getU0() + this.textureDensity;
    }

    @Override
    protected float getV1()
    {
        return this.getV0() + this.textureDensity;
    }

    @Override
    public void tick()
    {
        super.tick();
        if (this.level.getBlockState(this.pos).isAir())
        {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks)
    {
        Vec3 view = renderInfo.getPosition();
        float particleX = (float) (Mth.lerp((double) partialTicks, this.xo, this.x) - view.x());
        float particleY = (float) (Mth.lerp((double) partialTicks, this.yo, this.y) - view.y());
        float particleZ = (float) (Mth.lerp((double) partialTicks, this.zo, this.z) - view.z());
        Quaternion quaternion = this.direction.getRotation();
        Vector3f[] points = new Vector3f[]{new Vector3f(-1.0F, 0.0F, -1.0F), new Vector3f(-1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, 1.0F), new Vector3f(1.0F, 0.0F, -1.0F)};
        float scale = this.getQuadSize(partialTicks);

        for (int i = 0; i < 4; ++i)
        {
            Vector3f vector3f = points[i];
            vector3f.transform(quaternion);
            vector3f.mul(scale);
            vector3f.add(particleX, particleY, particleZ);
        }

        float f7 = this.getU0();
        float f8 = this.getU1();
        float f5 = this.getV0();
        float f6 = this.getV1();
        int j = this.getLightColor(partialTicks);
        float fade = Config.CLIENT.particle.bulletHoleFadeThreshold.get() >= 1.0f ? 1.0f : 1.0f - (Math.max((float) this.age - (float) this.lifetime * Config.CLIENT.particle.bulletHoleFadeThreshold.get().floatValue(), 0) / ((float) this.lifetime - (float) this.lifetime * Config.CLIENT.particle.bulletHoleFadeThreshold.get().floatValue()));
        buffer.vertex(points[0].x(), points[0].y(), points[0].z()).uv(f8, f6).color(this.rCol, this.gCol, this.bCol, this.alpha * fade).uv2(j).endVertex();
        buffer.vertex(points[1].x(), points[1].y(), points[1].z()).uv(f8, f5).color(this.rCol, this.gCol, this.bCol, this.alpha * fade).uv2(j).endVertex();
        buffer.vertex(points[2].x(), points[2].y(), points[2].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha * fade).uv2(j).endVertex();
        buffer.vertex(points[3].x(), points[3].y(), points[3].z()).uv(f7, f6).color(this.rCol, this.gCol, this.bCol, this.alpha * fade).uv2(j).endVertex();
    }

    @Override
    public ParticleRenderType getRenderType()
    {
        return ParticleRenderType.TERRAIN_SHEET;
    }
}
