package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
@Beta
public class AmmoItem extends Item
{
    public AmmoItem(Item.Properties properties)
    {
        super(properties);
        AmmoRegistry.getInstance().register(this);
    }
}
