package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class C2SMessageShooting extends PlayMessage<C2SMessageShooting>
{
    private boolean shooting;

    public C2SMessageShooting() {}

    public C2SMessageShooting(boolean shooting)
    {
        this.shooting = shooting;
    }

    @Override
    public void encode(C2SMessageShooting message, FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(message.shooting);
    }

    @Override
    public C2SMessageShooting decode(FriendlyByteBuf buffer)
    {
        return new C2SMessageShooting(buffer.readBoolean());
    }

    @Override
    public void handle(C2SMessageShooting message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ModSyncedDataKeys.SHOOTING.setValue(player, message.shooting);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
