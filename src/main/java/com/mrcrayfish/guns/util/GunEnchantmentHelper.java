package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    public static int getReloadInterval(ItemStack weapon)
    {
        int interval = 10;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.QUICK_HANDS.get(), weapon);
        if(level > 0)
        {
            interval /= 2;
        }
        return interval;
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.general.rate;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.TRIGGER_FINGER.get(), weapon);
        if(level > 0)
        {
            float newRate = rate * (0.25F * level);
            rate -= MathHelper.clamp(newRate, 0, rate);
        }
        return rate;
    }

    public static double getAimDownSightSpeed(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LIGHTWEIGHT.get(), weapon);
        return level > 0 ? 1.5 : 1.0;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.general.maxAmmo;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), weapon);
        if(level > 0)
        {
            capacity += (capacity / 2) * level;
        }
        return capacity;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon, Gun modifiedGun)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return 1.0 + 0.15 * level;
        }
        return 1.0;
    }

    public static float getPuncturingDamage(ItemStack weapon, Random rand, float damage)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PUNCTURING.get(), weapon);
        if(rand.nextInt(5) == 0)
        {
            float modifier = 0.05F * level;
            return damage + damage * modifier;
        }
        return damage;
    }
}
