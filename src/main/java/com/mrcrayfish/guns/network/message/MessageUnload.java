package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageUnload extends PlayMessage<MessageUnload>
{
    @Override
    public void encode(MessageUnload message, FriendlyByteBuf buffer) {}

    @Override
    public MessageUnload decode(FriendlyByteBuf buffer)
    {
        return new MessageUnload();
    }

    @Override
    public void handle(MessageUnload message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                ServerPlayHandler.handleUnload(player);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
