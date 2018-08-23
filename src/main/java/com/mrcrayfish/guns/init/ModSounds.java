package com.mrcrayfish.guns.init;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ModSounds 
{
	private static final Map<String, SoundEvent> SOUNDS = new HashMap<String, SoundEvent>();

	static
	{
		for(ItemGun gunItem : ModGuns.GUNS)
		{
			Gun gun = gunItem.getGun();
			if(!SOUNDS.containsKey(gun.sounds.fire))
			{
				ResourceLocation sound = new ResourceLocation(Reference.MOD_ID, gun.sounds.fire);
				SoundEvent event = new SoundEvent(sound).setRegistryName(gun.sounds.fire);
				SOUNDS.put(gun.sounds.fire, event);
			}
			if(!SOUNDS.containsKey(gun.sounds.reload))
			{
				ResourceLocation sound = new ResourceLocation(Reference.MOD_ID, gun.sounds.reload);
				SoundEvent event = new SoundEvent(sound).setRegistryName(gun.sounds.reload);
				SOUNDS.put(gun.sounds.reload, event);
			}
			if(!SOUNDS.containsKey(gun.sounds.silencedFire))
			{
				ResourceLocation sound = new ResourceLocation(Reference.MOD_ID, gun.sounds.silencedFire);
				SoundEvent event = new SoundEvent(sound).setRegistryName(gun.sounds.silencedFire);
				SOUNDS.put(gun.sounds.silencedFire, event);
			}
		}
	}

	public static void register()
	{
		SOUNDS.values().forEach(RegistrationHandler.Sounds::add);
	}
	
	@Nullable
	public static SoundEvent getSound(String name)
	{
		return SOUNDS.get(name);
	}
}
