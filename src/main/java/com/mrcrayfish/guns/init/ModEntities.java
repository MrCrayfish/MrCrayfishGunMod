package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.BiFunction;

/**
 * Author: MrCrayfish
 */
public class ModEntities
{
    public static final DeferredRegister<EntityType<?>> REGISTER = new DeferredRegister<>(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<EntityProjectile>> PROJECTILE = register("projectile", EntityProjectile::new);
    public static final RegistryObject<EntityType<EntityProjectile>> GRENADE = register("projectile", EntityProjectile::new);
    public static final RegistryObject<EntityType<EntityProjectile>> MISSILE = register("projectile", EntityProjectile::new);
    public static final RegistryObject<EntityType<EntityProjectile>> THROWABLE_GRENADE = register("projectile", EntityProjectile::new);
    public static final RegistryObject<EntityType<EntityProjectile>> THROWABLE_STUN_GRENADE = register("projectile", EntityProjectile::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, BiFunction<EntityType<T>, World, T> function)
    {
        EntityType<T> type = EntityType.Builder.create(function::apply, EntityClassification.MISC).setTrackingRange(100).setUpdateInterval(1).disableSummoning().immuneToFire().setShouldReceiveVelocityUpdates(true).build(id);
        return REGISTER.register(id, () -> type);
    }
}
