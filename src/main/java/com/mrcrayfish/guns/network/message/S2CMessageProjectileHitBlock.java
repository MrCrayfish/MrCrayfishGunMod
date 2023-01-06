package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class S2CMessageProjectileHitBlock extends PlayMessage<S2CMessageProjectileHitBlock>
{
    private double x;
    private double y;
    private double z;
    private BlockPos pos;
    private Direction face;

    public S2CMessageProjectileHitBlock() {}

    public S2CMessageProjectileHitBlock(double x, double y, double z, BlockPos pos, Direction face)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pos = pos;
        this.face = face;
    }

    @Override
    public void encode(S2CMessageProjectileHitBlock message, FriendlyByteBuf buffer)
    {
        buffer.writeDouble(message.x);
        buffer.writeDouble(message.y);
        buffer.writeDouble(message.z);
        buffer.writeBlockPos(message.pos);
        buffer.writeEnum(message.face);
    }

    @Override
    public S2CMessageProjectileHitBlock decode(FriendlyByteBuf buffer)
    {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        BlockPos pos = buffer.readBlockPos();
        Direction face = buffer.readEnum(Direction.class);
        return new S2CMessageProjectileHitBlock(x, y, z, pos, face);
    }

    @Override
    public void handle(S2CMessageProjectileHitBlock message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleProjectileHitBlock(message));
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
