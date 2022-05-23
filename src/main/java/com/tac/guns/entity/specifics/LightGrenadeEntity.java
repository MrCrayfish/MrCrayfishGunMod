package com.tac.guns.entity.specifics;

import com.tac.guns.Config;
import com.tac.guns.entity.GrenadeEntity;
import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.ThrowableItemEntity;
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
public class LightGrenadeEntity extends ThrowableGrenadeEntity
{
    public LightGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public LightGrenadeEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, LivingEntity entity)
    {
        super(entityType, world, entity);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.03F);
        this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
        //this.setMaxLife(20 * 2);
    }

    public LightGrenadeEntity(World world, LivingEntity entity, int timeLeft, float power)
    {
        super(ModEntities.THROWABLE_GRENADE.get(), world, entity);
        this.power = power;
        this.setShouldBounce(true);
        this.setItem(new ItemStack(ModItems.LIGHT_GRENADE.get()));
        this.setMaxLife(timeLeft);
        this.setGravityVelocity(0.0325F);
    }

    @Override
    public void tick()
    {
        super.tick();
        this.prevRotation = this.rotation;
        double speed = this.getMotion().length();
        if (speed > 0.1)
        {
            this.rotation += speed * 325;
        }
        if (this.world.isRemote)
        {
            this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY() + 0.25, this.getPosZ(), 0, 0.135, 0);
        }
    }
}
