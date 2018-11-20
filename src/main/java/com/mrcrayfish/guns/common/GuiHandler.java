package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.client.gui.GuiWorkbench;
import com.mrcrayfish.guns.common.container.ContainerWorkbench;
import com.mrcrayfish.guns.tileentity.TileEntityWorkbench;
import net.minecraft.entity.player.EntityPlayer;
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
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntityWorkbench)
        {
            return new ContainerWorkbench();
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof TileEntityWorkbench)
        {
            return new GuiWorkbench();
        }
        return null;
    }
}
