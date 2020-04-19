package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Author: MrCrayfish
 */
public class EntityMissile extends EntityProjectile
{
    public EntityMissile(EntityType<? extends EntityProjectile> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public EntityMissile(EntityType<? extends EntityProjectile> entityType, World worldIn, LivingEntity shooter, GunItem item, Gun modifiedGun)
    {
        super(entityType, worldIn, shooter, item, modifiedGun);
    }

    @Override
    protected void onTick()
    {
        if(this.world.isRemote)
        {
            for(int i = 5; i > 0; i--)
            {
                this.world.addParticle(ParticleTypes.CLOUD, true, this.getPosX() - (this.getMotion().getX() / i), this.getPosY() - (this.getMotion().getY() / i), this.getPosZ() - (this.getMotion().getZ() / i), 0, 0, 0);
            }
            if(this.world.rand.nextInt(2) == 0)
            {
                this.world.addParticle(ParticleTypes.SMOKE, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
                this.world.addParticle(ParticleTypes.FLAME, true, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
            }
        }
    }

    @Override
    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        this.createExplosion(x, y, z, true);
    }

    @Override
    protected void onHitBlock(BlockState state, BlockPos pos, double x, double y, double z)
    {
        this.createExplosion(x, y, z, true);
    }

    @Override
    public void onExpired()
    {
        this.createExplosion(this.getPosX(), this.getPosY(), this.getPosZ(), this.world instanceof ServerWorld);
    }

    private void createExplosion(double x, double y, double z, boolean particle)
    {
        //boolean canGunGrief = this.world.getGameRules().getBoolean("gunGriefing"); //TODO convert to config

        Explosion explosion = new ProjectileExplosion(this, x, y, z, Config.COMMON.missiles.explosionRadius.get(), Explosion.Mode.NONE); //TODO Make radius configurable
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        explosion.clearAffectedBlockPositions();

        if(particle)
        {
            ServerWorld worldServer = (ServerWorld) this.world;
            worldServer.spawnParticle(ParticleTypes.EXPLOSION, x, y, z, 1, 0.0, 0.0, 0.0, 0.0); //TODO test
        }
    }
}
