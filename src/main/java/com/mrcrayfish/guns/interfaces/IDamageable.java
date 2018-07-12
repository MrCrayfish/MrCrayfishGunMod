package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public interface IDamageable
{
    void onProjectileDamaged(World world, IBlockState state, BlockPos pos, EntityProjectile projectile);

}
