package com.mrcrayfish.guns.blockentity.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

/**
 * Author: MrCrayfish
 */
public interface IStorageBlock extends Container, MenuProvider
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
        ItemStack stack = ContainerHelper.removeItem(this.getInventory(), index, count);
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
    default boolean stillValid(Player player)
    {
        return false;
    }

    @Override
    default void startOpen(Player player) {}

    @Override
    default void stopOpen(Player player) {}

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
