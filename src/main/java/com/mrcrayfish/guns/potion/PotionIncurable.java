package com.mrcrayfish.guns.potion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class PotionIncurable extends PotionMod
{
    public PotionIncurable(boolean isBadEffect, boolean hasIcon, int liquidColor, String name)
    {
        super(isBadEffect, hasIcon, liquidColor, name);
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return new ArrayList<>();
    }
}