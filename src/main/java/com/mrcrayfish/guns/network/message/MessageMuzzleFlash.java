package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageMuzzleFlash implements IMessage
{
    private int entityId;

    public MessageMuzzleFlash() {}

    public MessageMuzzleFlash(int entityId)
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
        supplier.get().enqueueWork(() -> ClientHandler.handleMuzzleFlash(this));
        supplier.get().setPacketHandled(true);
    }

    public int getEntityId()
    {
        return this.entityId;
    }
}
