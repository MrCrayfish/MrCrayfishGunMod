package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.CommonHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage
{
    @Override
    public void encode(PacketBuffer buffer) {}

    @Override
    public void decode(PacketBuffer buffer) {}

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                CommonHandler.fireHeldGun(player);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
