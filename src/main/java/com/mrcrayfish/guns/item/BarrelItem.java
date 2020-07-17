package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Barrel;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class BarrelItem extends Item implements IBarrel, IColored
{
    private final Barrel barrel;

    public BarrelItem(Barrel barrel, Item.Properties properties)
    {
        super(properties);
        this.barrel = barrel;
    }

    @Override
    public Barrel getProperties()
    {
        return this.barrel;
    }
}
