package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.MrCrayfishGunMod;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageExplosionStunGrenade implements IMessage, IMessageHandler<MessageExplosionStunGrenade, IMessage>
{
    private double x, y, z;

    public MessageExplosionStunGrenade() {}

    public MessageExplosionStunGrenade(double x, double y, double z)
    {
        this.z = z;
        this.y = y;
        this.x = x;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public IMessage onMessage(MessageExplosionStunGrenade message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> MrCrayfishGunMod.proxy.createExplosionStunGrenade(message.x, message.y, message.z));
        return null;
    }
}