package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.particles.TrailData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class GunEnchantmentHelper
{
    public static ParticleOptions getParticle(ItemStack weapon)
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
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.QUICK_HANDS.get(), weapon);
        if(level > 0)
        {
            interval -= 3 * level;
        }
        return Math.max(interval, 1);
    }

    public static int getRate(ItemStack weapon, Gun modifiedGun)
    {
        int rate = modifiedGun.getGeneral().getRate();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.TRIGGER_FINGER.get(), weapon);
        if(level > 0)
        {
            float newRate = rate * (0.25F * level);
            rate -= Mth.clamp(newRate, 0, rate);
        }
        return rate;
    }

    public static double getAimDownSightSpeed(ItemStack weapon)
    {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTWEIGHT.get(), weapon);
        return level > 0 ? 1.5 : 1.0;
    }

    public static int getAmmoCapacity(ItemStack weapon, Gun modifiedGun)
    {
        int capacity = modifiedGun.getGeneral().getMaxAmmo();
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), weapon);
        if(level > 0)
        {
            capacity += Math.max(level, (capacity / 2) * level);
        }
        return capacity;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon)
    {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return 1.0 + 0.15 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage)
    {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if(level > 0)
        {
            return damage + damage * (0.1F * level);
        }
        return damage;
    }

    public static float getPuncturingChance(ItemStack weapon)
    {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.PUNCTURING.get(), weapon);
        return level * 0.05F;
    }
}
