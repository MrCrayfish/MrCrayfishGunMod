package com.tac.guns.tileentity.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IStorageBlock extends IInventory, INamedContainerProvider
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
    default boolean isUsableByPlayer(PlayerEntity player)
    {
        return false;
    }

    @Override
    default void openInventory(PlayerEntity player) {}

    @Override
    default void closeInventory(PlayerEntity player) {}

    @Override
    default boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    default void clear()
    {
        this.getInventory().clear();
    }
}
