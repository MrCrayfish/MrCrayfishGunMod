package com.mrcrayfish.guns.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Author: MrCrayfish
 */
public class ItemStackUtil
{
    public static CompoundNBT createTagCompound(ItemStack stack)
    {
        if(!stack.hasTag())
        {
            stack.setTag(new CompoundNBT());
        }
        return stack.getTag();
    }

    public static boolean areItemStackSameItem(ItemStack source, ItemStack other)
    {
        if(source.getItem() != other.getItem())
        {
            return false;
        }
        return source.getDamage() == other.getDamage();
    }

    public static boolean areItemStackEqualIgnoreTag(ItemStack source, ItemStack other)
    {
        if(source.getCount() != other.getCount())
        {
            return false;
        }
        else if(source.getItem() != other.getItem())
        {
            return false;
        }
        return source.getDamage() == other.getDamage();
    }

    public static void writeItemStackToBufIgnoreTag(ByteBuf buf, ItemStack stack)
    {
        if(stack.isEmpty())
        {
            buf.writeShort(-1);
            return;
        }
        buf.writeShort(Item.getIdFromItem(stack.getItem()));
        buf.writeByte(stack.getCount());
    }

    public static ItemStack readItemStackFromBufIgnoreTag(ByteBuf buf)
    {
        int id = buf.readShort();
        if(id < 0)
        {
            return ItemStack.EMPTY;
        }
        return new ItemStack(Item.getItemById(id), buf.readByte());
    }
}
