package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.item.AmmoItem;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class Gun implements INBTSerializable<CompoundNBT>
{
    public General general = new General();
    public Projectile projectile = new Projectile();
    public Sounds sounds = new Sounds();
    public Display display = new Display();
    public Modules modules = new Modules();

    public static class General implements INBTSerializable<CompoundNBT>
    {
        @Optional
        public boolean auto = false;
        public int rate;
        public GripType gripType = GripType.ONE_HANDED;
        public int maxAmmo;
        @Optional
        public int reloadSpeed = 1;
        @Optional
        public float recoilAngle;
        @Optional
        public float recoilKick;
        @Optional
        public float recoilDurationOffset;
        @Optional
        public float recoilAdsReduction = 0.9F;
        @Optional
        public int projectileAmount = 1;
        @Optional
        public boolean alwaysSpread;
        @Optional
        public float spread;

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putBoolean("Auto", this.auto);
            tag.putInt("Rate", this.rate);
            tag.putInt("GripType", this.gripType.ordinal());
            tag.putInt("MaxAmmo", this.maxAmmo);
            tag.putInt("ReloadSpeed", this.reloadSpeed);
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
            if(tag.contains("Auto", Constants.NBT.TAG_BYTE))
            {
                this.auto = tag.getBoolean("Auto");
            }
            if(tag.contains("Rate", Constants.NBT.TAG_INT))
            {
                this.rate = tag.getInt("Rate");
            }
            if(tag.contains("GripType", Constants.NBT.TAG_INT))
            {
                this.gripType = GripType.values()[tag.getInt("GripType")];
            }
            if(tag.contains("MaxAmmo", Constants.NBT.TAG_INT))
            {
                this.maxAmmo = tag.getInt("MaxAmmo");
            }
            if(tag.contains("ReloadSpeed", Constants.NBT.TAG_INT))
            {
                this.reloadSpeed = tag.getInt("ReloadSpeed");
            }
            if(tag.contains("RecoilAngle", Constants.NBT.TAG_FLOAT))
            {
                this.recoilAngle = tag.getFloat("RecoilAngle");
            }
            if(tag.contains("RecoilKick", Constants.NBT.TAG_FLOAT))
            {
                this.recoilKick = tag.getFloat("RecoilKick");
            }
            if(tag.contains("RecoilDurationOffset", Constants.NBT.TAG_FLOAT))
            {
                this.recoilDurationOffset = tag.getFloat("RecoilDurationOffset");
            }
            if(tag.contains("RecoilAdsReduction", Constants.NBT.TAG_FLOAT))
            {
                this.recoilAdsReduction = tag.getFloat("RecoilAdsReduction");
            }
            if(tag.contains("ProjectileAmount", Constants.NBT.TAG_INT))
            {
                this.projectileAmount = tag.getInt("ProjectileAmount");
            }
            if(tag.contains("AlwaysSpread", Constants.NBT.TAG_BYTE))
            {
                this.alwaysSpread = tag.getBoolean("AlwaysSpread");
            }
            if(tag.contains("Spread", Constants.NBT.TAG_FLOAT))
            {
                this.spread = tag.getFloat("Spread");
            }
        }

        public General copy()
        {
            General general = new General();
            general.auto = this.auto;
            general.rate = this.rate;
            general.gripType = this.gripType;
            general.maxAmmo = this.maxAmmo;
            general.reloadSpeed = this.reloadSpeed;
            general.recoilAngle = this.recoilAngle;
            general.recoilKick = this.recoilKick;
            general.recoilDurationOffset = this.recoilDurationOffset;
            general.recoilAdsReduction = this.recoilAdsReduction;
            general.projectileAmount = this.projectileAmount;
            general.alwaysSpread = this.alwaysSpread;
            general.spread = this.spread;
            return general;
        }
    }

    public static class Projectile implements INBTSerializable<CompoundNBT>
    {
        public ResourceLocation item;
        @Optional
        public boolean visible;
        public float damage;
        public float size;
        public double speed;
        public int life;
        @Optional
        public boolean gravity;
        @Optional
        public boolean damageReduceOverLife;
        @Optional
        public boolean damageReduceIfNotZoomed;
        @Optional
        public int trailColor = 0xFFD289;
        @Optional
        public double trailLengthMultiplier = 1.0;

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
            tag.putBoolean("DamageReduceIfNotZoomed", this.damageReduceIfNotZoomed);
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
            if(tag.contains("Visible", Constants.NBT.TAG_BYTE))
            {
                this.visible = tag.getBoolean("Visible");
            }
            if(tag.contains("Damage", Constants.NBT.TAG_FLOAT))
            {
                this.damage = tag.getFloat("Damage");
            }
            if(tag.contains("Size", Constants.NBT.TAG_FLOAT))
            {
                this.size = tag.getFloat("Size");
            }
            if(tag.contains("Speed", Constants.NBT.TAG_DOUBLE))
            {
                this.speed = tag.getDouble("Speed");
            }
            if(tag.contains("Life", Constants.NBT.TAG_INT))
            {
                this.life = tag.getInt("Life");
            }
            if(tag.contains("Gravity", Constants.NBT.TAG_BYTE))
            {
                this.gravity = tag.getBoolean("Gravity");
            }
            if(tag.contains("DamageReduceOverLife", Constants.NBT.TAG_BYTE))
            {
                this.damageReduceOverLife = tag.getBoolean("DamageReduceOverLife");
            }
            if(tag.contains("DamageReduceIfNotZoomed", Constants.NBT.TAG_BYTE))
            {
                this.damageReduceIfNotZoomed = tag.getBoolean("DamageReduceIfNotZoomed");
            }
            if(tag.contains("TrailColor", Constants.NBT.TAG_INT))
            {
                this.trailColor = tag.getInt("TrailColor");
            }
            if(tag.contains("TrailLengthMultiplier", Constants.NBT.TAG_DOUBLE))
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
            projectile.damageReduceIfNotZoomed = this.damageReduceIfNotZoomed;
            projectile.trailColor = this.trailColor;
            projectile.trailLengthMultiplier = this.trailLengthMultiplier;
            return projectile;
        }
    }

    public static class Sounds implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        public ResourceLocation fire;
        @Optional
        @Nullable
        public ResourceLocation reload;
        @Optional
        @Nullable
        public ResourceLocation cock;
        @Optional
        @Nullable
        public ResourceLocation silencedFire;

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
    }

    public static class Display implements INBTSerializable<CompoundNBT>
    {
        @Optional
        @Nullable
        public Flash flash;

        public static class Flash extends Positioned
        {
            public double size = 0.5;

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
                if(tag.contains("Size", Constants.NBT.TAG_DOUBLE))
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
        public Zoom zoom;
        public Attachments attachments = new Attachments();

        public static class Zoom extends Positioned
        {
            @Optional
            public float fovModifier;

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
                if(tag.contains("FovModifier", Constants.NBT.TAG_FLOAT))
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
        }

        public static class Attachments implements INBTSerializable<CompoundNBT>
        {
            @Optional
            @Nullable
            public Scope scope;
            @Optional
            @Nullable
            public Barrel barrel;

            public static class Scope extends ScaledPositioned
            {
                public Scope copy()
                {
                    Scope scope = new Scope();
                    scope.scale = this.scale;
                    scope.xOffset = this.xOffset;
                    scope.yOffset = this.yOffset;
                    scope.zOffset = this.zOffset;
                    return scope;
                }
            }

            public static class Barrel extends ScaledPositioned
            {
                public Barrel copy()
                {
                    Barrel barrel = new Barrel();
                    barrel.scale = this.scale;
                    barrel.xOffset = this.xOffset;
                    barrel.yOffset = this.yOffset;
                    barrel.zOffset = this.zOffset;
                    return barrel;
                }
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
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundNBT tag)
            {
                if(tag.contains("Scope", Constants.NBT.TAG_COMPOUND))
                {
                    Scope scope = new Scope();
                    scope.deserializeNBT(tag.getCompound("Scope"));
                    this.scope = scope;
                }
                if(tag.contains("Barrel", Constants.NBT.TAG_COMPOUND))
                {
                    Barrel barrel = new Barrel();
                    barrel.deserializeNBT(tag.getCompound("Barrel"));
                    this.barrel = barrel;
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
        public double xOffset;
        @Optional
        public double yOffset;
        @Optional
        public double zOffset;

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
            if(tag.contains("XOffset", Constants.NBT.TAG_DOUBLE))
            {
                this.xOffset = tag.getDouble("XOffset");
            }
            if(tag.contains("YOffset", Constants.NBT.TAG_DOUBLE))
            {
                this.yOffset = tag.getDouble("YOffset");
            }
            if(tag.contains("ZOffset", Constants.NBT.TAG_DOUBLE))
            {
                this.zOffset = tag.getDouble("ZOffset");
            }
        }
    }

    public static class ScaledPositioned extends Positioned
    {
        @Optional
        public double scale = 1.0;

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
            if(tag.contains("Scale", Constants.NBT.TAG_DOUBLE))
            {
                this.scale = tag.getDouble("Scale");
            }
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
                if(scopeStack.getItem() instanceof ScopeItem)
                {
                    scope = ((ScopeItem) scopeStack.getItem()).getScope();
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
            if(attachment.contains(type.getId(), Constants.NBT.TAG_COMPOUND))
            {
                return ItemStack.read(attachment.getCompound(type.getId()));
            }
        }
        return ItemStack.EMPTY;
    }

    public static float getAdditionalDamage(ItemStack gunStack)
    {
        CompoundNBT tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getFloat("AdditionalDamage");
    }

    public static ItemStack findAmmo(PlayerEntity player, ResourceLocation id)
    {
        if(player.isCreative())
        {
            AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(id);
            return ammo != null ? new ItemStack(ammo, 64) : ItemStack.EMPTY;
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
        CompoundNBT tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getBoolean("IgnoreAmmo") || tag.getInt("AmmoCount") > 0;
    }
}
