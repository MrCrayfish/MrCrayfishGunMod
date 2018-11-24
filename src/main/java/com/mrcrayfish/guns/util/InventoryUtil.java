package com.mrcrayfish.guns.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class InventoryUtil
{
    private static final Random RANDOM = new Random();

    public static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory)
    {
        for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
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

        while (!stack.isEmpty())
        {
            EntityItem entity = new EntityItem(worldIn, x + (double)offsetX, y + (double)offsetY, z + (double)offsetZ, stack.splitStack(RANDOM.nextInt(21) + 10));
            entity.motionX = RANDOM.nextGaussian() * 0.05000000074505806D;
            entity.motionY = RANDOM.nextGaussian() * 0.05000000074505806D + 0.20000000298023224D;
            entity.motionZ = RANDOM.nextGaussian() * 0.05000000074505806D;
            worldIn.spawnEntity(entity);
        }
    }

    public static int getItemAmount(EntityPlayer player, Item item)
    {
        int amount = 0;
        for(int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(stack != null && stack.getItem() == item)
            {
                amount += stack.getCount();
            }
        }
        return amount;
    }

    public static boolean hasItemAndAmount(EntityPlayer player, Item item, int amount)
    {
        int count = 0;
        for(ItemStack stack : player.inventory.mainInventory)
        {
            if(stack != null && stack.getItem() == item)
            {
                count += stack.getCount();
            }
        }
        return amount <= count;
    }

    public static boolean removeItemWithAmount(EntityPlayer player, Item item, int amount)
    {
        if(hasItemAndAmount(player, item, amount))
        {
            for(int i = 0; i < player.inventory.getSizeInventory(); i++)
            {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if(stack != null && stack.getItem() == item)
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
                        if(amount == 0) return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getItemStackAmount(EntityPlayer player, ItemStack find)
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

    public static boolean hasItemStack(EntityPlayer player, ItemStack find)
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

    public static boolean removeItemStack(EntityPlayer player, ItemStack find)
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
                    if(amount == 0) return true;
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
        else if(source.getItemDamage() != target.getItemDamage())
        {
            return false;
        }
        else if(source.getTagCompound() == null && target.getTagCompound() != null)
        {
            return false;
        }
        else
        {
            return (source.getTagCompound() == null || source.getTagCompound().equals(target.getTagCompound())) && source.areCapsCompatible(target);
        }
    }
}
