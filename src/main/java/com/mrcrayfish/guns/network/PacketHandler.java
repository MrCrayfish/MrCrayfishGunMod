package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.network.message.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
	private static int messageId = 0;

    private static enum Side
    {
        CLIENT, SERVER, BOTH;
    }

	public static void init()
	{
		registerMessage(MessageAim.class, Side.SERVER);
		registerMessage(MessageReload.class, Side.SERVER);
		registerMessage(MessageShoot.class, Side.SERVER);
		registerMessage(MessageMuzzleFlash.class, Side.CLIENT);
		registerMessage(MessageSound.class, Side.CLIENT);
		registerMessage(MessageSyncProperties.class, Side.CLIENT);
		registerMessage(MessageUnload.class, Side.SERVER);
		registerMessage(MessageExplosionStunGrenade.class, Side.CLIENT);
		registerMessage(MessageCraft.class, Side.SERVER);
		registerMessage(MessageReloadAnimation.class, Side.CLIENT);
	}

	private static void registerMessage(Class packet, Side side)
    {
        if (side != Side.CLIENT)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.SERVER);

        if (side != Side.SERVER)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.CLIENT);
    }

    private static void registerMessage(Class packet, net.minecraftforge.fml.relauncher.Side side)
    {
        INSTANCE.registerMessage(packet, packet, messageId++, side);
    }
}
