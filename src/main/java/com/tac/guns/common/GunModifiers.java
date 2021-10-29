package com.tac.guns.common;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.math.MathHelper;

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
            return MathHelper.clamp((int) (rate * 1.25), rate + 1, Integer.MAX_VALUE);
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

    // Everything from above were statics for back in the CGM days

    public static final IGunModifier TACTICAL_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.30F;
        }

        @Override
        public float kickModifier()
        {
            return 0.80F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.50F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.90F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.25F;
        }
    };
    public static final IGunModifier LIGHT_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.45F;
        }

        @Override
        public float kickModifier()
        {
            return 0.90F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.70F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.05F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.35F;
        }
    };
    public static final IGunModifier HEAVY_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.25F;
        }

        @Override
        public float kickModifier()
        {
            return 0.60F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.30F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.45F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.15F;
        }
    };
    public static final IGunModifier TACTICAL_GRIP_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.90F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.90F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.25F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.90F;
        }
    };
    public static final IGunModifier HEAVY_GRIP_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.70F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.85F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.85F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.80F;
        }
    };
    public static final IGunModifier TACTICAL_SILENCER = new IGunModifier()
    {
        @Override
        public boolean silencedFire()
        {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.50F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.25;
        }

        @Override
        public float criticalChance()
        {
            return 0.075F;
        }
        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0.5F;
        }
    };
    public static final IGunModifier MUZZLE_BRAKE_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.60F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 1.20F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 1.105F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.15F;
        }
    };
    public static final IGunModifier MUZZLE_COMPENSATOR_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 1.15F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.90F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.55F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.15F;
        }
    };
}
