package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageBullet implements IMessage
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
    public void encode(PacketBuffer buffer)
    {
        buffer.writeVarInt(this.entityId);
        buffer.writeDouble(this.posX);
        buffer.writeDouble(this.posY);
        buffer.writeDouble(this.posZ);
        buffer.writeDouble(this.motionX);
        buffer.writeDouble(this.motionY);
        buffer.writeDouble(this.motionZ);
        buffer.writeVarInt(this.trailColor);
        buffer.writeDouble(this.trailLengthMultiplier);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.entityId = buffer.readVarInt();
        this.posX = buffer.readDouble();
        this.posY = buffer.readDouble();
        this.posZ = buffer.readDouble();
        this.motionX = buffer.readDouble();
        this.motionY = buffer.readDouble();
        this.motionZ = buffer.readDouble();
        this.trailColor = buffer.readVarInt();
        this.trailLengthMultiplier = buffer.readDouble();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientHandler.handleMessageBullet(this));
        supplier.get().setPacketHandled(true);
    }

    public int getEntityId()
    {
        return entityId;
    }

    public double getPosX()
    {
        return posX;
    }

    public double getPosY()
    {
        return posY;
    }

    public double getPosZ()
    {
        return posZ;
    }

    public double getMotionX()
    {
        return motionX;
    }

    public double getMotionY()
    {
        return motionY;
    }

    public double getMotionZ()
    {
        return motionZ;
    }

    public int getTrailColor()
    {
        return trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return trailLengthMultiplier;
    }
}
