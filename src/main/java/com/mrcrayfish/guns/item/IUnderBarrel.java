package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.object.UnderBarrel;

/**
 * Author: MrCrayfish
 */
public interface IUnderBarrel extends IAttachment<UnderBarrel>
{
    @Override
    default Type getType()
    {
        return Type.UNDER_BARREL;
    }
}
