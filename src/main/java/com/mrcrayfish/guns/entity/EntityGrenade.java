package com.mrcrayfish.guns.entity;

import com.google.common.collect.Sets;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemAmmo;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Set;

/**
 * Author: MrCrayfish
 */
public class EntityGrenade extends EntityThrowableItem
{
    public float rotation;
    public float prevRotation;

    public EntityGrenade(World worldIn)
    {
        super(worldIn);
    }

    public EntityGrenade(World world, EntityPlayer player)
    {
        super(world, player);
        this.setShouldBounce(true);
        this.setGravityVelocity(0.05F);
        this.setItem(new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE.ordinal()));
        this.setMaxLife(20 * 3);
        this.setSize(0.25F, 0.25F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        prevRotation = rotation;

        float speed = (float) Math.sqrt(Math.pow(motionX, 2) + Math.pow(motionY, 2) + Math.pow(motionZ, 2));
        if(speed > 0.1)
        {
            rotation += speed * 50;
        }

        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 0.25, posZ, 0, 0, 0, 10);
    }

    @Override
    public void onDeath()
    {
        EntityGrenade.createGrenadeExplosion(this, thrower, posX, posY, posZ, 2.0F, false, true);
    }

    private static void createGrenadeExplosion(EntityGrenade grenade, Entity thrower, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking)
    {
        Explosion explosion = new ExplosionGrenade(grenade.world, grenade, thrower, x, y, z, strength, isFlaming, isSmoking);
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        if(!isSmoking)
        {
            explosion.clearAffectedBlockPositions();
        }

        if(grenade.world instanceof WorldServer)
        {
            WorldServer worldServer = (WorldServer) grenade.world;
            for(EntityPlayer entityplayer : worldServer.playerEntities)
            {
                if(entityplayer.getDistanceSq(x, y, z) < 4096.0D)
                {
                    ((EntityPlayerMP) entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
                }
            }
        }
    }

    public static class ExplosionGrenade extends Explosion
    {
        private World world;
        private Entity grenade;
        private Entity exploder;
        private double x, y, z;
        private float size;

        public ExplosionGrenade(World worldIn, Entity grenade, Entity exploder, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain)
        {
            super(worldIn, exploder, x, y, z, size, flaming, damagesTerrain);
            this.world = worldIn;
            this.grenade = grenade;
            this.exploder = exploder;
            this.x = x;
            this.y = y;
            this.z = z;
            this.size = size;
        }

        /**
         * Patches the explosion so the damage source can be controlled. It also includes the person
         * who threw the grenade to be included in the affected entities.
         */
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
                            float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                            double xPos = this.x;
                            double yPos = this.y;
                            double zPos = this.z;

                            for(float f1 = 0.3F; f > 0.0F; f -= 0.225F)
                            {
                                BlockPos blockpos = new BlockPos(xPos, yPos, zPos);
                                IBlockState iblockstate = this.world.getBlockState(blockpos);

                                if(iblockstate.getMaterial() != Material.AIR)
                                {
                                    float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(world, blockpos, (Entity) null, this);
                                    f -= (f2 + 0.3F) * 0.3F;
                                }

                                if(f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockpos, iblockstate, f)))
                                {
                                    set.add(blockpos);
                                }

                                xPos += d0 * 0.3D;
                                yPos += d1 * 0.3D;
                                zPos += d2 * 0.3D;
                            }
                        }
                    }
                }
            }

            this.getAffectedBlockPositions().addAll(set);
            float doubleSize = this.size * 2.0F;
            int minX = MathHelper.floor(this.x - (double) doubleSize - 1.0D);
            int maxX = MathHelper.floor(this.x + (double) doubleSize + 1.0D);
            int minY = MathHelper.floor(this.y - (double) doubleSize - 1.0D);
            int maxY = MathHelper.floor(this.y + (double) doubleSize + 1.0D);
            int minZ = MathHelper.floor(this.z - (double) doubleSize - 1.0D);
            int maxZ = MathHelper.floor(this.z + (double) doubleSize + 1.0D);

            List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY, (double) maxZ));
            Vec3d vec3d = new Vec3d(this.x, this.y, this.z);
            for(Entity entity : list)
            {
                if(!entity.isImmuneToExplosions())
                {
                    double distanceToExplosion = entity.getDistance(this.x, this.y, this.z) / (double) doubleSize;
                    if(distanceToExplosion <= 1.0D)
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
                            double damage = (1.0D - distanceToExplosion) * density;
                            entity.attackEntityFrom(new DamageSourceProjectile("grenade", grenade, exploder, new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE.ordinal())), (float) ((int) ((damage * damage + damage) / 2.0D * 7.0D * (double) doubleSize + 1.0D)));

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
}
