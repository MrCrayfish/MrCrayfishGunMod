package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.event.CommonEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
		GunConfig.SERVER.aggroMobs.setExemptionClasses();
	}

	public void init() {}

	public void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}

	public void showMuzzleFlash() {}

	public void playClientSound(SoundEvent sound) {}

	public void playClientSound(double posX, double posY, double posZ, SoundEvent event, SoundCategory category, float volume, float pitch) {}
}
