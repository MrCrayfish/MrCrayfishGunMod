package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
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
@Beta
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

    private final ProjectileFactory DEFAULT_FACTORY = EntityProjectile::new;

    private final Map<ResourceLocation, AmmoItem> AMMO = new HashMap<>();
    private final Map<ResourceLocation, ProjectileFactory> FACTORIES = new HashMap<>();

    void register(AmmoItem ammo)
    {
        Objects.requireNonNull(ammo.getRegistryName());
        AMMO.put(ammo.getRegistryName(), ammo);
    }

    public void registerProjectileFactory(AmmoItem ammo, ProjectileFactory factory)
    {
        FACTORIES.put(ammo.getRegistryName(), factory);
    }

    @Nullable
    public AmmoItem getAmmo(ResourceLocation id)
    {
        return AMMO.get(id);
    }

    public ProjectileFactory getFactory(ResourceLocation id)
    {
        return FACTORIES.getOrDefault(id, DEFAULT_FACTORY);
    }
}
