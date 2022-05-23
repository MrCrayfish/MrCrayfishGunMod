package com.tac.guns.tileentity;

import com.tac.guns.init.ModBlocks;
import com.tac.guns.init.ModTileEntities;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class FlashLightSource extends TileEntity implements ITickableTileEntity
{
    public FlashLightSource()
    {
        super(ModTileEntities.LIGHT_SOURCE.get());
    }

    public static int ticks;
    @Override
    public void tick() {
        this.ticks++;
        if (this.ticks > 4) {
            this.world.setBlockState(this.getPos(), Blocks.AIR.getDefaultState(), 3);
            this.world.removeTileEntity(this.getPos());
        }
    }
}