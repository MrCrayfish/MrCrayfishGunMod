package com.tac.guns.network.message;

import com.tac.guns.common.network.ServerPlayHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class MessageShoot implements IMessage
{
    private float rotationYaw;
    private float rotationPitch;

    private float randP;
    private float randY;

    public MessageShoot() {}

    public MessageShoot(float yaw, float pitch, float randP, float randY)
    {
        this.rotationPitch = pitch;
        this.rotationYaw = yaw;
        this.randP = randP;
        this.randY = randY;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
        buffer.writeFloat(this.randP);
        buffer.writeFloat(this.randY);
        }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.rotationYaw = buffer.readFloat();
        this.rotationPitch = buffer.readFloat();
        this.randP = buffer.readFloat();
        this.randY = buffer.readFloat();
        }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleShoot(this, player, randP, randY);
            }
        });
        supplier.get().setPacketHandled(true);
    }

    public float getRotationYaw()
    {
        return this.rotationYaw;
    }

    public float getRotationPitch()
    {
        return this.rotationPitch;
    }

    public float getRandP()
    {
        return this.randP;
    }

    public float getRandY()
    {
        return this.randY;
    }
}
