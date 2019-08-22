package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class ItemAttachment extends ItemColored implements IAttachment
{
    private final IAttachment.Type type;

    public ItemAttachment(ResourceLocation id, IAttachment.Type type)
    {
        this.setTranslationKey(id.getNamespace() + "." + id.getPath());
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
