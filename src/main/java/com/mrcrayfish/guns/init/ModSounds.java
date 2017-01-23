package com.mrcrayfish.guns.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds 
{
	private static final List<SoundEvent> SOUNDS = new ArrayList<SoundEvent>();
	
	public static SoundEvent shotgun_fire;
	
	public static void register()
	{
		shotgun_fire = registerSound("cgm:shotgun_fire");
	}
	
	@Nullable
	public static SoundEvent getSound(String name)
	{
		for(SoundEvent sound : SOUNDS)
		{
			if(sound.getSoundName().getResourcePath().equals(name))
			{
				return sound;
			}
		}
		return null;
	}
	
	private static SoundEvent registerSound(String soundNameIn)
    {
		ResourceLocation sound = new ResourceLocation(soundNameIn);
		SoundEvent event = GameRegistry.register(new SoundEvent(sound).setRegistryName(soundNameIn));
		SOUNDS.add(event);
        return event;
    }
}
