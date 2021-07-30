package com.mrcrayfish.guns.tileentity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.server.ServerWorld;

import java.util.stream.Stream;

public class SyncedTileEntity extends TileEntity
{
    public SyncedTileEntity(TileEntityType<?> tileEntityTypeIn)
    {
        super(tileEntityTypeIn);
    }

    protected void syncToClient()
    {
        this.setChanged();
        if(this.level != null && !this.level.isClientSide)
        {
            if(this.level instanceof ServerWorld)
            {
                SUpdateTileEntityPacket packet = this.getUpdatePacket();
                if(packet != null)
                {
                    ServerWorld server = (ServerWorld) this.level;
                    Stream<ServerPlayerEntity> players = server.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.worldPosition), false);
                    players.forEach(player -> player.connection.send(packet));
                }
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt)
    {
        this.deserializeNBT(pkt.getTag());
    }
}