package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.event.CommonEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEvents());
	}

	public void init() {}

	public void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {}
}
