package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.ConfigMod;
import com.mrcrayfish.guns.event.CommonEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		ConfigMod.SERVER.aggroMobs.setExemptionClasses();
	}

	public void init() {}

	public void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	public void showMuzzleFlash() {}

	public void playClientSound(SoundEvent sound) {}
}
