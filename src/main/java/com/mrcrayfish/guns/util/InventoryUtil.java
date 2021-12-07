package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.crafting.WorkbenchIngredient;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    public static int getItemStackAmount(Player player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.getInventory().items)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean removeItemStack(Player player, ItemStack find)
    {
        int amount = find.getCount();
        for(int i = 0; i < player.getInventory().getContainerSize(); i++)
        {
            ItemStack stack = player.getInventory().getItem(i);
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                if(amount - stack.getCount() < 0)
                {
                    stack.shrink(amount);
                    return true;
                }
                else
                {
                    amount -= stack.getCount();
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    if(amount == 0)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean areItemStacksEqualIgnoreCount(ItemStack source, ItemStack target)
    {
        if(source.getItem() != target.getItem())
        {
            return false;
        }
        else if(source.getDamageValue() != target.getDamageValue())
        {
            return false;
        }
        else if(source.getTag() == null && target.getTag() != null)
        {
            return false;
        }
        else
        {
            return (source.getTag() == null || source.getTag().equals(target.getTag())) && source.areCapsCompatible(target);
        }
    }

    public static boolean hasWorkstationIngredient(Player player, WorkbenchIngredient find)
    {
        int count = 0;
        for(ItemStack stack : player.getInventory().items)
        {
            if(!stack.isEmpty() && find.test(stack))
            {
                count += stack.getCount();
            }
        }
        return find.getCount() <= count;
    }

    public static boolean removeWorkstationIngredient(Player player, WorkbenchIngredient find)
    {
        int amount = find.getCount();
        for(int i = 0; i < player.getInventory().getContainerSize(); i++)
        {
            ItemStack stack = player.getInventory().getItem(i);
            if(!stack.isEmpty() && find.test(stack))
            {
                if(amount - stack.getCount() < 0)
                {
                    stack.shrink(amount);
                    return true;
                }
                else
                {
                    amount -= stack.getCount();
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    if(amount == 0) return true;
                }
            }
        }
        return false;
    }
}
