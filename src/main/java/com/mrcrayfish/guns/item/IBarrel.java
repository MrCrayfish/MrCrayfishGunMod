package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.Barrel;

/**
 * Author: Ocelot
 */
public interface IBarrel extends IAttachment<Barrel>
{
    @Override
    default Type getType()
    {
        return Type.BARREL;
    }
}
