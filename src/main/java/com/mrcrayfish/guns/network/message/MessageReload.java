package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.event.GunReloadEvent;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageReload extends PlayMessage<MessageReload>
{
    private boolean reload;

    public MessageReload() {}

    public MessageReload(boolean reload)
    {
        this.reload = reload;
    }

    @Override
    public void encode(MessageReload message, FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.reload);
    }

    @Override
    public MessageReload decode(FriendlyByteBuf buffer)
    {
        return new MessageReload(buffer.readBoolean());
    }

    @Override
    public void handle(MessageReload message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                ModSyncedDataKeys.RELOADING.setValue(player, message.reload); // This has to be set in order to verify the packet is sent if the event is cancelled
                if(!message.reload)
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
