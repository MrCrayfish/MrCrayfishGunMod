package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.CommonHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage
{
    private float rotationYaw;
    private float rotationPitch;

    public MessageShoot() {}

    public MessageShoot(PlayerEntity player)
    {
        this.rotationYaw = player.rotationYaw;
        this.rotationPitch = player.rotationPitch;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.rotationYaw = buffer.readFloat();
        this.rotationPitch = buffer.readFloat();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayerEntity player = supplier.get().getSender();
            if(player != null)
            {
                CommonHandler.fireHeldGun(this, player);
            }
        });
        supplier.get().setPacketHandled(true);
    }

    public float getRotationYaw()
    {
        return rotationYaw;
    }

    public float getRotationPitch()
    {
        return rotationPitch;
    }
}
