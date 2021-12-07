package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageProjectileHitEntity extends PlayMessage<MessageProjectileHitEntity>
{
    private double x;
    private double y;
    private double z;
    private int type;
    private boolean player;

    public MessageProjectileHitEntity() {}

    public MessageProjectileHitEntity(double x, double y, double z, int type, boolean player)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.player = player;
    }

    @Override
    public void encode(MessageProjectileHitEntity message, FriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeByte(message.type);
        buffer.writeBoolean(message.player);
    }

    @Override
    public MessageProjectileHitEntity decode(FriendlyByteBuf buffer)
    {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        byte type = buffer.readByte();
        boolean player = buffer.readBoolean();
        return new MessageProjectileHitEntity(x, y, z, type, player);
    }

    @Override
    public void handle(MessageProjectileHitEntity message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleProjectileHitEntity(message));
        supplier.get().setPacketHandled(true);
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public double getZ()
    {
        return this.z;
    }

    public boolean isHeadshot()
    {
        return this.type == HitType.HEADSHOT;
    }

    public boolean isCritical()
    {
        return this.type == HitType.CRITICAL;
    }

    public boolean isPlayer()
    {
        return this.player;
    }

    public static class HitType
    {
        public static final int NORMAL = 0;
        public static final int HEADSHOT = 1;
        public static final int CRITICAL = 2;
    }
}
