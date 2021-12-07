package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.common.network.ServerPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageShoot implements IMessage
{
    private float rotationYaw;
    private float rotationPitch;

    public MessageShoot() {}

    public MessageShoot(Player player)
    {
        this.rotationYaw = player.getYRot();
        this.rotationPitch = player.getXRot();
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        this.rotationYaw = buffer.readFloat();
        this.rotationPitch = buffer.readFloat();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() ->
        {
            ServerPlayer player = supplier.get().getSender();
            if(player != null)
            {
                ServerPlayHandler.handleShoot(this, player);
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
}
