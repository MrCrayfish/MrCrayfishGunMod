package com.mrcrayfish.guns;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.mrcrayfish.guns.item.GunRegistry;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.ServerGun;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import sun.nio.ch.Net;

import java.util.Map;
import java.util.Set;

@Config(modid = Reference.MOD_ID)
@Config.LangKey("config." + Reference.MOD_ID + ".title")
@EventBusSubscriber(modid = Reference.MOD_ID)
public class GunConfig
{
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

		@Config.Name("Controls")
		@Config.Comment("Configuration for control options")
		@Config.LangKey(Controls.PREFIX)
		public Controls controls = new Controls();
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

		@Config.Name("Network")
		@Config.Comment("Properties relating to network")
		@Config.LangKey(Network.PREFIX)
		public Network network = new Network();

		@Config.Name("Aggro Mobs")
		@Config.Comment("Nearby mobs are angered and/or scared by the firing of guns.")
		@Config.LangKey(AggroMobs.PREFIX)
		public AggroMobs aggroMobs = new AggroMobs();

		@Config.Name("Missiles")
		@Config.Comment("Properties relating to missiles.")
		@Config.LangKey(Missiles.PREFIX)
		public Missiles missiles = new Missiles();

		@Config.Name("Grenades")
		@Config.Comment("Properties relating to grenades.")
		@Config.LangKey(Grenades.PREFIX)
		public Grenades grenades = new Grenades();

		@Config.Name("Stun Grenades")
        @Config.Comment("Blinding/deafening properties of stun grenades.")
        @Config.LangKey(StunGrenades.PREFIX)
        public StunGrenades stunGrenades = new StunGrenades();

		@Config.Name("Projectile Spread")
		@Config.Comment("Properties relating to projectile spread")
		@Config.LangKey(ProjectileSpread.PREFIX)
		public ProjectileSpread projectileSpread = new ProjectileSpread();

		@Config.Name("Grow Bounding Box")
		@Config.Comment("The extra amount to expand an entity's bounding box when checking for projectile collision. Setting this value higher will make it easier to hit players")
		@Config.LangKey(PREFIX + ".grow_bounding_box")
		@Config.RangeDouble(min = 0.0, max = 1.0)
		public double growBoundingBoxAmount = 0.3;

		@Config.Name("Enable Head Shots")
		@Config.Comment("Enables the check for head shots for players. Projectiles that hit the head of a player will have increased damage.")
		@Config.LangKey(PREFIX + ".enabled_head_shots")
		public boolean enableHeadShots = true;

		@Config.Name("Head Shot Damage Multiplier")
		@Config.Comment("The value to multiply the damage by if projectile hit the players head")
		@Config.LangKey(PREFIX + ".head_shot_damage_multiplier")
		@Config.RangeDouble(min = 1.0)
		public double headShotDamageMultiplier = 1.1;
	}

	public static class Network
	{
		private static final String PREFIX = Server.PREFIX + ".network";

		@Config.Name("Projectile Tracking Range")
		@Config.Comment("The distance players need to be within to be able to track new projectiles trails. Higher values means you can see projectiles from that start from further away.")
		@Config.LangKey(PREFIX + ".projectile_tracking_range")
		@Config.RangeDouble(min = 0.0)
		public double projectileTrackingRange = 200.0;
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

	public static class Controls
	{
		private static final String PREFIX = Client.PREFIX + ".controls";

		@Config.Name("Use Old Controls")
		@Config.Comment("If true, uses the old controls in order to aim and shoot")
		@Config.LangKey(PREFIX + ".old_controls")
		public boolean oldControls = false;
	}

	public static class Missiles
	{
		private static final String PREFIX = Server.PREFIX + ".missiles.normal";

		@Config.Name("Explosion Radius")
		@Config.Comment("The max distance which the explosion is effective to.")
		@Config.LangKey(PREFIX + ".explosion_range")
		@Config.RangeDouble(min = 0)
		public double explosionRadius = 5.0;
	}

	public static class Grenades
	{
		private static final String PREFIX = Server.PREFIX + ".grenade.normal";

		@Config.Name("Explosion Radius")
		@Config.Comment("The max distance which the explosion is effective to.")
		@Config.LangKey(PREFIX + ".explosion_range")
		@Config.RangeDouble(min = 0)
		public double explosionRadius = 5.0;
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
        public int alphaOverlaySynced = 255;

        @Config.Name("Overlay Fade Threshold")
        @Config.Comment("Transparency of the overlay when blinded will be this alpha value, before eventually fading to 0 alpha.")
        @Config.LangKey(PREFIX + ".overlay.fade")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int alphaFadeThreshold = 40;
        public int alphaFadeThresholdSynced = Integer.MAX_VALUE;
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
        public float soundPercentageSynced = 0;

        @Config.Name("Sound Fade Threshold")
        @Config.Comment("After the duration drops to this many ticks, the ringing volume will gradually fade to 0 and other sound volumes will fade back to %100.")
        @Config.LangKey(PREFIX + ".sound.fade")
        @Config.RangeInt(min = 0)
        @Config.RequiresWorldRestart
        public int soundFadeThreshold = 90;
        public int soundFadeThresholdSynced = Integer.MAX_VALUE;

        @Config.Name("Ring Volume")
        @Config.Comment("Volume of the ringing sound when deafened will play at this volume, before eventually fading to 0.")
        @Config.LangKey(PREFIX + ".sound.ring")
        @Config.RangeDouble(min = 0)
        @Config.RequiresWorldRestart
        public double ringVolume = 1;
        public float ringVolumeSynced = 1;
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

    public static class ProjectileSpread
	{
		private static final String PREFIX = Server.PREFIX + ".projectile_spread";

		@Config.Name("Spread Threshold")
		@Config.Comment("The amount of time in milliseconds before logic to apply spread is skipped. The value indicates a reasonable amount of time before a weapon is considered stable again.")
		@Config.LangKey(PREFIX + ".spread_threshold")
		@Config.RangeInt(min = 0, max = 1000)
		public int spreadThreshold = 300;

		@Config.Name("Max Count")
		@Config.Comment("The amount of times a player has too shoot within the spread threshold before the maximum amount of spread is applied. Setting the value higher means it will take longer for the spread to be applied.")
		@Config.LangKey(PREFIX + ".max_count")
		@Config.RangeInt(min = 1)
		public int maxCount = 10;
	}

    /**
     * Sets client data values as dictated by the server, either for exclusive access on the client or for shared access
     */
    public static class SyncedData
    {
        private ImmutableMap<ResourceLocation, ServerGun> serverGunMap;
        private int blindnessAlphaOverlay, alphaFadeThreshold, soundFadeThreshold;
        private float soundPercentage, ringVolume;

        public void toBytes(ByteBuf buffer)
        {
			Map<ResourceLocation, ItemGun> guns = GunRegistry.getInstance().getGuns();
			buffer.writeInt(guns.size());
			guns.forEach((location, gun) ->
			{
				ByteBufUtils.writeUTF8String(buffer, location.toString());
				buffer.writeFloat(gun.getGun().projectile.damage);
				buffer.writeInt(gun.getGun().general.maxAmmo);
			});
            buffer.writeInt(GunConfig.SERVER.stunGrenades.blind.alphaOverlay);
            buffer.writeInt(GunConfig.SERVER.stunGrenades.blind.alphaFadeThreshold);
            buffer.writeFloat((float) GunConfig.SERVER.stunGrenades.deafen.soundPercentage);
            buffer.writeInt(GunConfig.SERVER.stunGrenades.deafen.soundFadeThreshold);
            buffer.writeFloat((float) GunConfig.SERVER.stunGrenades.deafen.ringVolume);
        }

        public void fromBytes(ByteBuf buffer)
        {
            ImmutableMap.Builder<ResourceLocation, ServerGun> builder = ImmutableMap.builder();
            int size = buffer.readInt();
            for(int i = 0; i < size; i++)
            {
				ResourceLocation id = new ResourceLocation(ByteBufUtils.readUTF8String(buffer));
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
			serverGunMap.forEach((id, serverGun) ->
			{
				ItemGun gun = GunRegistry.getInstance().getGun(id);
				if(gun != null)
				{
					gun.getGun().serverGun = serverGun;
				}
			});
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
}
