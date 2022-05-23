package com.tac.guns.interfaces;

import com.tac.guns.common.Gun;
import com.tac.guns.common.ProjectileManager;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.item.GunItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This class allows weapons to fire custom projectiles instead of the default implementation. The
 * grenade launcher uses this to spawn a grenade entity with custom physics. Use {@link ProjectileManager}
 * to register a factory.
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IProjectileFactory
{
    /**
     * Creates a new projectile entity.
     *
     * @param worldIn     the world the projectile is going to be spawned into
     * @param entity      the entity who fired the weapon
     * @param weapon      the item stack of the weapon
     * @param item        the gun item
     * @param modifiedGun the properties of the gun
     * @return a projectile entity
     */
    ProjectileEntity create(World worldIn, LivingEntity entity, ItemStack weapon, GunItem item, Gun modifiedGun);
}
