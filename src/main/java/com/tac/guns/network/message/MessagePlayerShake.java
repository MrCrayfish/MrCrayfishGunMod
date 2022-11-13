package com.tac.guns.network.message;


import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


/**
 * Author: https://github.com/Charles445/DamageTilt/blob/1.16/src/main/java/com/charles445/damagetilt/MessageUpdateAttackYaw.java, continued by Timeless devs
 */


public class MessagePlayerShake implements IMessage
{
    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeFloat(attackedAtYaw);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.attackedAtYaw = buffer.readFloat();
    }

    public float attackedAtYaw;

    public MessagePlayerShake() {}
    public MessagePlayerShake(float value) {this.attackedAtYaw = value;}
    public MessagePlayerShake(LivingEntity entity)
    {
        this.attackedAtYaw = entity.attackedAtYaw;
    }

    @OnlyIn(Dist.CLIENT)
    public static void fromMessage()
    {
        Minecraft.getInstance().player.hurtTime = 0;
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().setPacketHandled(true);
        if(supplier.get().getDirection() != NetworkDirection.PLAY_TO_CLIENT)
            return;

        Minecraft.getInstance().deferTask(() ->
        {
            fromMessage();
        });
    }
}


