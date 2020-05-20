package com.mrcrayfish.guns.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory)
    {
        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if(!itemstack.isEmpty())
            {
                spawnItemStack(worldIn, x, y, z, itemstack);
            }
        }
    }

    private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack)
    {
        float offsetX = RANDOM.nextFloat() * 0.25F + 0.1F;
        float offsetY = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetZ = RANDOM.nextFloat() * 0.25F + 0.1F;

        while(!stack.isEmpty())
        {
            ItemEntity entity = new ItemEntity(worldIn, x + (double) offsetX, y + (double) offsetY, z + (double) offsetZ, stack.split(RANDOM.nextInt(21) + 10));
            entity.setMotion(RANDOM.nextGaussian() * 0.05D, RANDOM.nextGaussian() * 0.25D, RANDOM.nextGaussian() * 0.05D);
            worldIn.addEntity(entity);
        }
    }

    public static int getItemAmount(PlayerEntity player, Item item)
    {
        int amount = 0;
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(!stack.isEmpty() && stack.getItem() == item)
            {
                amount += stack.getCount();
            }
        }
        return amount;
    }

    public static boolean hasItemAndAmount(PlayerEntity player, Item item, int amount)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(!stack.isEmpty() && stack.getItem() == item)
            {
                count += stack.getCount();
            }
        }
        return amount <= count;
    }

    public static boolean removeItemWithAmount(PlayerEntity player, Item item, int amount)
    {
        if(hasItemAndAmount(player, item, amount))
        {
            for(int i = 0; i < player.inventory.getSizeInventory(); i++)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(!stack.isEmpty() && stack.getItem() == item)
                {
                    if(amount - stack.getCount() < 0)
                    {
                        stack.shrink(amount);
                        return true;
                    }
                    else
                    {
                        amount -= stack.getCount();
                        player.inventory.mainInventory.set(i, ItemStack.EMPTY);
                        if(amount == 0)
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int getItemStackAmount(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean hasItemStack(PlayerEntity player, ItemStack find)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(!stack.isEmpty() && areItemStacksEqualIgnoreCount(stack, find))
            {
                count += stack.getCount();
            }
        }
        return find.getCount() <= count;
    }

    public static boolean removeItemStack(PlayerEntity player, ItemStack find)
    {
        int amount = find.getCount();
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
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
                    player.inventory.mainInventory.set(i, ItemStack.EMPTY);
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
        else if(source.getDamage() != target.getDamage())
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
}
