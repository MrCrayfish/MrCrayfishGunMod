package com.tac.guns.network.message;

import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpdateGunID implements IMessage
{
    private boolean regenerate;
    public MessageUpdateGunID(boolean regenerate) {this.regenerate = regenerate;}
    public MessageUpdateGunID(){this.regenerate=false;}

    @Override
    public void encode(PacketBuffer buffer)
    {}

    @Override
    public void decode(PacketBuffer buffer) {}

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null && !player.isSpectator())
            {
                try
                {
                    Validate.notNull(NetworkGunManager.get());
                    ServerPlayHandler.handleGunID(player, regenerate);
                }
                catch (Exception e)
                {

                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
