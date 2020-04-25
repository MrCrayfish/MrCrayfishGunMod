package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.common.ProjectileFactory;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

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

    private final ProjectileFactory DEFAULT_FACTORY = (worldIn, entity, item, modifiedGun) -> new EntityProjectile(ModEntities.PROJECTILE.get(), worldIn, entity, item, modifiedGun);

    private boolean setup = false;
    private final List<AmmoItem> registeredAmmo = new ArrayList<>();
    private final Map<ResourceLocation, AmmoItem> ammoMap = new HashMap<>();
    private final Map<ResourceLocation, ProjectileFactory> ammoFactories = new HashMap<>();

    void register(AmmoItem ammo)
    {
        this.registeredAmmo.add(ammo);
    }

    public void setup()
    {
        if(!this.setup)
        {
            this.registeredAmmo.forEach(ammoItem ->
            {
                Objects.requireNonNull(ammoItem.getRegistryName());
                this.ammoMap.put(ammoItem.getRegistryName(), ammoItem);
            });
            this.setup = true;
        }
    }

    public void registerProjectileFactory(AmmoItem ammo, ProjectileFactory factory)
    {
        this.ammoFactories.put(ammo.getRegistryName(), factory);
    }

    @Nullable
    public AmmoItem getAmmo(ResourceLocation id)
    {
        return this.ammoMap.get(id);
    }

    public ProjectileFactory getFactory(ResourceLocation id)
    {
        return this.ammoFactories.getOrDefault(id, DEFAULT_FACTORY);
    }
}
