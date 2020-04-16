package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.GunMod;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
@Beta
public class AttachmentItem extends ColoredItem implements IAttachment
{
    private final IAttachment.Type type;

    public AttachmentItem(Item.Properties properties, IAttachment.Type type)
    {
        super(properties);
        this.type = type;
    }

    @Override
    public IAttachment.Type getType()
    {
        return type;
    }
}
