package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageReload implements IMessage
{
    private boolean reload;

    public MessageReload() {}

    public MessageReload(boolean reload)
    {
        this.reload = reload;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.reload);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.reload = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, this.reload);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
