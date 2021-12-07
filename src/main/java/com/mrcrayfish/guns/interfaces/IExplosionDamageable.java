package com.mrcrayfish.guns.interfaces;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * An interface for notifying a block it has been destroyed by a projectile explosion.
 *
 * Author: Ocelot
 */
public interface IExplosionDamageable
{
    void onProjectileExploded(Level world, BlockState state, BlockPos pos, Entity entity);
}
