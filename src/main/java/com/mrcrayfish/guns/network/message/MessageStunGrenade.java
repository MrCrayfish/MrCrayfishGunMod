package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageStunGrenade implements IMessage
{
    private double x, y, z;

    public MessageStunGrenade() {}

    public MessageStunGrenade(double x, double y, double z)
    {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleExplosionStunGrenade(this));
        supplier.get().setPacketHandled(true);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}