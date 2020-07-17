package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Barrel;
import com.mrcrayfish.guns.object.Scope;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class BarrelItem extends ColoredItem implements IBarrel
{
    private final Barrel barrel;

    public BarrelItem(Barrel barrel, Item.Properties properties)
    {
        super(properties);
        this.barrel = barrel;
    }

    @Override
    public Barrel getBarrel()
    {
        return this.barrel;
    }
}
