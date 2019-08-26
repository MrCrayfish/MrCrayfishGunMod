package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Author: MrCrayfish
 */
public class EntityMissile extends EntityProjectile
{
    public EntityMissile(World worldIn)
    {
        super(worldIn);
    }

    public EntityMissile(World worldIn, EntityLivingBase shooter, ItemGun item, Gun modifiedGun)
    {
        super(worldIn, shooter, item, modifiedGun);
    }

    @Override
    protected void onTick()
    {
        for(int i = 5; i > 0; i--)
        {
            world.spawnParticle(EnumParticleTypes.CLOUD, true, this.posX - (this.motionX / i), this.posY - (this.motionY / i), this.posZ - (this.motionZ / i), 0, 0, 0);
        }
        if(world.rand.nextInt(2) == 0)
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY, this.posZ, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, true, this.posX, this.posY, this.posZ, 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        world.createExplosion(shooter, x, y, z, 3F, true);
    }

    @Override
    protected void onHitBlock(IBlockState state, BlockPos pos, double x, double y, double z)
    {
        world.createExplosion(shooter, x, y, z, 3F, true);
        WorldServer worldServer = (WorldServer) world;
        worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, x, y, z, 0, 0.0, 0.0, 0.0, 0);
    }

    @Override
    public void onExpired()
    {
        world.createExplosion(shooter, this.posX, this.posY, this.posZ, 3F, true);
        if(world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) world;
            worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, this.posX, this.posY, this.posZ, 0, 0.0, 0.0, 0.0, 0);
        }
    }
}
