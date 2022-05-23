package com.tac.guns.effect;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.Collections;
import java.util.List;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class IncurableEffect extends Effect
{
    public IncurableEffect(EffectType typeIn, int liquidColorIn)
    {
        super(typeIn, liquidColorIn);
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return Collections.emptyList();
    }


}
