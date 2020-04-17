package com.mrcrayfish.guns.common;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
@Beta
public interface ProjectileFactory
{
    EntityProjectile create(World worldIn, LivingEntity entity, GunItem item, Gun modifiedGun);
}
