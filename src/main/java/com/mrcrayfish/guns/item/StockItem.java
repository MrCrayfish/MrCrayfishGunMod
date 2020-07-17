package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Stock;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class StockItem extends Item implements IStock
{
    private Stock stock;

    public StockItem(Stock stock, Properties properties)
    {
        super(properties);
        this.stock = stock;
    }

    @Override
    public Stock getProperties()
    {
        return this.stock;
    }
}
