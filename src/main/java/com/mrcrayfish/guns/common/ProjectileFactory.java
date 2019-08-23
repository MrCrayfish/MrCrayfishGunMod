package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public interface ProjectileFactory
{
    EntityProjectile create(World worldIn, EntityLivingBase entity, Gun gun);
}
