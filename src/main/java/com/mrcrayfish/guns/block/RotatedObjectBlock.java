package com.mrcrayfish.guns.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public abstract class RotatedObjectBlock extends HorizontalBlock
{
    public static final DirectionProperty DIRECTION = HorizontalBlock.HORIZONTAL_FACING;

    public RotatedObjectBlock(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
    }

    @Override
    public boolean isTransparent(BlockState state)
    {
        return true;
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
