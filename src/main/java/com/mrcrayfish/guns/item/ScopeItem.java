package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Scope;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class ScopeItem extends Item implements IScope, IColored
{
    private final Scope scope;

    public ScopeItem(Scope scope, Item.Properties properties)
    {
        super(properties);
        this.scope = scope;
    }

    @Override
    public Scope getProperties()
    {
        return this.scope;
    }
}
