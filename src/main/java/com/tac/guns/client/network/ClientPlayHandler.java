package com.tac.guns.client.network;

import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.client.BulletTrail;
import com.tac.guns.client.CustomGunManager;
import com.tac.guns.client.audio.GunShotSound;
import com.tac.guns.client.handler.*;
import com.tac.guns.common.Gun;
import com.tac.guns.common.GunModifiers;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.init.ModParticleTypes;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.network.message.*;
import com.tac.guns.particles.BulletHoleData;
import com.tac.guns.util.GunModifierHelper;
import mod.chiselsandbits.ChiselsAndBits;
import mod.chiselsandbits.api.ChiselsAndBitsAPI;
import mod.chiselsandbits.api.chiseling.ChiselingOperation;
import mod.chiselsandbits.api.chiseling.IChiselingContext;
import mod.chiselsandbits.api.chiseling.IChiselingManager;
import mod.chiselsandbits.api.chiseling.mode.IChiselMode;
import mod.chiselsandbits.api.exceptions.SpaceOccupiedException;
import mod.chiselsandbits.api.multistate.accessor.IStateEntryInfo;
import mod.chiselsandbits.api.multistate.accessor.identifier.IAreaShapeIdentifier;
import mod.chiselsandbits.api.multistate.accessor.sortable.IPositionMutator;
import mod.chiselsandbits.api.multistate.mutator.IAreaMutator;
import mod.chiselsandbits.api.multistate.mutator.IMutableStateEntryInfo;
import mod.chiselsandbits.api.multistate.snapshot.IMultiStateSnapshot;
import mod.chiselsandbits.multistate.mutator.ChiselAdaptingWorldMutator;
import mod.chiselsandbits.multistate.mutator.WorldWrappingMutator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
//import mod.chiselsandbits.chiseling.

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
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

        if(message.getShooterId() == mc.player.getEntityId())
        {
            Minecraft.getInstance().getSoundHandler().play(new SimpleSound(message.getId(), SoundCategory.PLAYERS, message.getVolume(), message.getPitch(), false, 0, ISound.AttenuationType.LINEAR, 0, 0, 0, true));
        }
        else
        {
            Minecraft.getInstance().getSoundHandler().play(new GunShotSound(message.getId(), SoundCategory.PLAYERS, message.getX(), message.getY(), message.getZ(), message.getVolume(), message.getPitch(), message.isReload()));
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
        //if(GunMod.cabLoaded)
            //deleteBitOnHit();
        return manager.addParticle(data, x, y, z, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier, (rand.nextDouble() - 0.5) * velocityMultiplier);
    }
  /*  private static boolean deleteBitOnHit(BlockPos blockPos, BlockState blockState, double x, double y, double z)//(IParticleData data, double x, double y, double z, Random rand, double velocityMultiplier)
    {
        Minecraft mc = Minecraft.getInstance();
        ChiselAdaptingWorldMutator chiselAdaptingWorldMutator = new ChiselAdaptingWorldMutator(mc.world, blockPos);
        float bitSize = ChiselsAndBitsAPI.getInstance().getStateEntrySize().getSizePerBit();
        ChiselsAndBitsAPI.getInstance().getMutatorFactory().in(mc.world, blockPos).overrideInAreaTarget(Blocks.AIR.getDefaultState(), new Vector3d(bitSize*Math.abs(x),bitSize*Math.abs(y),bitSize*Math.abs(z)));
        return true;
    }
*/
    public static void handleProjectileHitBlock(MessageProjectileHitBlock message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        if (world != null) {
            /*if(GunMod.cabLoaded)
            {
                double holeX = 0.005 * message.getFace().getXOffset();
                double holeY = 0.005 * message.getFace().getYOffset();
                double holeZ = 0.005 * message.getFace().getZOffset();
                //System.out.println(world.getBlockState(message.getPos()).getBlock().getTranslatedName().getString());
                //deleteBitOnHit(message.getPos(), world.getBlockState(message.getPos()), holeX,holeY,holeZ);
            }
            else {*/
                BlockState state = world.getBlockState(message.getPos());
                double holeX = message.getX() + 0.005 * message.getFace().getXOffset();
                double holeY = message.getY() + 0.005 * message.getFace().getYOffset();
                double holeZ = message.getZ() + 0.005 * message.getFace().getZOffset();
                double distance = Math.sqrt(mc.player.getDistanceSq(message.getX(), message.getY(), message.getZ()));
                world.addParticle(new BulletHoleData(message.getFace(), message.getPos()), false, holeX, holeY, holeZ, 0, 0, 0);
                if (distance < 16.0) {
                    world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), false, message.getX(), message.getY(), message.getZ(), 0, 0, 0);
                }
                if (distance < 32.0) {
                    world.playSound(message.getX(), message.getY(), message.getZ(), state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.75F, 2.0F, false);
                }
            //}
        }
    }

    public static void handleProjectileHitEntity(MessageProjectileHitEntity message)
    {
        Minecraft mc = Minecraft.getInstance();
        World world = mc.world;
        if(world == null)
            return;

        SoundEvent event = getHitSound(message.isCritical(), message.isHeadshot(), message.isPlayer());
        if(event == null)
            return;

        mc.getSoundHandler().play(SimpleSound.master(event, 1.0F, 1.0F + world.rand.nextFloat() * 0.2F));
    }

    @Nullable
    private static SoundEvent getHitSound(boolean critical, boolean headshot, boolean player)
    {
        if(critical)
        {
            if(Config.CLIENT.sounds.playSoundWhenCritical.get())
            {
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(Config.CLIENT.sounds.criticalSound.get()));
                return event != null ? event : SoundEvents.ENTITY_PLAYER_ATTACK_CRIT;
            }
        }
        else if(headshot)
        {
            if(Config.CLIENT.sounds.playSoundWhenHeadshot.get())
            {
                //SoundEvent event = ModSounds.HEADSHOT_EXTENDED_PLAYFUL.get();//ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(Config.CLIENT.sounds.headshotSound.get()));
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(Config.CLIENT.sounds.headshotSound.get()));
                return event != null ? event : SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK;
            }
        }
        else if(player)
        {
            return SoundEvents.ENTITY_PLAYER_HURT;
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
