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
public class MessageAttachments extends PlayMessage<MessageAttachments>
{
    public MessageAttachments() {}

    @Override
    public void encode(MessageAttachments message, FriendlyByteBuf buffer) {}

    @Override
    public MessageAttachments decode(FriendlyByteBuf buffer)
    {
        return new MessageAttachments();
    }

    @Override
    public void handle(MessageAttachments message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleAttachments(player);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
