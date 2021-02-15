package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * An attachment class related to under barrels. Use {@link #create(IGunModifier...)} to create an
 * get.
 * <p>
 * Author: MrCrayfish
 */
public class UnderBarrel extends Attachment
{
    private UnderBarrel(IGunModifier... modifier)
    {
        super(modifier);
    }

    /**
     * Creates an under barrel get
     *
     * @param modifier an array of gun modifiers
     * @return an under barrel get
     */
    public static UnderBarrel create(IGunModifier... modifier)
    {
        return new UnderBarrel(modifier);
    }
}
