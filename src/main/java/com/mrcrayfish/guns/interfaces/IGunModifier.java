package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.object.Gun;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IGunModifier
{
    IGunModifier DEFAULT = new IGunModifier() {};

    default boolean silencedFire()
    {
        return false;
    }

    default double modifySilencedFireSoundRadius(double radius)
    {
        return radius;
    }

    default float modifyProjectileDamage(ItemStack stack, Gun modifiedGun, float damage)
    {
        return damage;
    }
}
