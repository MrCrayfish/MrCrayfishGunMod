package com.mrcrayfish.guns.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public interface IMessage
{
    void encode(FriendlyByteBuf buffer);

    void decode(FriendlyByteBuf buffer);

    void handle(Supplier<NetworkEvent.Context> supplier);
}
