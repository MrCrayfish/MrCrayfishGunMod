package com.mrcrayfish.guns.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Author: MrCrayfish
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
        buf.writeShort(Item.getId(stack.getItem()));
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
        return new ItemStack(Item.byId(id), buf.readByte());
    }
}
