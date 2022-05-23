package com.tac.guns.entity;

import com.tac.guns.Config;
import com.tac.guns.init.ModEntities;
import com.tac.guns.init.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ThrowableGrenadeEntity extends ThrowableItemEntity
{
    public float rotation;
    public float prevRotation;
    public float power;

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public ThrowableGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, LivingEntity entity)
    {
        super(entityType, world, entity);
        //this.setShouldBounce(true);
        //this.setGravityVelocity(0.05F);
        //this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
        //this.setMaxLife(20 * 3);
    }

    public ThrowableGrenadeEntity(World world, LivingEntity entity, int timeLeft, float power)
    {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.power = power;
        //this.setShouldBounce(true);
        //this.setGravityVelocity(0.045F);
        //this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
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
        if (speed > 0.1)
        {
            this.rotation += speed * 50;
        }
        if (this.world.isRemote)
        {
            this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY() + 0.25, this.getPosZ(), 0, 0.1, 0);
        }
    }

    @Override
    public void onDeath()
    {
        GrenadeEntity.createExplosion(this, this.power*Config.COMMON.grenades.explosionRadius.get().floatValue(), true);
    }
}
