package com.tac.guns.network;

import com.tac.guns.GunMod;
import com.tac.guns.client.CustomGunManager;
import com.tac.guns.client.CustomRigManager;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.NetworkRigManager;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class HandshakeHandler
{
    private static final Marker TAC_HANDSHAKE = MarkerManager.getMarker("TAC_HANDSHAKE");

    static void handleAcknowledge(HandshakeMessages.C2SAcknowledge message, Supplier<NetworkEvent.Context> c)
    {
        GunMod.LOGGER.debug(TAC_HANDSHAKE, "Received acknowledgement from client");
        c.get().setPacketHandled(true);
    }

    static void handleUpdateGuns(HandshakeMessages.S2CUpdateGuns message, Supplier<NetworkEvent.Context> c)
    {
        GunMod.LOGGER.debug(TAC_HANDSHAKE, "Received gun data from server");

        AtomicBoolean updatedRegisteredGuns = new AtomicBoolean(false);
        CountDownLatch block = new CountDownLatch(1);
        c.get().enqueueWork(() ->
        {
            updatedRegisteredGuns.set(true);
            if(!NetworkGunManager.updateRegisteredGuns(message))
            {
                updatedRegisteredGuns.set(false);
            }
            if(!CustomGunManager.updateCustomGuns(message))
            {
                updatedRegisteredGuns.set(false);
            }
            block.countDown();
        });

        try
        {
            block.await();
        }
        catch(InterruptedException e)
        {
            Thread.interrupted();
        }

        c.get().setPacketHandled(true);

        if(updatedRegisteredGuns.get())
        {
            GunMod.LOGGER.info("Successfully synchronized gun properties from server");
            PacketHandler.getHandshakeChannel().reply(new HandshakeMessages.C2SAcknowledge(), c.get());
        }
        else
        {
            GunMod.LOGGER.error("Failed to synchronize gun properties from server");
            c.get().getNetworkManager().closeChannel(new StringTextComponent("Connection closed - [Timeless and Classics Mod] Failed to synchronize gun properties from server"));
        }
    }

    static void handleUpdateRigs(HandshakeMessages.S2CUpdateRigs message, Supplier<NetworkEvent.Context> c)
    {
        GunMod.LOGGER.debug(TAC_HANDSHAKE, "Received rig data from server");

        AtomicBoolean updatedRegisteredRigs = new AtomicBoolean(false);
        CountDownLatch block = new CountDownLatch(1);
        c.get().enqueueWork(() ->
        {
            updatedRegisteredRigs.set(true);
            if(!NetworkRigManager.updateRegisteredRigs(message))
            {
                updatedRegisteredRigs.set(false);
            }
            if(!CustomRigManager.updateCustomRigs(message))
            {
                updatedRegisteredRigs.set(false);
            }
            block.countDown();
        });

        try
        {
            block.await();
        }
        catch(InterruptedException e)
        {
            Thread.interrupted();
        }

        c.get().setPacketHandled(true);

        if(updatedRegisteredRigs.get())
        {
            GunMod.LOGGER.info("Successfully synchronized rig properties from server");
            PacketHandler.getHandshakeChannel().reply(new HandshakeMessages.C2SAcknowledge(), c.get());
        }
        else
        {
            GunMod.LOGGER.error("Failed to synchronize rig properties from server");
            c.get().getNetworkManager().closeChannel(new StringTextComponent("Connection closed - [Timeless and Classics Mod] Failed to synchronize rig properties from server"));
        }
    }
}