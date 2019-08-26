package com.mrcrayfish.guns.world;

import com.google.common.collect.Sets;
import com.mrcrayfish.guns.entity.DamageSourceProjectile;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

/**
 * Author: MrCrayfish
 */
public class ProjectileExplosion extends Explosion
{
    private final World world;
    private final EntityProjectile projectile;
    private final double x, y, z;
    private double radius;

    public ProjectileExplosion(EntityProjectile projectile, double x, double y, double z, double radius, boolean damagesTerrain)
    {
        super(projectile.world, projectile.getShooter(), x, y, z, (float) (radius * 2), false, damagesTerrain);
        this.world = projectile.world;
        this.projectile = projectile;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    @Override
    public void doExplosionA()
    {
        float size = (float) (this.radius * 2);
        Entity shooter = projectile.getShooter();
        Set<BlockPos> set = Sets.newHashSet();
        for(int j = 0; j < 16; ++j)
        {
            for(int k = 0; k < 16; ++k)
            {
                for(int l = 0; l < 16; ++l)
                {
                    if(j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
                    {
                        double d0 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) l / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double posX = this.x;
                        double posY = this.y;
                        double posZ = this.z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.225F)
                        {
                            BlockPos pos = new BlockPos(posX, posY, posZ);
                            IBlockState state = this.world.getBlockState(pos);

                            if(state.getMaterial() != Material.AIR)
                            {
                                float resistance = shooter != null ? shooter.getExplosionResistance(this, this.world, pos, state) : state.getBlock().getExplosionResistance(world, pos, null, this);
                                f -= (resistance + 0.3F) * 0.3F;
                            }

                            if(f > 0.0F && (shooter == null || shooter.canExplosionDestroyBlock(this, this.world, pos, state, f)))
                            {
                                set.add(pos);
                            }

                            posX += d0 * 0.3D;
                            posY += d1 * 0.3D;
                            posZ += d2 * 0.3D;
                        }
                    }
                }
            }
        }
        this.getAffectedBlockPositions().addAll(set);

        int minX = MathHelper.floor(this.x - this.radius);
        int maxX = MathHelper.floor(this.x + this.radius);
        int minY = MathHelper.floor(this.y - this.radius);
        int maxY = MathHelper.floor(this.y + this.radius);
        int minZ = MathHelper.floor(this.z - this.radius);
        int maxZ = MathHelper.floor(this.z + this.radius);

        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ));
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
        for(Entity entity : list)
        {
            if(!entity.isImmuneToExplosions())
            {
                double distanceToExplosion = entity.getDistance(this.x, this.y, this.z);
                if(distanceToExplosion <= this.radius)
                {
                    double deltaX = entity.posX - this.x;
                    double deltaY = entity.posY + (double) entity.getEyeHeight() - this.y;
                    double deltaZ = entity.posZ - this.z;
                    double deltaDistance = (double) MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                    if(deltaDistance != 0.0D)
                    {
                        deltaX = deltaX / deltaDistance;
                        deltaY = deltaY / deltaDistance;
                        deltaZ = deltaZ / deltaDistance;

                        double density = (double) this.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double damage = ((this.radius - distanceToExplosion) / this.radius) * density;

                        entity.attackEntityFrom(new DamageSourceProjectile("bullet", projectile, shooter, projectile.getWeapon()), (float) (damage * projectile.getDamage()));

                        double motion = damage;
                        if(entity instanceof EntityLivingBase)
                        {
                            motion = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, damage);
                        }
                        entity.motionX += deltaX * motion;
                        entity.motionY += deltaY * motion;
                        entity.motionZ += deltaZ * motion;

                        if(entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityplayer = (EntityPlayer) entity;

                            if(!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            {
                                this.getPlayerKnockbackMap().put(entityplayer, new Vec3d(deltaX * damage, deltaY * damage, deltaZ * damage));
                            }
                        }
                    }
                }
            }
        }
    }
}
