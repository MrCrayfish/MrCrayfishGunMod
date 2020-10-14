package com.mrcrayfish.guns.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: Ocelot
 */
public interface IExplosionDamageable
{
    void onProjectileExploded(World world, BlockState state, BlockPos pos, Entity entity);
}
