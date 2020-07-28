package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * Author: MrCrayfish
 */
public class UnderBarrel extends Attachment
{
    private UnderBarrel(IGunModifier... modifier)
    {
        super(modifier);
    }

    public static UnderBarrel create(IGunModifier ... modifier)
    {
        return new UnderBarrel(modifier);
    }
}
