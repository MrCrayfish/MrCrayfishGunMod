package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.init.ModParticleTypes;
import com.mrcrayfish.guns.particles.TrailData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    public static IParticleData getParticle(ItemStack weapon)
    {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(weapon);
        if(enchantments.containsKey(ModEnchantments.FIRE_STARTER.get()))
        {
            return ParticleTypes.LAVA;
        }
        else if(enchantments.containsKey(ModEnchantments.PUNCTURING.get()))
        {
            return ParticleTypes.ENCHANTED_HIT;
        }
        return new TrailData(weapon.isEnchanted());
    }

    public static int getReloadInterval(ItemStack weapon)
    {
        int interval = 10;
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.QUICK_HANDS.get(), weapon);
        if(level > 0)
        {
            interval -= 3 * level;
        }
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
        return level > 0 ? 1.5 : 1.0;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getGeneral().getMaxAmmo();
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), weapon);
        if(level > 0)
        {
            capacity += (capacity / 2) * level;
        }
        return capacity;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return 1.0 + 0.15 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return damage + damage * (0.1F * level);
        }
        return damage;
    }

    public static float getPuncturingChance(ItemStack weapon)
    {
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PUNCTURING.get(), weapon);
        return level * 0.05F;
    }
}
