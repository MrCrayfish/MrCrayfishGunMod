package com.tac.guns.util;

import com.tac.guns.common.Gun;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunModifierHelper
{
    private static final IGunModifier[] EMPTY = {};

    private static IGunModifier[] getModifiers(ItemStack weapon, IAttachment.Type type)
    {
        ItemStack stack = Gun.getAttachment(type, weapon);
        if(!stack.isEmpty())
        {
            if(stack.getItem() instanceof IAttachment)
            {
                IAttachment attachment = (IAttachment) stack.getItem();
                return attachment.getProperties().getModifiers();
            }
        }
        return EMPTY;
    }

    private static IGunModifier[] getModifiers(ItemStack weapon)
    {
        if(!weapon.isEmpty())
        {
            if(weapon.getItem() instanceof TimelessGunItem)
            {
                TimelessGunItem gunItem = (TimelessGunItem) weapon.getItem();
                return gunItem.getModifiers();
            }
        }
        return EMPTY;
    }

    public static int getModifiedProjectileLife(ItemStack weapon, int life)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                life = modifier.modifyProjectileLife(life);
            }
        }

        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            life = modifier.modifyProjectileLife(life);
        }
        return life;
    }

    public static double getModifiedProjectileGravity(ItemStack weapon, double gravity)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                gravity = modifier.modifyProjectileGravity(gravity);
            }
        }

        IGunModifier[] modifiersD = getModifiers(weapon);
        for(IGunModifier modifier : modifiersD)
        {
            gravity = modifier.modifyProjectileGravity(gravity);
        }

        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                gravity += modifier.additionalProjectileGravity();
            }
        }

        IGunModifier[] modifierD = getModifiers(weapon);
        for(IGunModifier modifier : modifierD)
        {
            gravity += modifier.additionalProjectileGravity();
        }
        return gravity;
    }

    public static float getModifiedSpread(ItemStack weapon, float spread)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                spread = modifier.modifyProjectileSpread(spread);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            spread = modifier.modifyProjectileSpread(spread);
        }
        return spread;
    }

    public static double getModifiedProjectileSpeed(ItemStack weapon, double speed)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                speed = modifier.modifyProjectileSpeed(speed);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            speed = modifier.modifyProjectileSpeed(speed);
        }
        return speed;
    }

    public static float getFireSoundVolume(ItemStack weapon)
    {
        float volume = 1.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                volume = modifier.modifyFireSoundVolume(volume);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            volume = modifier.modifyFireSoundVolume(volume);
        }
        return MathHelper.clamp(volume, 0.0F, 16.0F);
    }

    public static double getMuzzleFlashSize(ItemStack weapon, double size)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                size = modifier.modifyMuzzleFlashSize(size);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            size = modifier.modifyMuzzleFlashSize(size);
        }
        return size;
    }

    public static float getKickReduction(ItemStack weapon)
    {
        float kickReduction = 1.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                kickReduction *= MathHelper.clamp(modifier.kickModifier(), 0.0F, 1.0F);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            kickReduction *= MathHelper.clamp(modifier.kickModifier(), 0.0F, 1.0F);
        }
        return 1.0F - kickReduction;
    }

    public static float getRecoilModifier(ItemStack weapon)
    {
        float recoilReduction = 1.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                recoilReduction *= MathHelper.clamp(modifier.recoilModifier(), 0.0F, 1.0F);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            recoilReduction *= MathHelper.clamp(modifier.recoilModifier(), 0.0F, 1.0F);
        }
        return 1.0F - recoilReduction;
    }

    public static float getHorizontalRecoilModifier(ItemStack weapon)
    {
        float reduction = 1.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                reduction *= MathHelper.clamp(modifier.horizontalRecoilModifier(), 0.0F, 1.0F);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            reduction *= MathHelper.clamp(modifier.horizontalRecoilModifier(), 0.0F, 1.0F);
        }
        return 1.0F - reduction;
    }

    public static boolean isSilencedFire(ItemStack weapon)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                if(modifier.silencedFire())
                {
                    return true;
                }
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            if(modifier.silencedFire())
            {
                return true;
            }
        }
        return false;
    }

    public static double getModifiedFireSoundRadius(ItemStack weapon, double radius)
    {
        double minRadius = radius;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                double newRadius = modifier.modifyFireSoundRadius(radius);
                if(newRadius < minRadius)
                {
                    minRadius = newRadius;
                }
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            double newRadius = modifier.modifyFireSoundRadius(radius);
            if(newRadius < minRadius)
            {
                minRadius = newRadius;
            }
        }
        return MathHelper.clamp(minRadius, 0.0, Double.MAX_VALUE);
    }

    public static float getAdditionalDamage(ItemStack weapon)
    {
        float additionalDamage = 0.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                additionalDamage += modifier.additionalDamage();
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            additionalDamage += modifier.additionalDamage();
        }
        return additionalDamage;
    }

    public static float getAdditionalHeadshotDamage(ItemStack weapon)
    {
        float additionalDamage = 0.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                additionalDamage += modifier.additionalHeadshotDamage();
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            additionalDamage += modifier.additionalHeadshotDamage();
        }
        return additionalDamage;
    }

    public static float getModifiedProjectileDamage(ItemStack weapon, float damage)
    {
        float finalDamage = damage;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                finalDamage = modifier.modifyProjectileDamage(finalDamage);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            finalDamage = modifier.modifyProjectileDamage(finalDamage);
        }
        return finalDamage;
    }

    public static float getModifiedDamage(ItemStack weapon, Gun modifiedGun, float damage)
    {
        float finalDamage = damage;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                finalDamage = modifier.modifyProjectileDamage(finalDamage);
            }
        }
        IGunModifier[] modifiersD1 = getModifiers(weapon);
        for(IGunModifier modifier : modifiersD1)
        {
            finalDamage = modifier.modifyProjectileDamage(finalDamage);
        }
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                finalDamage += modifier.additionalDamage();
            }
        }
        IGunModifier[] modifiersD2 = getModifiers(weapon);
        for(IGunModifier modifier : modifiersD2)
        {
            finalDamage += modifier.additionalDamage();
        }
        return finalDamage;
    }

    public static double getModifiedAimDownSightSpeed(ItemStack weapon, double speed)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                speed = modifier.modifyAimDownSightSpeed(speed);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            speed = modifier.modifyAimDownSightSpeed(speed);
        }
        return MathHelper.clamp(speed, 0.01, Double.MAX_VALUE);
    }

    public static int getModifiedRate(ItemStack weapon, int rate)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                rate = modifier.modifyFireRate(rate);
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            rate = modifier.modifyFireRate(rate);
        }
        return MathHelper.clamp(rate, 0, Integer.MAX_VALUE);
    }

    public static float getCriticalChance(ItemStack weapon)
    {
        float chance = 0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                chance += modifier.criticalChance();
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            chance += modifier.criticalChance();
        }
        chance += GunEnchantmentHelper.getPuncturingChance(weapon);
        return MathHelper.clamp(chance, 0F, 1F);
    }

    public static float getAdditionalWeaponWeight(ItemStack weapon)
    {
        float additionalWeight = 0.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                additionalWeight += modifier.additionalWeaponWeight();
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            additionalWeight += modifier.additionalWeaponWeight();
        }
        return additionalWeight;
    }

    public static float getModifierOfWeaponWeight(ItemStack weapon)
    {
        float modifierWeight = 0.0F;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier[] modifiers = getModifiers(weapon, IAttachment.Type.values()[i]);
            for(IGunModifier modifier : modifiers)
            {
                modifierWeight += modifier.modifyWeaponWeight();
            }
        }
        IGunModifier[] modifiers = getModifiers(weapon);
        for(IGunModifier modifier : modifiers)
        {
            modifierWeight += modifier.modifyWeaponWeight();
        }
        return modifierWeight;
    }
}
