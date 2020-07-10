package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class GrenadeEntity extends ProjectileEntity
{
    public GrenadeEntity(EntityType<? extends ProjectileEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public GrenadeEntity(EntityType<? extends ProjectileEntity> entityType, World world, LivingEntity shooter, GunItem item, Gun modifiedGun)
    {
        super(entityType, world, shooter, item, modifiedGun);
    }

    @Override
    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        createExplosion(this, this.getDamage() / 5f);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, double x, double y, double z)
    {
        createExplosion(this, this.getDamage() / 5f);
    }

    @Override
    public void onExpired()
    {
        createExplosion(this, this.getDamage() / 5f);
    }
}
