package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.common.ProjectileFactory;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: MrCrayfish
 */
public class AmmoRegistry
{
    private static AmmoRegistry instance = null;

    public static AmmoRegistry getInstance()
    {
        if(instance == null)
        {
            instance = new AmmoRegistry();
        }
        return instance;
    }

    private final ProjectileFactory DEFAULT_FACTORY = (worldIn, entity, gun) -> new EntityProjectile(worldIn, entity, gun.projectile);

    private final Map<ResourceLocation, ItemAmmo> AMMO = new HashMap<>();
    private final Map<ResourceLocation, ProjectileFactory> FACTORIES = new HashMap<>();

    void register(ItemAmmo ammo)
    {
        Objects.requireNonNull(ammo.getRegistryName());
        AMMO.put(ammo.getRegistryName(), ammo);
    }

    public void registerProjectileFactory(ItemAmmo ammo, ProjectileFactory factory)
    {
        FACTORIES.put(ammo.getRegistryName(), factory);
    }

    @Nullable
    public ItemAmmo getAmmo(ResourceLocation id)
    {
        return AMMO.get(id);
    }

    public ProjectileFactory getFactory(ResourceLocation id)
    {
        return FACTORIES.getOrDefault(id, DEFAULT_FACTORY);
    }
}
