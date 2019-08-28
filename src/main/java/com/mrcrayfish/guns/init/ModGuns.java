package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModGuns
{
    @GameRegistry.ObjectHolder("cgm:handgun")
    public static final Item PISTOL = null;

    @GameRegistry.ObjectHolder("cgm:shotgun")
    public static final Item SHOTGUN = null;

    @GameRegistry.ObjectHolder("cgm:rifle")
    public static final Item RIFLE = null;

    @GameRegistry.ObjectHolder("cgm:grenade_launcher")
    public static final ItemGun GRENADE_LAUNCHER = null;

    @GameRegistry.ObjectHolder("cgm:bazooka")
    public static final Item BAZOOKA = null;

    @GameRegistry.ObjectHolder("cgm:chain_gun")
    public static final Item CHAIN_GUN = null;

    @GameRegistry.ObjectHolder("cgm:assault_rifle")
    public static final Item ASSAULT_RIFLE = null;

    @GameRegistry.ObjectHolder("cgm:machine_pistol")
    public static final Item MACHINE_PISTOL = null;

    @GameRegistry.ObjectHolder("cgm:part")
    public static final Item PARTS = null;

    @GameRegistry.ObjectHolder("cgm:basic_ammo")
    public static final Item BASIC_AMMO = null;

    @GameRegistry.ObjectHolder("cgm:advanced_ammo")
    public static final Item ADVANCED_AMMO = null;

    @GameRegistry.ObjectHolder("cgm:shell")
    public static final Item SHELL = null;

    @GameRegistry.ObjectHolder("cgm:missile")
    public static final ItemAmmo MISSILE = null;

    @GameRegistry.ObjectHolder("cgm:grenade")
    public static final ItemAmmo GRENADE = null;

    @GameRegistry.ObjectHolder("cgm:stun_grenade")
    public static final Item STUN_GRENADE = null;

    @GameRegistry.ObjectHolder("cgm:scope")
    public static final Item SCOPES = null;

    @GameRegistry.ObjectHolder("cgm:silencer")
    public static final Item SILENCER = null;

    public static void register()
    {
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "handgun")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "shotgun")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "rifle")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "grenade_launcher")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "bazooka")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "chain_gun")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "assault_rifle")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGun(new ResourceLocation(Reference.MOD_ID, "machine_pistol")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));

        register(new ItemAmmo(new ResourceLocation(Reference.MOD_ID, "basic_ammo")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemAmmo(new ResourceLocation(Reference.MOD_ID, "advanced_ammo")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemAmmo(new ResourceLocation(Reference.MOD_ID, "shell")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemAmmo(new ResourceLocation(Reference.MOD_ID, "missile")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemGrenade(new ResourceLocation(Reference.MOD_ID, "grenade")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));
        register(new ItemStunGrenade(new ResourceLocation(Reference.MOD_ID, "stun_grenade")).setCreativeTab(MrCrayfishGunMod.GUN_TAB));

        register(new ItemPart());
        register(new ItemScope());
        register(new ItemAttachment(new ResourceLocation(Reference.MOD_ID, "silencer"), IAttachment.Type.BARREL));
    }

    private static void register(Item item)
    {
        RegistrationHandler.Items.add(item);
    }
}
