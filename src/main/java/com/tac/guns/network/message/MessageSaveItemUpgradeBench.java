package com.tac.guns.network.message;

import com.tac.guns.client.network.ClientPlayHandler;
import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageSaveItemUpgradeBench implements IMessage
{
    private BlockPos pos;

    public MessageSaveItemUpgradeBench() {
    }
    public MessageSaveItemUpgradeBench(BlockPos pos)
    {
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public void decode(PacketBuffer buffer) {
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {

        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                supplier.get().enqueueWork(() -> ServerPlayHandler. handleUpgradeBenchItem(this, player));
            }
        });
        supplier.get().setPacketHandled(true);
    }
    public BlockPos getPos() {
        return this.pos;
    }
}
