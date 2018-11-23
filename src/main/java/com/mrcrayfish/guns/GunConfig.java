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
@Config.LangKey("config." + Reference.MOD_ID + ".title")
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

	@Config.Name("Client")
	@Config.Comment("Client-only configs.")
	@Config.LangKey(PREFIX + "client")
	public static final Client CLIENT = new Client();

	@Config.Name("Server")
	@Config.Comment("Server-only configs.")
	@Config.LangKey(PREFIX + "server")
	public static final Server SERVER = new Server();

	public static class Client
	{
		@Config.Name("Sounds")
		@Config.Comment("Control sounds triggered by guns")
		@Config.LangKey(PREFIX + "client.sounds")
		public Sounds sound = new Sounds();

		@Config.Name("Display")
		@Config.Comment("Configuration for display related options")
		@Config.LangKey(PREFIX + "client.display")
		public Display display = new Display();
	}

	public static class Sounds
	{
		@Config.Name("Play Hit Sound")
		@Config.Comment("If true, a ding sound will play when you successfully hit a player with a gun")
		@Config.LangKey(PREFIX + "client.sounds.hit_sound")
		public boolean hitSound = true;
	}

	public static class Server
	{
		@Config.Name("Aggro Mobs")
		@Config.Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@Config.LangKey(PREFIX + "server.aggro")
		public AggroMobs aggroMobs = new AggroMobs();

		@Config.Name("Guns")
		@Config.Comment("Change the properties of guns")
		@Config.LangKey(PREFIX + "server.guns")
		public Guns guns = new Guns();
	}

	public static class AggroMobs
	{
		@Config.Name("Aggro Mobs Enabled")
		@Config.Comment("If true, nearby mobs are angered and/or scared by the firing of guns.")
		@Config.LangKey(PREFIX + "server.aggro.enabled")
		public boolean enabled = true;

		@Config.Name("Anger Hostile Mobs")
		@Config.Comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.")
		@Config.LangKey(PREFIX + "server.aggro.hostile")
		public boolean angerHostileMobs = true;

		@Config.Name("Range Silenced")
		@Config.Comment("Any mobs within a sphere of this radius will aggro on the shooter of a silenced gun.")
		@Config.LangKey(PREFIX + "server.aggro.silenced")
		@RangeDouble(min = 0)
		public double rangeSilenced = 10;

		@Config.Name("Range Unsilenced")
		@Config.Comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.")
		@Config.LangKey(PREFIX + "server.aggro.unsilenced")
		@RangeDouble(min = 0)
		public double rangeUnsilenced = 20;

		@Config.Name("Exempt Mob Classes")
		@Config.Comment("Any mobs of classes with class paths in this list will not aggro on shooters.")
		@Config.LangKey(PREFIX + "server.aggro.exempt")
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

	public static class Display
	{
		@Config.Name("Workbench Animation")
		@Config.Comment("If true, an animation is performed while cycling items in the Workbench")
		@Config.LangKey(PREFIX + "client.display.workbench_animation")
		public boolean workbenchAnimation = true;
	}

	public static class Guns
	{
		@Config.Name("Pistol")
		@Config.Comment("Change the properties of the Pistol")
		@Config.LangKey(PREFIX + "server.guns.pistol")
		public Gun pistol = ID_TO_GUN.get("handgun");

		@Config.Name("Shotgun")
		@Config.Comment("Change the properties of the Shotgun")
		@Config.LangKey(PREFIX + "server.guns.shotgun")
		public Gun shotgun = ID_TO_GUN.get("shotgun");

		@Config.Name("Rifle")
		@Config.Comment("Change the properties of the Rifle")
		@Config.LangKey(PREFIX + "server.guns.rifle")
		public Gun rifle = ID_TO_GUN.get("rifle");

		@Config.Name("Grenade Launcher")
		@Config.Comment("Change the properties of the Grenade Launcher")
		@Config.LangKey(PREFIX + "server.guns.grenade_launcher")
		public Gun grenadeLauncher = ID_TO_GUN.get("grenade_launcher");

		@Config.Name("Bazooka")
		@Config.Comment("Change the properties of the Bazooka")
		@Config.LangKey(PREFIX + "server.guns.bazooka")
		public Gun bazooka = ID_TO_GUN.get("bazooka");

		@Config.Name("Minigun")
		@Config.Comment("Change the properties of the Minigun")
		@Config.LangKey(PREFIX + "server.guns.chain_gun")
		public Gun chainGun = ID_TO_GUN.get("chain_gun");

		@Config.Name("Assault Rifle")
		@Config.Comment("Change the properties of the Assault Rifle")
		@Config.LangKey(PREFIX + "server.guns.assault_rifle")
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
			if(field.getDeclaredAnnotation(Gun.Ignored.class) != null || field.getDeclaredAnnotation(Gun.Optional.class) != null)
				continue;

			if(field.get(t) == null)
			{
				return false;
			}

			if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
			{
				return validateFields(field.get(t));
			}
		}
		return true;
	}
}
