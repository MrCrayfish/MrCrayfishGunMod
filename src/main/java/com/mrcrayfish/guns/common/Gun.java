package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.annotation.Ignored;
import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import com.mrcrayfish.guns.item.attachment.IScope;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public final class Gun implements INBTSerializable<CompoundNBT>
{
    private General general = new General();
    private Projectile projectile = new Projectile();
    private Sounds sounds = new Sounds();
    private Display display = new Display();
    private Modules modules = new Modules();

    public General getGeneral()
    {
        return this.general;
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
        private int rate;
        @Ignored
        private GripType gripType = GripType.ONE_HANDED;
        private int maxAmmo;
        @Optional
        private int reloadAmount = 1;
        @Optional
        private float recoilAngle;
        @Optional
        private float recoilKick;
        @Optional
        private float recoilDurationOffset;
        @Optional
        private float recoilAdsReduction = 0.2F;
        @Optional
        private int projectileAmount = 1;
        @Optional
        private boolean alwaysSpread;
        @Optional
        private float spread;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean("Auto", this.auto);
            tag.putInt("Rate", this.rate);
            tag.putString("GripType", this.gripType.getId().toString());
            tag.putInt("MaxAmmo", this.maxAmmo);
            tag.putInt("ReloadSpeed", this.reloadAmount);
            tag.putFloat("RecoilAngle", this.recoilAngle);
            tag.putFloat("RecoilKick", this.recoilKick);
            tag.putFloat("RecoilDurationOffset", this.recoilDurationOffset);
            tag.putFloat("RecoilAdsReduction", this.recoilAdsReduction);
            tag.putInt("ProjectileAmount", this.projectileAmount);
            tag.putBoolean("AlwaysSpread", this.alwaysSpread);
            tag.putFloat("Spread", this.spread);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Auto", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.auto = tag.getBoolean("Auto");
            }
            if(tag.contains("Rate", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.rate = tag.getInt("Rate");
            }
            if(tag.contains("GripType", Constants.NBT.TAG_STRING))
            {
                this.gripType = GripType.getType(ResourceLocation.tryCreate(tag.getString("GripType")));
            }
            if(tag.contains("MaxAmmo", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.maxAmmo = tag.getInt("MaxAmmo");
            }
            if(tag.contains("ReloadSpeed", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.reloadAmount = tag.getInt("ReloadSpeed");
            }
            if(tag.contains("RecoilAngle", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilAngle = tag.getFloat("RecoilAngle");
            }
            if(tag.contains("RecoilKick", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilKick = tag.getFloat("RecoilKick");
            }
            if(tag.contains("RecoilDurationOffset", Constants.NBT.TAG_ANY_NUMERIC))
            {
                this.recoilDurationOffset = tag.getFloat("RecoilDurationOffset");
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
        }

        /**
         * @return A copy of the general get
         */
        public General copy()
        {
            General general = new General();
            general.auto = this.auto;
            general.rate = this.rate;
            general.gripType = this.gripType;
            general.maxAmmo = this.maxAmmo;
            general.reloadAmount = this.reloadAmount;
            general.recoilAngle = this.recoilAngle;
            general.recoilKick = this.recoilKick;
            general.recoilDurationOffset = this.recoilDurationOffset;
            general.recoilAdsReduction = this.recoilAdsReduction;
            general.projectileAmount = this.projectileAmount;
            general.alwaysSpread = this.alwaysSpread;
            general.spread = this.spread;
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
         * @return The fire rate of this weapon in ticks
         */
        public int getRate()
        {
            return this.rate;
        }

        /**
         * @return The type of grip this weapon uses
         */
        public GripType getGripType()
        {
            return this.gripType;
        }

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
         * @return The duration offset for recoil. This reduces the duration of recoil animation
         */
        public float getRecoilDurationOffset()
        {
            return this.recoilDurationOffset;
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
    }

    public static class Projectile implements INBTSerializable<CompoundNBT>
    {
        private ResourceLocation item = new ResourceLocation(Reference.MOD_ID, "basic_ammo");
        @Optional
        private boolean visible;
        private float damage;
        private float size;
        private double speed;
        private int life;
        @Optional
        private boolean gravity;
        @Optional
        private boolean damageReduceOverLife;
        @Optional
        private int trailColor = 0xFFD289;
        @Optional
        private double trailLengthMultiplier = 1.0;

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
                this.fire = new ResourceLocation(tag.getString("Fire"));
            }
            if(tag.contains("Reload", Constants.NBT.TAG_STRING))
            {
                this.reload = new ResourceLocation(tag.getString("Reload"));
            }
            if(tag.contains("Cock", Constants.NBT.TAG_STRING))
            {
                this.cock = new ResourceLocation(tag.getString("Cock"));
            }
            if(tag.contains("SilencedFire", Constants.NBT.TAG_STRING))
            {
                this.silencedFire = new ResourceLocation(tag.getString("SilencedFire"));
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

        @Nullable
        public Flash getFlash()
        {
            return this.flash;
        }

        public static class Flash extends Positioned
        {
            private double size = 0.5;

            @Override
            public CompoundNBT serializeNBT()
            {
                CompoundNBT tag = super.serializeNBT();
                tag.putDouble("Size", this.size);
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
            }

            public Flash copy()
            {
                Flash flash = new Flash();
                flash.size = this.size;
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
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            if(this.flash != null)
            {
                tag.put("Flash", this.flash.serializeNBT());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Flash", Constants.NBT.TAG_COMPOUND))
            {
                Flash flash = new Flash();
                flash.deserializeNBT(tag.getCompound("Flash"));
                this.flash = flash;
            }
        }

        public Display copy()
        {
            Display display = new Display();
            if(flash != null)
            {
                display.flash = flash.copy();
            }
            return display;
        }
    }

    public static class Modules implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        private Zoom zoom;
        private Attachments attachments = new Attachments();

        @Nullable
        public Zoom getZoom()
        {
            return this.zoom;
        }

        public Attachments getAttachments()
        {
            return this.attachments;
        }

        public static class Zoom extends Positioned
        {
            @Optional
            private float fovModifier;

            @Override
            public CompoundNBT serializeNBT()
            {
                CompoundNBT tag = super.serializeNBT();
                tag.putFloat("FovModifier", this.fovModifier);
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
            }

            public Zoom copy()
            {
                Zoom zoom = new Zoom();
                zoom.fovModifier = this.fovModifier;
                zoom.xOffset = this.xOffset;
                zoom.yOffset = this.yOffset;
                zoom.zOffset = this.zOffset;
                return zoom;
            }

            public float getFovModifier()
            {
                return this.fovModifier;
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
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundNBT tag)
            {
                if(tag.contains("Scope", Constants.NBT.TAG_COMPOUND))
                {
                    this.scope = new ScaledPositioned(tag.getCompound("Scope"));
                }
                if(tag.contains("Barrel", Constants.NBT.TAG_COMPOUND))
                {
                    this.barrel = new ScaledPositioned(tag.getCompound("Barrel"));
                }
                if(tag.contains("Stock", Constants.NBT.TAG_COMPOUND))
                {
                    this.stock = new ScaledPositioned(tag.getCompound("Stock"));
                }
                if(tag.contains("UnderBarrel", Constants.NBT.TAG_COMPOUND))
                {
                    this.underBarrel = new ScaledPositioned(tag.getCompound("UnderBarrel"));
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
                return attachments;
            }
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            if(this.zoom != null)
            {
                tag.put("Zoom", this.zoom.serializeNBT());
            }
            tag.put("Attachments", this.attachments.serializeNBT());
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Zoom", Constants.NBT.TAG_COMPOUND))
            {
                Zoom zoom = new Zoom();
                zoom.deserializeNBT(tag.getCompound("Zoom"));
                this.zoom = zoom;
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
                modules.zoom = this.zoom.copy();
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

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.put("General", this.general.serializeNBT());
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
            }
        }
        return null;
    }

    public boolean canAimDownSight()
    {
        return this.canAttachType(IAttachment.Type.SCOPE) || this.modules.zoom != null;
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

    public static ItemStack findAmmo(PlayerEntity player, ResourceLocation id)
    {
        if(player.isCreative())
        {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            return item != null ? new ItemStack(item, Integer.MAX_VALUE) : ItemStack.EMPTY;
        }
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(isAmmo(stack, id))
            {
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
