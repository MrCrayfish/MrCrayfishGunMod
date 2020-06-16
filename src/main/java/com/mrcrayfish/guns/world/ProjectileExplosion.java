package com.mrcrayfish.guns.world;

import com.google.common.collect.Sets;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
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
    private final Entity projectile;
    private final Entity shooter;
    private final ItemStack weapon;
    private float damage;
    private final double x, y, z;
    private double radius;

    public ProjectileExplosion(ProjectileEntity projectile, double x, double y, double z, double radius, Explosion.Mode mode)
    {
        super(projectile.world, projectile.getShooter(), x, y, z, (float) (radius * 2), false, mode);
        this.world = projectile.world;
        this.projectile = projectile;
        this.shooter = projectile.getShooter();
        this.weapon = projectile.getWeapon();
        this.damage = projectile.getDamage();
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    public ProjectileExplosion(World world, Entity shooter, Entity source, ItemStack weapon, double x, double y, double z, float damage, double radius, Explosion.Mode mode)
    {
        super(world, shooter, x, y, z, (float) (radius * 2), false, mode);
        this.world = world;
        this.projectile = source;
        this.shooter = shooter;
        this.weapon = weapon;
        this.damage = damage;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
    }

    @Override
    public void doExplosionA()
    {
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
                        float f = (float) (this.radius * (0.7F + this.world.rand.nextFloat() * 0.6F));
                        double d4 = this.x;
                        double d6 = this.y;
                        double d8 = this.z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
                        {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            BlockState blockstate = this.world.getBlockState(blockpos);
                            IFluidState ifluidstate = this.world.getFluidState(blockpos);
                            if(!blockstate.isAir(this.world, blockpos) || !ifluidstate.isEmpty())
                            {
                                float f2 = Math.max(blockstate.getExplosionResistance(this.world, blockpos, this.shooter, this), ifluidstate.getExplosionResistance(this.world, blockpos, this.shooter, this));
                                if(this.shooter != null)
                                {
                                    f2 = this.shooter.getExplosionResistance(this, this.world, blockpos, blockstate, ifluidstate, f2);
                                }

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if(f > 0.0F && (this.shooter == null || this.shooter.canExplosionDestroyBlock(this, this.world, blockpos, blockstate, f)))
                            {
                                set.add(blockpos);
                            }

                            d4 += d0 * (double) 0.3F;
                            d6 += d1 * (double) 0.3F;
                            d8 += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.getAffectedBlockPositions().addAll(set);
        float f3 = (float) this.radius;
        int k1 = MathHelper.floor(this.x - (double) f3 - 1.0D);
        int l1 = MathHelper.floor(this.x + (double) f3 + 1.0D);
        int i2 = MathHelper.floor(this.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
        int j2 = MathHelper.floor(this.z - (double) f3 - 1.0D);
        int j1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this.shooter, new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f3);
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for(int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);
            if(!entity.isImmuneToExplosions())
            {
                double d12 = (double) (MathHelper.sqrt(entity.getDistanceSq(vec3d)) / f3);
                if(d12 <= 1.0D)
                {
                    double d5 = entity.getPosX() - this.x;
                    double d7 = entity.getPosYEye() - this.y;
                    double d9 = entity.getPosZ() - this.z;
                    double d13 = (double) MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if(d13 != 0.0D)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double) getBlockDensity(vec3d, entity);
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(this.getDamageSource(), (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = d10;
                        if(entity instanceof LivingEntity)
                        {
                            d11 = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, d10);
                        }

                        entity.setMotion(entity.getMotion().add(d5 * d11, d7 * d11, d9 * d11));
                        if(entity instanceof PlayerEntity)
                        {
                            PlayerEntity playerentity = (PlayerEntity) entity;
                            if(!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.isFlying))
                            {
                                this.getPlayerKnockbackMap().put(playerentity, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    /*@Override
    public void doExplosionA()
    {
        float size = (float) (this.radius * 2);
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
                            BlockState state = this.world.getBlockState(pos);

                            if(state.getMaterial() != Material.AIR)
                            {
                                float resistance = this.shooter != null ? this.shooter.getExplosionResistance(this, this.world, pos, state) : state.getBlock().getExplosionResistance(world, pos, null, this);
                                f -= (resistance + 0.3F) * 0.3F;
                            }

                            if(f > 0.0F && (this.shooter == null || this.shooter.canExplosionDestroyBlock(this, this.world, pos, state, f)))
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
                    double deltaX = entity.getPosX() - this.x;
                    double deltaY = entity.getPosY() + (double) entity.getEyeHeight() - this.y;
                    double deltaZ = entity.getPosZ() - this.z;
                    double deltaDistance = (double) MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                    if(deltaDistance != 0.0D)
                    {
                        deltaX = deltaX / deltaDistance;
                        deltaY = deltaY / deltaDistance;
                        deltaZ = deltaZ / deltaDistance;

                        double density = (double) getBlockDensity(vec3d, entity);
                        double damage = ((this.radius - distanceToExplosion) / this.radius) * density;

                        entity.attackEntityFrom(new DamageSourceProjectile("bullet", projectile, shooter, this.weapon), (float) (damage * this.damage));

                        double motion = damage;
                        if(entity instanceof LivingEntity)
                        {
                            motion = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, damage);
                        }

                        entity.getMotion().add(deltaX * motion, deltaY * motion, deltaZ * motion);

                        if(entity instanceof PlayerEntity)
                        {
                            PlayerEntity player = (PlayerEntity) entity;

                            if(!player.isSpectator() && (!player.isCreative() || !player.abilities.isFlying))
                            {
                                this.getPlayerKnockbackMap().put(player, new Vec3d(deltaX * damage, deltaY * damage, deltaZ * damage));
                            }
                        }
                    }
                }
            }
        }
    }*/
}
