package com.tac.guns.block;

import com.tac.guns.tileentity.FlashLightSource;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class FlashLightBlock extends AirBlock implements ITileEntityProvider
{
    public static final Material flashLightBlock;

    public FlashLightBlock() {
        super(Properties.create(flashLightBlock).doesNotBlockMovement().noDrops().setAir().zeroHardnessAndResistance().setLightLevel((p_235470_0_) -> {
            return 15;
        }));
    }
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 15;
    }
    public boolean hasTileEntity(BlockState state) {
        return true;
    }
    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new FlashLightSource();
    }
    static {
        flashLightBlock = (new Builder(MaterialColor.AIR)).doesNotBlockMovement().notSolid().build();
    }
}
