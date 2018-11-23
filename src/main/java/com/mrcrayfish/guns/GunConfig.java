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
@LangKey("config." + Reference.MOD_ID + ".title")
@EventBusSubscriber(modid = Reference.MOD_ID)
public class GunConfig
{
	@Ignore
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

	@Ignore
    public static final String PREFIX = "config." + Reference.MOD_ID + ".";

	@Name("Client")
	@Comment("Client-only configs.")
	@LangKey(Client.PREFIX)
	public static final Client CLIENT = new Client();

	@Name("Server")
	@Comment("Server-only configs.")
	@LangKey(Server.PREFIX)
	public static final Server SERVER = new Server();

	public static class Client
	{
	    private static final String PREFIX = GunConfig.PREFIX + "client";

		@Name("Sounds")
		@Comment("Control sounds triggered by guns")
		@LangKey(PREFIX + ".sounds")
		public Sounds sound = new Sounds();
	}

	public static class Sounds
	{
	    private static final String PREFIX = Client.PREFIX + ".sounds";

		@Name("Play Hit Sound")
		@Comment("If true, a ding sound will play when you successfully hit a player with a gun")
		@LangKey(PREFIX + ".hit_sound")
		public boolean hitSound = true;
	}

	public static class Server
	{
	    private static final String PREFIX = GunConfig.PREFIX + "server";

		@Name("Aggro Mobs")
		@Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@LangKey(AggroMobs.PREFIX)
		public AggroMobs aggroMobs = new AggroMobs();

		@Name("Guns")
		@Comment("Change the properties of guns")
		@LangKey(Guns.PREFIX)
		public Guns guns = new Guns();

		@Name("Stun Grenades")
        @Comment("Blinding/deafening properties of stun grenades.")
        @LangKey(StunGrenades.PREFIX)
        public StunGrenades stunGrenades = new StunGrenades();
	}

	public static class AggroMobs
	{
	    private static final String PREFIX = Server.PREFIX + ".aggro";

		@Name("Aggro Mobs Enabled")
		@Comment("If true, nearby mobs are angered and/or scared by the firing of guns.")
		@LangKey(PREFIX + ".enabled")
		public boolean enabled = true;

		@Name("Anger Hostile Mobs")
		@Comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.")
		@LangKey(PREFIX + ".hostile")
		public boolean angerHostileMobs = true;

		@Name("Range Silenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of a silenced gun.")
		@LangKey(PREFIX + ".silenced")
		@RangeDouble(min = 0)
		public double rangeSilenced = 10;

		@Name("Range Unsilenced")
		@Comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.")
		@LangKey(PREFIX + ".unsilenced")
		@RangeDouble(min = 0)
		public double rangeUnsilenced = 20;

		@Name("Exempt Mob Classes")
		@Comment("Any mobs of classes with class paths in this list will not aggro on shooters.")
		@LangKey(PREFIX + ".exempt")
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
	    private static final String PREFIX = Server.PREFIX + ".guns";

		@Name("Pistol")
		@Comment("Change the properties of the Pistol")
		@LangKey(PREFIX + ".pistol")
		public Gun pistol = ID_TO_GUN.get("handgun");

		@Name("Shotgun")
		@Comment("Change the properties of the Shotgun")
		@LangKey(PREFIX + ".shotgun")
		public Gun shotgun = ID_TO_GUN.get("shotgun");

		@Name("Rifle")
		@Comment("Change the properties of the Rifle")
		@LangKey(PREFIX + ".rifle")
		public Gun rifle = ID_TO_GUN.get("rifle");

		@Name("Grenade Launcher")
		@Comment("Change the properties of the Grenade Launcher")
		@LangKey(PREFIX + ".grenade_launcher")
		public Gun grenadeLauncher = ID_TO_GUN.get("grenade_launcher");

		@Name("Bazooka")
		@Comment("Change the properties of the Bazooka")
		@LangKey(PREFIX + ".bazooka")
		public Gun bazooka = ID_TO_GUN.get("bazooka");

		@Name("Minigun")
		@Comment("Change the properties of the Minigun")
		@LangKey(PREFIX + ".chain_gun")
		public Gun chainGun = ID_TO_GUN.get("chain_gun");

		@Name("Assault Rifle")
		@Comment("Change the properties of the Assault Rifle")
		@LangKey(PREFIX + ".assault_rifle")
		public Gun assaultRifle = ID_TO_GUN.get("assault_rifle");
	}

	public static class StunGrenades
    {
	    private static final String PREFIX = Server.PREFIX + ".grenade.stun";

	    @Name("Blind")
        @Comment("Blinding properties of stun grenades.")
        @LangKey(Blind.PREFIX)
        public Blind blind = new Blind();

	    @Name("Deafen")
        @Comment("Deafening properties of stun grenades.")
        @LangKey(Deafen.PREFIX)
        public Deafen deafen = new Deafen();
    }

	public static class Blind
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".blind";

	    @Name("Effect Criteria")
        @Comment("Criteria that determines the presence/absence and duration of the blinding effect.")
        @LangKey(EffectCriteria.PREFIX + ".blind")
        public EffectCriteria criteria = new EffectCriteria(15, 220, 10, 170, 0.75, true);

        @Name("Overlay Alpha")
        @Comment("After the duration drops to this many ticks, the transparency of the overlay when blinded will gradually fade to 0 alpha.")
        @LangKey(PREFIX + ".overlay.alpha")
        @RangeInt(min = 0, max = 255)
        @RequiresWorldRestart
        public int alphaOverlay = 255;
        public static int alphaOverlaySynced = 255;

        @Name("Overlay Fade Threshold")
        @Comment("Transparency of the overlay when blinded will be this alpha value, before eventually fading to 0 alpha.")
        @LangKey(PREFIX + ".overlay.fade")
        @RangeInt(min = 0)
        @RequiresWorldRestart
        public int alphaFadeThreshold = 40;
        public static int alphaFadeThresholdSynced = Integer.MAX_VALUE;
    }

	public static class Deafen
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".deafen";

	    @Name("Effect Criteria")
        @Comment("Criteria that determines the presence/absence and duration of the deafening effect.")
        @LangKey(EffectCriteria.PREFIX + ".deafen")
        public EffectCriteria criteria = new EffectCriteria(15, 280, 100, 360, 0.75, false);

        @Name("Sound Percentage")
        @Comment("Volume of most game sounds when deafened will play at this percent, before eventually fading back to %100.")
        @LangKey(PREFIX + ".sound.percentage")
        @RangeDouble(min = 0, max = 1)
        @RequiresWorldRestart
        public double soundPercentage = 0.05;
        public static float soundPercentageSynced = 0;

        @Name("Sound Fade Threshold")
        @Comment("After the duration drops to this many ticks, the ringing volume will gradually fade to 0 and other sound volumes will fade back to %100.")
        @LangKey(PREFIX + ".sound.fade")
        @RangeInt(min = 0)
        @RequiresWorldRestart
        public int soundFadeThreshold = 90;
        public static int soundFadeThresholdSynced = Integer.MAX_VALUE;

        @Name("Ring Volume")
        @Comment("Volume of the ringing sound when deafened will play at this volume, before eventually fading to 0.")
        @LangKey(PREFIX + ".sound.ring")
        @RangeDouble(min = 0)
        @RequiresWorldRestart
        public double ringVolume = 1;
        public static float ringVolumeSynced = 1;
    }

	public static class EffectCriteria
    {
	    private static final String PREFIX = StunGrenades.PREFIX + ".effect_criteria";

	    @Name("Radius")
        @Comment("Grenade must be no more than this many meters away to have an effect.")
        @LangKey(PREFIX + ".radius")
        @RangeDouble(min = 0)
        public double radius;

        @Name("Max Duration")
        @Comment("Effect will have this duration (in ticks) if the grenade is directly at the player's eyes while looking directly at it.")
        @LangKey(PREFIX + ".duration.max")
        @RangeInt(min = 0)
        public int durationMax;

        @Name("Min Duration By Distance")
        @Comment("Effect will have this duration (in ticks) if the grenade is the maximum distance from the player's eyes while looking directly at it.")
        @LangKey(PREFIX + ".duration.min")
        @RangeInt(min = 0)
        public int durationMin;

        @Name("Angle Of Effect")
        @Comment("Angle between the eye/looking direction and the eye/grenade direction must be no more than half this many degrees to have an effect.")
        @LangKey(PREFIX + ".angle.effect")
        @RangeDouble(min = 0, max = 360)
        public double angleEffect;

        @Name("Max Attenuation By Angle")
        @Comment("After duration is attenuated by distance, it will be further attenuated depending on the angle (in degrees) between the eye/looking "
                + "direction and the eye/grenade direction. This is done by multiplying it by 1 (no attenuation) if the angle is 0; and by this value if "
                + "the angle is the maximum within the angle of effect.")
        @LangKey(PREFIX + ".angle.attenuation.max")
        @RangeDouble(min = 0, max = 1)
        public double angleAttenuationMax;

        @Name("Raytrace Opaque Blocks")
        @Comment("If true, the effect is only applied if the line between the eyes and the grenade does not intersect any non-liquid blocks with an opacity greater than 0.")
        @LangKey(PREFIX + ".raytrace.opaque")
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
