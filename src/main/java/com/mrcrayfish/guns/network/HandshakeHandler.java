package com.mrcrayfish.guns.network;

import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.obfuscate.Obfuscate;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class HandshakeHandler
{
    private static final Marker CGM_HANDSHAKE = MarkerManager.getMarker("CGM_HANDSHAKE");

    static void handleAcknowledge(HandshakeMessages.C2SAcknowledge message, Supplier<NetworkEvent.Context> c)
    {
        Obfuscate.LOGGER.debug(CGM_HANDSHAKE, "Received acknowledgement from client");
        c.get().setPacketHandled(true);
    }

    static void handleUpdateGuns(HandshakeMessages.S2CUpdateGuns message, Supplier<NetworkEvent.Context> c)
    {
        Obfuscate.LOGGER.debug(CGM_HANDSHAKE, "Received gun objects from server");
        c.get().setPacketHandled(true);
        if(!NetworkGunManager.updateRegisteredGuns(message))
        {
            c.get().getNetworkManager().closeChannel(new StringTextComponent("Connection closed - [MrCrayfish's Gun Mod] failed to update gun data from server"));
            return;
        }
        PacketHandler.getHandshakeChannel().reply(new HandshakeMessages.C2SAcknowledge(), c.get());
    }
}