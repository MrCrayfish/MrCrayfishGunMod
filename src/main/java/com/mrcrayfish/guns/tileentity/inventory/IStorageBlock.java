package com.mrcrayfish.guns.tileentity.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Author: MrCrayfish
 */
public interface IStorageBlock extends IInventory, INamedContainerProvider
{
    NonNullList<ItemStack> getInventory();

    @Override
    default int getContainerSize()
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
    default ItemStack getItem(int index)
    {
        return index >= 0 && index < this.getInventory().size() ? this.getInventory().get(index) : ItemStack.EMPTY;
    }

    @Override
    default ItemStack removeItem(int index, int count)
    {
        ItemStack stack = ItemStackHelper.removeItem(this.getInventory(), index, count);
        if (!stack.isEmpty())
        {
            this.setChanged();
        }
        return stack;
    }

    @Override
    default ItemStack removeItemNoUpdate(int index)
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
    default void setItem(int index, ItemStack stack)
    {
        this.getInventory().set(index, stack);
        if(!stack.isEmpty() && stack.getCount() > this.getMaxStackSize())
        {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
    }

    @Override
    default int getMaxStackSize()
    {
        return 64;
    }

    @Override
    default boolean stillValid(PlayerEntity player)
    {
        return false;
    }

    @Override
    default void startOpen(PlayerEntity player) {}

    @Override
    default void stopOpen(PlayerEntity player) {}

    @Override
    default boolean canPlaceItem(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    default void clearContent()
    {
        this.getInventory().clear();
    }
}
