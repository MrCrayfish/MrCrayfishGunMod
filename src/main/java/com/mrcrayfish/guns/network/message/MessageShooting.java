package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShooting implements IMessage
{
    private boolean shooting;

    public MessageShooting() {}

    public MessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(this.shooting);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.shooting = buffer.readBoolean();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ModSyncedDataKeys.SHOOTING.setValue(player, this.shooting);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
