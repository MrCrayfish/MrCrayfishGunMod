package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.event.GunReloadEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageReload implements IMessage
{
    private boolean reload;

    public MessageReload()
    {
    }

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
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, this.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!this.reload)
                    return;

                ItemStack gun = player.getMainHandItem();
                if(MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)))
                {
                    SyncedPlayerData.instance().set(player, ModSyncedDataKeys.RELOADING, false);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
