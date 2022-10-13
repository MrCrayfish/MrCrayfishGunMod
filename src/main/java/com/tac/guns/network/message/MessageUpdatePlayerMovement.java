package com.tac.guns.network.message;


import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */


public class MessageUpdatePlayerMovement implements IMessage
{
    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.handle); buffer.writeFloat(this.prevDist);
    }
    @Override
    public void decode(PacketBuffer buffer)
    {
        this.handle = buffer.readBoolean();
        this.prevDist = buffer.readFloat();
    }
    public MessageUpdatePlayerMovement() {}
    private boolean handle;
    private float prevDist = 0.0F;
    public MessageUpdatePlayerMovement(boolean handle, float prevDist)
    {
        this.handle = handle;
        this.prevDist = prevDist;
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> {ServerPlayHandler.handleMovementUpdate(supplier.get().getSender(), this.handle, this.prevDist);});
        //supplier.get().enqueueWork(() -> {ServerPlayHandler.handleMovementUpdateLow(supplier.get().getSender());});
        supplier.get().setPacketHandled(true);
    }
}


