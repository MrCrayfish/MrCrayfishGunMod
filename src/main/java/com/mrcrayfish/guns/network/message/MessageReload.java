package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.event.GunReloadEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

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
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(this.reload);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.reload = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                ModSyncedDataKeys.RELOADING.setValue(player, this.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!this.reload)
                    return;

                ItemStack gun = player.getMainHandItem();
                if(MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Pre(player, gun)))
                {
                    ModSyncedDataKeys.RELOADING.setValue(player, false);
                    return;
                }
                MinecraftForge.EVENT_BUS.post(new GunReloadEvent.Post(player, gun));
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
