package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.client.gui.GuiWorkbench;
import com.mrcrayfish.guns.common.container.ContainerWorkbench;
import com.mrcrayfish.guns.tileentity.WorkbenchTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class GuiHandler implements IGuiHandler
{
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof WorkbenchTileEntity)
        {
            return new ContainerWorkbench(player.inventory, (WorkbenchTileEntity) tileEntity);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof WorkbenchTileEntity)
        {
            return new GuiWorkbench(player.inventory, (WorkbenchTileEntity) tileEntity);
        }
        return null;
    }
}
