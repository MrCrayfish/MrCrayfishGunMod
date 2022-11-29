package com.tac.guns.common;

import com.tac.guns.interfaces.IGunModifier;

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
    public static final IGunModifier ELCAN_DR_14X_ADS = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.75F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.725F; }
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
            return 0.675F;
        }

        @Override
        public float kickModifier()
        {
            return 0.9F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.9F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.90F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.6F;
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
            return 0.925F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.925F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.935F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.775F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.185F; }
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
            return 0.85F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.85F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.81F;
        }
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.45F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.575F; }
        @Override
        public float modifyWeaponWeight() { return 0.10F; }
    };
    public static final IGunModifier LIGHT_GRIP_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.925F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.975F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 1.125F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.98F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.05F; }
    };
    public static final IGunModifier TACTICAL_GRIP_MODIFIER = new IGunModifier()
    {
        @Override
        public float recoilModifier()
        {
            return 0.85F;
        }

        @Override
        public float modifyProjectileSpread(float spread)
        {
            return spread * 0.975F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed)
        {
            return speed * 0.975F;
        }

        @Override
        public float horizontalRecoilModifier()
        {
            return 0.875F;
        }
        @Override
        public float additionalWeaponWeight() { return 0.125F; }
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
            return volume * 0.60F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.275;
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
        public float additionalWeaponWeight() { return 0.575F; }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.8625;
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
            return 1.05F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.175F;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.25F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.3;
        }

        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };
    public static final IGunModifier MUZZLE_COMPENSATOR_MODIFIER = new IGunModifier()
    {
        @Override
        public float horizontalRecoilModifier()
        {
            return 0.8F;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 1.175F;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.075F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }

        @Override
        public float additionalWeaponWeight() { return 0.125F; }
    };

    //////////////////////// SPECIFICS PER WEAPON
    public static final IGunModifier DE_LISLE_MOD = new IGunModifier()
    {
        @Override
        public boolean silencedFire()
        {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.175F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.175;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.805;
        }
    };
    public static final IGunModifier M1_GARAND_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.125;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.735;
        }
    };
    public static final IGunModifier SIG_MCX_SPEAR_MOD = new IGunModifier()
    {
        @Override
        public boolean silencedFire()
        {
            return true;
        }

        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.375F;
        }

        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.325;
        }

        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0F;
        }

        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.75;
        }
    };
    public static final IGunModifier MP9_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {return speed*1.125;}
    };
    public static final IGunModifier SKS_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.125;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.85;
        }
    };
    public static final IGunModifier SKS_TAC_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.125;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.95;
        }
    };
    public static final IGunModifier M1014_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.25;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.925;
        }
    };
    public static final IGunModifier M249_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.75;
        }
    };
    public static final IGunModifier QBZ_191_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.85;
        }
    };

    public static final IGunModifier STEN_OSS_MOD = new IGunModifier()
    {
        @Override
        public boolean silencedFire()
        {
            return true;
        }
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.175F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.205;
        }
        @Override
        public double modifyMuzzleFlashSize(double size)
        {
            return size * 0F;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.85;
        }
    };
    public static final IGunModifier ESPADON_MOD = new IGunModifier()
    {
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.375F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.205;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.80;
        }
    };

    public static final IGunModifier M16A4_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.8;
        }
    };
    public static final IGunModifier SCAR_H_MOD = new IGunModifier()
    {
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.425F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.675;
        }
    };
    public static final IGunModifier SCAR_L_MOD = new IGunModifier()
    {
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.125F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.05;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.775;
        }
    };
    public static final IGunModifier SPR_15_MOD = new IGunModifier()
    {
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 0.925F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.1;
        }
    };
    public static final IGunModifier MK47_MOD = new IGunModifier()
    {
        @Override
        public float modifyFireSoundVolume(float volume)
        {
            return volume * 1.35F;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.25;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.885;
        }
    };

    public static final IGunModifier AK47_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.9;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.02;
        }
    };

    public static final IGunModifier M60_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.35;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.65;
        }
    };

    public static final IGunModifier DP28_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.35;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.5;
        }
    };
    public static final IGunModifier VECTOR_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.9;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.15;
        }
        @Override
        public double additionalProjectileGravity() {
            return -0.025;
        }
    };
    public static final IGunModifier AA_12_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.725;
        }
    };

    public static final IGunModifier M870_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.85;
        }
    };

    public static final IGunModifier DEAGLE50_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.35;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.85;
        }
        @Override
        public double additionalProjectileGravity() {
            return -0.025;
        }
    };

    public static final IGunModifier MP5A5_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.225;
        }
        @Override
        public double additionalProjectileGravity() {
            return 0.015;
        }
    };
    public static final IGunModifier HK416_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.075;
        }
        @Override
        public double additionalProjectileGravity() {
            return -0.015;
        }
    };

    public static final IGunModifier MK18_MOD1_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.125;
        }
        @Override
        public double additionalProjectileGravity() {
            return -0.01;
        }
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 0.875;
        }
    };
    public static final IGunModifier TYPE81x_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.15;
        }

        @Override
        public double additionalProjectileGravity() {
            return -0.02;
        }
    };
    public static final IGunModifier MP7_MOD = new IGunModifier()
    {
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*1.225;
        }
        @Override
        public double additionalProjectileGravity() {
            return 0.02;
        }
    };

    public static final IGunModifier AIAWP_MOD = new IGunModifier()
    {
        @Override
        public double modifyFireSoundRadius(double radius)
        {
            return radius * 1.5;
        }
        @Override
        public double modifyAimDownSightSpeed(double speed) {
            return speed*0.8;
        }
        @Override
        public double additionalProjectileGravity() {
            return -0.01;
        }
    };
}
