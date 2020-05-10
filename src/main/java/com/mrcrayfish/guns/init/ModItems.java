package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.object.Barrel;
import com.mrcrayfish.guns.object.Scope;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
    public static final DeferredRegister<Item> REGISTER = new DeferredRegister<>(ForgeRegistries.ITEMS, Reference.MOD_ID);

    public static final RegistryObject<GunItem> PISTOL = REGISTER.register("pistol", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SHOTGUN = REGISTER.register("shotgun", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> RIFLE = REGISTER.register("rifle", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<GunItem> GRENADE_LAUNCHER = REGISTER.register("grenade_launcher", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> BAZOOKA = REGISTER.register("bazooka", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MINI_GUN = REGISTER.register("mini_gun", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> ASSAULT_RIFLE = REGISTER.register("assault_rifle", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MACHINE_PISTOL = REGISTER.register("machine_pistol", () -> new GunItem(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));

    public static final RegistryObject<Item> BASIC_AMMO = REGISTER.register("basic_ammo", () -> new AmmoItem(new Item.Properties().group(GunMod.GROUP)));
    public static final RegistryObject<Item> ADVANCED_AMMO = REGISTER.register("advanced_ammo", () -> new AmmoItem(new Item.Properties().group(GunMod.GROUP)));
    public static final RegistryObject<Item> SHELL = REGISTER.register("shell", () -> new AmmoItem(new Item.Properties().group(GunMod.GROUP)));
    public static final RegistryObject<AmmoItem> MISSILE = REGISTER.register("missile", () -> new AmmoItem(new Item.Properties().group(GunMod.GROUP)));
    public static final RegistryObject<AmmoItem> GRENADE = REGISTER.register("grenade", () -> new GrenadeItem(new Item.Properties().group(GunMod.GROUP)));
    public static final RegistryObject<Item> STUN_GRENADE = REGISTER.register("stun_grenade", () -> new StunGrenadeItem(new Item.Properties().group(GunMod.GROUP)));

    public static final RegistryObject<Item> SHORT_SCOPE = REGISTER.register("short_scope", () -> new ScopeItem(Scope.create(0.1F, 1.55F).viewFinderOffset(0.3), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> MEDIUM_SCOPE = REGISTER.register("medium_scope", () -> new ScopeItem(Scope.create(0.25F, 1.625F).viewFinderOffset(0.3), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> LONG_SCOPE = REGISTER.register("long_scope", () -> new ScopeItem(Scope.create(0.4F, 1.5F).viewFinderOffset(0.25), new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
    public static final RegistryObject<Item> SILENCER = REGISTER.register("silencer", () -> new BarrelItem(Barrel.create(8.0F), new Item.Properties().group(GunMod.GROUP)));
}
