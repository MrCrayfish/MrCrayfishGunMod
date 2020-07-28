package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ProjectileManager
{
    private static ProjectileManager instance = null;

    public static ProjectileManager getInstance()
    {
        if(instance == null)
        {
            instance = new ProjectileManager();
        }
        return instance;
    }

    private final ProjectileFactory DEFAULT_FACTORY = (worldIn, entity, weapon, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, weapon, item, modifiedGun);
    private final Map<ResourceLocation, ProjectileFactory> projectileFactoryMap = new HashMap<>();

    public void registerFactory(Item ammo, ProjectileFactory factory)
    {
        this.projectileFactoryMap.put(ammo.getRegistryName(), factory);
    }

    public ProjectileFactory getFactory(ResourceLocation id)
    {
        return this.projectileFactoryMap.getOrDefault(id, DEFAULT_FACTORY);
    }
}
