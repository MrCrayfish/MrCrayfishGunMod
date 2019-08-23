package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.object.Gun;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class EntityGrenade extends EntityProjectile
{
    public EntityGrenade(World worldIn)
    {
        super(worldIn);
    }

    public EntityGrenade(World worldIn, EntityLivingBase shooter, Gun.Projectile projectile)
    {
        super(worldIn, shooter, projectile);
    }

    @Override
    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        world.createExplosion(shooter, x, y, z, 1.5F, true);
    }

    @Override
    protected void onHitBlock(IBlockState state, BlockPos pos, double x, double y, double z)
    {
        world.createExplosion(shooter, x, y, z, 1.5F, true);
    }
}
