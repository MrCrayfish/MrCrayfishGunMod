package com.mrcrayfish.guns.object;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Gun
{
	public String id;
	public General general = new General();
	public Projectile projectile;
	public Sounds sounds;
	public Display display = new Display();
	public Modules modules = new Modules();

	public boolean canAttachScope()
	{
		return modules.attachments != null && modules.attachments.scope != null;
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

	public static class General
	{
		@Optional public boolean auto = false;
		public int rate;
		public GripType gripType;
		public int maxAmmo;
	}
	
	public static class Projectile implements INBTSerializable<NBTTagCompound>
	{
		public ItemAmmo.Type type;
		public boolean visible;
		public float damage;
		public float size;
		public double speed;
		public int life;
		@Optional public boolean gravity;
		@Optional public boolean damageReduceOverLife;
		@Optional public boolean damageReduceIfNotZoomed;

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
		public String fire;
		public String reload;
		public String cock;
	}

	public static class Display
	{
		@Optional public Flash flash;

		public static class Flash
		{
			@Optional public double xOffset;
			@Optional public double yOffset;
			@Optional public double zOffset;
		}
	}

	public static class Modules
	{
		@Optional public Zoom zoom;
		public Attachments attachments = new Attachments();

		public static class Zoom
		{
			@Optional public float fovModifier;
			@Optional public boolean smooth;
			@Optional public double xOffset;
			@Optional public double yOffset;
			@Optional public double zOffset;
		}

		public static class Attachments
		{
			@Optional public Scope scope;

			public static class Scope
			{
				@Optional public boolean smooth;
				@Optional public double xOffset;
				@Optional public double yOffset;
				@Optional public double zOffset;
			}
		}
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Optional
	{

	}
}
