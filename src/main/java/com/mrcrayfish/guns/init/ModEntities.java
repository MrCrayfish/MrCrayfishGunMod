package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.entity.*;
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

    public static final RegistryObject<EntityType<ProjectileEntity>> PROJECTILE = register("projectile", ProjectileEntity::new);
    public static final RegistryObject<EntityType<GrenadeEntity>> GRENADE = register("grenade", GrenadeEntity::new);
    public static final RegistryObject<EntityType<MissileEntity>> MISSILE = register("missile", MissileEntity::new);
    public static final RegistryObject<EntityType<ThrowableGrenadeEntity>> THROWABLE_GRENADE = register("throwable_grenade", ThrowableGrenadeEntity::new);
    public static final RegistryObject<EntityType<ThrowableStunGrenadeEntity>> THROWABLE_STUN_GRENADE = register("throwable_stun_grenade", ThrowableStunGrenadeEntity::new);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String id, BiFunction<EntityType<T>, World, T> function)
    {
        EntityType<T> type = EntityType.Builder.create(function::apply, EntityClassification.MISC).size(0.25F, 0.25F).setTrackingRange(100).setUpdateInterval(1).disableSummoning().immuneToFire().setShouldReceiveVelocityUpdates(true).build(id);
        return REGISTER.register(id, () -> type);
    }
}
