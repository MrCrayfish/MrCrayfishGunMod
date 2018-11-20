package com.mrcrayfish.guns.tileentity.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Author: MrCrayfish
 */
public interface IStorageBlock extends IInventory
{
    NonNullList<ItemStack> getInventory();

    @Override
    default int getSizeInventory()
    {
        return this.getInventory().size();
    }

    @Override
    default boolean isEmpty()
    {
        for(ItemStack itemstack : this.getInventory())
        {
            if(!itemstack.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getStackInSlot(int index)
    {
        return index >= 0 && index < this.getInventory().size() ? this.getInventory().get(index) : ItemStack.EMPTY;
    }

    @Override
    default ItemStack decrStackSize(int index, int count)
    {
        ItemStack stack = ItemStackHelper.getAndSplit(this.getInventory(), index, count);
        if (!stack.isEmpty())
        {
            this.markDirty();
        }
        return stack;
    }

    @Override
    default ItemStack removeStackFromSlot(int index)
    {
        ItemStack stack = this.getInventory().get(index);
        if (stack.isEmpty())
        {
            return ItemStack.EMPTY;
        }
        else
        {
            this.getInventory().set(index, ItemStack.EMPTY);
            return stack;
        }
    }

    @Override
    default void setInventorySlotContents(int index, ItemStack stack)
    {
        this.getInventory().set(index, stack);
        if(!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    default int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    default boolean isUsableByPlayer(EntityPlayer player)
    {
        return false;
    }

    @Override
    default void openInventory(EntityPlayer player) {}

    @Override
    default void closeInventory(EntityPlayer player) {}

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    default int getField(int id)
    {
        return 0;
    }

    @Override
    default void setField(int id, int value) {}

    @Override
    default int getFieldCount()
    {
        return 0;
    }

    @Override
    default void clear()
    {
        this.getInventory().clear();
    }

    @Override
    default boolean hasCustomName()
    {
        return false;
    }

}
