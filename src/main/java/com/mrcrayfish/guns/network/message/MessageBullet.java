package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.object.Bullet;
import com.mrcrayfish.guns.proxy.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Author: MrCrayfish
 */
public class MessageBullet implements IMessage, IMessageHandler<MessageBullet, IMessage>
{
    private int entityId;
    private double posX;
    private double posY;
    private double posZ;
    private double motionX;
    private double motionY;
    private double motionZ;
    private int trailColor;
    private double trailLengthMultiplier;

    public MessageBullet() {}

    public MessageBullet(int entityId, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int trailColor, double trailLengthMultiplier)
    {
        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.trailColor = trailColor;
        this.trailLengthMultiplier = trailLengthMultiplier;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeDouble(this.posX);
        buf.writeDouble(this.posY);
        buf.writeDouble(this.posZ);
        buf.writeDouble(this.motionX);
        buf.writeDouble(this.motionY);
        buf.writeDouble(this.motionZ);
        buf.writeInt(this.trailColor);
        buf.writeDouble(this.trailLengthMultiplier);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.posX = buf.readDouble();
        this.posY = buf.readDouble();
        this.posZ = buf.readDouble();
        this.motionX = buf.readDouble();
        this.motionY = buf.readDouble();
        this.motionZ = buf.readDouble();
        this.trailColor = buf.readInt();
        this.trailLengthMultiplier = buf.readDouble();
    }

    @Override
    public IMessage onMessage(MessageBullet message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
        {
            EntityProjectile projectile = null;
            Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
            if(entity instanceof EntityProjectile)
            {
                projectile = (EntityProjectile) entity;
            }
            Bullet bullet = new Bullet(projectile, message.entityId, message.posX, message.posY, message.posZ, message.motionX, message.motionY, message.motionZ, message.trailColor, message.trailLengthMultiplier);
            ClientProxy.renderEvents.addBullet(bullet);
        });
        return null;
    }
}
