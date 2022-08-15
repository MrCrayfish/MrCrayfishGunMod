package com.tac.guns.common.container;

import com.tac.guns.client.handler.command.GuiEditor;
import com.tac.guns.crafting.WorkbenchRecipes;
import com.tac.guns.init.ModContainers;
import com.tac.guns.item.GunItem;
import com.tac.guns.tileentity.UpgradeBenchTileEntity;
import com.tac.guns.tileentity.WorkbenchTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class UpgradeBenchContainer extends Container
{
    private UpgradeBenchTileEntity upgradeBench;
    private BlockPos pos;

    public UpgradeBenchContainer(int windowId, IInventory playerInventory, UpgradeBenchTileEntity workbench)
    {
        super(ModContainers.UPGRADE_BENCH.get(), windowId);
        this.upgradeBench = workbench;
        this.pos = workbench.getPos();

        int offset = WorkbenchRecipes.isEmpty(workbench.getWorld()) ? 0 : 28;

        this.addSlot(new Slot(workbench, 0, 17400, 40000)
        {
            @Override
            public boolean isItemValid(ItemStack stack)
            {
                return true;
            }

            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, -112 + 8 + x * 18, 103 + y * 18)); // 102
            }
        }

        for(int x = 0; x < 9; x++)
        {
            this.addSlot(new Slot(playerInventory, x,  -112 + 8 + x * 18, 161)); // 160
        }

    }
    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return upgradeBench.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
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
            else
            {
                if(slotStack.getItem() instanceof DyeItem)
                {
                    if(!this.mergeItemStack(slotStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index < 28)
                {
                    if(!this.mergeItemStack(slotStack, 28, 36, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(index <= 36 && !this.mergeItemStack(slotStack, 1, 28, false))
                {
                    return ItemStack.EMPTY;
                }
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

    public UpgradeBenchTileEntity getBench()
    {
        return upgradeBench;
    }

    public BlockPos getPos()
    {
        return pos;
    }

}
