package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Scope;
import net.minecraft.item.Item;

/**
 * Author: MrCrayfish
 */
public class ScopeItem extends Item implements IScope, IColored
{
    private final Scope scope;
    private final boolean colored;

    public ScopeItem(Scope scope, Item.Properties properties)
    {
        super(properties);
        this.scope = scope;
        this.colored = true;
    }

    public ScopeItem(Scope scope, Item.Properties properties, boolean colored)
    {
        super(properties);
        this.scope = scope;
        this.colored = colored;
    }

    @Override
    public Scope getProperties()
    {
        return this.scope;
    }

    @Override
    public boolean canColor()
    {
        return this.colored;
    }
}
