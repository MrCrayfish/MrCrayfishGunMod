package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.interfaces.IGunModifier;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

/**
 * Author: MrCrayfish
 */
public class GunModifierHelper
{
    private static IGunModifier getModifier(ItemStack weapon, IAttachment.Type type)
    {
        ItemStack stack = Gun.getAttachment(type, weapon);
        if(!stack.isEmpty() && stack.getItem() instanceof IAttachment)
        {
            IAttachment attachment = (IAttachment) stack.getItem();
            return attachment.getProperties().getModifier();
        }
        return IGunModifier.DEFAULT;
    }

    public static boolean isSilencedFire(ItemStack weapon)
    {
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier modifier = getModifier(weapon, IAttachment.Type.values()[i]);
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
            IGunModifier modifier = getModifier(weapon, IAttachment.Type.values()[i]);
            double newRadius = modifier.modifySilencedFireSoundRadius(radius);
            if(newRadius < minRadius)
            {
                minRadius = newRadius;
            }
        }
        return MathHelper.clamp(minRadius, 0.0, Double.MAX_VALUE);
    }

    public static float getModifiedDamage(ItemStack weapon, Gun modifiedGun, float damage)
    {
        float finalDamage = damage;
        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            IGunModifier modifier = getModifier(weapon, IAttachment.Type.values()[i]);
            float newDamage = modifier.modifyProjectileDamage(weapon, modifiedGun, damage);
            finalDamage += (newDamage - damage);
        }
        return finalDamage;
    }
}
