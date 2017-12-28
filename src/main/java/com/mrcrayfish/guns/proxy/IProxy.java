package com.mrcrayfish.guns.proxy;

import net.minecraft.util.EnumParticleTypes;

public interface IProxy
{
	void preInit();
	
	void init();

	void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed);
}
