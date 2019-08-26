package com.mrcrayfish.guns.object;

import com.google.gson.*;
import com.mrcrayfish.guns.item.IAttachment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

public class Gun implements INBTSerializable<NBTTagCompound>
{
	@Ignored public ServerGun serverGun;
	public String id;
	public General general = new General();
	public Projectile projectile = new Projectile();
	public Sounds sounds = new Sounds();
	public Display display = new Display();
	public Modules modules = new Modules();

	public static class General implements INBTSerializable<NBTTagCompound>
	{
		@Optional public boolean auto = false;
		public int rate;
		public GripType gripType;
		public int maxAmmo;
		@Optional public int reloadSpeed = 1;
		@Optional public float recoilAngle;
		@Optional public float recoilKick;
		@Optional public float recoilDurationOffset;
		@Optional public int projectileAmount = 1;
		@Optional public boolean alwaysSpread;
		@Optional public float spread;

		public int getMaxAmmo(Gun modifiedGun)
		{
			int maxAmmo = this.maxAmmo;
			if(modifiedGun.serverGun != null)
			{
				maxAmmo = modifiedGun.serverGun.maxAmmo;
			}
			if(modifiedGun.general.maxAmmo != maxAmmo)
			{
				maxAmmo = modifiedGun.general.maxAmmo;
			}
			return maxAmmo;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("auto", auto);
			tag.setInteger("rate", rate);
			tag.setInteger("gripType", gripType.ordinal());
			tag.setInteger("maxAmmo", maxAmmo);
			tag.setInteger("reloadSpeed", reloadSpeed);
			tag.setFloat("recoilAngle", recoilAngle);
			tag.setFloat("recoilKick", recoilKick);
			tag.setFloat("recoilDurationOffset", recoilDurationOffset);
			tag.setInteger("projectileAmount", projectileAmount);
			tag.setBoolean("alwaysSpread", alwaysSpread);
			tag.setFloat("spread", spread);
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
			if(tag.hasKey("recoilAngle", Constants.NBT.TAG_FLOAT))
			{
				this.recoilAngle = tag.getFloat("recoilAngle");
			}
			if(tag.hasKey("recoilKick", Constants.NBT.TAG_FLOAT))
			{
				this.recoilKick = tag.getFloat("recoilKick");
			}
			if(tag.hasKey("recoilDurationOffset", Constants.NBT.TAG_FLOAT))
			{
				this.recoilDurationOffset = tag.getFloat("recoilDurationOffset");
			}
			if(tag.hasKey("projectileAmount", Constants.NBT.TAG_INT))
			{
				this.projectileAmount = tag.getInteger("projectileAmount");
			}
			if(tag.hasKey("alwaysSpread", Constants.NBT.TAG_BYTE))
			{
				this.alwaysSpread = tag.getBoolean("alwaysSpread");
			}
			if(tag.hasKey("spread", Constants.NBT.TAG_FLOAT))
			{
				this.spread = tag.getFloat("spread");
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
	
	public static class Projectile implements INBTSerializable<NBTTagCompound>
	{
		public ResourceLocation item;
		@Optional public boolean visible;
		public float damage;
		public float size;
		public double speed;
		public int life;
		@Optional public boolean gravity;
		@Optional public boolean damageReduceOverLife;
		@Optional public boolean damageReduceIfNotZoomed;

		public float getDamage(Gun modifiedGun)
		{
			float damage = this.damage;
			if(modifiedGun.serverGun != null)
			{
				damage = modifiedGun.serverGun.damage;
			}
			if(modifiedGun.projectile.damage != this.damage && modifiedGun.projectile.damage != damage)
			{
				damage = modifiedGun.projectile.damage;
			}
			return damage;
		}

		@Override
		public NBTTagCompound serializeNBT() 
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("item", this.item.toString());
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
			if(tag.hasKey("item", Constants.NBT.TAG_STRING))
			{
				this.item = new ResourceLocation(tag.getString("item"));
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
			projectile.item = item;
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

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface Ignored {}

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
		gun.serverGun = serverGun;
		gun.id = id;
		gun.general = general.copy();
		gun.projectile = projectile.copy();
		gun.sounds = sounds.copy();
		gun.display = display.copy();
		gun.modules = modules.copy();
		return gun;
	}

	public void setServerGun(ServerGun serverGun)
	{
		this.serverGun = serverGun;
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
			JsonObject general = JsonUtils.getJsonObject(json.getAsJsonObject(), "General");
			this.base.general.maxAmmo = JsonUtils.getInt(general, "Max Ammo", this.base.general.maxAmmo);
			this.base.general.reloadSpeed = JsonUtils.getInt(general, "Reload Speed", this.base.general.reloadSpeed);
			this.base.general.projectileAmount = JsonUtils.getInt(general, "Projectile Count", this.base.general.projectileAmount);
			this.base.general.alwaysSpread = JsonUtils.getBoolean(general, "Always Spread", this.base.general.alwaysSpread);
			this.base.general.spread = JsonUtils.getFloat(general, "Spread", this.base.general.spread);

			JsonObject projectile = JsonUtils.getJsonObject(json.getAsJsonObject(), "Projectile");
			this.base.projectile.damage = JsonUtils.getFloat(projectile, "Damage", this.base.projectile.damage);
			this.base.projectile.damageReduceOverLife = JsonUtils.getBoolean(projectile, "Damage Falloff", this.base.projectile.damageReduceOverLife);
			this.base.projectile.gravity = JsonUtils.getBoolean(projectile, "Gravity", this.base.projectile.gravity);
			this.base.projectile.life = JsonUtils.getInt(projectile, "Ticks Before Removed", this.base.projectile.life);
			this.base.projectile.damageReduceIfNotZoomed = JsonUtils.getBoolean(projectile, "Reduce Damage If Not Zoomed", this.base.projectile.damageReduceIfNotZoomed);
			this.base.projectile.size = JsonUtils.getFloat(projectile, "Size", this.base.projectile.size);
			this.base.projectile.speed = JsonUtils.getFloat(projectile, "Speed", (float) this.base.projectile.speed);

			return this.base;
		}
	}
}
