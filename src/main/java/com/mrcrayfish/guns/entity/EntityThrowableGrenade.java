package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Author: MrCrayfish
 */
public class EntityThrowableGrenade extends EntityThrowableItem
{
    public float rotation;
    public float prevRotation;

    public EntityThrowableGrenade(World worldIn)
    {
        super(worldIn);
    }

    public EntityThrowableGrenade(World world, PlayerEntity player)
    {
        super(world, player);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModItems.GRENADE));
        this.setMaxLife(20 * 3);
        this.setSize(0.25F, 0.25F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        prevRotation = rotation;

        float speed = (float) Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionY, 2) + Math.pow(motionZ, 2));
        if(speed > 0.1)
        {
            rotation += speed * 50;
        }

        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.25, posZ, 0, 0, 0, 10);
    }

    @Override
    public void onDeath()
    {
        EntityThrowableGrenade.createGrenadeExplosion(this, thrower, posX, posY, posZ, 2.0F, false, true);
    }

    private static void createGrenadeExplosion(EntityThrowableGrenade grenade, Entity thrower, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        boolean canGunGrief = grenade.world.getGameRules().getBoolean("gunGriefing");
        Explosion explosion = new ProjectileExplosion(grenade.world, thrower, grenade, grenade.getItem(), x, y, z, ModItems.GRENADE_LAUNCHER.getGun().projectile.damage, Config.SERVER.grenades.explosionRadius, canGunGrief);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        explosion.clearAffectedBlockPositions();

        if(grenade.world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) grenade.world;
            worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, x, y, z, 0, 0.0, 0.0, 0.0, 0);
        }
    }
}
