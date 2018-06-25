package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class ItemAttachment extends Item implements IAttachment
{
    private final String type;

    public ItemAttachment(String id, String type)
    {
        this.setUnlocalizedName(id);
        this.setRegistryName(id);
        this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
        this.type = type;
    }

    @Override
    public String getType()
    {
        return type;
    }
}
