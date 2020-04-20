package com.mrcrayfish.guns.object;

import com.google.gson.*;
import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

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
            general.auto = auto;
            general.rate = rate;
            general.gripType = gripType;
            general.maxAmmo = maxAmmo;
            general.reloadSpeed = reloadSpeed;
            general.recoilAngle = recoilAngle;
            general.recoilKick = recoilKick;
            general.recoilDurationOffset = recoilDurationOffset;
            general.projectileAmount = projectileAmount;
            general.alwaysSpread = alwaysSpread;
            general.spread = spread;
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
        public int trailColor = 0xFFFFFF;
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
            projectile.item = item;
            projectile.visible = visible;
            projectile.damage = damage;
            projectile.size = size;
            projectile.speed = speed;
            projectile.life = life;
            projectile.gravity = gravity;
            projectile.damageReduceOverLife = damageReduceOverLife;
            projectile.damageReduceIfNotZoomed = damageReduceIfNotZoomed;
            projectile.trailColor = trailColor;
            projectile.trailLengthMultiplier = trailLengthMultiplier;
            return projectile;
        }
    }

    public static class Sounds implements INBTSerializable<CompoundNBT>
    {
        public String fire = "";
        public String reload = "";
        public String cock = "";
        public String silencedFire = "silenced_fire";

        public String getFire(Gun modifiedGun)
        {
            String fire = this.fire;
            if(!modifiedGun.sounds.fire.equals(fire))
            {
                fire = modifiedGun.sounds.fire;
            }
            return fire;
        }

        public String getReload(Gun modifiedGun)
        {
            String reload = this.reload;
            if(!modifiedGun.sounds.reload.equals(reload))
            {
                reload = modifiedGun.sounds.reload;
            }
            return reload;
        }

        public String getCock(Gun modifiedGun)
        {
            String cock = this.cock;
            if(!modifiedGun.sounds.cock.equals(cock))
            {
                cock = modifiedGun.sounds.cock;
            }
            return cock;
        }

        public String getSilencedFire(Gun modifiedGun)
        {
            String silencedFire = this.silencedFire;
            if(!modifiedGun.sounds.silencedFire.equals(silencedFire))
            {
                silencedFire = modifiedGun.sounds.silencedFire;
            }
            return silencedFire;
        }

        @Override
        public CompoundNBT serializeNBT()
        {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Fire", fire);
            tag.putString("Reload", reload);
            tag.putString("Cock", cock);
            tag.putString("SilencedFire", silencedFire);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT tag)
        {
            if(tag.contains("Fire", Constants.NBT.TAG_STRING))
            {
                this.fire = tag.getString("Fire");
            }
            if(tag.contains("Reload", Constants.NBT.TAG_STRING))
            {
                this.reload = tag.getString("Reload");
            }
            if(tag.contains("Cock", Constants.NBT.TAG_STRING))
            {
                this.cock = tag.getString("Cock");
            }
            if(tag.contains("SilencedFire", Constants.NBT.TAG_STRING))
            {
                this.silencedFire = tag.getString("SilencedFire");
            }
        }

        public Sounds copy()
        {
            Sounds sounds = new Sounds();
            sounds.fire = fire;
            sounds.reload = reload;
            sounds.cock = cock;
            sounds.silencedFire = silencedFire;
            return sounds;
        }
    }

    public static class Display
    {
        @Optional
        public Flash flash;

        public static class Flash extends ScaledPositioned
        {
            public Flash copy()
            {
                Flash flash = new Flash();
                flash.scale = scale;
                flash.xOffset = xOffset;
                flash.yOffset = yOffset;
                flash.zOffset = zOffset;
                return flash;
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

    public static class Modules
    {
        @Optional
        public Zoom zoom;
        public Attachments attachments = new Attachments();

        public static class Zoom extends Positioned
        {
            @Optional
            public float fovModifier;
            @Optional
            public boolean smooth;

            public Zoom copy()
            {
                Zoom zoom = new Zoom();
                zoom.fovModifier = fovModifier;
                zoom.smooth = smooth;
                zoom.xOffset = xOffset;
                zoom.yOffset = yOffset;
                zoom.zOffset = zOffset;
                return zoom;
            }
        }

        public static class Attachments
        {
            @Optional
            public Scope scope;
            @Optional
            public Barrel barrel;

            public static class Scope extends ScaledPositioned
            {
                @Optional
                public boolean smooth;

                public Scope copy()
                {
                    Scope scope = new Scope();
                    scope.smooth = smooth;
                    scope.scale = scale;
                    scope.xOffset = xOffset;
                    scope.yOffset = yOffset;
                    scope.zOffset = zOffset;
                    return scope;
                }
            }

            public static class Barrel extends ScaledPositioned
            {
                public Barrel copy()
                {
                    Barrel barrel = new Barrel();
                    barrel.scale = scale;
                    barrel.xOffset = xOffset;
                    barrel.yOffset = yOffset;
                    barrel.zOffset = zOffset;
                    return barrel;
                }
            }

            public Attachments copy()
            {
                Attachments attachments = new Attachments();
                if(scope != null)
                {
                    attachments.scope = scope.copy();
                }
                if(barrel != null)
                {
                    attachments.barrel = barrel.copy();
                }
                return attachments;
            }
        }

        public Modules copy()
        {
            Modules modules = new Modules();
            if(zoom != null)
            {
                modules.zoom = zoom.copy();
            }
            modules.attachments = attachments.copy();
            return modules;
        }
    }

    public static class Positioned
    {
        @Optional
        public double xOffset;
        @Optional
        public double yOffset;
        @Optional
        public double zOffset;
    }

    public static class ScaledPositioned extends Positioned
    {
        @Optional
        public double scale = 1.0;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT tag = new CompoundNBT();
        tag.put("General", general.serializeNBT());
        tag.put("Projectile", projectile.serializeNBT());
        tag.put("Sounds", sounds.serializeNBT());
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
        gun.general = general.copy();
        gun.projectile = projectile.copy();
        gun.sounds = sounds.copy();
        gun.display = display.copy();
        gun.modules = modules.copy();
        return gun;
    }

    public boolean canAttachType(@Nullable IAttachment.Type type)
    {
        if(modules.attachments != null && type != null)
        {
            switch(type)
            {
                case SCOPE:
                    return modules.attachments.scope != null;
                case BARREL:
                    return modules.attachments.barrel != null;
            }
        }
        return false;
    }

    @Nullable
    public ScaledPositioned getAttachmentPosition(IAttachment.Type type)
    {
        if(modules.attachments != null)
        {
            switch(type)
            {
                case SCOPE:
                    return modules.attachments.scope;
                case BARREL:
                    return modules.attachments.barrel;
            }
        }
        return null;
    }

    public static ItemStack getScope(ItemStack gun)
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

    public static ItemStack getAttachment(IAttachment.Type type, ItemStack gun)
    {
        CompoundNBT compound = gun.getTag();
        if(compound != null && compound.contains("Attachments", Constants.NBT.TAG_COMPOUND))
        {
            CompoundNBT attachment = compound.getCompound("Attachments");
            if(attachment.contains(type.getName(), Constants.NBT.TAG_COMPOUND))
            {
                return ItemStack.read(attachment.getCompound(type.getName()));
            }
        }
        return ItemStack.EMPTY;
    }

    public static float getAdditionalDamage(ItemStack gunStack)
    {
        CompoundNBT tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getFloat("AdditionalDamage");
    }

    public static class ResourceLocationSerializer implements JsonSerializer<ResourceLocation>
    {
        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context)
        {
            return new JsonPrimitive(src.toString());
        }
    }

    public static class ResourceLocationDeserializer implements JsonDeserializer<ResourceLocation>
    {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return new ResourceLocation(json.getAsString());
        }
    }

    public static class Serializer implements JsonSerializer<Gun>
    {
        @Override
        public JsonElement serialize(Gun gun, Type typeOfSrc, JsonSerializationContext context)
        {
            JsonObject parent = new JsonObject();

            JsonObject general = new JsonObject();
            general.addProperty("Max Ammo", gun.general.maxAmmo);
            general.addProperty("Reload Speed", gun.general.reloadSpeed);
            general.addProperty("Projectile Count", gun.general.projectileAmount);
            general.addProperty("Always Spread", gun.general.alwaysSpread);
            general.addProperty("Spread", gun.general.spread);
            parent.add("General", general);

            JsonObject projectile = new JsonObject();
            projectile.addProperty("Damage", gun.projectile.damage);
            projectile.addProperty("Damage Falloff", gun.projectile.damageReduceOverLife);
            projectile.addProperty("Gravity", gun.projectile.gravity);
            projectile.addProperty("Ticks Before Removed", gun.projectile.life);
            projectile.addProperty("Reduce Damage If Not Zoomed", gun.projectile.damageReduceIfNotZoomed);
            projectile.addProperty("Size", gun.projectile.size);
            projectile.addProperty("Speed", gun.projectile.speed);
            parent.add("Projectile", projectile);

            return parent;
        }
    }

    public static class Deserializer implements JsonDeserializer<Gun>
    {
        private Gun base;

        public Deserializer(Gun base)
        {
            this.base = base;
        }

        @Override
        public Gun deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {

            JsonObject general = JSONUtils.getJsonObject(json.getAsJsonObject(), "General");
            this.base.general.maxAmmo = JSONUtils.getInt(general, "Max Ammo", this.base.general.maxAmmo);
            this.base.general.reloadSpeed = JSONUtils.getInt(general, "Reload Speed", this.base.general.reloadSpeed);
            this.base.general.projectileAmount = JSONUtils.getInt(general, "Projectile Count", this.base.general.projectileAmount);
            this.base.general.alwaysSpread = JSONUtils.getBoolean(general, "Always Spread", this.base.general.alwaysSpread);
            this.base.general.spread = JSONUtils.getFloat(general, "Spread", this.base.general.spread);

            JsonObject projectile = JSONUtils.getJsonObject(json.getAsJsonObject(), "Projectile");
            this.base.projectile.damage = JSONUtils.getFloat(projectile, "Damage", this.base.projectile.damage);
            this.base.projectile.damageReduceOverLife = JSONUtils.getBoolean(projectile, "Damage Falloff", this.base.projectile.damageReduceOverLife);
            this.base.projectile.gravity = JSONUtils.getBoolean(projectile, "Gravity", this.base.projectile.gravity);
            this.base.projectile.life = JSONUtils.getInt(projectile, "Ticks Before Removed", this.base.projectile.life);
            this.base.projectile.damageReduceIfNotZoomed = JSONUtils.getBoolean(projectile, "Reduce Damage If Not Zoomed", this.base.projectile.damageReduceIfNotZoomed);
            this.base.projectile.size = JSONUtils.getFloat(projectile, "Size", this.base.projectile.size);
            this.base.projectile.speed = JSONUtils.getFloat(projectile, "Speed", (float) this.base.projectile.speed);

            return this.base;
        }
    }
}
