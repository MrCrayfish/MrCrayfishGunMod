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
        this.markDirty();
        if(this.world != null && !this.world.isRemote)
        {
            if(this.world instanceof ServerWorld)
            {
                SUpdateTileEntityPacket packet = this.getUpdatePacket();
                if(packet != null)
                {
                    ServerWorld server = (ServerWorld) this.world;
                    Stream<ServerPlayerEntity> players = server.getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(this.pos), false);
                    players.forEach(player -> player.connection.sendPacket(packet));
                }
            }
        }
    }

    @Override
    public CompoundNBT getUpdateTag()
    {
        return this.write(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt)
    {
        this.deserializeNBT(pkt.getNbtCompound()); //TODO keep an eye on this as blockstate is null in the implementation
    }
}