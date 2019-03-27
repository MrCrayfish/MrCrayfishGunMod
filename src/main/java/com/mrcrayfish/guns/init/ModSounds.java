package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModSounds 
{
	private static final Map<String, SoundEvent> SOUNDS = new HashMap<String, SoundEvent>();

	static
	{
		for(ItemGun gunItem : ModGuns.GUNS.values())
		{
			Gun gun = gunItem.getGun();
			register(gun.sounds.fire);
			register(gun.sounds.reload);
			register(gun.sounds.silencedFire);
		}
		register("grenade_stun_explosion");
		register("grenade_stun_ring");
	}

    private static void register(String name)
    {
        if(!SOUNDS.containsKey(name))
        {
        	ResourceLocation sound = new ResourceLocation(Reference.MOD_ID, name);
        	SoundEvent event = new SoundEvent(sound).setRegistryName(name);
        	SOUNDS.put(name, event);
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
