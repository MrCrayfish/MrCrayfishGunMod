package com.mrcrayfish.guns.interfaces;

import com.mrcrayfish.guns.common.GunModifiers;
import com.mrcrayfish.guns.item.attachment.impl.Barrel;

/**
 * An interface that allows control over the behaviour of weapons through attachments.
 * See {@link GunModifiers} for examples of how this can be implemented. Implementations can then
 * be passed to "create" method of attachment objects. See {@link Barrel#create(float, IGunModifier...)}
 * <p>
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

    default float modifyProjectileDamage(float damage)
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
        return gravity;
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

    default double modifyAimDownSightSpeed(double speed)
    {
        return speed;
    }

    default int modifyFireRate(int rate)
    {
        return rate;
    }
}
