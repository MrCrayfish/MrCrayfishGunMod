package com.tac.guns.util;

import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.item.GunItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunEnchantmentHelper
{
    // UNSAFE... REMOVING DEFAULTS MAY CAUSE PROBLEMS, CURRENT SOLUTION FOR ADDONS TO INCORPORATE THEIR OWN UPGRADE TABLE ENTRIES
    public static ArrayList<Enchantment> enchs = new ArrayList<Enchantment>()
    {{
        add(ModEnchantments.ACCELERATOR.get());
        add(ModEnchantments.OVER_CAPACITY.get());
        add(ModEnchantments.PUNCTURING.get());
        add(ModEnchantments.RIFLING.get());
        add(ModEnchantments.BUFFERED.get());
        add(ModEnchantments.LIGHTWEIGHT.get());
    }};

    public static HashMap<Enchantment, UTR[]> upgradeableEnchs = new HashMap<Enchantment, UTR[]>()
    {{
        put(ModEnchantments.ACCELERATOR.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d)});
        put(ModEnchantments.OVER_CAPACITY.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d) });
        put(ModEnchantments.PUNCTURING.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d) });
        put(ModEnchantments.RIFLING.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d) });
        put(ModEnchantments.BUFFERED.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d) });
        put(ModEnchantments.LIGHTWEIGHT.get(), new UTR[]{ new UTR(1, 0.25d), new UTR(2, 0.5d), new UTR(3, 1d) });
    }};

    public static int getReloadInterval(ItemStack weapon)
    {
        int interval = ((GunItem)weapon.getItem()).getGun().getReloads().getinterReloadPauseTicks();//10;
        //int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.QUICK_HANDS.get(), weapon);
        //if(level > 0)
        //{
        //    interval -= 3 * level;
        //}
        return Math.max(interval, 1);
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.getGeneral().getRate();
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
        if(level > 0)
        {
            return 1.0 + (0.075 * level);
        }
        return 1;
    }
    public static float getSpreadModifier(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.RIFLING.get(), weapon);
        if(level > 0)
        {
            return 1.0f - (0.0333f * level);
        }
        return 1f;
    }
    public static float getWeightModifier(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LIGHTWEIGHT.get(), weapon);
        if(level > 0)
        {
            return 0.4f * level;
        }
        return 0f;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getReloads().isOpenBolt() ? modifiedGun.getReloads().getMaxAmmo() : modifiedGun.getReloads().getMaxAmmo()+1;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), weapon);
        if(level > 0 && level < modifiedGun.getReloads().getMaxAdditionalAmmoPerOC().length+1)
        {
            capacity += modifiedGun.getReloads().getMaxAdditionalAmmoPerOC()[level-1];
        }
        else if(level > 0)
        {
            capacity += (capacity / 2) * level-3;
        }
        return capacity;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return 1.0 + 0.075 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return damage + damage * (0.0875F * level);
        }
        return damage;
    }
    public static float getBufferedRecoil(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BUFFERED.get(), weapon);
        if(level > 0)
        {
            return (1-(0.03F * level));
        }
        return 1;
    }
    public static float getPuncturingChance(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PUNCTURING.get(), weapon);
        return level * 0.05F;
    }
}
