package com.mrcrayfish.guns.common.container;

import com.mrcrayfish.guns.tileentity.TileEntityWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Author: MrCrayfish
 */
public class ContainerWorkbench extends Container
{
    private TileEntityWorkbench workbench;
    private BlockPos pos;

    public ContainerWorkbench(IInventory playerInventory, TileEntityWorkbench workbench)
    {
        this.workbench = workbench;
        this.pos = workbench.getPos();

        this.addSlotToContainer(new Slot(workbench, 0, 187, 30)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return stack.getItem() == Items.DYE;
            }

            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 9; y++)
            {
                this.addSlotToContainer(new Slot(playerInventory, y + x * 9 + 9, 8 + y * 18, 120 + x * 18));
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlotToContainer(new Slot(playerInventory, x, 8 + x * 18, 178));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return workbench.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            if(index == 0)
            {
                if(!this.mergeItemStack(slotStack, 1, 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(index > 0)
            {
                if(slotStack.getItem() instanceof ItemDye)
                {
                    if(!this.mergeItemStack(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index >= 1 && index < 28)
                {
                    if(!this.mergeItemStack(slotStack, 28, 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index > 27 && index <= 36 && !this.mergeItemStack(slotStack, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(slotStack, 1, 36, false))
            {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if(slotStack.getCount() == stack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }

        return stack;
    }

    public TileEntityWorkbench getWorkbench()
    {
        return workbench;
    }

    public BlockPos getPos()
    {
        return pos;
    }

}
