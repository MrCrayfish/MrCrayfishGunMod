package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import net.minecraft.item.Item;

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
