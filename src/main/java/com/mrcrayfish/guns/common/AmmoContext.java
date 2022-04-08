package com.mrcrayfish.guns.common;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class AmmoContext
{
    public static final AmmoContext NONE = new AmmoContext(ItemStack.EMPTY, null);

    private final ItemStack stack;
    private final IInventory inventory;

    public AmmoContext(ItemStack stack, @Nullable IInventory inventory)
    {
        this.stack = stack;
        this.inventory = inventory;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    @Nullable
    public IInventory getInventory()
    {
        return this.inventory;
    }
}
