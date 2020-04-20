package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.common.NetworkGunManager;

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
	public NetworkGunManager getNetworkGunManager()
	{
		return this.manager;
	}
}
