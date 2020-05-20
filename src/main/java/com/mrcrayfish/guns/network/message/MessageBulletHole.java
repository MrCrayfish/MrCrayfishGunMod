package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageBulletHole implements IMessage
{
    private double x;
    private double y;
    private double z;
    private Direction direction;
    private BlockPos pos;

    public MessageBulletHole() {}

    public MessageBulletHole(double x, double y, double z, Direction direction, BlockPos pos)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.pos = pos;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeEnumValue(this.direction);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.direction = buffer.readEnumValue(Direction.class);
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientHandler.handleMessageBulletHole(this));
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

    public Direction getDirection()
    {
        return this.direction;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }
}
