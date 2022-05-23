package com.tac.guns.init;

import com.tac.guns.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds 
{
	public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Reference.MOD_ID);

	public static final RegistryObject<SoundEvent> ITEM_GRENADE_PIN = register("item.grenade.pin");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_EXPLOSION = register("entity.stun_grenade.explosion");
	public static final RegistryObject<SoundEvent> ENTITY_STUN_GRENADE_RING = register("entity.stun_grenade.ring");
	public static final RegistryObject<SoundEvent> UI_WEAPON_ATTACH = register("ui.weapon.attach");

	// Register the name of the sound via the Object name for each block in our sounds.json, these are all sounds for TaC, unlike above which remain from CGM
	public static final RegistryObject<SoundEvent> M1911_FIRE = register("item.fire_m1911");
	public static final RegistryObject<SoundEvent> M1911_FIRES = register("item.fire_m1911s");
	public static final RegistryObject<SoundEvent> MOSIN_NAGANT_FIRE = register("item.fire_mosin-nagant");
	public static final RegistryObject<SoundEvent> FIRE_30 = register("item.fire_30-30");
	public static final RegistryObject<SoundEvent> AK74_FIRE = register("item.fire_ak74");
	public static final RegistryObject<SoundEvent> AK74_FIREs = register("item.fire_ak74s");
	public static final RegistryObject<SoundEvent> AK47_FIRE = register("item.fire_ak47");
	public static final RegistryObject<SoundEvent> AK47_FIREs = register("item.fire_ak47s");
	public static final RegistryObject<SoundEvent> DP28_FIRE = register("item.fire_dp28");
	public static final RegistryObject<SoundEvent> GLOCK_FIRE = register("item.fire_glock");
	public static final RegistryObject<SoundEvent> GLOCK_FIREs = register("item.fire_glocks");
	public static final RegistryObject<SoundEvent> M16_FIRE = register("item.fire_m16");
	public static final RegistryObject<SoundEvent> M16_FIREs = register("item.fire_m16s");
	public static final RegistryObject<SoundEvent> M60_FIRE = register("item.fire_m60");
	public static final RegistryObject<SoundEvent> M92FS_FIRE = register("item.fire_m92fs");
	public static final RegistryObject<SoundEvent> M92FS_FIREs = register("item.fire_m92fss");
	public static final RegistryObject<SoundEvent> M1851_FIRE = register("item.fire_m1851");
	public static final RegistryObject<SoundEvent> THOMPSON_FIRE = register("item.fire_thompson");
	public static final RegistryObject<SoundEvent> DB_FIRE = register("item.fire_db");
	public static final RegistryObject<SoundEvent> DMR_FIRE = register("item.dmr_fire");
	public static final RegistryObject<SoundEvent> DMR_FIREs = register("item.dmr_fire_s");
	public static final RegistryObject<SoundEvent> MDS_FIRE = register("item.modern_sniper_fire");
	public static final RegistryObject<SoundEvent> MTAR_FIRE = register("item.mtar_fire");
	public static final RegistryObject<SoundEvent> MTAR_FIREs = register("item.mtar_fire_s");
	public static final RegistryObject<SoundEvent> FAL_FIRE = register("item.fal_fire");
	public static final RegistryObject<SoundEvent> MP5_FIRE = register("item.mp5_fire");
	public static final RegistryObject<SoundEvent> MP5_FIREs = register("item.mp5_fires");
	public static final RegistryObject<SoundEvent> REM700_FIRE = register("item.remington_700");
	public static final RegistryObject<SoundEvent> REM700_FIREs = register("item.remington_700s");
	public static final RegistryObject<SoundEvent> M870_FIRE = register("item.m870");
	public static final RegistryObject<SoundEvent> MP9_FIREs = register("item.mp9s");
	public static final RegistryObject<SoundEvent> P226_FIREs = register("item.p226s");
	public static final RegistryObject<SoundEvent> SAIGA_FIRE = register("item.saiga");
	public static final RegistryObject<SoundEvent> SIG552_FIRE = register("item.sig552");
	public static final RegistryObject<SoundEvent> SIG552_FIREs = register("item.sig552s");
	public static final RegistryObject<SoundEvent> P226_FIRE = register("item.p226");

	public static final RegistryObject<SoundEvent> AA12_FIRE = register("item.aa12");
	public static final RegistryObject<SoundEvent> AWP_FIRE = register("item.ai_awp");
	public static final RegistryObject<SoundEvent> HK416_FIRE = register("item.hk416");
	public static final RegistryObject<SoundEvent> M24_FIRE = register("item.m24");
	public static final RegistryObject<SoundEvent> M1928_FIRE = register("item.m1928");
	public static final RegistryObject<SoundEvent> MK14_FIRE = register("item.mk14");
	public static final RegistryObject<SoundEvent> RPG7_FIRE = register("item.rpg7");
	public static final RegistryObject<SoundEvent> STEN_FIRE = register("item.sten");
	public static final RegistryObject<SoundEvent> VECTOR_FIRE = register("item.vector");



	//public static final RegistryObject<SoundEvent> COCK = register("item.pistol.cock");
	public static final RegistryObject<SoundEvent> HEADSHOT_EXTENDED_PLAYFUL = register("item.headshot");

	private static RegistryObject<SoundEvent> register(String key)
	{
		return REGISTER.register(key, () -> new SoundEvent(new ResourceLocation(Reference.MOD_ID, key)));
	}
}
