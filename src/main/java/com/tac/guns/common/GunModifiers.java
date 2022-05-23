package com.tac.guns.common;

import com.tac.guns.interfaces.IGunModifier;
import net.minecraft.util.math.MathHelper;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunModifiers
{
    // All statics below this point are used for weaponDefault status's

    public static final IGunModifier COYOTE_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.97F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.10F; }
    };
    public static final IGunModifier AIMPOINT_T1_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.925F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.10F; }
    };
    public static final IGunModifier EOTECH_N_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.93F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.35F; }
    };
    public static final IGunModifier VORTEX_UH_1_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.97F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.25F; }
    };
    public static final IGunModifier EOTECH_SHORT_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.98F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.30F; }
    };
    public static final IGunModifier SRS_RED_DOT_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.90F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.30F; }
    };
    public static final IGunModifier ACOG_4_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.88F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.425F; }
    };
    public static final IGunModifier QMK152_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.835F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.5F; }
    };
    public static final IGunModifier VORTEX_LPVO_1_6_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.85F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.65F; }
    };
    public static final IGunModifier LONGRANGE_8x_SCOPE_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.75F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.7F; }
    };

    public static final IGunModifier OLD_LONGRANGE_8x_SCOPE_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.625F;
        }
        @Override
        public float additionalWeaponWeight() { return 2.2F; }
    };
    public static final IGunModifier OLD_LONGRANGE_4x_SCOPE_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.75F;
        }
        @Override
        public float additionalWeaponWeight() { return 1.2F; }
    };

    public static final IGunModifier MINI_DOT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.975F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };
    public static final IGunModifier MICRO_HOLO_SIGHT_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.975F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };

    public static final IGunModifier TACTICAL_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.65F;
        }

        @Override
        public float kickModifier()
        {
            return 0.80F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.70F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.90F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.675F;
        }

        @Override
        public float additionalWeaponWeight() { return 0.35F; }

        @Override
        public float modifyWeaponWeight() { return 0.035F; }
    };
    public static final IGunModifier LIGHT_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.775F;
        }

        @Override
        public float kickModifier()
        {
            return 0.875F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.90F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.075F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.825F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.225F; }
    };
    public static final IGunModifier HEAVY_STOCK_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.475F;
        }

        @Override
        public float kickModifier()
        {
            return 0.625F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.725F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.73F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.35F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.575F; }
        @Override
        public float modifyWeaponWeight() { return 0.10F; }
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
            return spread * 0.925F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.2215F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.94F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.075F; }
    };
    public static final IGunModifier HEAVY_GRIP_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.85F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.875F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.855F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.875F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.15F; }
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
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.93F;
        }
        /*@Override
        public float additionalHeadshotDamage() {return 1.3F;}*/

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0F;
        }

        @Override
        public float additionalWeaponWeight() { return 0.5F; }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.825;
        }
    };
    public static final IGunModifier PISTOL_SILENCER = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.875;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.9125F;
        }
        @Override
        public boolean silencedFire()
        {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.35F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.125;
        }

        /*@Override
        public float additionalHeadshotDamage() {return 1.55F;}*/

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0F;
        }

        @Override
        public float additionalWeaponWeight() { return 0.375F; }
    };
    public static final IGunModifier MUZZLE_BRAKE_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.725F;
        }

        /*@Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 1.125F;
        }*/

        @Override
        public float horizontalRecoilModifier()
        {
            return 1.07F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.175F;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.15F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.2;
        }

        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };
    public static final IGunModifier MUZZLE_COMPENSATOR_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 1.05F;
        }

        /*@Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.75F;
        }*/

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.75F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.15F;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.05F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.05;
        }

        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };
}
