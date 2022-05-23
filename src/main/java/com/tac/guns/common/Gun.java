package com.tac.guns.common;

import com.tac.guns.Reference;
import com.tac.guns.annotation.Ignored;
import com.tac.guns.annotation.Optional;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.item.attachment.IScope;
import com.tac.guns.item.attachment.impl.Scope;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

import javax.annotation.Nullable;
import java.util.ArrayList;


public final class Gun implements INBTSerializable<CompoundNBT>
{
    private General general = new General();
    private Reloads reloads = new Reloads();
    private Projectile projectile = new Projectile();
    private Sounds sounds = new Sounds();
    private Display display = new Display();
    private Modules modules = new Modules();

    public General getGeneral()
    {
        return this.general;
    }

    public Reloads getReloads()
    {
        return this.reloads;
    }

    public Projectile getProjectile()
    {
        return this.projectile;
    }

    public Sounds getSounds()
    {
        return this.sounds;
    }

    public Display getDisplay()
    {
        return this.display;
    }

    public Modules getModules()
    {
        return this.modules;
    }

    public static class General implements INBTSerializable<CompoundNBT>
    {
        @Optional
        private boolean auto = false;
        @Optional
        private boolean boltAction = false;
        @Optional
        private int rate;
        @Optional
        private int[] rateSelector = new int[]{0,1};
        @Ignored
        private GripType gripType = GripType.ONE_HANDED;
        @Optional
        private float recoilAngle = 1.0F;
        @Optional
        private float recoilKick;
        @Optional
        private float horizontalRecoilAngle = 2.0F;
        @Optional
        private float cameraRecoilModifier = 1.75F; // How much to divide out of camera recoil, use for either softening camera shake while keeping high recoil feeling weapons
        @Optional
        private float recoilDuration = 0.25F;
        @Optional
        private float weaponRecoilDuration = 0.5F; // Recoil up until the weapon cooldown is under this value (0.1 == 10% recoil time left, use to help scale with high firerate weapons and their weapon recoil feel)
        @Optional
        private float recoilAdsReduction = 0.2F;
        @Optional
        private int projectileAmount = 1;
        @Optional
        private boolean alwaysSpread = true;
        @Optional
        private float spread;
        @Optional
        private float weightKilo = 0.0F;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean("Auto", this.auto);
            tag.putBoolean("BoltAction", this.boltAction);
            tag.putInt("Rate", this.rate);
            tag.putIntArray("RateSelector", this.rateSelector);
            tag.putString("GripType", this.gripType.getId().toString());
            tag.putFloat("RecoilAngle", this.recoilAngle); // x2 for quick camera recoil reduction balancing
            tag.putFloat("RecoilKick", this.recoilKick/1.5f);
            tag.putFloat("HorizontalRecoilAngle", this.horizontalRecoilAngle/1.5f); // x2 for quick camera recoil reduction balancing
            tag.putFloat("CameraRecoilModifier", this.cameraRecoilModifier);
            tag.putFloat("RecoilDurationOffset", this.recoilDuration);
            tag.putFloat("WeaponRecoilDuration", this.weaponRecoilDuration);
            tag.putFloat("RecoilAdsReduction", this.recoilAdsReduction);
            tag.putInt("ProjectileAmount", this.projectileAmount);
            tag.putBoolean("AlwaysSpread", this.alwaysSpread);
            tag.putFloat("Spread", this.spread);
            tag.putFloat("WeightKilo", this.weightKilo);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Auto", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.auto = tag.getBoolean("Auto");
            }
            if(tag.contains("BoltAction", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.boltAction = tag.getBoolean("BoltAction");
            }
            if(tag.contains("Rate", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.rate = tag.getInt("Rate");
            }
            if(tag.contains("RateSelector", Constants.NBT.TAG_INT_ARRAY))
            {
                this.rateSelector = tag.getIntArray("RateSelector");
            }
            if(tag.contains("GripType", Constants.NBT.TAG_STRING))
            {
                this.gripType = GripType.getType(ResourceLocation.tryCreate(tag.getString("GripType")));
            }
            if(tag.contains("RecoilAngle", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilAngle = tag.getFloat("RecoilAngle");
            }
            if(tag.contains("RecoilKick", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilKick = tag.getFloat("RecoilKick");
            }
            if(tag.contains("HorizontalRecoilAngle", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.horizontalRecoilAngle = tag.getFloat("HorizontalRecoilAngle");
            }
            if(tag.contains("CameraRecoilModifier", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.cameraRecoilModifier = tag.getFloat("CameraRecoilModifier");
            }
            if(tag.contains("RecoilDurationOffset", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilDuration = tag.getFloat("RecoilDurationOffset");
            }
            if(tag.contains("WeaponRecoilDuration", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.weaponRecoilDuration = tag.getFloat("WeaponRecoilDuration");
            }
            if(tag.contains("RecoilAdsReduction", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilAdsReduction = tag.getFloat("RecoilAdsReduction");
            }
            if(tag.contains("ProjectileAmount", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.projectileAmount = tag.getInt("ProjectileAmount");
            }
            if(tag.contains("AlwaysSpread", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.alwaysSpread = tag.getBoolean("AlwaysSpread");
            }
            if(tag.contains("Spread", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.spread = tag.getFloat("Spread");
            }
            if(tag.contains("WeightKilo", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.weightKilo = tag.getFloat("WeightKilo");
            }
        }

        /**
         * @return A copy of the general get
         */
        public General copy()
        {
            General general = new General();
            general.auto = this.auto;
            general.boltAction = this.boltAction;
            general.rate = this.rate;
            general.rateSelector = this.rateSelector;
            general.gripType = this.gripType;
            general.recoilAngle = this.recoilAngle;
            general.recoilKick = this.recoilKick;
            general.horizontalRecoilAngle = this.horizontalRecoilAngle;
            general.cameraRecoilModifier = this.cameraRecoilModifier;
            general.recoilDuration = this.recoilDuration;
            general.weaponRecoilDuration = this.weaponRecoilDuration;
            general.recoilAdsReduction = this.recoilAdsReduction;
            general.projectileAmount = this.projectileAmount;
            general.alwaysSpread = this.alwaysSpread;
            general.spread = this.spread;
            general.weightKilo = this.weightKilo;
            return general;
        }

        /**
         * @return If this gun is automatic or not
         */
        public boolean isAuto()
        {
            return this.auto;
        }

        /**
         * @return If the gun exits aim during Cooldown
         */
        public boolean isBoltAction()
        {
            return this.boltAction;
        }

        /**
         * @return The fire rate of this weapon in ticks
         */
        public int getRate()
        {
            return this.rate;
        }

        /**
         * @return The fire modes supported by the weapon, [0,1,2,3,4,5] [Safety, Single, Three round burst, Auto, Special 1, Special 2]
         */
        public int[] getRateSelector()
        {
            return this.rateSelector;
        }

        /**
         * @return The type of grip this weapon uses
         */
        public GripType getGripType()
        {
            return this.gripType;
        }

        /**
         * @return The amount of recoil this gun produces upon firing in degrees
         */
        public float getRecoilAngle()
        {
            return this.recoilAngle;
        }

        /**
         * @return The amount of kick this gun produces upon firing
         */
        public float getRecoilKick()
        {
            return this.recoilKick;
        }

        /**
         * @return The amount of horizontal kick this gun produces upon firing
         */
        public float getHorizontalRecoilAngle()
        {
            return this.horizontalRecoilAngle;
        }

        /**
         * @return How much to divide out of camera recoil, use for either softening camera shake while keeping high recoil feeling weapons
         */
        public float getCameraRecoilModifier()
        {
            return this.cameraRecoilModifier;
        }

        /**
         * @return The duration offset for recoil. This reduces the duration of recoil animation
         */
        public float getRecoilDuration()
        {
            return this.recoilDuration;
        }

        /**
         * @return Recoil (the weapon) up until the weapon cooldown is under this value (0.1 == 10% recoil time left, use to help scale with high firerate weapons and their weapon recoil feel)
         */
        public float getWeaponRecoilDuration()
        {
            return this.weaponRecoilDuration;
        }

        /**
         * @return The amount of reduction applied when aiming down this weapon's sight
         */
        public float getRecoilAdsReduction()
        {
            return this.recoilAdsReduction;
        }

        /**
         * @return The amount of projectiles this weapon fires
         */
        public int getProjectileAmount()
        {
            return this.projectileAmount;
        }

        /**
         * @return If this weapon should always spread it's projectiles according to {@link #getSpread()}
         */
        public boolean isAlwaysSpread()
        {
            return this.alwaysSpread;
        }

        /**
         * @return The maximum amount of degrees applied to the initial pitch and yaw direction of
         * the fired projectile.
         */
        public float getSpread()
        {
            return this.spread;
        }

        /**
         * @return The maximum amount of degrees applied to the initial pitch and yaw direction of
         * the fired projectile.
         */
        public float getWeightKilo()
        {
            return this.weightKilo;//*1.25f;
        }
    }

    public static class Reloads implements INBTSerializable<CompoundNBT>
    {
        private int maxAmmo = 20;
        @Optional
        private boolean magFed = false;
        @Optional
        private int reloadMagTimer = 20;
        @Optional
        private int additionalReloadEmptyMagTimer = 0;
        @Optional
        private int reloadAmount = 1;
        @Optional
        private int[] maxAdditionalAmmoPerOC = new int[]{};
      /*@Optional                                          Impl at some point, allow additional reload times per OC (OverCap)
        private int[] maxAdditionalAmmoPerOC = new int[]{};*/
        @Optional
        private int preReloadPauseTicks = 0;
        @Optional
        private int interReloadPauseTicks = 1;
        @Optional
        private boolean openBolt = false;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("MaxAmmo", this.maxAmmo);
            tag.putBoolean("MagFed", this.magFed);
            tag.putInt("ReloadSpeed", this.reloadAmount);
            tag.putInt("ReloadMagTimer", this.reloadMagTimer);
            tag.putInt("AdditionalReloadEmptyMagTimer", this.additionalReloadEmptyMagTimer);
            tag.putIntArray("MaxAmmunitionPerOverCap", this.maxAdditionalAmmoPerOC);
            tag.putInt("ReloadPauseTicks", this.preReloadPauseTicks);
            tag.putInt("InterReloadPauseTicks", this.interReloadPauseTicks);
            tag.putBoolean("OpenBolt", this.openBolt);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("MaxAmmo", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.maxAmmo = tag.getInt("MaxAmmo");
            }
            if(tag.contains("MagFed", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.magFed = tag.getBoolean("MagFed");
            }
            if(tag.contains("ReloadSpeed", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.reloadAmount = tag.getInt("ReloadSpeed");
            }
            if(tag.contains("ReloadMagTimer", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.reloadMagTimer = tag.getInt("ReloadMagTimer");
            }
            if(tag.contains("AdditionalReloadEmptyMagTimer", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.additionalReloadEmptyMagTimer = tag.getInt("AdditionalReloadEmptyMagTimer");
            }
            if(tag.contains("MaxAmmunitionPerOverCap", Constants.NBT.TAG_INT_ARRAY))
            {
                this.maxAdditionalAmmoPerOC = tag.getIntArray("MaxAmmunitionPerOverCap");
            }
            if(tag.contains("ReloadPauseTicks", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.preReloadPauseTicks = tag.getInt("ReloadPauseTicks");
            }
            if(tag.contains("InterReloadPauseTicks", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.interReloadPauseTicks = tag.getInt("InterReloadPauseTicks");
            }
            if(tag.contains("OpenBolt", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.openBolt = tag.getBoolean("OpenBolt");
            }
        }

        /**
         * @return A copy of the general get
         */
        public Reloads copy()
        {
            Reloads reloads = new Reloads();
            reloads.magFed = this.magFed;
            reloads.maxAmmo = this.maxAmmo;
            reloads.reloadAmount = this.reloadAmount;
            reloads.reloadMagTimer = this.reloadMagTimer;
            reloads.additionalReloadEmptyMagTimer = this.additionalReloadEmptyMagTimer;
            reloads.maxAdditionalAmmoPerOC = this.maxAdditionalAmmoPerOC;
            reloads.preReloadPauseTicks = this.preReloadPauseTicks;
            reloads.interReloadPauseTicks = this.interReloadPauseTicks;
            reloads.openBolt = this.openBolt;
            return reloads;
        }

        /**
         * @return Does this gun reload all ammunition following a single timer and replenish
         */
        public boolean isMagFed() {return this.magFed;}
        /**
         * @return The maximum amount of ammo this weapon can hold
         */
        public int getMaxAmmo()
        {
            return this.maxAmmo;
        }
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int getReloadAmount()
        {
            return this.reloadAmount;
        }
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int getReloadMagTimer()
        {
            return this.reloadMagTimer;
        }
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int getAdditionalReloadEmptyMagTimer()
        {
            return this.additionalReloadEmptyMagTimer;
        }
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int[] getMaxAdditionalAmmoPerOC() {return this.maxAdditionalAmmoPerOC;}
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int getPreReloadPauseTicks()
        {
            return this.preReloadPauseTicks;
        }
        /**
         * @return The amount of ammo to add to the weapon each reload cycle
         */
        public int getinterReloadPauseTicks() {return this.interReloadPauseTicks;}
        /**
         * @return Does this gun reload all ammunition following a single timer and replenish
         */
        public boolean isOpenBolt()
        {
            return this.openBolt;
        }
    }

    public static class Projectile implements INBTSerializable<CompoundNBT>
    {
        private ResourceLocation item = new ResourceLocation(Reference.MOD_ID, "basic_ammo");
        @Optional
        private boolean visible = true;
        @Optional
        private float damage;
        @Optional
        private float size;
        @Optional
        private double speed;
        @Optional
        private int life;
        @Optional
        private boolean gravity = true;
        @Optional
        private boolean damageReduceOverLife = true;
        @Optional
        private int trailColor = 0xFFD289;
        @Optional
        private double trailLengthMultiplier = 1;
        @Optional
        private boolean ricochet = true;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Item", this.item.toString());
            tag.putBoolean("Visible", this.visible);
            tag.putFloat("Damage", this.damage);
            tag.putFloat("Size", this.size);
            tag.putDouble("Speed", this.speed);
            tag.putInt("Life", this.life);
            tag.putBoolean("Gravity", this.gravity);
            tag.putBoolean("DamageReduceOverLife", this.damageReduceOverLife);
            tag.putInt("TrailColor", this.trailColor);
            tag.putDouble("TrailLengthMultiplier", this.trailLengthMultiplier);
            tag.putBoolean("Ricochet", this.ricochet);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Item", Constants.NBT.TAG_STRING))
            {
                this.item = new ResourceLocation(tag.getString("Item"));
            }
            if(tag.contains("Visible", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.visible = tag.getBoolean("Visible");
            }
            if(tag.contains("Damage", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.damage = tag.getFloat("Damage");
            }
            if(tag.contains("Size", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.size = tag.getFloat("Size");
            }
            if(tag.contains("Speed", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.speed = tag.getDouble("Speed");
            }
            if(tag.contains("Life", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.life = tag.getInt("Life");
            }
            if(tag.contains("Gravity", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.gravity = tag.getBoolean("Gravity");
            }
            if(tag.contains("DamageReduceOverLife", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.damageReduceOverLife = tag.getBoolean("DamageReduceOverLife");
            }
            if(tag.contains("TrailColor", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.trailColor = tag.getInt("TrailColor");
            }
            if(tag.contains("TrailLengthMultiplier", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.trailLengthMultiplier = tag.getDouble("TrailLengthMultiplier");
            }
            if(tag.contains("Ricochet", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.ricochet = tag.getBoolean("Ricochet");
            }
        }

        public Projectile copy()
        {
            Projectile projectile = new Projectile();
            projectile.item = this.item;
            projectile.visible = this.visible;
            projectile.damage = this.damage;
            projectile.size = this.size;
            projectile.speed = this.speed;
            projectile.life = this.life;
            projectile.gravity = this.gravity;
            projectile.damageReduceOverLife = this.damageReduceOverLife;
            projectile.trailColor = this.trailColor;
            projectile.trailLengthMultiplier = this.trailLengthMultiplier;
            projectile.ricochet = this.ricochet;
            return projectile;
        }

        /**
         * @return The registry id of the ammo item
         */
        public ResourceLocation getItem()
        {
            return this.item;
        }

        /**
         * @return If this projectile should be visible when rendering
         */
        public boolean isVisible()
        {
            return this.visible;
        }

        /**
         * @return The damage caused by this projectile
         */
        public float getDamage()
        {
            return this.damage;
        }

        /**
         * @return The size of the projectile entity bounding box
         */
        public float getSize()
        {
            return this.size;
        }

        /**
         * @return The speed the projectile moves every tick
         */
        public double getSpeed()
        {
            return this.speed;
        }

        /**
         * @return The amount of ticks before this projectile is removed
         */
        public int getLife()
        {
            return this.life;
        }

        /**
         * @return If gravity should be applied to the projectile
         */
        public boolean isGravity()
        {
            return this.gravity;
        }

        /**
         * @return If the damage should reduce the further the projectile travels
         */
        public boolean isDamageReduceOverLife()
        {
            return this.damageReduceOverLife;
        }

        /**
         * @return The color of the projectile trail in rgba integer format
         */
        public int getTrailColor()
        {
            return this.trailColor;
        }

        /**
         * @return The multiplier to change the length of the projectile trail
         */
        public double getTrailLengthMultiplier()
        {
            return this.trailLengthMultiplier;
        }
        /**
         * @return If the bullet will bounce off of hard blocks
         */
        public boolean isRicochet()
        {
            return this.ricochet;
        }
    }

    public static class Sounds implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        private ResourceLocation fire;
        @Optional
        @Nullable
        private ResourceLocation reload;
        @Optional
        @Nullable
        private ResourceLocation cock;
        @Optional
        @Nullable
        private ResourceLocation silencedFire;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            if(this.fire != null)
            {
                tag.putString("Fire", this.fire.toString());
            }
            if(this.reload != null)
            {
                tag.putString("Reload", this.reload.toString());
            }
            if(this.cock != null)
            {
                tag.putString("Cock", this.cock.toString());
            }
            if(this.silencedFire != null)
            {
                tag.putString("SilencedFire", this.silencedFire.toString());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Fire", Constants.NBT.TAG_STRING))
            {
                this.fire = this.createSound(tag, "Fire");
            }
            if(tag.contains("Reload", Constants.NBT.TAG_STRING))
            {
                this.reload = this.createSound(tag, "Reload");
            }
            if(tag.contains("Cock", Constants.NBT.TAG_STRING))
            {
                this.cock = this.createSound(tag, "Cock");
            }
            if(tag.contains("SilencedFire", Constants.NBT.TAG_STRING))
            {
                this.silencedFire = this.createSound(tag, "SilencedFire");
            }
        }

        public Sounds copy()
        {
            Sounds sounds = new Sounds();
            sounds.fire = this.fire;
            sounds.reload = this.reload;
            sounds.cock = this.cock;
            sounds.silencedFire = this.silencedFire;
            return sounds;
        }

        @Nullable
        private ResourceLocation createSound(CompoundNBT tag, String key)
        {
            String sound = tag.getString(key);
            return sound.isEmpty() ? null : new ResourceLocation(sound);
        }

        /**
         * @return The registry id of the sound event when firing this weapon
         */
        @Nullable
        public ResourceLocation getFire()
        {
            return this.fire;
        }

        /**
         * @return The registry iid of the sound event when reloading this weapon
         */
        @Nullable
        public ResourceLocation getReload()
        {
            return this.reload;
        }

        /**
         * @return The registry iid of the sound event when cocking this weapon
         */
        @Nullable
        public ResourceLocation getCock()
        {
            return this.cock;
        }

        /**
         * @return The registry iid of the sound event when silenced firing this weapon
         */
        @Nullable
        public ResourceLocation getSilencedFire()
        {
            return this.silencedFire;
        }
    }

    public static class Display implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        private Flash flash;

        @Optional
        private int weaponType = 0;

        @Optional
        @Nullable
        private float hipfireScale = 0.75F;

        @Optional
        @Nullable
        private float hipfireMoveScale = 0.5F;

        @Optional
        @Nullable
        private float hipfireRecoilScale = 1.0F;

        @Optional
        @Nullable
        private boolean showDynamicHipfire = true;

        @Nullable
        public float getHipfireScale()
        {
            return this.hipfireScale;
        }

        @Nullable
        public float getHipfireMoveScale()
        {
            return this.hipfireMoveScale;
        }

        @Nullable
        public float getHipfireRecoilScale()
        {
            return this.hipfireRecoilScale;
        }

        @Nullable
        public boolean isDynamicHipfire()
        {
            return this.showDynamicHipfire;
        }
        @Nullable
        public int getWeaponType()
        {
            return this.weaponType;
        }

        @Nullable
        public Flash getFlash()
        {
            return this.flash;
        }

        public static class Flash extends Positioned
        {
            private double size = 0.5;
            private double smokeSize = 2.0;

            @Override
            public CompoundNBT serializeNBT()
            {
                CompoundNBT tag = super.serializeNBT();
                tag.putDouble("Size", this.size);
                tag.putDouble("SmokeSize", this.smokeSize);
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundNBT tag)
            {
                super.deserializeNBT(tag);
                if(tag.contains("Size", Constants.NBT.TAG_ANY_NUMERIC))
                {
                    this.size = tag.getDouble("Size");
                }
                if(tag.contains("SmokeSize", Constants.NBT.TAG_ANY_NUMERIC))
                {
                    this.smokeSize = tag.getDouble("SmokeSize");
                }
            }

            public Flash copy()
            {
                Flash flash = new Flash();
                flash.size = this.size;
                flash.smokeSize = this.smokeSize;
                flash.xOffset = this.xOffset;
                flash.yOffset = this.yOffset;
                flash.zOffset = this.zOffset;
                return flash;
            }

            /**
             * @return The size/scale of the muzzle flash render
             */
            public double getSize()
            {
                return this.size;
            }

            /**
             * @return The size/scale of the muzzle smoke render
             */
            public double getSmokeSize() { return this.smokeSize;}
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            if(this.flash != null)
            {
                tag.put("Flash", this.flash.serializeNBT());
            }
            tag.putFloat("HipFireScale", this.hipfireScale);
            tag.putFloat("HipFireMoveScale", this.hipfireMoveScale);
            tag.putFloat("HipFireRecoilScale", this.hipfireRecoilScale/2); // Compensate for camera changes
            tag.putBoolean("ShowDynamicHipfire", this.showDynamicHipfire);
            tag.putInt("WeaponType", this.weaponType);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Flash", Constants.NBT.TAG_COMPOUND))
            {
                CompoundNBT flashTag = tag.getCompound("Flash");
                if(!flashTag.isEmpty())
                {
                    Flash flash = new Flash();
                    flash.deserializeNBT(tag.getCompound("Flash"));
                    this.flash = flash;
                }
                else
                {
                    this.flash = null;
                }
            }
            if(tag.contains("HipFireScale", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.hipfireScale = tag.getFloat("HipFireScale");
            }
            if(tag.contains("HipFireMoveScale", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.hipfireMoveScale = tag.getFloat("HipFireMoveScale");
            }
            if(tag.contains("HipFireRecoilScale", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.hipfireRecoilScale = tag.getFloat("HipFireRecoilScale");
            }
            if(tag.contains("ShowDynamicHipfire", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.showDynamicHipfire = tag.getBoolean("ShowDynamicHipfire");
            }
            if(tag.contains("WeaponType", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.weaponType = tag.getInt("WeaponType");
            }
        }

        public Display copy()
        {
            Display display = new Display();
            if(flash != null)
            {
                display.flash = flash.copy();
            }
            if(hipfireScale != 0)
            {
                display.hipfireScale = this.hipfireScale;
            }
            if(hipfireMoveScale != 0)
            {
                display.hipfireMoveScale = this.hipfireMoveScale;
            }
            if(hipfireRecoilScale != 0)
            {
                display.hipfireRecoilScale = this.hipfireRecoilScale;
            }
            if(weaponType != 0)
            {
                display.weaponType = this.weaponType;
            }

            // Should always contain a value, true or false, does not check for empty
            display.hipfireRecoilScale = this.hipfireRecoilScale;

            return display;
        }
    }

    public static class Modules implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        private Zoom[] zoom = new Zoom[]{};

        private Attachments attachments = new Attachments();

        @Ignored
        private int zoomOptions;

        @Nullable

        public Zoom[] getZoom()
        {
            return this.zoom;
        }

        public Attachments getAttachments()
        {
            return this.attachments;
        }

        public int getZoomOptions(){return this.zoomOptions;}

        public static class Zoom extends Positioned
        {
            @Optional
            private float fovModifier;

            @Optional
            private double stabilityOffset = 0.225;

            @Override
            public CompoundNBT serializeNBT()
            {
                CompoundNBT tag = super.serializeNBT();
                tag.putFloat("FovModifier", this.fovModifier);
                tag.putDouble("StabilityOffset", this.stabilityOffset);
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundNBT tag)
            {
                super.deserializeNBT(tag);
                if(tag.contains("FovModifier", Constants.NBT.TAG_ANY_NUMERIC))
                {
                    this.fovModifier = tag.getFloat("FovModifier");
                }
                if(tag.contains("StabilityOffset", Constants.NBT.TAG_ANY_NUMERIC))
                {
                    this.stabilityOffset = tag.getDouble("StabilityOffset");
                }
            }

            public Zoom copy()
            {
                Zoom zoom = new Zoom();
                zoom.fovModifier = this.fovModifier;
                zoom.stabilityOffset = this.stabilityOffset;
                zoom.xOffset = this.xOffset;
                zoom.yOffset = this.yOffset;
                zoom.zOffset = this.zOffset;
                return zoom;
            }

            public float getFovModifier()
            {
                return this.fovModifier;
            }
            public double getStabilityOffset()
            {
                return this.stabilityOffset;
            }
        }


        public static class Attachments implements INBTSerializable<CompoundNBT>
        {
            @Optional
            @Nullable
            private ScaledPositioned scope;
            @Optional
            @Nullable
            private ScaledPositioned barrel;
            @Optional
            @Nullable
            private ScaledPositioned stock;
            @Optional
            @Nullable
            private ScaledPositioned underBarrel;
            @Optional
            @Nullable
            private ScaledPositioned sideRail;
            @Optional
            @Nullable
            private ScaledPositioned oldScope;
            @Optional
            @Nullable
            private PistolScope pistolScope;
            @Optional
            @Nullable
            private ScaledPositioned pistolBarrel;

            @Nullable
            public ScaledPositioned getScope()
            {
                return this.scope;
            }

            @Nullable
            public ScaledPositioned getBarrel()
            {
                return this.barrel;
            }

            @Nullable
            public ScaledPositioned getStock()
            {
                return this.stock;
            }

            @Nullable
            public ScaledPositioned getUnderBarrel()
            {
                return this.underBarrel;
            }
            @Nullable
            public ScaledPositioned getSideRail()
            {
                return this.sideRail;
            }
            @Nullable
            public ScaledPositioned getOldScope()
            {
                return this.oldScope;
            }
            @Nullable
            public PistolScope getPistolScope()
            {
                return this.pistolScope;
            }
            @Nullable
            public ScaledPositioned getPistolBarrel()
            {
                return this.pistolBarrel;
            }


            @Override
            public CompoundNBT serializeNBT()
            {
                CompoundNBT tag = new CompoundNBT();
                if(this.scope != null)
                {
                    tag.put("Scope", this.scope.serializeNBT());
                }
                if(this.barrel != null)
                {
                    tag.put("Barrel", this.barrel.serializeNBT());
                }
                if(this.stock != null)
                {
                    tag.put("Stock", this.stock.serializeNBT());
                }
                if(this.underBarrel != null)
                {
                    tag.put("UnderBarrel", this.underBarrel.serializeNBT());
                }
                if(this.oldScope != null)
                {
                    tag.put("OldScope", this.oldScope.serializeNBT());
                }
                if(this.sideRail != null)
                {
                    tag.put("SideRail", this.sideRail.serializeNBT());
                }
                if(this.pistolScope != null)
                {
                    tag.put("PistolScope", this.pistolScope.serializeNBT());
                }
                if(this.pistolBarrel != null)
                {
                    tag.put("PistolBarrel", this.pistolBarrel.serializeNBT());
                }
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundNBT tag)
            {
                if(tag.contains("Scope", Constants.NBT.TAG_COMPOUND))
                {
                    this.scope = this.createScaledPositioned(tag, "Scope");
                }
                if(tag.contains("Barrel", Constants.NBT.TAG_COMPOUND))
                {
                    this.barrel = this.createScaledPositioned(tag, "Barrel");
                }
                if(tag.contains("Stock", Constants.NBT.TAG_COMPOUND))
                {
                    this.stock = this.createScaledPositioned(tag, "Stock");
                }
                if(tag.contains("UnderBarrel", Constants.NBT.TAG_COMPOUND))
                {
                    this.underBarrel = this.createScaledPositioned(tag, "UnderBarrel");
                }
                if(tag.contains("OldScope", Constants.NBT.TAG_COMPOUND))
                {
                    this.oldScope = this.createScaledPositioned(tag, "OldScope");
                }
                if(tag.contains("SideRail", Constants.NBT.TAG_COMPOUND))
                {
                    this.sideRail = this.createScaledPositioned(tag, "SideRail");
                }
                if(tag.contains("PistolScope", Constants.NBT.TAG_COMPOUND))
                {
                    this.pistolScope = this.createPistolScope(tag, "PistolScope");
                }
                if(tag.contains("PistolBarrel", Constants.NBT.TAG_COMPOUND))
                {
                    this.pistolBarrel = this.createScaledPositioned(tag, "PistolBarrel");
                }
            }

            public Attachments copy()
            {
                Attachments attachments = new Attachments();
                if(this.scope != null)
                {
                    attachments.scope = this.scope.copy();
                }
                if(this.barrel != null)
                {
                    attachments.barrel = this.barrel.copy();
                }
                if(this.stock != null)
                {
                    attachments.stock = this.stock.copy();
                }
                if(this.underBarrel != null)
                {
                    attachments.underBarrel = this.underBarrel.copy();
                }
                if(this.oldScope != null)
                {
                    attachments.oldScope = this.oldScope.copy();
                }
                if(this.sideRail != null)
                {
                    attachments.sideRail = this.sideRail.copy();
                }
                if(this.pistolScope != null)
                {
                    attachments.pistolScope = this.pistolScope.copy();
                }
                if(this.pistolBarrel != null)
                {
                    attachments.pistolBarrel = this.pistolBarrel.copy();
                }
                return attachments;
            }

            @Nullable
            private ScaledPositioned createScaledPositioned(CompoundNBT tag, String key)
            {
                CompoundNBT attachment = tag.getCompound(key);
                return attachment.isEmpty() ? null : new ScaledPositioned(attachment);
            }

            @Nullable
            private PistolScope createPistolScope(CompoundNBT tag, String key)
            {
                CompoundNBT attachment = tag.getCompound(key);
                return attachment.isEmpty() ? null : new PistolScope(attachment);
            }
        }

        /*@Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            //if(!ArrayUtils.isEmpty(this.zoom))
            //GunMod.LOGGER.log(Level.FATAL, zoom.length);
            if(this.zoom != null) {
                //GunMod.LOGGER.log(Level.FATAL, this.zoom[0].getFovModifier());
                tag.put("Zoom", zoom.clone()[0].serializeNBT());
            }
            *//*if(this.zoom != null)
            {


                *//**//*int zoomIterator = 0;
                for(Zoom sight : this.zoom)
                {

                    GunMod.LOGGER.log(Level.FATAL, sight.fovModifier);
                    tag.put("Zoom" + Integer.toString(zoomIterator), sight.serializeNBT());
                    zoomIterator++;
                }
                tag.putInt("ZoomIterator",zoomIterator);*//**//*
                Zoom test = zoom[0].copy();
                tag.put("Zoom0",test.serializeNBT());
            }*//*
            tag.put("Attachments", this.attachments.serializeNBT());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
*//*            if(tag.contains("ZoomIterator", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.zoomOptions = tag.getInt("ZoomIterator");
            }*//*
            if(tag.contains("Zoom", Constants.NBT.TAG_COMPOUND))
            {
                this.zoom[0].deserializeNBT(tag.getCompound("Zoom"));
            }


            if(tag.contains("Attachments", Constants.NBT.TAG_COMPOUND))
            {
                this.attachments.deserializeNBT(tag.getCompound("Attachments"));
            }
        }

        public Modules copy()
        {
            Modules modules = new Modules();
            modules.zoomOptions = this.zoomOptions;
            modules.zoom = this.zoom.clone();
            modules.attachments = this.attachments.copy();
            return modules;
        }*/
        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();

            int zoomIterator = 0;

            if(!ArrayUtils.isEmpty(this.zoom))
            {
                for(Zoom sight : this.zoom)
                {
                    tag.put("Zoom" + zoomIterator, sight.serializeNBT());
                    zoomIterator++;
                }
            }
            tag.putInt("ZoomIterator",zoomIterator);
            tag.put("Attachments", this.attachments.serializeNBT());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(ArrayUtils.isEmpty(this.getZoom()))
            {

                ArrayList<Zoom> zoomArr = new ArrayList<Zoom>(){};

                int zoomItor = tag.getInt("ZoomIterator");

                for(int itor = 0; itor <= zoomItor; itor++)
                {
                    Zoom zoom = new Zoom();
                    zoom.deserializeNBT(tag.getCompound("Zoom" + itor));
                    zoomArr.add(zoom);
                }

                this.zoom = zoomArr.toArray(this.zoom).clone();
            }

            if(tag.contains("Attachments", Constants.NBT.TAG_COMPOUND))
            {
                this.attachments.deserializeNBT(tag.getCompound("Attachments"));
            }
        }
        public Modules copy()
        {
            Modules modules = new Modules();
            if(this.zoom != null)
            {
                modules.zoom = this.zoom.clone();
            }
            modules.attachments = this.attachments.copy();
            return modules;
        }
    }

    public static class Positioned implements INBTSerializable<CompoundNBT>
    {
        @Optional
        protected double xOffset;
        @Optional
        protected double yOffset;
        @Optional
        protected double zOffset;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putDouble("XOffset", this.xOffset);
            tag.putDouble("YOffset", this.yOffset);
            tag.putDouble("ZOffset", this.zOffset);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("XOffset", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.xOffset = tag.getDouble("XOffset");
            }
            if(tag.contains("YOffset", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.yOffset = tag.getDouble("YOffset");
            }
            if(tag.contains("ZOffset", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.zOffset = tag.getDouble("ZOffset");
            }
        }

        public double getXOffset()
        {
            return this.xOffset;
        }

        public double getYOffset()
        {
            return this.yOffset;
        }

        public double getZOffset()
        {
            return this.zOffset;
        }

        public Positioned copy()
        {
            Positioned positioned = new Positioned();
            positioned.xOffset = this.xOffset;
            positioned.yOffset = this.yOffset;
            positioned.zOffset = this.zOffset;
            return positioned;
        }
    }

    public static class ScaledPositioned extends Positioned
    {
        @Optional
        protected double scale = 1.0;

        public ScaledPositioned() {}

        public ScaledPositioned(CompoundNBT tag)
        {
            this.deserializeNBT(tag);
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = super.serializeNBT();
            tag.putDouble("Scale", this.scale);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            super.deserializeNBT(tag);
            if(tag.contains("Scale", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.scale = tag.getDouble("Scale");
            }
        }

        public double getScale()
        {
            return this.scale;
        }

        @Override
        public ScaledPositioned copy()
        {
            ScaledPositioned positioned = new ScaledPositioned();
            positioned.xOffset = this.xOffset;
            positioned.yOffset = this.yOffset;
            positioned.zOffset = this.zOffset;
            positioned.scale = this.scale;
            return positioned;
        }
    }

    public static class PistolScope extends ScaledPositioned
    {
        @Required
        private boolean doRenderMount;
        @Required
        private boolean doOnSlideMovement;
        //private HashSet

        public PistolScope() {}

        public PistolScope(CompoundNBT tag)
        {
            this.deserializeNBT(tag);
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = super.serializeNBT();
            tag.putBoolean("RenderMount", this.doRenderMount);
            tag.putBoolean("DoOnSlideMovement", this.doOnSlideMovement);
            //tag.put
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            super.deserializeNBT(tag);
            if(tag.contains("RenderMount", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.doRenderMount = tag.getBoolean("RenderMount");
            }
            if(tag.contains("DoOnSlideMovement", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.doOnSlideMovement = tag.getBoolean("DoOnSlideMovement");
            }
        }

        public boolean getDoRenderMount()
        {
            return this.doRenderMount;
        }
        public boolean getDoOnSlideMovement()
        {
            return this.doOnSlideMovement;
        }

        @Override
        public PistolScope copy()
        {
            PistolScope positioned = new PistolScope();
            positioned.xOffset = this.xOffset;
            positioned.yOffset = this.yOffset;
            positioned.zOffset = this.zOffset;
            positioned.scale = this.scale;
            positioned.doRenderMount = this.doRenderMount;
            positioned.doOnSlideMovement = this.doOnSlideMovement;
            return positioned;
        }
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.put("General", this.general.serializeNBT());
        tag.put("Reloads", this.reloads.serializeNBT());
        tag.put("Projectile", this.projectile.serializeNBT());
        tag.put("Sounds", this.sounds.serializeNBT());
        tag.put("Display", this.display.serializeNBT());
        tag.put("Modules", this.modules.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT tag)
    {
        if(tag.contains("General", Constants.NBT.TAG_COMPOUND))
        {
            this.general.deserializeNBT(tag.getCompound("General"));
        }
        if(tag.contains("Reloads", Constants.NBT.TAG_COMPOUND))
        {
            this.reloads.deserializeNBT(tag.getCompound("Reloads"));
        }
        if(tag.contains("Projectile", Constants.NBT.TAG_COMPOUND))
        {
            this.projectile.deserializeNBT(tag.getCompound("Projectile"));
        }
        if(tag.contains("Sounds", Constants.NBT.TAG_COMPOUND))
        {
            this.sounds.deserializeNBT(tag.getCompound("Sounds"));
        }
        if(tag.contains("Display", Constants.NBT.TAG_COMPOUND))
        {
            this.display.deserializeNBT(tag.getCompound("Display"));
        }
        if(tag.contains("Modules", Constants.NBT.TAG_COMPOUND))
        {
            this.modules.deserializeNBT(tag.getCompound("Modules"));
        }
    }



    public static Gun create(CompoundNBT tag)
    {
        Gun gun = new Gun();
        gun.deserializeNBT(tag);
        return gun;
    }

    public Gun copy()
    {
        Gun gun = new Gun();
        gun.general = this.general.copy();
        gun.reloads = this.reloads.copy();
        gun.projectile = this.projectile.copy();
        gun.sounds = this.sounds.copy();
        gun.display = this.display.copy();
        gun.modules = this.modules.copy();
        return gun;
    }

    public boolean canAttachType(@Nullable IAttachment.Type type)
    {
        if(this.modules.attachments != null && type != null)
        {
            switch(type)
            {
                case SCOPE:
                    return this.modules.attachments.scope != null;
                case BARREL:
                    return this.modules.attachments.barrel != null;
                case STOCK:
                    return this.modules.attachments.stock != null;
                case UNDER_BARREL:
                    return this.modules.attachments.underBarrel != null;
                case SIDE_RAIL:
                    return this.modules.attachments.sideRail != null;
                case OLD_SCOPE:
                    return this.modules.attachments.oldScope != null;
                case PISTOL_SCOPE:
                    return this.modules.attachments.pistolScope != null;
                case PISTOL_BARREL:
                    return this.modules.attachments.pistolBarrel != null;
            }
        }
        return false;
    }

    @Nullable
    public ScaledPositioned getAttachmentPosition(IAttachment.Type type)
    {
        if(this.modules.attachments != null)
        {
            switch(type)
            {
                case SCOPE:
                    return this.modules.attachments.scope;
                case BARREL:
                    return this.modules.attachments.barrel;
                case STOCK:
                    return this.modules.attachments.stock;
                case UNDER_BARREL:
                    return this.modules.attachments.underBarrel;
                case SIDE_RAIL:
                    return this.modules.attachments.sideRail;
                case OLD_SCOPE:
                    return this.modules.attachments.oldScope;
                case PISTOL_SCOPE:
                    return this.modules.attachments.pistolScope;
                case PISTOL_BARREL:
                    return this.modules.attachments.pistolBarrel;
            }
        }
        return null;
    }

    public boolean canAimDownSight()
    {
        return this.canAttachType(IAttachment.Type.SCOPE) || this.canAttachType(IAttachment.Type.OLD_SCOPE) || this.canAttachType(IAttachment.Type.PISTOL_SCOPE) || this.modules.zoom != null;
    }

    public static ItemStack getScopeStack(ItemStack gun)
    {
        CompoundNBT compound = gun.getTag();
        if(compound != null && compound.contains("Attachments", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT attachment = compound.getCompound("Attachments");
            if(attachment.contains("Scope", Constants.NBT.TAG_COMPOUND))
            {
                return ItemStack.read(attachment.getCompound("Scope"));
            }
            else if(attachment.contains("OldScope", Constants.NBT.TAG_COMPOUND))
            {
                return ItemStack.read(attachment.getCompound("OldScope"));
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean hasAttachmentEquipped(ItemStack stack, Gun gun, IAttachment.Type type)
    {
        if(!gun.canAttachType(type))
            return false;

        CompoundNBT compound = stack.getTag();
        if(compound != null && compound.contains("Attachments", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT attachment = compound.getCompound("Attachments");
            return attachment.contains(type.getTagKey(), Constants.NBT.TAG_COMPOUND);
        }
        return false;
    }

    @Nullable
    public static Scope getScope(ItemStack gun)
    {
        CompoundNBT compound = gun.getTag();
        if(compound != null && compound.contains("Attachments", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT attachment = compound.getCompound("Attachments");
            if(attachment.contains("Scope", Constants.NBT.TAG_COMPOUND))
            {
                ItemStack scopeStack = ItemStack.read(attachment.getCompound("Scope"));
                Scope scope = null;
                if(scopeStack.getItem() instanceof IScope)
                {
                    scope = ((IScope) scopeStack.getItem()).getProperties();
                }
                return scope;
            }
            else if(attachment.contains("OldScope", Constants.NBT.TAG_COMPOUND))
            {
                ItemStack OldScopeStack = ItemStack.read(attachment.getCompound("OldScope"));
                Scope scope = null;
                if(OldScopeStack.getItem() instanceof IScope)
                {
                    scope = ((IScope) OldScopeStack.getItem()).getProperties();
                }
                return scope;
            }
            else if(attachment.contains("PistolScope", Constants.NBT.TAG_COMPOUND))
            {
                ItemStack OldScopeStack = ItemStack.read(attachment.getCompound("PistolScope"));
                Scope scope = null;
                if(OldScopeStack.getItem() instanceof IScope)
                {
                    scope = ((IScope) OldScopeStack.getItem()).getProperties();
                }
                return scope;
            }
        }
        return null;
    }

    public static ItemStack getAttachment(IAttachment.Type type, ItemStack gun)
    {
        CompoundNBT compound = gun.getTag();
        if(compound != null && compound.contains("Attachments", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT attachment = compound.getCompound("Attachments");
            if(attachment.contains(type.getTagKey(), Constants.NBT.TAG_COMPOUND))
            {
                return ItemStack.read(attachment.getCompound(type.getTagKey()));
            }
        }
        return ItemStack.EMPTY;
    }

    public static float getAdditionalDamage(ItemStack gunStack)
    {
        CompoundNBT tag = gunStack.getOrCreateTag();
        return tag.getFloat("AdditionalDamage");
    }

    public static ItemStack[] findAmmo(PlayerEntity player, ResourceLocation id) // Refactor to return multiple stacks, reload to take as much of value as required from hash
    {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        if(player.isCreative())
        {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            stacks.add(item != null ? new ItemStack(item, Integer.MAX_VALUE) : ItemStack.EMPTY);
            return stacks.toArray(new ItemStack[]{});
            //return item != null ? new ItemStack(item, Integer.MAX_VALUE) : ItemStack.EMPTY;
        }
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(isAmmo(stack, id))
            {
                stacks.add(stack);
            }
        }
        return stacks.toArray(new ItemStack[]{});
    }

    private static boolean isAmmo(ItemStack stack, ResourceLocation id)
    {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }

    public static boolean hasAmmo(ItemStack gunStack)
    {
        CompoundNBT tag = gunStack.getOrCreateTag();
        return tag.getBoolean("IgnoreAmmo") || tag.getInt("AmmoCount") > 0;
    }
}
