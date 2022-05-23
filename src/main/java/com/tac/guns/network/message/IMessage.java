package com.tac.guns.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IMessage
{
    void encode(PacketBuffer buffer);

    void decode(PacketBuffer buffer);

    void handle(Supplier<NetworkEvent.Context> supplier);
}
