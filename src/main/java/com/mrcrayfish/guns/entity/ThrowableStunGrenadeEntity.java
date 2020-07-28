package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.Config.EffectCriteria;
import com.mrcrayfish.guns.init.ModEntities;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModPotions;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageStunGrenade;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class ThrowableStunGrenadeEntity extends ThrowableGrenadeEntity
{
    public ThrowableStunGrenadeEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, World world)
    {
        super(entityType, world);
    }

    public ThrowableStunGrenadeEntity(EntityType<? extends ThrowableGrenadeEntity> entityType, World world, LivingEntity player)
    {
        super(entityType, world, player);
        this.setItem(new ItemStack(ModItems.STUN_GRENADE.get()));
    }

    public ThrowableStunGrenadeEntity(World world, LivingEntity player, int maxCookTime)
    {
        super(ModEntities.THROWABLE_STUN_GRENADE.get(), world, player);
        this.setItem(new ItemStack(ModItems.STUN_GRENADE.get()));
        this.setMaxLife(maxCookTime);
    }

    @SubscribeEvent
    public static void blindMobs(LivingSetAttackTargetEvent event)
    {
        if (Config.COMMON.stunGrenades.blind.blindMobs.get() && event.getTarget() != null && event.getEntityLiving() instanceof MobEntity
                && ModPotions.BLINDED.get().getEffects().stream().anyMatch(effect -> event.getEntityLiving().isPotionActive(effect.getPotion())))
            ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
    }

    @Override
    public void onDeath()
    {
        double y = this.getPosY() + this.getType().getSize().height * 0.5;
        world.playSound(null, this.getPosX(), y, this.getPosZ(), ModSounds.ENTITY_STUN_GRENADE_EXPLOSION.get(), SoundCategory.BLOCKS, 4, (1 + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
        if(world.isRemote)
        {
            return;
        }
        PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getPosX(), y, this.getPosZ(), 64, this.world.func_234923_W_())), new MessageStunGrenade(this.getPosX(), y, this.getPosZ()));

        // Calculate bounds of area where potentially effected players my be
        double diameter = Math.max(Config.COMMON.stunGrenades.deafen.criteria.radius.get(), Config.COMMON.stunGrenades.blind.criteria.radius.get()) * 2 + 1;
        int minX = MathHelper.floor(this.getPosX() - diameter);
        int maxX = MathHelper.floor(this.getPosX() + diameter);
        int minY = MathHelper.floor(y - diameter);
        int maxY = MathHelper.floor(y + diameter);
        int minZ = MathHelper.floor(this.getPosZ() - diameter);
        int maxZ = MathHelper.floor(this.getPosZ() + diameter);

        // Affect all non-spectating players in range of the blast
        Vector3d grenade = new Vector3d(this.getPosX(), y, this.getPosZ());
        Vector3d eyes, directionGrenade;
        double distance;
        for(LivingEntity entity : world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)))
        {
            if(entity.isImmuneToExplosions()) continue;

            eyes = entity.getEyePosition(1.0F);
            directionGrenade = grenade.subtract(eyes);
            distance = directionGrenade.length();

            // Calculate angle between eye-gaze line and eye-grenade line
            double angle = Math.toDegrees(Math.acos(entity.getLook(1.0F).dotProduct(directionGrenade.normalize())));

            // Apply effects as determined by their criteria
            if(this.calculateAndApplyEffect(ModPotions.DEAFENED.get(), Config.COMMON.stunGrenades.deafen.criteria, entity, grenade, eyes, distance, angle)
                && Config.COMMON.stunGrenades.deafen.panicMobs.get())
            {
                entity.setRevengeTarget(entity);
            }
            if(this.calculateAndApplyEffect(ModPotions.BLINDED.get(), Config.COMMON.stunGrenades.blind.criteria, entity, grenade, eyes, distance, angle)
                && Config.COMMON.stunGrenades.blind.blindMobs.get() && entity instanceof MobEntity)
            {
                ((MobEntity) entity).setAttackTarget(null);
            }
        }
    }

    private boolean calculateAndApplyEffect(Potion potion, EffectCriteria criteria, LivingEntity entity, Vector3d grenade, Vector3d eyes, double distance, double angle)
    {
        double angleMax = criteria.angleEffect.get() * 0.5;
        if(distance <= criteria.radius.get() && angleMax > 0 && angle <= angleMax)
        {
            // Verify that light can pass through all blocks obstructing the entity's line of sight to the grenade
            if(potion != ModPotions.BLINDED.get() || !Config.COMMON.stunGrenades.blind.criteria.raytraceOpaqueBlocks.get()
                    || rayTraceOpaqueBlocks(this.world, eyes, grenade, false, false, false) == null)
            {
                // Duration attenuated by distance
                int durationBlinded = (int) Math.round(criteria.durationMax.get() - (criteria.durationMax.get() - criteria.durationMin.get()) * (distance / criteria.radius.get()));

                // Duration further attenuated by angle
                durationBlinded *= 1 - (angle * (1 - criteria.angleAttenuationMax.get())) / angleMax;

                for(EffectInstance instance : potion.getEffects())
                {
                    entity.addPotionEffect(new EffectInstance(instance.getPotion(), durationBlinded, 0, false, false));
                }
                return !(entity instanceof PlayerEntity);
            }
        }
        return false;
    }

    @Nullable
    public RayTraceResult rayTraceOpaqueBlocks(World world, Vector3d start, Vector3d end, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock)
    {
        if(!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z))
        {
            if(!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z))
            {
                int endX = MathHelper.floor(end.x);
                int endY = MathHelper.floor(end.y);
                int endZ = MathHelper.floor(end.z);
                int startX = MathHelper.floor(start.x);
                int startY = MathHelper.floor(start.y);
                int startZ = MathHelper.floor(start.z);
                BlockPos pos = new BlockPos(startX, startY, startZ);
                BlockState stateInside = world.getBlockState(pos);

                // Added light opacity check
                if(stateInside.getOpacity(world, pos) != 0 && (!ignoreBlockWithoutBoundingBox || stateInside.getCollisionShape(world, pos) != VoxelShapes.empty()))
                {
                    RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                    if(raytraceresult != null)
                    {
                        return raytraceresult;
                    }
                }

                RayTraceResult raytraceresult2 = null;
                int limit = 200;
                while(limit-- >= 0)
                {
                    if(Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z))
                    {
                        return null;
                    }

                    if(startX == endX && startY == endY && startZ == endZ)
                    {
                        return returnLastUncollidableBlock ? raytraceresult2 : null;
                    }

                    boolean completedX = true;
                    boolean completedY = true;
                    boolean completedZ = true;
                    double d0 = 999;
                    double d1 = 999;
                    double d2 = 999;

                    if(endX > startX)
                    {
                        d0 = startX + 1;
                    }
                    else if(endX < startX)
                    {
                        d0 = startX;
                    }
                    else
                    {
                        completedX = false;
                    }

                    if(endY > startY)
                    {
                        d1 = startY + 1;
                    }
                    else if(endY < startY)
                    {
                        d1 = startY;
                    }
                    else
                    {
                        completedY = false;
                    }

                    if(endZ > startZ)
                    {
                        d2 = startZ + 1;
                    }
                    else if(endZ < startZ)
                    {
                        d2 = startZ;
                    }
                    else
                    {
                        completedZ = false;
                    }

                    double d3 = 999;
                    double d4 = 999;
                    double d5 = 999;
                    double d6 = end.x - start.x;
                    double d7 = end.y - start.y;
                    double d8 = end.z - start.z;

                    if(completedX) d3 = (d0 - start.x) / d6;

                    if(completedY) d4 = (d1 - start.y) / d7;

                    if(completedZ) d5 = (d2 - start.z) / d8;

                    if(d3 == -0) d3 = -1.0E-4D;

                    if(d4 == -0) d4 = -1.0E-4D;

                    if(d5 == -0) d5 = -1.0E-4D;

                    Direction direction;

                    if(d3 < d4 && d3 < d5)
                    {
                        direction = endX > startX ? Direction.WEST : Direction.EAST;
                        start = new Vector3d(d0, start.y + d7 * d3, start.z + d8 * d3);
                    }
                    else if(d4 < d5)
                    {
                        direction = endY > startY ? Direction.DOWN : Direction.UP;
                        start = new Vector3d(start.x + d6 * d4, d1, start.z + d8 * d4);
                    }
                    else
                    {
                        direction = endZ > startZ ? Direction.NORTH : Direction.SOUTH;
                        start = new Vector3d(start.x + d6 * d5, start.y + d7 * d5, d2);
                    }

                    startX = MathHelper.floor(start.x) - (direction == Direction.EAST ? 1 : 0);
                    startY = MathHelper.floor(start.y) - (direction == Direction.UP ? 1 : 0);
                    startZ = MathHelper.floor(start.z) - (direction == Direction.SOUTH ? 1 : 0);
                    pos = new BlockPos(startX, startY, startZ);
                    BlockState state = world.getBlockState(pos);

                    // Added light opacity check
                    if(state.getOpacity(world, pos) != 0 && (!ignoreBlockWithoutBoundingBox || state.getMaterial() == Material.PORTAL || state.getCollisionShape(world, pos) != VoxelShapes.empty()))
                    {
                        return world.rayTraceBlocks(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                    }
                }
                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            return null;
        }
        return null;
    }
}