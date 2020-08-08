package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * An attachment class related to barrels. Barrels need to specify the length in order to render
 * the muzzle flash correctly. Use {@link #create(float, IGunModifier...)} to create an instance.
 * <p>
 * Author: MrCrayfish
 */
public class Barrel extends Attachment
{
    private float length;

    private Barrel(float length, IGunModifier... modifier)
    {
        super(modifier);
        this.length = length;
    }

    /**     * @return The length of the barrel in pixels
     */
    public float getLength()
    {
        return this.length;
    }

    /**
     * Creates a barrel instance
     *
     * @param length    the length of the barrel model in pixels
     * @param modifiers an array of gun modifiers
     * @return a barrel instance
     */
    public static Barrel create(float length, IGunModifier... modifiers)
    {
        return new Barrel(length, modifiers);
    }
}
