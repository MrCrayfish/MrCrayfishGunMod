package com.mrcrayfish.guns.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.Stream;

public class SyncedBlockEntity extends BlockEntity
{
    public SyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state)
    {
        super(type, pos, state);
    }

    protected void syncToClient()
    {
        this.setChanged();
        if(this.level != null && !this.level.isClientSide)
        {
            if(this.level instanceof ServerLevel)
            {
                ClientboundBlockEntityDataPacket packet = this.getUpdatePacket();
                if(packet != null)
                {
                    ServerLevel server = (ServerLevel) this.level;
                    List<ServerPlayer> players = server.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.worldPosition), false);
                    players.forEach(player -> player.connection.send(packet));
                }
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag()
    {
        return this.saveWithFullMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(final Connection net, final ClientboundBlockEntityDataPacket pkt)
    {
        this.deserializeNBT(pkt.getTag());
    }
}