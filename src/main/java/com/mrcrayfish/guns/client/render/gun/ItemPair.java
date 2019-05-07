package com.mrcrayfish.guns.client.render.gun;

import net.minecraft.item.Item;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: MrCrayfish
 */
public class ItemPair extends Pair<Item, Integer>
{
    public final Item item;
    public final int meta;

    public ItemPair(Item item, int meta)
    {
        this.item = item;
        this.meta = meta;
    }

    @Override
    public Item getLeft()
    {
        return item;
    }

    @Override
    public Integer getRight()
    {
        return meta;
    }

    @Override
    public Integer setValue(Integer value)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Item.getIdFromItem(item);
        result = prime * result + meta;
        return result;
    }

    public static ItemPair of(Item item, int meta)
    {
        return new ItemPair(item, meta);
    }
}
