package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public interface IDamageable
{
    @Deprecated
    void onBlockDamaged(World world, BlockState state, BlockPos pos, int damage);

    default void onBlockDamaged(World world, BlockState state, BlockPos pos, ProjectileEntity projectile, float rawDamage, int damage)
    {
        this.onBlockDamaged(world, state, pos, damage);
    }
}
