package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.MessageAim;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

	public static void init()
	{
		INSTANCE.registerMessage(MessageAim.class, MessageAim.class, 0, Side.SERVER);
	}
}
