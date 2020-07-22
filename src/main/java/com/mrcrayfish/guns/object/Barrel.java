package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * Author: MrCrayfish
 */
public class Barrel extends Attachment
{
    private float length;

    private Barrel(float length, IGunModifier ... modifier)
    {
        super(modifier);
        this.length = length;
    }

    public float getLength()
    {
        return this.length;
    }

    public static Barrel create(float length, IGunModifier ... modifier)
    {
        return new Barrel(length, modifier);
    }
}
