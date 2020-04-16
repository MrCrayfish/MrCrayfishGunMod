package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

/**
 * Author: MrCrayfish
 */
public class ColoredItem extends Item
{
    public ColoredItem(Item.Properties properties)
    {
        super(properties);
    }

    public boolean hasColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return tagCompound.contains("Color", Constants.NBT.TAG_INT);
    }

    public int getColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return tagCompound.getInt("Color");
    }

    public void setColor(ItemStack stack, int color)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        tagCompound.putInt("Color", color);
    }

    public void removeColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        tagCompound.remove("Color");
    }
}
