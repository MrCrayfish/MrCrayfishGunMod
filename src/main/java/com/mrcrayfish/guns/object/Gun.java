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

public class Gun
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

	public static class General
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
			this.type = ItemAmmo.Type.values()[tag.getInteger("type")];
			this.visible = tag.getBoolean("visible");
			this.damage = tag.getFloat("damage");
			this.size = tag.getFloat("size");
			this.speed = tag.getDouble("speed");
			this.life = tag.getInteger("life");
			this.gravity = tag.getBoolean("gravity");
			this.damageReduceOverLife = tag.getBoolean("damageReduceOverLife");
			this.damageReduceIfNotZoomed = tag.getBoolean("damageReduceIfNotZoomed");
		}
	}
	
	public static class Sounds
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
		public String silenced_fire = "silenced_fire";
	}

	public static class Display
	{
		@Optional public Flash flash;

		public static class Flash extends ScaledPositioned {}
	}

	public static class Modules
	{
		@Optional public Zoom zoom;
		public Attachments attachments = new Attachments();

		public static class Zoom extends Positioned
		{
			@Optional public float fovModifier;
			@Optional public boolean smooth;
		}

		public static class Attachments
		{
			@Optional public Scope scope;
			@Optional public Barrel barrel;

			public static class Scope extends ScaledPositioned
			{
				@Optional public boolean smooth;
			}

			public static class Barrel extends ScaledPositioned {}
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
	public @interface Optional
	{

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
