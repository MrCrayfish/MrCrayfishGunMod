package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Barrel;

/**
 * Author: MrCrayfish
 */
public class BarrelItem extends AttachmentItem
{
    private Barrel barrel;

    public BarrelItem(Barrel barrel, Properties properties)
    {
        super(properties, Type.BARREL);
        this.barrel = barrel;
    }

    public Barrel getBarrel()
    {
        return barrel;
    }
}
