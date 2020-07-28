package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Stock;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class StockItem extends Item implements IStock, IColored
{
    private final Stock stock;
    private final boolean colored;

    public StockItem(Stock stock, Properties properties)
    {
        super(properties);
        this.stock = stock;
        this.colored = true;
    }

    public StockItem(Stock stock, Properties properties, boolean colored)
    {
        super(properties);
        this.stock = stock;
        this.colored = colored;
    }

    @Override
    public Stock getProperties()
    {
        return this.stock;
    }

    @Override
    public boolean canColor()
    {
        return this.colored;
    }
}
