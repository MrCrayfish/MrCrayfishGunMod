package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<SoundEvent> ITEM_PISTOL_FIRE = register("item.pistol.fire");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_SILENCED_FIRE = register("item.pistol.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_PISTOL_RELOAD = register("item.pistol.reload");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_FIRE = register("item.shotgun.fire");
	public static final RegistryObject<SoundEvent> ITEM_SHOTGUN_SILENCED_FIRE = register("item.shotgun.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_FIRE = register("item.rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_RIFLE_SILENCED_FIRE = register("item.rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_FIRE = register("item.assault_rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_ASSAULT_RIFLE_SILENCED_FIRE = register("item.assault_rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_LAUNCHER_FIRE = register("item.grenade_launcher.fire");
	public static final RegistryObject<SoundEvent> ITEM_BAZOOKA_FIRE = register("item.bazooka.fire");
	public static final RegistryObject<SoundEvent> ITEM_MINI_GUN_FIRE = register("item.mini_gun.fire");
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_FIRE = register("item.machine_pistol.fire");
	public static final RegistryObject<SoundEvent> ITEM_MACHINE_PISTOL_SILENCED_FIRE = register("item.machine_pistol.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_FIRE = register("item.heavy_rifle.fire");
	public static final RegistryObject<SoundEvent> ITEM_HEAVY_RIFLE_SILENCED_FIRE = register("item.heavy_rifle.silenced_fire");
	public static final RegistryObject<SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_EXPLOSION = register("entity.stun_grenade.explosion");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_RING = register("entity.stun_grenade.ring");
	public static final RegistryObject<SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");

	private static RegistryObject<SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, key)));
	}
}
