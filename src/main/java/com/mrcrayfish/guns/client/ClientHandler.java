package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.event.BulletRenderer;
import com.mrcrayfish.guns.client.event.GunRenderer;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.network.message.MessageBullet;
import com.mrcrayfish.guns.network.message.MessageStunGrenade;
import com.mrcrayfish.guns.object.Bullet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.Random;

/**
 * Author: MrCrayfish
 */
public class ClientHandler
{
    private static final GunRenderer GUN_RENDERER = new GunRenderer();
    private static final BulletRenderer BULLET_RENDERER = new BulletRenderer();

    public static void setup()
    {
        MinecraftForge.EVENT_BUS.register(GUN_RENDERER);
        MinecraftForge.EVENT_BUS.register(BULLET_RENDERER);
        if(GunMod.controllableLoaded)
        {
            MinecraftForge.EVENT_BUS.register(new ControllerEvents());
        }
    }

    public static GunRenderer getGunRenderer()
    {
        return GUN_RENDERER;
    }

    public static BulletRenderer getBulletRenderer()
    {
        return BULLET_RENDERER;
    }

    public static void handleMessageBullet(MessageBullet message)
    {
        World world = Minecraft.getInstance().world;
        if(world != null)
        {
            Entity entity = world.getEntityByID(message.getEntityId());
            EntityProjectile projectile = (EntityProjectile) entity; //TODO test if this works. entity could be null but does java still cast?
            BULLET_RENDERER.addBullet(new Bullet(projectile, message));
        }
    }

    public static void handleExplosionStunGrenade(MessageStunGrenade message)
    {
        Minecraft mc = Minecraft.getInstance();
        ParticleManager particleManager = mc.particles;
        World world = mc.world;
        Random rand = world.rand;
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

    public static void showMuzzleFlash()
    {
        GUN_RENDERER.drawFlash = true;
    }

    public static boolean isLookingAtInteractableBlock()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.objectMouseOver != null && mc.world != null)
        {
            if(mc.objectMouseOver instanceof BlockRayTraceResult)
            {
                BlockRayTraceResult result = (BlockRayTraceResult) mc.objectMouseOver;
                BlockState state = mc.world.getBlockState(result.getPos());
                Block block = state.getBlock();
                return block instanceof ContainerBlock || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE;
            }
        }
        return false;
    }
}
