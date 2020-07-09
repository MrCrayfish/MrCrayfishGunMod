package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.object.Barrel;

/**
 * Author: Ocelot
 */
@Beta
public interface IBarrel extends IAttachment
{
    @Override
    default Type getType()
    {
        return Type.BARREL;
    }

    /**
     * @return The additional barrel information about this item
     */
    Barrel getBarrel();
}
