package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public interface ProjectileFactory
{
    ProjectileEntity create(World worldIn, LivingEntity entity, ItemStack weapon, GunItem item, Gun modifiedGun);
}
