package com.mrcrayfish.guns.init;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds 
{
	private static final Map<String, SoundEvent> SOUNDS = new HashMap<String, SoundEvent>();

	public static void register()
	{
		for(ItemGun gunItem : ModGuns.GUNS)
		{
			Gun gun = gunItem.getGun();
			if(!SOUNDS.containsKey(gun.sounds.fire))
			{
				registerSound("cgm:" + gun.sounds.fire);
			}
		}
	}
	
	@Nullable
	public static SoundEvent getSound(String name)
	{
		return SOUNDS.get(name);
	}
	
	private static void registerSound(String soundNameIn)
    {
		ResourceLocation sound = new ResourceLocation(soundNameIn);
		SoundEvent event = GameRegistry.register(new SoundEvent(sound).setRegistryName(soundNameIn));
		SOUNDS.put(sound.getResourcePath(), event);
    }
}
