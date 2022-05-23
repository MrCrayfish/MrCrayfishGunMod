package com.tac.guns.network.message;

import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageLightChange implements IMessage
{
    private int[] range;

    public MessageLightChange() {}

    public MessageLightChange(int[] range)
    {
        this.range = range;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarIntArray(this.range);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.range = buffer.readVarIntArray();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleFlashLight(player, this.range);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
