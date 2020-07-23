package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    public static int getReloadInterval(ItemStack weapon)
    {
        int interval = 10;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(weapon);
        if(enchantments.containsKey(ModEnchantments.QUICK_HANDS.get()))
        {
            interval /= 2;
        }
        return interval;
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.general.rate;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(weapon);
        if(enchantments.containsKey(ModEnchantments.TRIGGER_FINGER.get()))
        {
            float level = enchantments.get(ModEnchantments.TRIGGER_FINGER.get());
            float newRate = rate * (0.25F * level);
            rate -= MathHelper.clamp(newRate, 0, rate);
        }
        return rate;
    }
}
