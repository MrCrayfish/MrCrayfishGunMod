package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

    public GrenadeEntity(EntityType<? extends ProjectileEntity> entityType, World world, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        super(entityType, world, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3d hitVec, Vec3d startVec, Vec3d endVec, boolean headshot)
    {
        createExplosion(this, this.getDamage() / 5F, true);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z)
    {
        createExplosion(this, this.getDamage() / 5F, true);
    }

    @Override
    public void onExpired()
    {
        createExplosion(this, this.getDamage() / 5F, true);
    }
}
