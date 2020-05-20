package com.mrcrayfish.guns.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public abstract class BlockRotatedObject extends HorizontalBlock
{
    public static final DirectionProperty DIRECTION = HorizontalBlock.HORIZONTAL_FACING;

    public BlockRotatedObject(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(DIRECTION, context.getPlacementHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(DIRECTION);
    }
}
