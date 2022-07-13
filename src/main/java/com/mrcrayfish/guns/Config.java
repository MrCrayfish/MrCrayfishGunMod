package com.mrcrayfish.guns;

import com.mrcrayfish.guns.client.SwayType;
import com.mrcrayfish.guns.client.render.crosshair.Crosshair;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

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
        public final Controls controls;
        public final Experimental experimental;

        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.push("client");
            {
                this.sounds = new Sounds(builder);
                this.display = new Display(builder);
                this.particle = new Particle(builder);
                this.controls = new Controls(builder);
                this.experimental = new Experimental(builder);
            }
            builder.pop();
        }
    }

    /**
     * Sound related config options
     */
    public static class Sounds
    {
        public final ForgeConfigSpec.BooleanValue playSoundWhenHeadshot;
        public final ForgeConfigSpec.ConfigValue<String> headshotSound;
        public final ForgeConfigSpec.BooleanValue playSoundWhenCritical;
        public final ForgeConfigSpec.ConfigValue<String> criticalSound;
        public final ForgeConfigSpec.DoubleValue impactSoundDistance;

        public Sounds(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Control sounds triggered by guns").push("sounds");
            {
                this.playSoundWhenHeadshot = builder.comment("If true, a sound will play when you successfully hit a headshot on a entity with a gun").define("playSoundWhenHeadshot", true);
                this.headshotSound = builder.comment("The sound to play when a headshot occurs").define("headshotSound", "minecraft:entity.player.attack.knockback");
                this.playSoundWhenCritical = builder.comment("If true, a sound will play when you successfully hit a critical on a entity with a gun").define("playSoundWhenCritical", true);
                this.criticalSound = builder.comment("The sound to play when a critical occurs").define("criticalSound", "minecraft:entity.player.attack.crit");
                this.impactSoundDistance = builder.comment("The maximum distance impact sounds from bullet can be heard").defineInRange("impactSoundDistance", 32.0, 0.0, 32.0);
            }
            builder.pop();
        }
    }

    /**
     * Display related config options
     */
    public static class Display
    {
        public final ForgeConfigSpec.BooleanValue oldAnimations;
        public final ForgeConfigSpec.ConfigValue<String> crosshair;
        public final ForgeConfigSpec.BooleanValue cooldownIndicator;
        public final ForgeConfigSpec.BooleanValue weaponSway;
        public final ForgeConfigSpec.DoubleValue swaySensitivity;
        public final ForgeConfigSpec.EnumValue<SwayType> swayType;
        public final ForgeConfigSpec.BooleanValue cameraRollEffect;
        public final ForgeConfigSpec.DoubleValue cameraRollAngle;
        public final ForgeConfigSpec.BooleanValue sprintAnimation;
        public final ForgeConfigSpec.DoubleValue bobbingIntensity;

        public Display(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Configuration for display related options").push("display");
            {
                this.oldAnimations = builder.comment("If true, uses the old animation poses for weapons. This is only for nostalgic reasons and not recommended to switch back.").define("oldAnimations", false);
                this.crosshair = builder.comment("The custom crosshair to use for weapons. Go to (Options > Controls > Mouse Settings > Crosshair) in game to change this!").define("crosshair", Crosshair.DEFAULT.getLocation().toString());
                this.cooldownIndicator = builder.comment("If enabled, renders a cooldown indicator to make it easier to learn when you fire again.").define("cooldownIndicator", true);
                this.weaponSway = builder.comment("If enabled, the weapon will sway when the player moves their look direction. This does not affect aiming and is only visual.").define("weaponSway", true);
                this.swaySensitivity = builder.comment("The sensistivity of the visual weapon sway when the player moves their look direciton. The higher the value the more sway.").defineInRange("swaySensitivity", 0.3, 0.0, 1.0);
                this.swayType = builder.comment("The animation to use for sway. Directional follows the camera better while Drag is more immersive").defineEnum("swayType", SwayType.DRAG);
                this.cameraRollEffect = builder.comment("If enabled, the camera will roll when strafing while holding a gun. This creates a more immersive feeling.").define("cameraRollEffect", true);
                this.cameraRollAngle = builder.comment("When Camera Roll Effect is enabled, this is the absolute maximum angle the roll on the camera can approach.").defineInRange("cameraRollAngle", 1.5F, 0F, 45F);
                this.sprintAnimation = builder.comment("Enables the sprinting animation on weapons for better immersion. This only applies to weapons that support a sprinting animation.").define("sprintingAnimation", true);
                this.bobbingIntensity = builder.comment("The intensity of the custom bobbing animation while holding a gun").defineInRange("bobbingIntensity", 1.0, 0.0, 2.0);
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
        public final ForgeConfigSpec.DoubleValue impactParticleDistance;

        public Particle(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to particles").push("particle");
            {
                this.bulletHoleLifeMin = builder.comment("The minimum duration in ticks before bullet holes will disappear").defineInRange("bulletHoleLifeMin", 150, 0, Integer.MAX_VALUE);
                this.bulletHoleLifeMax = builder.comment("The maximum duration in ticks before bullet holes will disappear").defineInRange("bulletHoleLifeMax", 200, 0, Integer.MAX_VALUE);
                this.bulletHoleFadeThreshold = builder.comment("The percentage of the maximum life that must pass before particles begin fading away. 0 makes the particles always fade and 1 removes facing completely").defineInRange("bulletHoleFadeThreshold", 0.98, 0, 1.0);
                this.enableBlood = builder.comment("If true, blood will will spawn from entities that are hit from a projectile").define("enableBlood", false);
                this.impactParticleDistance = builder.comment("The maximum distance impact particles can be seen from the player").defineInRange("impactParticleDistance", 32.0, 0.0, 64.0);
            }
            builder.pop();
        }
    }

    public static class Controls
    {
        public final ForgeConfigSpec.DoubleValue aimDownSightSensitivity;

        public Controls(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to controls").push("controls");
            {
                this.aimDownSightSensitivity = builder.comment("A value to multiple the mouse sensitivity by when aiming down weapon sights. Go to (Options > Controls > Mouse Settings > ADS Sensitivity) in game to change this!").defineInRange("aimDownSightSensitivity", 0.75, 0.0, 1.0);
            }
            builder.pop();
        }
    }

    public static class Experimental
    {
        public Experimental(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Experimental options").push("experimental");
            {
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
        public final Griefing griefing;
        public final ForgeConfigSpec.DoubleValue growBoundingBoxAmount;
        public final ForgeConfigSpec.BooleanValue enableHeadShots;
        public final ForgeConfigSpec.DoubleValue headShotDamageMultiplier;
        public final ForgeConfigSpec.DoubleValue criticalDamageMultiplier;
        public final ForgeConfigSpec.BooleanValue ignoreLeaves;
        public final ForgeConfigSpec.BooleanValue enableKnockback;
        public final ForgeConfigSpec.DoubleValue knockbackStrength;
        public final ForgeConfigSpec.BooleanValue improvedHitboxes;

        public Gameplay(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to gameplay").push("gameplay");
            {
                this.griefing = new Griefing(builder);
                this.growBoundingBoxAmount = builder.comment("The extra amount to expand an entity's bounding box when checking for projectile collision. Setting this value higher will make it easier to hit entities").defineInRange("growBoundingBoxAmount", 0.3, 0.0, 1.0);
                this.enableHeadShots = builder.comment("Enables the check for head shots for players. Projectiles that hit the head of a player will have increased damage.").define("enableHeadShots", true);
                this.headShotDamageMultiplier = builder.comment("The value to multiply the damage by if projectile hit the players head").defineInRange("headShotDamageMultiplier", 1.25, 1.0, Double.MAX_VALUE);
                this.criticalDamageMultiplier = builder.comment("The value to multiply the damage by if projectile is a critical hit").defineInRange("criticalDamageMultiplier", 1.5, 1.0, Double.MAX_VALUE);
                this.ignoreLeaves = builder.comment("If true, projectiles will ignore leaves when checking for collision").define("ignoreLeaves", true);
                this.enableKnockback = builder.comment("If true, projectiles will cause knockback when an entity is hit. By default this is set to true to match the behaviour of Minecraft.").define("enableKnockback", true);
                this.knockbackStrength = builder.comment("Sets the strengthof knockback when shot by a bullet projectile. Knockback must be enabled for this to take effect. If value is equal to zero, knockback will use default minecraft value").defineInRange("knockbackStrength", 0.15, 0.0, 1.0);
                this.improvedHitboxes = builder.comment("If true, improves the accuracy of weapons by considering the ping of the player. This has no affect on singleplayer. This will add a little overhead if enabled.").define("improvedHitboxes", false);
            }
            builder.pop();
        }
    }

    /**
     * Gun griefing related config options
     */
    public static class Griefing
    {
        public final ForgeConfigSpec.BooleanValue enableBlockRemovalOnExplosions;
        public final ForgeConfigSpec.BooleanValue enableGlassBreaking;
        public final ForgeConfigSpec.BooleanValue setFireToBlocks;

        public Griefing(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties related to gun griefing").push("griefing");
            {
                this.enableBlockRemovalOnExplosions = builder.comment("If enabled, allows block removal on explosions").define("enableBlockRemovalOnExplosions", true);
                this.enableGlassBreaking = builder.comment("If enabled, allows guns to shoot out glass").define("enableGlassBreaking", true);
                this.setFireToBlocks = builder.comment("If true, allows guns enchanted with Fire Starter to light and spread fires on blocks").define("setFireToBlocks", true);
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
        public final ForgeConfigSpec.DoubleValue unsilencedRange;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> exemptEntities;

        public AggroMobs(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Properties relating to mob aggression").push("aggro_mobs");
            {
                this.enabled = builder.comment("If true, nearby mobs are angered and/or scared by the firing of guns.").define("enabled", true);
                this.angerHostileMobs = builder.comment("If true, in addition to causing peaceful mobs to panic, firing a gun will also cause nearby hostile mobs to target the shooter.").define("angerHostileMobs", true);
                this.unsilencedRange = builder.comment("Any mobs within a sphere of this radius will aggro on the shooter of an unsilenced gun.").defineInRange("unsilencedRange", 20.0, 0.0, Double.MAX_VALUE);
                this.exemptEntities = builder.comment("Any mobs of defined will not aggro on shooters").defineList("exemptMobs", Collections.emptyList(), o -> true);
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
                this.maxCount = builder.comment("The amount of times a player has to shoot within the spread threshold before the maximum amount of spread is applied. Setting the value higher means it will take longer for the spread to be applied.").defineInRange("maxCount", 10, 1, Integer.MAX_VALUE);
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
        public final ForgeConfigSpec.DoubleValue reloadMaxDistance;
        public final ForgeConfigSpec.BooleanValue enableCameraRecoil;
        public final ForgeConfigSpec.IntValue cooldownThreshold;
        public final Experimental experimental;

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
                    this.reloadMaxDistance = builder.comment("The maximum distance reloading can be heard by players.").defineInRange("reloadMaxDistance", 24, 0, Double.MAX_VALUE);
                }
                builder.pop();

                this.enableCameraRecoil = builder.comment("If true, enables camera recoil when firing a weapon").define("enableCameraRecoil", true);
                this.cooldownThreshold = builder.comment("The maximum amount of cooldown time remaining before the server will accept another shoot packet from a client. This allows for a litle slack since the server may be lagging").defineInRange("cooldownThreshold", 0, 75, 1000);

                this.experimental = new Experimental(builder);
            }
            builder.pop();
        }

        public static class Experimental
        {
            public final ForgeConfigSpec.BooleanValue forceDyeableAttachments;

            public Experimental(ForgeConfigSpec.Builder builder)
            {
                builder.push("experimental");
                this.forceDyeableAttachments = builder.comment("Forces all attachments to be dyeable regardless if they have an affect on the model. This is useful if your server uses custom models for attachments and the models have dyeable elements").define("forceDyeableAttachments", false);
                builder.pop();
            }
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

    public static void saveClientConfig()
    {
        clientSpec.save();
    }
}
