package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageProjectileHitBlock implements IMessage
{
    private double x;
    private double y;
    private double z;
    private BlockPos pos;
    private Direction face;

    public MessageProjectileHitBlock() {}

    public MessageProjectileHitBlock(double x, double y, double z, BlockPos pos, Direction face)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = pos;
        this.face = face;
    }

    @Override
    public void encode(PacketBuffer buffer)
    {
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeBlockPos(this.pos);
        buffer.writeEnum(this.face);
    }

    @Override
    public void decode(PacketBuffer buffer)
    {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.pos = buffer.readBlockPos();
        this.face = buffer.readEnum(Direction.class);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleProjectileHitBlock(this));
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

    public BlockPos getPos()
    {
        return this.pos;
    }

    public Direction getFace()
    {
        return this.face;
    }
}
