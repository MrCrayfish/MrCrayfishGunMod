package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * An attachment class related to stocks. Use {@link #create(IGunModifier...)} to create an instance.
 * <p>
 * Author: MrCrayfish
 */
public class Stock extends Attachment
{
    private Stock(IGunModifier... modifier)
    {
        super(modifier);
    }

    /**
     * Creates a stock instance
     *
     * @param modifier an array of gun modifiers
     * @return a stock instance
     */
    public static Stock create(IGunModifier... modifier)
    {
        return new Stock(modifier);
    }
}
