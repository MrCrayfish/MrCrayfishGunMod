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
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.ServerGun;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;

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
	@Config.LangKey(Client.PREFIX)
	public static final Client CLIENT = new Client();

	@Config.Name("Server")
	@Config.Comment("Server-only configs.")
	@Config.LangKey(Server.PREFIX)
	public static final Server SERVER = new Server();

	public static class Client
	{
	    private static final String PREFIX = GunConfig.PREFIX + "client";

		@Config.Name("Sounds")
		@Config.Comment("Control sounds triggered by guns")
		@Config.LangKey(Sounds.PREFIX)
		public Sounds sound = new Sounds();

		@Config.Name("Display")
		@Config.Comment("Configuration for display related options")
		@Config.LangKey(Display.PREFIX)
		public Display display = new Display();
	}

	public static class Sounds
	{
	    private static final String PREFIX = Client.PREFIX + ".sounds";

		@Config.Name("Play Hit Sound")
		@Config.Comment("If true, a ding sound will play when you successfully hit a player with a gun")
		@Config.LangKey(PREFIX + ".hit_sound")
		public boolean hitSound = true;
	}

	public static class Server
	{
	    private static final String PREFIX = GunConfig.PREFIX + "server";

		@Config.Name("Aggro Mobs")
		@Config.Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@Config.LangKey(AggroMobs.PREFIX)
		public AggroMobs aggroMobs = new AggroMobs();

		@Config.Name("Guns")
		@Config.Comment("Change the properties of guns")
		@Config.LangKey(Guns.PREFIX)
		public Guns guns = new Guns();

		@Config.Name("Stun Grenades")
        @Config.Comment("Blinding/deafening properties of stun grenades.")
        @Config.LangKey(StunGrenades.PREFIX)
        public StunGrenades stunGrenades = new StunGrenades();
	}

	public static class AggroMobs
	{
	    private static final String PREFIX = Server.PREFIX + ".aggro";

		@Config.Name("Aggro Mobs Enabled")
		@Config.Comment("If true, nearby mobs are angered and/or scared by the firing of guns.")
		@Config.LangKey(PREFIX + ".enabled")
		public boolean enabled = true;

		@Config.Name("Anger Hostile Mobs")
		@Config.Comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.")
		@Config.LangKey(PREFIX + ".hostile")
		public boolean angerHostileMobs = true;

		@Config.Name("Range Silenced")
		@Config.Comment("Any mobs within a sphere of this radius will aggro on the shooter of a silenced gun.")
		@Config.LangKey(PREFIX + ".silenced")
		@Config.RangeDouble(min = 0)
		public double rangeSilenced = 10;

		@Config.Name("Range Unsilenced")
		@Config.Comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.")
		@Config.LangKey(PREFIX + ".unsilenced")
		@Config.RangeDouble(min = 0)
		public double rangeUnsilenced = 20;

		@Config.Name("Exempt Mob Classes")
		@Config.Comment("Any mobs of classes with class paths in this list will not aggro on shooters.")
		@Config.LangKey(PREFIX + ".exempt")
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
	    private static final String PREFIX = Client.PREFIX + ".display";

		@Config.Name("Workbench Animation")
		@Config.Comment("If true, an animation is performed while cycling items in the Workbench")
		@Config.LangKey(PREFIX + ".workbench_animation")
		public boolean workbenchAnimation = true;
	}

	public static class Guns
	{
	    private static final String PREFIX = Server.PREFIX + ".guns";

		@Config.Name("Pistol")
		@Config.Comment("Change the properties of the Pistol")
		@Config.LangKey(PREFIX + ".pistol")
		public Gun pistol = ID_TO_GUN.get("handgun");

		@Config.Name("Shotgun")
		@Config.Comment("Change the properties of the Shotgun")
		@Config.LangKey(PREFIX + ".shotgun")
		public Gun shotgun = ID_TO_GUN.get("shotgun");

		@Config.Name("Rifle")
		@Config.Comment("Change the properties of the Rifle")
		@Config.LangKey(PREFIX + ".rifle")
		public Gun rifle = ID_TO_GUN.get("rifle");

		@Config.Name("Grenade Launcher")
		@Config.Comment("Change the properties of the Grenade Launcher")
		@Config.LangKey(PREFIX + ".grenade_launcher")
		public Gun grenadeLauncher = ID_TO_GUN.get("grenade_launcher");

		@Config.Name("Bazooka")
		@Config.Comment("Change the properties of the Bazooka")
		@Config.LangKey(PREFIX + ".bazooka")
		public Gun bazooka = ID_TO_GUN.get("bazooka");

		@Config.Name("Minigun")
		@Config.Comment("Change the properties of the Minigun")
		@Config.LangKey(PREFIX + ".chain_gun")
		public Gun chainGun = ID_TO_GUN.get("chain_gun");

		@Config.Name("Assault Rifle")
		@Config.Comment("Change the properties of the Assault Rifle")
		@Config.LangKey(PREFIX + ".assault_rifle")
		public Gun assaultRifle = ID_TO_GUN.get("assault_rifle");
	}

	public static class StunGrenades
    {
	    private static final String PREFIX = Server.PREFIX + ".grenade.stun";

	    @Config.Name("Blind")
        @Config.Comment("Blinding properties of stun grenades.")
        @Config.LangKey(Blind.PREFIX)
        public Blind blind = new Blind();

	    @Config.Name("Deafen")
        @Config.Comment("Deafening properties of stun grenades.")
        @Config.LangKey(Deafen.PREFIX)
        public Deafen deafen = new Deafen();
    }

	public static class Blind
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".blind";

	    @Config.Name("Effect Criteria")
        @Config.Comment("Criteria that determines the presence/absence and duration of the blinding effect.")
        @Config.LangKey(EffectCriteria.PREFIX + ".blind")
        public EffectCriteria criteria = new EffectCriteria(15, 220, 10, 170, 0.75, true);

        @Config.Name("Overlay Alpha")
        @Config.Comment("After the duration drops to this many ticks, the transparency of the overlay when blinded will gradually fade to 0 alpha.")
        @Config.LangKey(PREFIX + ".overlay.alpha")
        @Config.RangeInt(min = 0, max = 255)
        @Config.RequiresWorldRestart
        public int alphaOverlay = 255;
        public static int alphaOverlaySynced = 255;

        @Config.Name("Overlay Fade Threshold")
        @Config.Comment("Transparency of the overlay when blinded will be this alpha value, before eventually fading to 0 alpha.")
        @Config.LangKey(PREFIX + ".overlay.fade")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int alphaFadeThreshold = 40;
        public static int alphaFadeThresholdSynced = Integer.MAX_VALUE;

        @Config.Name("Blind Mobs")
        @Config.Comment("If true, hostile mobs will be unable to target entities while they have the blinded effect.")
        @Config.LangKey(PREFIX + ".mobs.blind")
        public boolean blindMobs;
    }

	public static class Deafen
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".deafen";

	    @Config.Name("Effect Criteria")
        @Config.Comment("Criteria that determines the presence/absence and duration of the deafening effect.")
        @Config.LangKey(EffectCriteria.PREFIX + ".deafen")
        public EffectCriteria criteria = new EffectCriteria(15, 280, 100, 360, 0.75, false);

        @Config.Name("Sound Percentage")
        @Config.Comment("Volume of most game sounds when deafened will play at this percent, before eventually fading back to %100.")
        @Config.LangKey(PREFIX + ".sound.percentage")
        @Config.RangeDouble(min = 0, max = 1)
        @Config.RequiresWorldRestart
        public double soundPercentage = 0.05;
        public static float soundPercentageSynced = 0;

        @Config.Name("Sound Fade Threshold")
        @Config.Comment("After the duration drops to this many ticks, the ringing volume will gradually fade to 0 and other sound volumes will fade back to %100.")
        @Config.LangKey(PREFIX + ".sound.fade")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int soundFadeThreshold = 90;
        public static int soundFadeThresholdSynced = Integer.MAX_VALUE;

        @Config.Name("Ring Volume")
        @Config.Comment("Volume of the ringing sound when deafened will play at this volume, before eventually fading to 0.")
        @Config.LangKey(PREFIX + ".sound.ring")
        @Config.RangeDouble(min = 0)
        @Config.RequiresWorldRestart
        public double ringVolume = 1;
        public static float ringVolumeSynced = 1;

        @Config.Name("Panic Mobs")
        @Config.Comment("If true, peaceful mobs will panic upon being deafened by a stun grenade explosion.")
        @Config.LangKey(PREFIX + ".mobs.panic")
        public boolean panicMobs;
    }

	public static class EffectCriteria
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".effect_criteria";

	    @Config.Name("Radius")
        @Config.Comment("Grenade must be no more than this many meters away to have an effect.")
        @Config.LangKey(PREFIX + ".radius")
        @Config.RangeDouble(min = 0)
        public double radius;

        @Config.Name("Max Duration")
        @Config.Comment("Effect will have this duration (in ticks) if the grenade is directly at the player's eyes while looking directly at it.")
        @Config.LangKey(PREFIX + ".duration.max")
        @Config.RangeInt(min = 0)
        public int durationMax;

        @Config.Name("Min Duration By Distance")
        @Config.Comment("Effect will have this duration (in ticks) if the grenade is the maximum distance from the player's eyes while looking directly at it.")
        @Config.LangKey(PREFIX + ".duration.min")
        @Config.RangeInt(min = 0)
        public int durationMin;

        @Config.Name("Angle Of Effect")
        @Config.Comment("Angle between the eye/looking direction and the eye/grenade direction must be no more than half this many degrees to have an effect.")
        @Config.LangKey(PREFIX + ".angle.effect")
        @Config.RangeDouble(min = 0, max = 360)
        public double angleEffect;

        @Config.Name("Max Attenuation By Angle")
        @Config.Comment("After duration is attenuated by distance, it will be further attenuated depending on the angle (in degrees) between the eye/looking "
                + "direction and the eye/grenade direction. This is done by multiplying it by 1 (no attenuation) if the angle is 0; and by this value if "
                + "the angle is the maximum within the angle of effect.")
        @Config.LangKey(PREFIX + ".angle.attenuation.max")
        @Config.RangeDouble(min = 0, max = 1)
        public double angleAttenuationMax;

        @Config.Name("Raytrace Opaque Blocks")
        @Config.Comment("If true, the effect is only applied if the line between the eyes and the grenade does not intersect any non-liquid blocks with an opacity greater than 0.")
        @Config.LangKey(PREFIX + ".raytrace.opaque")
        public boolean raytraceOpaqueBlocks;

        public EffectCriteria(double radius, int durationMax, int durationMin, double angleEffect, double angleAttenuationMax, boolean raytraceOpaqueBlocks)
        {
            this.radius = radius;
            this.durationMax = durationMax;
            this.durationMin = durationMin;
            this.angleEffect = angleEffect;
            this.angleAttenuationMax = angleAttenuationMax;
            this.raytraceOpaqueBlocks = raytraceOpaqueBlocks;
        }
    }

    /**
     * Sets client data values as dictated by the server, either for exclusive access on the client or for shared access
     */
    public static class SyncedData
    {
        private ImmutableMap<String, ServerGun> serverGunMap;
        private int blindnessAlphaOverlay, alphaFadeThreshold, soundFadeThreshold;
        private float soundPercentage, ringVolume;

        public void toBytes(ByteBuf buffer)
        {
            buffer.writeInt(GunConfig.ID_TO_GUN.size());
            GunConfig.ID_TO_GUN.forEach((id, gun) ->
            {
                ByteBufUtils.writeUTF8String(buffer, id);
                buffer.writeFloat(gun.projectile.damage);
                buffer.writeInt(gun.general.maxAmmo);
            });
            buffer.writeInt(GunConfig.SERVER.stunGrenades.blind.alphaOverlay);
            buffer.writeInt(GunConfig.SERVER.stunGrenades.blind.alphaFadeThreshold);
            buffer.writeFloat((float) GunConfig.SERVER.stunGrenades.deafen.soundPercentage);
            buffer.writeInt(GunConfig.SERVER.stunGrenades.deafen.soundFadeThreshold);
            buffer.writeFloat((float) GunConfig.SERVER.stunGrenades.deafen.ringVolume);
        }

        public void fromBytes(ByteBuf buffer)
        {
            ImmutableMap.Builder<String, ServerGun> builder = ImmutableMap.builder();
            int size = buffer.readInt();
            for(int i = 0; i < size; i++)
            {
                String id = ByteBufUtils.readUTF8String(buffer);
                ServerGun gun = new ServerGun();
                gun.damage = buffer.readFloat();
                gun.maxAmmo = buffer.readInt();
                builder.put(id, gun);
            }
            serverGunMap = builder.build();
            blindnessAlphaOverlay = buffer.readInt();
            alphaFadeThreshold = buffer.readInt();
            soundPercentage = buffer.readFloat();
            soundFadeThreshold = buffer.readInt();
            ringVolume = buffer.readFloat();
        }

        public void syncClientToServer()
        {
            GunConfig.ID_TO_GUN.forEach((id, gun) -> gun.serverGun = serverGunMap.get(id));
            GunConfig.SERVER.stunGrenades.blind.alphaOverlaySynced = blindnessAlphaOverlay;
            GunConfig.SERVER.stunGrenades.blind.alphaFadeThresholdSynced = alphaFadeThreshold;
            GunConfig.SERVER.stunGrenades.deafen.soundPercentageSynced = soundPercentage;
            GunConfig.SERVER.stunGrenades.deafen.soundFadeThresholdSynced = soundFadeThreshold;
            GunConfig.SERVER.stunGrenades.deafen.ringVolumeSynced = ringVolume;
        }
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
