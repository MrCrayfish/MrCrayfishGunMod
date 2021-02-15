package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class MissileEntity extends ProjectileEntity
{
    public MissileEntity(EntityType<? extends ProjectileEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public MissileEntity(EntityType<? extends ProjectileEntity> entityType, World worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun);
    }

    @Override
    protected void onProjectileTick()
    {
        if (this.world.isRemote)
        {
            for (int i = 5; i > 0; i--)
            {
                this.world.addParticle(ParticleTypes.CLOUD, true, this.getPosX() - (this.getMotion().getX() / i), this.getPosY() - (this.getMotion().getY() / i), this.getPosZ() - (this.getMotion().getZ() / i), 0, 0, 0);
            }
            if (this.world.rand.nextInt(2) == 0)
            {
                this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
                this.world.addParticle(ParticleTypes.FLAME, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(Entity entity, Vec3d hitVec, Vec3d startVec, Vec3d endVec, boolean headshot)
    {
        createExplosion(this, Config.COMMON.missiles.explosionRadius.get().floatValue(), false);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, Direction face, double x, double y, double z)
    {
        createExplosion(this, Config.COMMON.missiles.explosionRadius.get().floatValue(), false);
    }

    @Override
    public void onExpired()
    {
        createExplosion(this, Config.COMMON.missiles.explosionRadius.get().floatValue(), false);
    }
}
