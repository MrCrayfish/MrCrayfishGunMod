package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.interfaces.IGunModifier;
import net.minecraft.util.Mth;

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
        public float modifyProjectileDamage(float damage)
        {
            return damage * 0.75F;
        }
    };

    public static final IGunModifier SLOW_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.95F;
        }
    };

    public static final IGunModifier SLOWER_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.9F;
        }
    };
    
    public static final IGunModifier SLOWEST_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.8F;
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
            return spread * 0.75F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.95F;
        }
    };

    public static final IGunModifier STABILISED = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.4F;
        }

        @Override
        public float kickModifier()
        {
            return 0.3F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.5F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.9F;
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
            return spread * 0.25F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.5F;
        }

        @Override
        public int modifyFireRate(int rate)
        {
            return Mth.clamp((int) (rate * 1.25), rate + 1, Integer.MAX_VALUE);
        }
    };

    public static final IGunModifier LIGHT_RECOIL = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.75F;
        }

        @Override
        public float kickModifier()
        {
            return 0.75F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.2F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.8F;
        }
    };

    public static final IGunModifier REDUCED_RECOIL = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.5F;
        }

        @Override
        public float kickModifier()
        {
            return 0.5F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.95F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.5F;
        }
    };
    
    public static final IGunModifier SNIPER_RECOIL = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.2F;
        }

        @Override
        public float kickModifier()
        {
            return 0.2F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.95F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.5F;
        }
        
        @Override
        public int modifyFireRate(int rate)
        {
            return Mth.clamp((int) (rate * 1.75), rate + 1, Integer.MAX_VALUE);
        }
    };
    
    public static final IGunModifier LASER = new IGunModifier()
    {
        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.1F;
        }
    };
}
