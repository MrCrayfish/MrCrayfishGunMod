package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.annotation.*;

public class Gun implements INBTSerializable<NBTTagCompound>
{
	@Config.Ignore
	public String id;

	@Config.Name("General")
	@Config.Comment("Change the general properties of the gun")
	@Config.LangKey(GunConfig.PREFIX + "gun.general")
	public General general = new General();

	@Config.Name("Projectile")
	@Config.Comment("Change the properties of the projectile fired from the gun")
	@Config.LangKey(GunConfig.PREFIX + "gun.projectile")
	public Projectile projectile = new Projectile();

	@Config.Name("Sounds")
	@Config.Comment("Change around the sounds of the gun")
	@Config.LangKey(GunConfig.PREFIX + "gun.sounds")
	public Sounds sounds = new Sounds();

	@Config.Ignore
	public Display display = new Display();

	@Config.Ignore
	public Modules modules = new Modules();

	public static class General implements INBTSerializable<NBTTagCompound>
	{
		@Optional
		@Config.Ignore
		public boolean auto = false;

		@Config.Ignore
		public int rate;

		@Config.Ignore
		public GripType gripType;

		@Config.Name("Max Ammo")
		@Config.Comment("The maximum amount of ammo this gun can hold")
		@Config.LangKey(GunConfig.PREFIX + "gun.general.max_ammo")
		public int maxAmmo;

		@Optional
		@Config.Name("Reload Speed")
		@Config.Comment("The amount of bullets added to the gun on each reload")
		@Config.LangKey(GunConfig.PREFIX + "gun.general.reload_speed")
		public int reloadSpeed = 1;

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("auto", auto);
			tag.setInteger("rate", rate);
			tag.setInteger("gripType", gripType.ordinal());
			tag.setInteger("maxAmmo", maxAmmo);
			tag.setInteger("reloadSpeed", reloadSpeed);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag)
		{
			if(tag.hasKey("auto", Constants.NBT.TAG_BYTE))
			{
				this.auto = tag.getBoolean("auto");
			}
			if(tag.hasKey("rate", Constants.NBT.TAG_INT))
			{
				this.rate = tag.getInteger("rate");
			}
			if(tag.hasKey("gripType", Constants.NBT.TAG_INT))
			{
				this.gripType = GripType.values()[tag.getInteger("gripType")];
			}
			if(tag.hasKey("maxAmmo", Constants.NBT.TAG_INT))
			{
				this.maxAmmo = tag.getInteger("maxAmmo");
			}
			if(tag.hasKey("reloadSpeed", Constants.NBT.TAG_INT))
			{
				this.reloadSpeed = tag.getInteger("reloadSpeed");
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
			return general;
		}
	}
	
	public static class Projectile implements INBTSerializable<NBTTagCompound>
	{
		@Config.Ignore
		public ItemAmmo.Type type;

		@Optional
		@Config.Name("Visible")
		@Config.Comment("If true, will render the projectile. This is disabled for fast projectiles because of rendering issues")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.visible")
		public boolean visible;

		@Config.Name("Damage")
		@Config.Comment("The damage this gun will cause. Each value is equivalent to half a heart.")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.damage")
		public float damage;

		@Config.Name("Size")
		@Config.Comment("The amount of bullets added to the gun on each reload")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.size")
		public float size;

		@Config.Name("Speed")
		@Config.Comment("The amount of bullets added to the gun on each reload")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.speed")
		public double speed;

		@Config.Name("Life")
		@Config.Comment("The amount of ticks before the projectile is removed for the world")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.life")
		public int life;

		@Optional
		@Config.Name("Gravity")
		@Config.Comment("If true, the projectile will be affected by gravity and drop")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.gravity")
		public boolean gravity;

		@Optional
		@Config.Name("Damage Falloff")
		@Config.Comment("If true, the damage of the gun will reduce the further the target is away")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.damage_fall_off")
		public boolean damageReduceOverLife;

		@Optional
		@Config.Name("Reduce Damage Not Zoomed")
		@Config.Comment("If true, the damage of the gun will be reduced if not zoomed")
		@Config.LangKey(GunConfig.PREFIX + "gun.projectile.damage_reduce_zoomed")
		public boolean damageReduceIfNotZoomed;

		@Override
		public NBTTagCompound serializeNBT() 
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("type", this.type.ordinal());
			tag.setBoolean("visible", this.visible);
			tag.setFloat("damage", this.damage);
			tag.setFloat("size", this.size);
			tag.setDouble("speed", this.speed);
			tag.setInteger("life", this.life);
			tag.setBoolean("gravity", this.gravity);
			tag.setBoolean("damageReduceOverLife", this.damageReduceOverLife);
			tag.setBoolean("damageReduceIfNotZoomed", this.damageReduceIfNotZoomed);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag) 
		{
			if(tag.hasKey("type", Constants.NBT.TAG_INT))
			{
				this.type = ItemAmmo.Type.values()[tag.getInteger("type")];
			}
			if(tag.hasKey("visible", Constants.NBT.TAG_BYTE))
			{
				this.visible = tag.getBoolean("visible");
			}
			if(tag.hasKey("damage", Constants.NBT.TAG_FLOAT))
			{
				this.damage = tag.getFloat("damage");
			}
			if(tag.hasKey("size", Constants.NBT.TAG_FLOAT))
			{
				this.size = tag.getFloat("size");
			}
			if(tag.hasKey("speed", Constants.NBT.TAG_DOUBLE))
			{
				this.speed = tag.getDouble("speed");
			}
			if(tag.hasKey("life", Constants.NBT.TAG_INT))
			{
				this.life = tag.getInteger("life");
			}
			if(tag.hasKey("gravity", Constants.NBT.TAG_BYTE))
			{
				this.gravity = tag.getBoolean("gravity");
			}
			if(tag.hasKey("damageReduceOverLife", Constants.NBT.TAG_BYTE))
			{
				this.damageReduceOverLife = tag.getBoolean("damageReduceOverLife");
			}
			if(tag.hasKey("damageReduceIfNotZoomed", Constants.NBT.TAG_BYTE))
			{
				this.damageReduceIfNotZoomed = tag.getBoolean("damageReduceIfNotZoomed");
			}
		}

		public Projectile copy()
		{
			Projectile projectile = new Projectile();
			projectile.type = type;
			projectile.visible = visible;
			projectile.damage = damage;
			projectile.size = size;
			projectile.speed = speed;
			projectile.life = life;
			projectile.gravity = gravity;
			projectile.damageReduceOverLife = damageReduceOverLife;
			projectile.damageReduceIfNotZoomed = damageReduceIfNotZoomed;
			return projectile;
		}
	}
	
	public static class Sounds implements INBTSerializable<NBTTagCompound>
	{
		@Config.Name("Fire")
		@Config.Comment("The sound played when the gun is fired")
		@Config.LangKey(GunConfig.PREFIX + "gun.sounds.fire")
		public String fire = "";

		@Config.Name("Reload")
		@Config.Comment("The sound played when the gun is reloading")
		@Config.LangKey(GunConfig.PREFIX + "gun.sounds.reload")
		public String reload = "";

		@Config.Name("Cock")
		@Config.Comment("The sound played when the gun is cocked")
		@Config.LangKey(GunConfig.PREFIX + "gun.sounds.cock")
		public String cock = "";

		@Optional
		@Config.Name("Silenced")
		@Config.Comment("The sound played when gun is fired with the silencer attached")
		@Config.LangKey(GunConfig.PREFIX + "gun.sounds.silenced_fire")
		public String silencedFire = "silenced_fire";


		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("fire", fire);
			tag.setString("reload", reload);
			tag.setString("cock", cock);
			tag.setString("silencedFire", silencedFire);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag)
		{
			if(tag.hasKey("fire", Constants.NBT.TAG_STRING))
			{
				this.fire = tag.getString("fire");
			}
			if(tag.hasKey("reload", Constants.NBT.TAG_STRING))
			{
				this.reload = tag.getString("reload");
			}
			if(tag.hasKey("cock", Constants.NBT.TAG_STRING))
			{
				this.cock = tag.getString("cock");
			}
			if(tag.hasKey("silencedFire", Constants.NBT.TAG_STRING))
			{
				this.silencedFire = tag.getString("silencedFire");
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
		@Optional public Flash flash;

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
			display.flash = flash.copy();
			return display;
		}
	}

	public static class Modules
	{
		@Optional public Zoom zoom;
		public Attachments attachments = new Attachments();

		public static class Zoom extends Positioned
		{
			@Optional public float fovModifier;
			@Optional public boolean smooth;

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
			@Optional public Scope scope;
			@Optional public Barrel barrel;

			public static class Scope extends ScaledPositioned
			{
				@Optional public boolean smooth;

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
		@Optional public double xOffset;
		@Optional public double yOffset;
		@Optional public double zOffset;
	}

	public static class ScaledPositioned extends Positioned
	{
		@Optional public double scale = 1.0;
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Optional {}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("id", id);
		tag.setTag("general", general.serializeNBT());
		tag.setTag("projectile", projectile.serializeNBT());
		tag.setTag("sounds", sounds.serializeNBT());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound tag)
	{
		if(tag.hasKey("id", Constants.NBT.TAG_STRING))
		{
			this.id = tag.getString("id");
		}
		if(tag.hasKey("general", Constants.NBT.TAG_COMPOUND))
		{
			this.general.deserializeNBT(tag.getCompoundTag("general"));
		}
		if(tag.hasKey("projectile", Constants.NBT.TAG_COMPOUND))
		{
			this.projectile.deserializeNBT(tag.getCompoundTag("projectile"));
		}
		if(tag.hasKey("sounds", Constants.NBT.TAG_COMPOUND))
		{
			this.sounds.deserializeNBT(tag.getCompoundTag("sounds"));
		}
	}

	public Gun copy()
	{
		Gun gun = new Gun();
		gun.id = id;
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

	@Nullable
	public static ItemStack getScope(ItemStack gun)
	{
		if(gun.hasTagCompound())
		{
			if(gun.getTagCompound().hasKey("attachments", Constants.NBT.TAG_COMPOUND))
			{
				NBTTagCompound attachment = gun.getTagCompound().getCompoundTag("attachments");
				if(attachment.hasKey("scope", Constants.NBT.TAG_COMPOUND))
				{
					return new ItemStack(attachment.getCompoundTag("scope"));
				}
			}
		}
		return null;
	}

	public static ItemStack getAttachment(IAttachment.Type type, ItemStack gun)
	{
		if(gun.hasTagCompound())
		{
			if(gun.getTagCompound().hasKey("attachments", Constants.NBT.TAG_COMPOUND))
			{
				NBTTagCompound attachment = gun.getTagCompound().getCompoundTag("attachments");
				if(attachment.hasKey(type.getName(), Constants.NBT.TAG_COMPOUND))
				{
					return new ItemStack(attachment.getCompoundTag(type.getName()));
				}
			}
		}
		return ItemStack.EMPTY;
	}
}
