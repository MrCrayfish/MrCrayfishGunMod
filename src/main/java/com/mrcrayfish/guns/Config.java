package com.mrcrayfish.guns;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config
{
    /**
     * Client related config options
     */
    public static class Client
    {
        public final Sounds sounds;
        public final Display display;
        public final Particle particle;

        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.push("client");
            {
                this.sounds = new Sounds(builder);
                this.display = new Display(builder);
                this.particle = new Particle(builder);
            }
            builder.pop();
        }
    }

    /**
     * Sound related config options
     */
    public static class Sounds
    {
        public final ForgeConfigSpec.BooleanValue hitSound;

        public Sounds(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Control sounds triggered by guns").push("sounds");
            {
                this.hitSound = builder.comment("If true, a ding sound will play when you successfully hit a player with a gun").define("playSoundWhenTargetHit", true);
            }
            builder.pop();
        }
    }

    /**
     * Display related config options
     */
    public static class Display
    {
        public Display(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Configuration for display related options").push("display");
            {

            }
            builder.pop();
        }
    }

    /**
     * Particle related config options
     */
    public static class Particle
    {
        public final ForgeConfigSpec.IntValue bulletHoleLifeMin;
        public final ForgeConfigSpec.IntValue bulletHoleLifeMax;
        public final ForgeConfigSpec.DoubleValue bulletHoleFadeThreshold;
        public final ForgeConfigSpec.BooleanValue enableBlood;

        public Particle(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to particles").push("particle");
            {
                this.bulletHoleLifeMin = builder.comment("The minimum duration in ticks before bullet holes will disappear").defineInRange("bulletHoleLifeMin", 150, 0, Integer.MAX_VALUE);
                this.bulletHoleLifeMax = builder.comment("The maximum duration in ticks before bullet holes will disappear").defineInRange("bulletHoleLifeMax", 200, 0, Integer.MAX_VALUE);
                this.bulletHoleFadeThreshold = builder.comment("The percentage of the maximum life that must pass before particles begin fading away. 0 makes the particles always fade and 1 removes facing completely").defineInRange("bulletHoleFadeThreshold", 0.98, 0, 1.0);
                this.enableBlood = builder.comment("If true, blood will will spawn from entities that are hit from a projectile").define("enableBlood", false);
            }
            builder.pop();
        }
    }

    /**
     * Common config options
     */
    public static class Common
    {
        public final Gameplay gameplay;
        public final Network network;
        public final AggroMobs aggroMobs;
        public final Missiles missiles;
        public final Grenades grenades;
        public final StunGrenades stunGrenades;
        public final ProjectileSpread projectileSpread;

        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.push("common");
            {
                this.gameplay = new Gameplay(builder);
                this.network = new Network(builder);
                this.aggroMobs = new AggroMobs(builder);
                this.missiles = new Missiles(builder);
                this.grenades = new Grenades(builder);
                this.stunGrenades = new StunGrenades(builder);
                this.projectileSpread = new ProjectileSpread(builder);
            }
            builder.pop();
        }
    }

    /**
     * Gameplay related config options
     */
    public static class Gameplay
    {
        public final ForgeConfigSpec.BooleanValue enableGunGriefing;
        public final ForgeConfigSpec.DoubleValue growBoundingBoxAmount;
        public final ForgeConfigSpec.BooleanValue enableHeadShots;
        public final ForgeConfigSpec.DoubleValue headShotDamageMultiplier;
        public final ForgeConfigSpec.BooleanValue ignoreLeaves;
        public final ForgeConfigSpec.BooleanValue enableKnockback;
        public final ForgeConfigSpec.BooleanValue improvedHitboxes;

        public Gameplay(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to gameplay").push("gameplay");
            {
                this.enableGunGriefing = builder.comment("If enable, allows guns to shoot out glass and remove blocks on explosions").define("enableGunGriefing", true);
                this.growBoundingBoxAmount = builder.comment("The extra amount to expand an entity's bounding box when checking for projectile collision. Setting this value higher will make it easier to hit entities").defineInRange("growBoundingBoxAmount", 0.0625, 0.0, 1.0);
                this.enableHeadShots = builder.comment("Enables the check for head shots for players. Projectiles that hit the head of a player will have increased damage.").define("enableHeadShots", true);
                this.headShotDamageMultiplier = builder.comment("The value to multiply the damage by if projectile hit the players head").defineInRange("headShotDamageMultiplier", 1.25, 1.0, Double.MAX_VALUE);
                this.ignoreLeaves = builder.comment("If true, projectiles will ignore leaves when checking for collision").define("ignoreLeaves", true);
                this.enableKnockback = builder.comment("If true, projectiles will cause knockback when an entity is hit. By default this is set to true to match the behaviour of Minecraft.").define("enableKnockback", true);
                this.improvedHitboxes = builder.comment("If true, improves the accuracy of weapons by considering the ping of the player. This has no affect on singleplayer. This will add a little overhead if enabled.").define("improvedHitboxes", false);
            }
            builder.pop();
        }
    }

    /**
     * Network related config options
     */
    public static class Network
    {
        public final ForgeConfigSpec.DoubleValue projectileTrackingRange;

        public Network(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to network").push("network");
            {
                this.projectileTrackingRange = builder.comment("The distance players need to be within to be able to track new projectiles trails. Higher values means you can see projectiles from that start from further away.").defineInRange("projectileTrackingRange", 200.0, 1, Double.MAX_VALUE);
            }
            builder.pop();
        }
    }

    /**
     * Mob aggression related config options
     */
    public static class AggroMobs
    {
        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.BooleanValue angerHostileMobs;
        public final ForgeConfigSpec.DoubleValue range;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> exemptEntities;

        public AggroMobs(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to mob aggression").push("aggro_mobs");
            {
                this.enabled = builder.comment("If true, nearby mobs are angered and/or scared by the firing of guns.").define("enabled", true);
                this.angerHostileMobs = builder.comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.").define("angerHostileMobs", true);
                this.range = builder.comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.").defineInRange("unsilencedRange", 20.0, 0.0, Double.MAX_VALUE);
                this.exemptEntities = builder.comment("Any mobs of defined will not aggro on shooters").defineList("exemptMobs", Arrays.asList("minecraft:villager", "minecraft:bee"), o -> true);
            }
            builder.pop();
        }
    }

    /**
     * Missile related config options
     */
    public static class Missiles
    {
        public final ForgeConfigSpec.DoubleValue explosionRadius;

        public Missiles(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to missiles").push("missiles");
            {
                this.explosionRadius = builder.comment("The max distance which the explosion is effective to").defineInRange("explosionRadius", 5.0, 0.0, Double.MAX_VALUE);
            }
            builder.pop();
        }
    }

    /**
     * Grenade related config options
     */
    public static class Grenades
    {
        public final ForgeConfigSpec.DoubleValue explosionRadius;

        public Grenades(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to grenades").push("grenades");
            {
                this.explosionRadius = builder.comment("The max distance which the explosion is effective to").defineInRange("explosionRadius", 5.0, 0.0, Double.MAX_VALUE);
            }
            builder.pop();
        }
    }

    /**
     * Stun Grenade related config options
     */
    public static class StunGrenades
    {
        public final Blind blind;
        public final Deafen deafen;

        public StunGrenades(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to stun grenades").push("stun_grenades");
            {
                this.blind = new Blind(builder);
                this.deafen = new Deafen(builder);
            }
            builder.pop();
        }
    }

    /**
     * Stun grenade blinding related config options
     */
    public static class Blind
    {
        public final EffectCriteria criteria;
        public final ForgeConfigSpec.BooleanValue blindMobs;

        public Blind(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Blinding properties of stun grenades").push("blind");
            {
                this.criteria = new EffectCriteria(builder, 15, 220, 10, 170, 0.75, true);
                this.blindMobs = builder.comment("If true, hostile mobs will be unable to target entities while they are blinded by a stun grenade.").define("blindMobs", true);
            }
            builder.pop();
        }
    }

    /**
     * Stun grenade deafening related config options
     */
    public static class Deafen
    {
        public final EffectCriteria criteria;
        public final ForgeConfigSpec.BooleanValue panicMobs;

        public Deafen(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Deafening properties of stun grenades").push("deafen");
            {
                this.criteria = new EffectCriteria(builder, 15, 280, 100, 360, 0.75, false);
                this.panicMobs = builder.comment("If true, peaceful mobs will panic upon being deafened by a stun grenade.").define("panicMobs", true);
            }
            builder.pop();
        }
    }

    /**
     * Config options for effect criteria
     */
    public static class EffectCriteria
    {
        public final ForgeConfigSpec.DoubleValue radius;
        public final ForgeConfigSpec.IntValue durationMax;
        public final ForgeConfigSpec.IntValue durationMin;
        public final ForgeConfigSpec.DoubleValue angleEffect;
        public final ForgeConfigSpec.DoubleValue angleAttenuationMax;
        public final ForgeConfigSpec.BooleanValue raytraceOpaqueBlocks;

        public EffectCriteria(ForgeConfigSpec.Builder builder, double radius, int durationMax, int durationMin, double angleEffect, double angleAttenuationMax, boolean raytraceOpaqueBlocks)
        {
            builder.push("effect_criteria");
            {
                this.radius = builder.comment("Grenade must be no more than this many meters away to have an effect.").defineInRange("radius", radius, 0.0, Double.MAX_VALUE);
                this.durationMax = builder.comment("Effect will have this duration (in ticks) if the grenade is directly at the player's eyes while looking directly at it.").defineInRange("durationMax", durationMax, 0, Integer.MAX_VALUE);
                this.durationMin = builder.comment("Effect will have this duration (in ticks) if the grenade is the maximum distance from the player's eyes while looking directly at it.").defineInRange("durationMin", durationMin, 0, Integer.MAX_VALUE);
                this.angleEffect = builder.comment("Angle between the eye/looking direction and the eye/grenade direction must be no more than half this many degrees to have an effect.").defineInRange("angleEffect", angleEffect, 0, 360);
                this.angleAttenuationMax = builder.comment("After duration is attenuated by distance, it will be further attenuated depending on the angle (in degrees) between the eye/looking direction and the eye/grenade direction. This is done by multiplying it by 1 (no attenuation) if the angle is 0; and by this value if the angle is the maximum within the angle of effect.").defineInRange("angleAttenuationMax", angleAttenuationMax, 0.0, 1.0);
                this.raytraceOpaqueBlocks = builder.comment("If true, the effect is only applied if the line between the eyes and the grenade does not intersect any non-liquid blocks with an opacity greater than 0.").define("raytraceOpaqueBlocks", raytraceOpaqueBlocks);
            }
            builder.pop();
        }
    }

    /**
     * Projectile spread config options
     */
    public static class ProjectileSpread
    {
        public final ForgeConfigSpec.IntValue spreadThreshold;
        public final ForgeConfigSpec.IntValue maxCount;

        public ProjectileSpread(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to projectile spread").push("projectile_spread");
            {
                this.spreadThreshold = builder.comment("The amount of time in milliseconds before logic to apply spread is skipped. The value indicates a reasonable amount of time before a weapon is considered stable again.").defineInRange("spreadThreshold", 300, 0, 1000);
                this.maxCount = builder.comment("The amount of times a player has too shoot within the spread threshold before the maximum amount of spread is applied. Setting the value higher means it will take longer for the spread to be applied.").defineInRange("maxCount", 10, 1, Integer.MAX_VALUE);
            }
            builder.pop();
        }
    }

    /**
     * Server related config options
     */
    public static class Server
    {
        public final ForgeConfigSpec.IntValue alphaOverlay;
        public final ForgeConfigSpec.IntValue alphaFadeThreshold;
        public final ForgeConfigSpec.DoubleValue soundPercentage;
        public final ForgeConfigSpec.IntValue soundFadeThreshold;
        public final ForgeConfigSpec.DoubleValue ringVolume;
        public final ForgeConfigSpec.DoubleValue gunShotMaxDistance;
        public final ForgeConfigSpec.BooleanValue enableCameraRecoil;

        public Server(ForgeConfigSpec.Builder builder)
        {
            builder.push("server");
            {
                builder.comment("Stun Grenade related properties").push("grenade");
                {
                    this.alphaOverlay = builder.comment("After the duration drops to this many ticks, the transparency of the overlay when blinded will gradually fade to 0 alpha.").defineInRange("alphaOverlay", 255, 0, 255);
                    this.alphaFadeThreshold = builder.comment("Transparency of the overlay when blinded will be this alpha value, before eventually fading to 0 alpha.").defineInRange("alphaFadeThreshold", 40, 0, Integer.MAX_VALUE);
                    this.soundPercentage = builder.comment("Volume of most game sounds when deafened will play at this percent, before eventually fading back to %100.").defineInRange("soundPercentage", 0.05, 0.0, 1.0);
                    this.soundFadeThreshold = builder.comment("After the duration drops to this many ticks, the ringing volume will gradually fade to 0 and other sound volumes will fade back to %100.").defineInRange("soundFadeThreshold", 90, 0, Integer.MAX_VALUE);
                    this.ringVolume = builder.comment("Volume of the ringing sound when deafened will play at this volume, before eventually fading to 0.").defineInRange("ringVolume", 1.0, 0.0, 1.0);
                }
                builder.pop();

                builder.comment("Audio properties").push("audio");
                {
                    this.gunShotMaxDistance = builder.comment("The maximum distance weapons can be heard by players.").defineInRange("gunShotMaxDistance", 100, 0, Double.MAX_VALUE);
                }
                builder.pop();

                this.enableCameraRecoil = builder.comment("If true, enables camera recoil when firing a weapon").define("enableCameraRecoil", true);
            }
            builder.pop();
        }
    }

    static final ForgeConfigSpec clientSpec;
    public static final Config.Client CLIENT;

    static final ForgeConfigSpec commonSpec;
    public static final Config.Common COMMON;

    static final ForgeConfigSpec serverSpec;
    public static final Config.Server SERVER;

    static
    {
        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
        serverSpec = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
    }
}
