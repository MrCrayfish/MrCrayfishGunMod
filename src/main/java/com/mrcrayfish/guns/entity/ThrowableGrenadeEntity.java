package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Author: MrCrayfish
 */
public class ThrowableGrenadeEntity extends ThrowableItemEntity
{
    public float rotation;
    public float prevRotation;

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, LivingEntity entity)
    {
        super(entityType, world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModItems.GRENADE.get()));
        this.setMaxLife(20 * 3);
    }

    public ThrowableGrenadeEntity(World world, LivingEntity entity, int timeLeft)
    {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModItems.GRENADE.get()));
        this.setMaxLife(timeLeft);
    }

    @Override
    protected void registerData()
    {

    }

    @Override
    public void tick()
    {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getMotion().length();
        if(speed > 0.1)
        {
            this.rotation += speed * 50;
        }
        if(this.world.isRemote)
        {
            this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY() + 0.25, this.getPosZ(), 0, 0, 0);
        }
    }

    @Override
    public void onDeath()
    {
        ThrowableGrenadeEntity.createGrenadeExplosion(this, this.owner, this.getPosX(), this.getPosY(), this.getPosZ(), 2.0F, false, true);
    }

    private static void createGrenadeExplosion(ThrowableGrenadeEntity grenade, Entity thrower, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion explosion = new ProjectileExplosion(grenade.world, thrower, grenade, grenade.getItem(), x, y, z, ModItems.GRENADE_LAUNCHER.get().getGun().projectile.damage, Config.COMMON.grenades.explosionRadius.get(), Explosion.Mode.NONE);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        explosion.clearAffectedBlockPositions();

        if(grenade.world instanceof ServerWorld)
        {
            ServerWorld worldServer = (ServerWorld) grenade.world;
            worldServer.spawnParticle(ParticleTypes.EXPLOSION, x, y, z, 0, 0.0, 0.0, 0.0, 0);
        }
    }
}
