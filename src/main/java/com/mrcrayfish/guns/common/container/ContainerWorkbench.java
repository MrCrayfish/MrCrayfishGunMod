package com.mrcrayfish.guns.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Author: MrCrayfish
 */
public class ContainerWorkbench extends Container
{
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return false;
    }
}
