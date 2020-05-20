package com.mrcrayfish.guns.object;

/**
 * Author: MrCrayfish
 */
public class Barrel
{
    private float length;

    private Barrel(float length)
    {
        this.length = length;
    }

    public float getLength()
    {
        return length;
    }

    public static Barrel create(float length)
    {
        return new Barrel(length);
    }
}
