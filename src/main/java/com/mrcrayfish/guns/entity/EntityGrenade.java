package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.world.ProjectileExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * Author: MrCrayfish
 */
public class EntityGrenade extends EntityProjectile
{
    public EntityGrenade(World worldIn)
    {
        super(worldIn);
    }

    public EntityGrenade(World worldIn, EntityLivingBase shooter, ItemGun item, Gun modifiedGun)
    {
        super(worldIn, shooter, item, modifiedGun);
    }

    @Override
    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        this.createExplosion(x, y, z, true);
    }

    @Override
    protected void onHitBlock(IBlockState state, BlockPos pos, double x, double y, double z)
    {
        this.createExplosion(x, y, z, true);
    }

    @Override
    public void onExpired()
    {
        this.createExplosion(this.posX, this.posY, this.posZ, this.world instanceof WorldServer);
    }

    private void createExplosion(double x, double y, double z, boolean particle)
    {
        boolean canGunGrief = this.world.getGameRules().getBoolean("gunGriefing");

        Explosion explosion = new ProjectileExplosion(this, x, y, z, GunConfig.SERVER.grenades.explosionRadius, canGunGrief);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        explosion.clearAffectedBlockPositions();

        if(particle)
        {
            WorldServer worldServer = (WorldServer) this.world;
            worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, x, y, z, 0, 0.0, 0.0, 0.0, 0);
        }
    }
}
