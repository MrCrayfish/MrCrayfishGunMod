package com.mrcrayfish.guns.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

public class TileEntitySynced extends TileEntity
{
    protected void syncToClient()
    {
        this.markDirty();
        if(!world.isRemote)
        {
            if(world instanceof WorldServer)
            {
                WorldServer server = (WorldServer) world;
                PlayerChunkMapEntry entry = server.getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4);
                if(entry != null)
                {
                    SPacketUpdateTileEntity packet = getUpdatePacket();
                    if(packet != null)
                    {
                        entry.sendPacket(packet);
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }
}