package com.tac.guns.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class BufferUtil
{
    /**
     * Writes an ItemStack to a buffer without it's tag compound
     *
     * @param buf   the byte buffer to write to
     * @param stack the item stack to write
     */
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

    /**
     * Reads an ItemStack from a buffer that has no tag compound.
     *
     * @param buf the byte buffer to read from
     * @return the read item stack
     */
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
