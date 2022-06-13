package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * An interface for notifying a block it has been hit by a projectile.
 *
 * Author: MrCrayfish
 */
public interface IDamageable
{
    @Deprecated
    default void onBlockDamaged(Level world, BlockState state, BlockPos pos, int damage)
    {
    }

    default void onBlockDamaged(Level world, BlockState state, BlockPos pos, ProjectileEntity projectile, float rawDamage, int damage)
    {
        this.onBlockDamaged(world, state, pos, damage);
    }
}
