package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Gun;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Casey on 24-Jul-17.
 */
public interface IProjectile
{
    void onHitEntity(Gun.Projectile projectile, Entity entity);

    void onHitBlock(Gun.Projectile projectile, IBlockState state, BlockPos pos);
}
