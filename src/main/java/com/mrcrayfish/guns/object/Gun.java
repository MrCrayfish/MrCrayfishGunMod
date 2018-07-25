package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.annotation.*;

public class Gun
{
	public String id;
	public General general = new General();
	public Projectile projectile;
	public Sounds sounds;
	public Display display = new Display();
	public Modules modules = new Modules();

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

	public static class General
	{
		@Optional public boolean auto = false;
		public int rate;
		public GripType gripType;
		public int maxAmmo;
		@Optional public int reloadSpeed = 1;
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
		@Optional public String silenced_fire = "silenced_fire";
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
}
