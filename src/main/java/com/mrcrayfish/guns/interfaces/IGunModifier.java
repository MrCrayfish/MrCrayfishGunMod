package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.object.Gun;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public interface IGunModifier
{
    default float modifyFireSoundVolume(float volume)
    {
        return volume;
    }

    default boolean silencedFire()
    {
        return false;
    }

    default double modifyFireSoundRadius(double radius)
    {
        return radius;
    }

    default float additionalDamage()
    {
        return 0.0F;
    }

    default float modifyProjectileDamage(ItemStack stack, Gun modifiedGun, float damage)
    {
        return damage;
    }

    default double modifyProjectileSpeed(double speed)
    {
        return speed;
    }

    default float modifyProjectileSpread(float spread)
    {
        return spread;
    }

    default double additionalProjectileGravity()
    {
        return 0;
    }

    default double modifyProjectileGravity(double gravity)
    {
        return 1.0;
    }

    default int modifyProjectileLife(int life)
    {
        return life;
    }

    default float recoilModifier()
    {
        return 1.0F;
    }

    default float kickModifier()
    {
        return 1.0F;
    }

    default double modifyMuzzleFlashSize(double size)
    {
        return size;
    }
}
