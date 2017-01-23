package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.init.ModGuns;

public class ClientProxy implements IProxy
{

	@Override
	public void init()
	{
		ModGuns.registerRenders();
	}

}
