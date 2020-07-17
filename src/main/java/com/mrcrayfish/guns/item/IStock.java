package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Stock;

/**
 * Author: MrCrayfish
 */
public interface IStock extends IAttachment<Stock>
{
    @Override
    default Type getType()
    {
        return Type.STOCK;
    }
}
