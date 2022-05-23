package com.tac.guns.world;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ProjectileExplosion extends Explosion
{
    private static final ExplosionContext DEFAULT_CONTEXT = new ExplosionContext();

    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float size;
    private final Entity exploder;
    private final ExplosionContext context;

    public ProjectileExplosion(World world, Entity exploder, @Nullable DamageSource source, @Nullable ExplosionContext context, double x, double y, double z, float size, boolean causesFire, Mode mode)
    {
        super(world, exploder, source, context, x, y, z, size, causesFire, mode);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.exploder = exploder;
        this.context = context == null ? DEFAULT_CONTEXT : context;
    }

    @Override
    public void doExplosionA()
    {
        Set<BlockPos> set = Sets.newHashSet();
        for(int x = 0; x < 16; x++)
        {
            for(int y = 0; y < 16; y++)
            {
                for(int z = 0; z < 16; z++)
                {
                    if(x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15)
                    {
                        double d0 = (double) ((float) x / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) y / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) z / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double blockX = this.x;
                        double blockY = this.y;
                        double blockZ = this.z;

                        for(; f > 0.0F; f -= 0.225F)
                        {
                            BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                            BlockState blockState = this.world.getBlockState(pos);
                            FluidState fluidState = this.world.getFluidState(pos);
                            Optional<Float> optional = this.context.getExplosionResistance(this, this.world, pos, blockState, fluidState);
                            if(optional.isPresent())
                            {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if(f > 0.0F && this.context.canExplosionDestroyBlock(this, this.world, pos, blockState, f))
                            {
                                set.add(pos);
                            }

                            blockX += d0 * (double) 0.3F;
                            blockY += d1 * (double) 0.3F;
                            blockZ += d2 * (double) 0.3F;
                        }
                    }
                }
            }
        }

        this.getAffectedBlockPositions().addAll(set);

        float radius = this.size * 2.0F;
        int minX = MathHelper.floor(this.x - (double) radius - 1.0D);
        int maxX = MathHelper.floor(this.x + (double) radius + 1.0D);
        int minY = MathHelper.floor(this.y - (double) radius - 1.0D);
        int maxY = MathHelper.floor(this.y + (double) radius + 1.0D);
        int minZ = MathHelper.floor(this.z - (double) radius - 1.0D);
        int maxZ = MathHelper.floor(this.z + (double) radius + 1.0D);

        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ));

        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, entities, radius);

        Vector3d explosionPos = new Vector3d(this.x, this.y, this.z);
        for(Entity entity : entities)
        {
            if(entity.isImmuneToExplosions())
                continue;

            double strength = (double) (MathHelper.sqrt(entity.getDistanceSq(explosionPos)) / radius);
            if(strength > 1.0D)
                continue;

            double deltaX = entity.getPosX() - this.x;
            double deltaY = (entity instanceof TNTEntity ? entity.getPosY() : entity.getPosYEye()) - this.y;
            double deltaZ = entity.getPosZ() - this.z;
            double distanceToExplosion = (double) MathHelper.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

            if(distanceToExplosion != 0.0D)
            {
                deltaX /= distanceToExplosion;
                deltaY /= distanceToExplosion;
                deltaZ /= distanceToExplosion;
            }
            else
            {
                // Fixes an issue where explosion exactly on the player would cause no damage
                deltaX = 0.0;
                deltaY = 1.0;
                deltaZ = 0.0;
            }

            double blockDensity = (double) getBlockDensity(explosionPos, entity);
            double damage = (1.0D - strength) * blockDensity;
            entity.attackEntityFrom(this.getDamageSource(), (float) ((int) ((damage * damage + damage) / 2.0D * 7.0D * (double) radius + 1.0D)));

            double blastDamage = damage;
            if(entity instanceof LivingEntity)
            {
                blastDamage = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) entity, damage);
            }
            entity.setMotion(entity.getMotion().add(deltaX * blastDamage, deltaY * blastDamage, deltaZ * blastDamage));

            if(entity instanceof PlayerEntity)
            {
                PlayerEntity player = (PlayerEntity) entity;
                if(!player.isSpectator() && (!player.isCreative() || !player.abilities.isFlying))
                {
                    this.getPlayerKnockbackMap().put(player, new Vector3d(deltaX * damage, deltaY * damage, deltaZ * damage));
                }
            }
        }
    }
}
