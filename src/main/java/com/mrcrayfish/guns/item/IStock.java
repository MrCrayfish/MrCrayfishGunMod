package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Stock;

/**
 * Author: MrCrayfish
 */
public interface IStock extends IAttachment
{
    @Override
    default Type getType()
    {
        return Type.STOCK;
    }

    /**
     * @return The additional stock information about this item
     */
    Stock getStock();
}
