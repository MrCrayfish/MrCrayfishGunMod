package com.tac.guns.network.message;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.CustomGun;
import com.tac.guns.common.CustomGunLoader;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.Validate;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpdateGunID implements IMessage
{
    public MessageUpdateGunID() {}

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
                    ServerPlayHandler.handleGunID(player);
                }
                catch (Exception e)
                {

                }
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
