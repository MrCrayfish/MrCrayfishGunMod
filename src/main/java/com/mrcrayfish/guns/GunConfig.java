package com.mrcrayfish.guns;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Config(modid = Reference.MOD_ID)
@LangKey("config." + Reference.MOD_ID + ".title")
@EventBusSubscriber(modid = Reference.MOD_ID)
public class GunConfig
{
	@Config.Ignore
	public static final ImmutableMap<String, Gun> ID_TO_GUN;

	static
	{
		ImmutableMap.Builder<String, Gun> builder = ImmutableMap.builder();
		Reader reader = new InputStreamReader(ModGuns.class.getResourceAsStream("/assets/cgm/guns.json"));
		JsonParser parser = new JsonParser();
		JsonArray elements = parser.parse(reader).getAsJsonArray();
		try
		{
			Gson gson = new Gson();
			for(JsonElement element : elements)
			{
				Gun gun = gson.fromJson(element, new TypeToken<Gun>() {}.getType());
				if(!validateFields(gun))
				{
					if(gun.id != null)
					{
						throw new NullPointerException("The gun '" + gun.id + "' is missing required attributes");
					}
					else
					{
						throw new NullPointerException("Invalid gun entry");
					}
				}
				builder.put(gun.id, gun);
			}
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
		}
		ID_TO_GUN = builder.build();
	}

	@Config.Ignore
    public static final String PREFIX = "config." + Reference.MOD_ID + ".";

	@Name("Client")
	@Comment("Client-only configs.")
	@LangKey(PREFIX + "client")
	public static final Client CLIENT = new Client();

	@Name("Server")
	@Comment("Server-only configs.")
	@LangKey(PREFIX + "server")
	public static final Server SERVER = new Server();

	public static class Client
	{
		@Name("Sounds")
		@Comment("Control sounds triggered by guns")
		@LangKey(PREFIX + "client.sounds")
		public Sounds sound = new Sounds();
	}

	public static class Sounds
	{
		@Name("Play Hit Sound")
		@Comment("If true, a ding sound will play when you successfully hit a player with a gun")
		@LangKey(PREFIX + "client.sounds.hit_sound")
		public boolean hitSound = true;
	}

	public static class Server
	{
		@Name("Aggro Mobs")
		@Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@LangKey(PREFIX + "server.aggro")
		public AggroMobs aggroMobs = new AggroMobs();

		@Name("Guns")
		@Comment("Change the properties of guns")
		@LangKey(PREFIX + "server.guns")
		public Guns guns = new Guns();
	}

	public static class AggroMobs
	{
		@Name("Aggro Mobs Enabled")
		@Comment("If true, nearby mobs are angered and/or scared by the firing of guns.")
		@LangKey(PREFIX + "server.aggro.enabled")
		public boolean enabled = true;

		@Name("Anger Hostile Mobs")
		@Comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.")
		@LangKey(PREFIX + "server.aggro.hostile")
		public boolean angerHostileMobs = true;

		@Name("Range Silenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of a silenced gun.")
		@LangKey(PREFIX + "server.aggro.silenced")
		@RangeDouble(min = 0)
		public double rangeSilenced = 10;

		@Name("Range Unsilenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.")
		@LangKey(PREFIX + "server.aggro.unsilenced")
		@RangeDouble(min = 0)
		public double rangeUnsilenced = 20;

		@Name("Exempt Mob Classes")
		@Comment("Any mobs of classes with class paths in this list will not aggro on shooters.")
		@LangKey(PREFIX + "server.aggro.exempt")
		public String[] exemptClassNames = new String[] {"net.minecraft.entity.passive.EntityVillager"};
		public static Set<Class> exemptClasses = Sets.<Class>newHashSet();

		public void setExemptionClasses()
		{
			exemptClasses.clear();
			for (String className : exemptClassNames)
			{
				String prefix = "Exempt aggro mob class '" + className;
				try
				{
					Class<?> classMob = Class.forName(className);
					if (EntityLivingBase.class.isAssignableFrom(classMob))
					{
						exemptClasses.add(classMob);
					}
					else
					{
						MrCrayfishGunMod.logger.warn(prefix + "' must exend EntityLivingBase.");
					}
				}
				catch (ClassNotFoundException e)
				{
					MrCrayfishGunMod.logger.warn(prefix + "' was not found:", e);
				}
			}
		}
	}

	public static class Guns
	{
		@Name("Pistol")
		@Comment("Change the properties of the Pistol")
		@LangKey(PREFIX + "server.guns.pistol")
		public Gun pistol = ID_TO_GUN.get("handgun");

		@Name("Shotgun")
		@Comment("Change the properties of the Shotgun")
		@LangKey(PREFIX + "server.guns.shotgun")
		public Gun shotgun = ID_TO_GUN.get("shotgun");

		@Name("Rifle")
		@Comment("Change the properties of the Rifle")
		@LangKey(PREFIX + "server.guns.rifle")
		public Gun rifle = ID_TO_GUN.get("rifle");

		@Name("Grenade Launcher")
		@Comment("Change the properties of the Grenade Launcher")
		@LangKey(PREFIX + "server.guns.grenade_launcher")
		public Gun grenadeLauncher = ID_TO_GUN.get("grenade_launcher");

		@Name("Bazooka")
		@Comment("Change the properties of the Bazooka")
		@LangKey(PREFIX + "server.guns.bazooka")
		public Gun bazooka = ID_TO_GUN.get("bazooka");

		@Name("Minigun")
		@Comment("Change the properties of the Minigun")
		@LangKey(PREFIX + "server.guns.chain_gun")
		public Gun chainGun = ID_TO_GUN.get("chain_gun");

		@Name("Assault Rifle")
		@Comment("Change the properties of the Assault Rifle")
		@LangKey(PREFIX + "server.guns.assault_rifle")
		public Gun assaultRifle = ID_TO_GUN.get("assault_rifle");
	}

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equalsIgnoreCase(Reference.MOD_ID))
		{
			ConfigManager.sync(Reference.MOD_ID, Type.INSTANCE);
			SERVER.aggroMobs.setExemptionClasses();
		}
	}

	private static <T> boolean validateFields(@Nonnull T t) throws IllegalAccessException
	{
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields)
		{
			if(field.getDeclaredAnnotation(Gun.Optional.class) == null)
			{
				if(field.get(t) == null)
				{
					return false;
				}

				if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
				{
					return validateFields(field.get(t));
				}
			}
		}
		return true;
	}
}
