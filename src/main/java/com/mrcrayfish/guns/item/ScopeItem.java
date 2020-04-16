package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Scope;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class ScopeItem extends AttachmentItem
{
    private final Scope scope;

    public ScopeItem(Scope scope, Item.Properties properties)
    {
        super(properties, IAttachment.Type.SCOPE);
        this.scope = scope;
    }

    public Scope getScope()
    {
        return this.scope;
    }
}
