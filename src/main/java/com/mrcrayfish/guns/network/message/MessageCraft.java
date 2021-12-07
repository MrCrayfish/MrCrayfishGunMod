package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageCraft implements IMessage
{
    private ResourceLocation id;
    private BlockPos pos;

    public MessageCraft() {}

    public MessageCraft(ResourceLocation id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(this.id);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.id = buffer.readResourceLocation();
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleCraft(player, this.id, this.pos);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
