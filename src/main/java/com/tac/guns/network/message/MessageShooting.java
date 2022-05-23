package com.tac.guns.network.message;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.init.ModSyncedDataKeys;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageShooting implements IMessage
{
    private boolean shooting;

    public MessageShooting() {}

    public MessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.shooting);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.shooting = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.SHOOTING, this.shooting);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
