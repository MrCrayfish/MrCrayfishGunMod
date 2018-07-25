package com.mrcrayfish.guns.interfaces;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public interface IDamageable
{
    void onBlockDamaged(World world, IBlockState state, BlockPos pos, int damage);
}
