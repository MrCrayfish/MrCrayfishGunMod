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
public class MessageUpdateMoveInacc implements IMessage
{
    private float dist = 0;

    public MessageUpdateMoveInacc() {}

    public MessageUpdateMoveInacc(float dist)
    {
        this.dist = dist;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeFloat(this.dist);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.dist = buffer.readFloat();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.MOVING, this.dist);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
