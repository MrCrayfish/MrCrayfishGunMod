package com.mrcrayfish.guns.client.network;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.BulletTrail;
import com.mrcrayfish.guns.client.CustomGunManager;
import com.mrcrayfish.guns.client.audio.GunShotSound;
import com.mrcrayfish.guns.client.handler.BulletTrailRenderingHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.init.ModParticleTypes;
import com.mrcrayfish.guns.network.message.MessageBlood;
import com.mrcrayfish.guns.network.message.MessageBulletTrail;
import com.mrcrayfish.guns.network.message.MessageGunSound;
import com.mrcrayfish.guns.network.message.MessageProjectileHitBlock;
import com.mrcrayfish.guns.network.message.MessageProjectileHitEntity;
import com.mrcrayfish.guns.network.message.MessageRemoveProjectile;
import com.mrcrayfish.guns.network.message.MessageStunGrenade;
import com.mrcrayfish.guns.network.message.MessageUpdateGuns;
import com.mrcrayfish.guns.particles.BulletHoleData;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class ClientPlayHandler
{
    public static void handleMessageGunSound(MessageGunSound message)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return;

        if(message.showMuzzleFlash())
        {
            GunRenderingHandler.get().showMuzzleFlashForPlayer(message.getShooterId());
        }

        if(message.getShooterId() == mc.player.getId())
        {
            Minecraft.getInstance().getSoundManager().play(new SimpleSound(message.getId(), SoundCategory.PLAYERS, message.getVolume(), message.getPitch(), false, 0, ISound.AttenuationType.NONE, 0, 0, 0, true));
        }
        else
        {
            Minecraft.getInstance().getSoundManager().play(new GunShotSound(message.getId(), SoundCategory.PLAYERS, message.getX(), message.getY(), message.getZ(), message.getVolume(), message.getPitch(), message.isReload()));
        }
    }

    public static void handleMessageBlood(MessageBlood message)
    {
        if(!Config.CLIENT.particle.enableBlood.get())
        {
            return;
        }
        World world = Minecraft.getInstance().level;
        if(world != null)
        {
            for(int i = 0; i < 10; i++)
            {
                world.addParticle(ModParticleTypes.BLOOD.get(), true, message.getX(), message.getY(), message.getZ(), 0.5, 0, 0.5);
            }
        }
    }

    public static void handleMessageBulletTrail(MessageBulletTrail message)
    {
        World world = Minecraft.getInstance().level;
        if(world != null)
        {
            int[] entityIds = message.getEntityIds();
            Vector3d[] positions = message.getPositions();
            Vector3d[] motions = message.getMotions();
            ItemStack item = message.getItem();
            int trailColor = message.getTrailColor();
            double trailLengthMultiplier = message.getTrailLengthMultiplier();
            int life = message.getLife();
            double gravity = message.getGravity();
            int shooterId = message.getShooterId();
            boolean enchanted = message.isEnchanted();
            IParticleData data = message.getParticleData();
            for(int i = 0; i < message.getCount(); i++)
            {
                BulletTrailRenderingHandler.get().add(new BulletTrail(entityIds[i], positions[i], motions[i], item, trailColor, trailLengthMultiplier, life, gravity, shooterId, enchanted, data));
            }
        }
    }

    public static void handleExplosionStunGrenade(MessageStunGrenade message)
    {
        Minecraft mc = Minecraft.getInstance();
        ParticleManager particleManager = mc.particleEngine;
        World world = mc.level;
        double x = message.getX();
        double y = message.getY();
        double z = message.getZ();

        /* Spawn lingering smoke particles */
        for(int i = 0; i < 30; i++)
        {
            spawnParticle(particleManager, ParticleTypes.CLOUD, x, y, z, world.random, 0.2);
        }

        /* Spawn fast moving smoke/spark particles */
        for(int i = 0; i < 30; i++)
        {
            Particle smoke = spawnParticle(particleManager, ParticleTypes.SMOKE, x, y, z, world.random, 4.0);
            smoke.setLifetime((int) ((8 / (Math.random() * 0.1 + 0.4)) * 0.5));
            spawnParticle(particleManager, ParticleTypes.CRIT, x, y, z, world.random, 4.0);
        }
    }

    private static Particle spawnParticle(ParticleManager manager, IParticleData data, double x, double y, double z, Random rand, double velocityMultiplier)
    {
        return manager.createParticle(data, x, y, z, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }

    public static void handleProjectileHitBlock(MessageProjectileHitBlock message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.level;
        if(world != null)
        {
            BlockState state = world.getBlockState(message.getPos());
            double holeX = message.getX() + 0.005 * message.getFace().getStepX();
            double holeY = message.getY() + 0.005 * message.getFace().getStepY();
            double holeZ = message.getZ() + 0.005 * message.getFace().getStepZ();
            double distance = Math.sqrt(mc.player.distanceToSqr(message.getX(), message.getY(), message.getZ()));
            world.addParticle(new BulletHoleData(message.getFace(), message.getPos()), false, holeX, holeY, holeZ, 0, 0, 0);
            if(distance < Config.CLIENT.particle.impactParticleDistance.get())
            {
                for(int i = 0; i < 4; i++)
                {
                    Vector3i normal = message.getFace().getNormal();
                    Vector3d motion = new Vector3d(normal.getX(), normal.getY(), normal.getZ());
                    motion.add(getRandomDir(world.random), getRandomDir(world.random), getRandomDir(world.random));
                    world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), false, message.getX(), message.getY(), message.getZ(), 0, 0, 0);
                }
            }
            if(distance <= Config.CLIENT.sounds.impactSoundDistance.get())
            {
                world.playLocalSound(message.getX(), message.getY(), message.getZ(), state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 2.0F, 2.0F, false);
            }
        }
    }

    private static double getRandomDir(Random random)
    {
        return -0.25 + random.nextDouble() * 0.5;
    }

    public static void handleProjectileHitEntity(MessageProjectileHitEntity message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.level;
        if(world == null)
            return;

        SoundEvent event = getHitSound(message.isCritical(), message.isHeadshot(), message.isPlayer());
        if(event == null)
            return;

        mc.getSoundManager().play(SimpleSound.forUI(event, 1.0F, 1.0F + world.random.nextFloat() * 0.2F));
    }

    @Nullable
    private static SoundEvent getHitSound(boolean critical, boolean headshot, boolean player)
    {
        if(critical)
        {
            if(Config.CLIENT.sounds.playSoundWhenCritical.get())
            {
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(Config.CLIENT.sounds.criticalSound.get()));
                return event != null ? event : SoundEvents.PLAYER_ATTACK_CRIT;
            }
        }
        else if(headshot)
        {
            if(Config.CLIENT.sounds.playSoundWhenHeadshot.get())
            {
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(Config.CLIENT.sounds.headshotSound.get()));
                return event != null ? event : SoundEvents.PLAYER_ATTACK_KNOCKBACK;
            }
        }
        else if(player)
        {
            return SoundEvents.PLAYER_HURT;
        }
        return null;
    }


    public static void handleRemoveProjectile(MessageRemoveProjectile message)
    {
        BulletTrailRenderingHandler.get().remove(message.getEntityId());
    }

    public static void handleUpdateGuns(MessageUpdateGuns message)
    {
        NetworkGunManager.updateRegisteredGuns(message);
        CustomGunManager.updateCustomGuns(message);
    }
}
