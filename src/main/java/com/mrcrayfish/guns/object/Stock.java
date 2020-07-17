package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;

/**
 * Author: MrCrayfish
 */
public class Stock extends Attachment
{
    private final float recoilReduction;

    private Stock(float recoilReduction, IGunModifier modifier)
    {
        super(modifier);
        this.recoilReduction = recoilReduction;
    }

    public float getRecoilReduction()
    {
        return this.recoilReduction;
    }

    public static Stock create(float recoilReduction, IGunModifier modifier)
    {
        return new Stock(recoilReduction, modifier);
    }
}
