package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.interfaces.IGunModifier;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.item.ItemStack;

/**
 * Author: MrCrayfish
 */
public class GunModifiers
{
    public static final IGunModifier SILENCED = new IGunModifier()
    {
        @Override
        public boolean silencedFire()
        {
            return true;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.25;
        }
    };

    public static final IGunModifier REDUCED_DAMAGE = new IGunModifier()
    {
        @Override
        public float modifyProjectileDamage(ItemStack stack, Gun modifiedGun, float damage)
        {
            return damage * 0.75F;
        }
    };

    public static final IGunModifier BETTER_CONTROL = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.3F;
        }

        @Override
        public float kickModifier()
        {
            return 0.8F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.8F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.9F;
        }
    };

    public static final IGunModifier STABILISED = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.45F;
        }

        @Override
        public float kickModifier()
        {
            return 0.3F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.3F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.6F;
        }
    };

    public static final IGunModifier SUPER_STABILISED = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.1F;
        }

        @Override
        public float kickModifier()
        {
            return 0.1F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.15F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.4F;
        }
    };
}
