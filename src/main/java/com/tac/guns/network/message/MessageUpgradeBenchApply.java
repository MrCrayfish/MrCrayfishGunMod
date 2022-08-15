package com.tac.guns.network.message;

import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.init.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpgradeBenchApply implements IMessage
{
    private ResourceLocation id;
    private BlockPos pos;
    private int ench;

    private int enchIndx;
    public MessageUpgradeBenchApply() {}

    public MessageUpgradeBenchApply(ResourceLocation id, BlockPos pos, int ench, int enchIndx)
    {
        this.id = id;
        this.pos = pos;
        this.ench = ench;
        this.enchIndx = enchIndx;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeResourceLocation(this.id);
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.ench);
        buffer.writeInt(this.enchIndx);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.id = buffer.readResourceLocation();
        this.pos = buffer.readBlockPos();
        this.ench = buffer.readInt();
        this.enchIndx = buffer.readInt();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleUpgradeBenchApply(player, this.id, this.pos, this.ench, this.enchIndx);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
