package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * Author: MrCrayfish
 */
public class Stock extends Attachment
{
    private Stock(IGunModifier ... modifier)
    {
        super(modifier);
    }

    public static Stock create(IGunModifier ... modifier)
    {
        return new Stock(modifier);
    }
}
