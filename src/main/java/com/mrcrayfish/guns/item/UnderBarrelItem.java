package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.UnderBarrel;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class UnderBarrelItem extends Item implements IUnderBarrel, IColored
{
    private final UnderBarrel underBarrel;
    private final boolean colored;

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = true;
    }

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties, boolean colored)
    {
        super(properties);
        this.underBarrel = underBarrel;
        this.colored = colored;
    }

    @Override
    public UnderBarrel getProperties()
    {
        return this.underBarrel;
    }

    @Override
    public boolean canColor()
    {
        return this.colored;
    }
}
