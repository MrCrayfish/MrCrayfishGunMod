package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.UnderBarrel;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class UnderBarrelItem extends Item implements IUnderBarrel
{
    private UnderBarrel underBarrel;

    public UnderBarrelItem(UnderBarrel underBarrel, Properties properties)
    {
        super(properties);
        this.underBarrel = underBarrel;
    }

    @Override
    public UnderBarrel getProperties()
    {
        return this.underBarrel;
    }
}
