package com.mrcrayfish.guns.client.network;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.BulletTrail;
import com.mrcrayfish.guns.client.CustomGunManager;
import com.mrcrayfish.guns.client.audio.GunShotSound;
import com.mrcrayfish.guns.client.handler.BulletTrailRenderingHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.init.ModParticleTypes;
import com.mrcrayfish.guns.init.ModSounds;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

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

        SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(message.getId());
        if(soundEvent != null)
        {
            if(message.getShooterId() == mc.player.getEntityId())
            {
                Minecraft.getInstance().getSoundHandler().play(new SimpleSound(soundEvent.getName(), SoundCategory.PLAYERS, message.getVolume(), message.getPitch(), false, 0, ISound.AttenuationType.NONE, 0, 0, 0, true));
            }
            else
            {
                Minecraft.getInstance().getSoundHandler().play(new GunShotSound(soundEvent, SoundCategory.PLAYERS, message.getX(), message.getY(), message.getZ(), message.getVolume(), message.getPitch()));
            }
        }
    }

    public static void handleMessageBlood(MessageBlood message)
    {
        if(!Config.CLIENT.particle.enableBlood.get())
        {
            return;
        }
        World world = Minecraft.getInstance().world;
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
        World world = Minecraft.getInstance().world;
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
            for(int i = 0; i < message.getCount(); i++)
            {
                BulletTrailRenderingHandler.get().add(new BulletTrail(entityIds[i], positions[i], motions[i], item, trailColor, trailLengthMultiplier, life, gravity, shooterId));
            }
        }
    }

    public static void handleExplosionStunGrenade(MessageStunGrenade message)
    {
        Minecraft mc = Minecraft.getInstance();
        ParticleManager particleManager = mc.particles;
        World world = mc.world;
        double x = message.getX();
        double y = message.getY();
        double z = message.getZ();

        /* Spawn lingering smoke particles */
        for(int i = 0; i < 30; i++)
        {
            spawnParticle(particleManager, ParticleTypes.CLOUD, x, y, z, world.rand, 0.2);
        }

        /* Spawn fast moving smoke/spark particles */
        for(int i = 0; i < 30; i++)
        {
            Particle smoke = spawnParticle(particleManager, ParticleTypes.SMOKE, x, y, z, world.rand, 4.0);
            smoke.setMaxAge((int) ((8 / (Math.random() * 0.1 + 0.4)) * 0.5));
            spawnParticle(particleManager, ParticleTypes.CRIT, x, y, z, world.rand, 4.0);
        }
    }

    private static Particle spawnParticle(ParticleManager manager, IParticleData data, double x, double y, double z, Random rand, double velocityMultiplier)
    {
        return manager.addParticle(data, x, y, z, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }

    public static void handleProjectileHitBlock(MessageProjectileHitBlock message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        if(world != null)
        {
            BlockState state = world.getBlockState(message.getPos());
            double holeX = message.getX() + 0.005 * message.getFace().getXOffset();
            double holeY = message.getY() + 0.005 * message.getFace().getYOffset();
            double holeZ = message.getZ() + 0.005 * message.getFace().getZOffset();
            double distance = Math.sqrt(mc.player.getDistanceSq(message.getX(), message.getY(), message.getZ()));
            world.addParticle(new BulletHoleData(message.getFace(), message.getPos()), false, holeX, holeY, holeZ, 0, 0, 0);
            if(distance < 16.0)
            {
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), false, message.getX(), message.getY(), message.getZ(), 0, 0, 0);
            }
            if(distance < 32.0)
            {
                world.playSound(message.getX(), message.getY(), message.getZ(), state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.75F, 2.0F, false);
            }
        }
    }

    public static void handleProjectileHitEntity(MessageProjectileHitEntity message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        if(world == null)
            return;

        SoundEvent event = message.isHeadshot() ? ModSounds.ENTITY_HEADSHOT.get() : message.isCritical() ? ModSounds.ENTITY_CRITICAL.get() : message.isPlayer() ? SoundEvents.ENTITY_PLAYER_HURT : null;
        if(event == null)
            return;

        mc.getSoundHandler().play(SimpleSound.master(event, 1.0F, 0.95F + world.rand.nextFloat() * 0.1F));
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
