package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.object.Scope;

/**
 * Author: Ocelot
 */
@Beta
public interface IScope extends IAttachment<Scope>
{
    @Override
    default Type getType()
    {
        return Type.SCOPE;
    }
}
