package com.tac.guns.item;

import net.minecraft.item.Item;

/**
 * A basic item class that implements {@link IArmorPlate} to indicate this item is a Plate
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ArmorPlateItem extends Item implements IArmorPlate
{
    public ArmorPlateItem(Properties properties)
    {
        super(properties);
    }
}
