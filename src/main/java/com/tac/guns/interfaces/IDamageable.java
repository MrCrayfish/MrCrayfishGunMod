package com.tac.guns.interfaces;

import com.tac.guns.entity.ProjectileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An interface for notifying a block it has been hit by a projectile.
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IDamageable
{
    @Deprecated
    default void onBlockDamaged(World world, BlockState state, BlockPos pos, int damage)
    {
    }

    default void onBlockDamaged(World world, BlockState state, BlockPos pos, ProjectileEntity projectile, float rawDamage, int damage)
    {
        this.onBlockDamaged(world, state, pos, damage);
    }
}
