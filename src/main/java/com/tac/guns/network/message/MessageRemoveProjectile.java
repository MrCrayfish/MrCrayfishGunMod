package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
    public void encode(PacketBuffer buffer)
    {
        buffer.writeInt(this.entityId);
    }

    @Override
    public void decode(PacketBuffer buffer)
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
