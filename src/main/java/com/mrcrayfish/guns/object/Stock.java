package com.mrcrayfish.guns.object;

/**
 * Author: MrCrayfish
 */
public class Stock
{
    private final float recoilReduction;

    private Stock(float recoilReduction)
    {
        this.recoilReduction = recoilReduction;
    }

    public float getRecoilReduction()
    {
        return this.recoilReduction;
    }

    public static Stock create(float recoilReduction)
    {
        return new Stock(recoilReduction);
    }
}
