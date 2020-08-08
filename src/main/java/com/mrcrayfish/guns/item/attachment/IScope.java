package com.mrcrayfish.guns.item.attachment;

import com.mrcrayfish.guns.item.attachment.impl.Scope;

/**
 * An interface to turn an any item into a scope attachment. This is useful if your item extends a
 * custom item class otherwise {@link com.mrcrayfish.guns.item.ScopeItem} can be used instead of
 * this interface.
 * <p>
 * Author: Ocelot
 */
public interface IScope extends IAttachment<Scope>
{
    /**
     * @return The type of this attachment
     */
    @Override
    default Type getType()
    {
        return Type.SCOPE;
    }
}
