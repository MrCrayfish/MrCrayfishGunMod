package com.tac.guns.network.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.tac.guns.client.screen.UpgradeBenchScreen;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.init.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.commons.lang3.SerializationUtils;

import java.beans.XMLEncoder;
import java.io.*;
import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageUpgradeBenchApply implements IMessage
{
    // Ew public
   public BlockPos pos;
   public String reqKey;
    public MessageUpgradeBenchApply() {}

    public MessageUpgradeBenchApply(BlockPos pos, String reqKey)
    {
        this.pos = pos;
        this.reqKey = reqKey;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeString(this.reqKey);


    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.pos = buffer.readBlockPos();
        this.reqKey = buffer.readString();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleUpgradeBenchApply(this, player);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}
