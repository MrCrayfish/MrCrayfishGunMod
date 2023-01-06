package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class S2CMessageBlood extends PlayMessage<S2CMessageBlood>
{
    private double x;
    private double y;
    private double z;

    public S2CMessageBlood() {}

    public S2CMessageBlood(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void encode(S2CMessageBlood message, FriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
    }

    @Override
    public S2CMessageBlood decode(FriendlyByteBuf buffer)
    {
        return new S2CMessageBlood(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void handle(S2CMessageBlood message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleMessageBlood(message));
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
}
