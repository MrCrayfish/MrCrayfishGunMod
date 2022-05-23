package com.tac.guns.item.attachment.impl;

import com.tac.guns.interfaces.IGunModifier;

/**
 * An attachment class related to under barrels. Use {@link #create(IGunModifier...)} to create an
 * get.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class SideRail extends Attachment
{
    private SideRail(IGunModifier... modifier)
    {
        super(modifier);
    }

    /**
     * Creates an under barrel get
     *
     * @param modifier an array of gun modifiers
     * @return an under barrel get
     */
    public static SideRail create(IGunModifier... modifier)
    {
        return new SideRail(modifier);
    }
}
