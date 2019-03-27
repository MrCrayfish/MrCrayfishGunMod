package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;

/**
 * Author: MrCrayfish
 */
public class ItemAttachment extends ItemColored implements IAttachment
{
    private final IAttachment.Type type;

    public ItemAttachment(String id, IAttachment.Type type)
    {
        this.setUnlocalizedName(id);
        this.setRegistryName(id);
        this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
        this.type = type;
    }

    @Override
    public IAttachment.Type getType()
    {
        return type;
    }
}
