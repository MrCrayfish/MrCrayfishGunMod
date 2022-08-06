package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
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
public class C2SMessageCraft extends PlayMessage<C2SMessageCraft>
{
    private ResourceLocation id;
    private BlockPos pos;

    public C2SMessageCraft() {}

    public C2SMessageCraft(ResourceLocation id, BlockPos pos)
    {
        this.id = id;
        this.pos = pos;
    }

    @Override
    public void encode(C2SMessageCraft message, FriendlyByteBuf buffer)
    {
        buffer.writeResourceLocation(message.id);
        buffer.writeBlockPos(message.pos);
    }

    @Override
    public C2SMessageCraft decode(FriendlyByteBuf buffer)
    {
        return new C2SMessageCraft(buffer.readResourceLocation(), buffer.readBlockPos());
    }

    @Override
    public void handle(C2SMessageCraft message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleCraft(player, message.id, message.pos);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
