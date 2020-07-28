package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Barrel;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class BarrelItem extends Item implements IBarrel, IColored
{
    private final Barrel barrel;
    private final boolean colored;

    public BarrelItem(Barrel barrel, Item.Properties properties)
    {
        super(properties);
        this.barrel = barrel;
        this.colored = true;
    }

    public BarrelItem(Barrel barrel, Item.Properties properties, boolean colored)
    {
        super(properties);
        this.barrel = barrel;
        this.colored = colored;
    }

    @Override
    public Barrel getProperties()
    {
        return this.barrel;
    }

    @Override
    public boolean canColor()
    {
        return this.colored;
    }
}
