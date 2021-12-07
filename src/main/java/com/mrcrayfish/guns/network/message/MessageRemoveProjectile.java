package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageRemoveProjectile implements IMessage
{
    private int entityId;

    public MessageRemoveProjectile() {}

    public MessageRemoveProjectile(int entityId)
    {
        this.entityId = entityId;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.entityId = buffer.readInt();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleRemoveProjectile(this));
        supplier.get().setPacketHandled(true);
    }

    public int getEntityId()
    {
        return this.entityId;
    }
}
