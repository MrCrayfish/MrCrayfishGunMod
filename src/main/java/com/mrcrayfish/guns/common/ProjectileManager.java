package com.mrcrayfish.guns.common;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.init.ModEntities;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Author: MrCrayfish
 */
@Beta
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

    private final ProjectileFactory DEFAULT_FACTORY = (worldIn, entity, item, modifiedGun) -> new ProjectileEntity(ModEntities.PROJECTILE.get(), worldIn, entity, item, modifiedGun);
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
