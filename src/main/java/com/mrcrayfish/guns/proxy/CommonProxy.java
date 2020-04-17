package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.common.NetworkGunManager;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;

public class CommonProxy
{
	private NetworkGunManager manager;

	public void init() {}

	public void postInit() {}

	public boolean canShoot()
	{
		return false;
	}

	public boolean isZooming()
	{
		return false;
	}

	public void startReloadAnimation() {}

	public void setGunPropertiesManager(@Nullable NetworkGunManager manager)
	{
		this.manager = manager;
	}

	@Nullable
	public NetworkGunManager getGunPropertiesManager()
	{
		return this.manager;
	}
}
