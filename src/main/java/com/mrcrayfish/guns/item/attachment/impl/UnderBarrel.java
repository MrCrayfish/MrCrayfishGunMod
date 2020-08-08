package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * An attachment class related to under barrels. Use {@link #create(IGunModifier...)} to create an
 * instance.
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
     * Creates an under barrel instance
     *
     * @param modifier an array of gun modifiers
     * @return an under barrel instance
     */
    public static UnderBarrel create(IGunModifier... modifier)
    {
        return new UnderBarrel(modifier);
    }
}
