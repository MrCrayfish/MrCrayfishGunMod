package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageRemoveProjectile extends PlayMessage<MessageRemoveProjectile>
{
    private int entityId;

    public MessageRemoveProjectile() {}

    public MessageRemoveProjectile(int entityId)
    {
        this.entityId = entityId;
    }

    @Override
    public void encode(MessageRemoveProjectile message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.entityId);
    }

    @Override
    public MessageRemoveProjectile decode(FriendlyByteBuf buffer)
    {
        return new MessageRemoveProjectile(buffer.readInt());
    }

    @Override
    public void handle(MessageRemoveProjectile message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleRemoveProjectile(message));
        supplier.get().setPacketHandled(true);
    }

    public int getEntityId()
    {
        return this.entityId;
    }
}
