package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * Author: MrCrayfish
 */
public abstract class Attachment
{
    private final IGunModifier modifier;

    public Attachment(IGunModifier modifier)
    {
        this.modifier = modifier;
    }

    public IGunModifier getModifier()
    {
        return this.modifier;
    }
}
