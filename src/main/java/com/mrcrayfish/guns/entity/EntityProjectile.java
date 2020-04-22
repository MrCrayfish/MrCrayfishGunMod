package com.mrcrayfish.guns.entity;

import com.google.common.base.Predicate;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.SpreadTracker;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.item.AmmoItem;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.EntityResult;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Gun.Projectile;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class EntityProjectile extends Entity implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> PROJECTILE_TARGETS = input -> input != null && !input.isSpectator() && input.canBeCollidedWith();

    protected int shooterId;
    protected LivingEntity shooter;
    protected Gun.General general;
    protected Gun.Projectile projectile;
    private ItemStack weapon = ItemStack.EMPTY;
    private ItemStack item = ItemStack.EMPTY;
    protected float damageModifier = 1.0F;
    protected float additionalDamage = 0.0F;
    protected EntitySize entitySize;

    public EntityProjectile(EntityType<? extends Entity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public EntityProjectile(EntityType<? extends Entity> entityType, World worldIn, LivingEntity shooter, GunItem item, Gun modifiedGun)
    {
        this(entityType, worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.general = modifiedGun.general;
        this.projectile = modifiedGun.projectile;
        this.entitySize = new EntitySize(this.projectile.size, this.projectile.size, false);

        Vec3d dir = this.getDirection(shooter, item, modifiedGun);
        this.setMotion(dir.x * this.projectile.speed, dir.y * this.projectile.speed, dir.z * this.projectile.speed);
        this.updateHeading();
        this.setPosition(shooter.getPosX(), shooter.getPosY() + shooter.getEyeHeight(), shooter.getPosZ());

        AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(this.projectile.item);
        if(ammo != null)
        {
            this.item = new ItemStack(ammo);
        }
    }

    @Override
    protected void registerData()
    {

    }

    @Override
    public EntitySize getSize(Pose pose)
    {
        return this.entitySize;
    }

    private Vec3d getDirection(LivingEntity shooter, GunItem item, Gun modifiedGun)
    {
        float gunSpread = modifiedGun.general.spread;

        if(gunSpread == 0F)
        {
            return this.getVectorFromRotation(shooter.rotationPitch, shooter.rotationYaw);
        }

        if(!modifiedGun.general.alwaysSpread)
        {
            gunSpread *= SpreadTracker.get(shooter.getUniqueID()).getSpread(item);
        }

        return this.getVectorFromRotation(shooter.rotationPitch - (gunSpread / 2.0F) + rand.nextFloat() * gunSpread, shooter.getRotationYawHead() - (gunSpread / 2.0F) + rand.nextFloat() * gunSpread);
    }

    public void setWeapon(ItemStack weapon)
    {
        this.weapon = weapon.copy();
    }

    public ItemStack getWeapon()
    {
        return weapon;
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public ItemStack getItem()
    {
        return new ItemStack(Blocks.REDSTONE_BLOCK);
    }

    public void setDamageModifier(float damageModifier)
    {
        this.damageModifier = damageModifier;
    }

    public void setAdditionalDamage(float additionalDamage)
    {
        this.additionalDamage = additionalDamage;
    }

    @Override
    public void tick()
    {
        super.tick();
        this.updateHeading();
        this.onTick();

        //TODO convert to new ProjectileHelper
        Vec3d startVec = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        Vec3d endVec = new Vec3d(this.getPosX() + this.getMotion().getX(), this.getPosY() + this.getMotion().getY(), this.getPosZ() + this.getMotion().getZ());
        RayTraceResult result = this.world.rayTraceBlocks(new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        startVec = new Vec3d(this.getPosX(), this.getPosY(), this.getPosZ());
        endVec = new Vec3d(this.getPosX() + this.getMotion().getX(), this.getPosY() + this.getMotion().getY(), this.getPosZ() + this.getMotion().getZ());

        if(result != null)
        {
            endVec = new Vec3d(result.getHitVec().x, result.getHitVec().y, result.getHitVec().z);
        }

        EntityResult entityResult = this.findEntityOnPath(startVec, endVec);
        if(entityResult != null)
        {
            result = new EntityRayTraceResult(entityResult.entity, entityResult.hitVec);
        }

        if(result instanceof EntityRayTraceResult && ((EntityRayTraceResult) result).getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) ((EntityRayTraceResult) result).getEntity();

            if(this.shooter instanceof PlayerEntity && !((PlayerEntity) this.shooter).canAttackPlayer(player))
            {
                result = null;
            }
        }

        if(result != null && !world.isRemote)
        {
            this.onHit(result);
        }

        double nextPosX = this.getPosX() + this.getMotion().getX();
        double nextPosY = this.getPosY() + this.getMotion().getY();
        double nextPosZ = this.getPosZ() + this.getMotion().getZ();
        this.setPosition(nextPosX, nextPosY, nextPosZ);

        if(this.projectile.gravity)
        {
            this.setMotion(this.getMotion().add(0, -0.05,0));
        }

        if(this.ticksExisted >= this.projectile.life)
        {
            if(this.isAlive())
            {
                this.onExpired();
            }
            this.remove();
        }
    }

    protected void onTick()
    {
    }

    protected void onExpired()
    {
    }

    @Nullable
    protected EntityResult findEntityOnPath(Vec3d start, Vec3d end)
    {
        Vec3d hitVec = null;
        Entity foundEntity = null;
        List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0), PROJECTILE_TARGETS);
        double closestDistance = 0.0D;
        for(Entity entity : entities)
        {
            if(!entity.equals(this.shooter))
            {
                AxisAlignedBB boundingBox = entity.getBoundingBox().expand(0, 0.0625, 0); //Player bounding box is one pixel off from the top of the head
                Optional<Vec3d> hitPos = boundingBox.rayTrace(start, end);
                if(!hitPos.isPresent())
                {
                    boundingBox = entity.getBoundingBox().grow(Config.COMMON.growBoundingBoxAmount.get(), 0.0625, Config.COMMON.growBoundingBoxAmount.get());
                    hitPos = boundingBox.rayTrace(start, end);
                    if(!hitPos.isPresent())
                    {
                        continue;
                    }

                    Vec3d entityVec = new Vec3d(entity.getPosX(), hitPos.get().y, entity.getPosZ());
                    RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(hitPos.get(), entityVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
                    if(raytraceresult.getType() == RayTraceResult.Type.MISS)
                    {
                        continue;
                    }
                }

                double distanceToHit = start.squareDistanceTo(hitPos.get());
                if(distanceToHit < closestDistance || closestDistance == 0.0)
                {
                    hitVec = hitPos.get();
                    foundEntity = entity;
                    closestDistance = distanceToHit;
                }
            }
        }
        return foundEntity != null ? new EntityResult(foundEntity, hitVec) : null;
    }

    private void onHit(RayTraceResult result)
    {
        if(result instanceof BlockRayTraceResult)
        {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            BlockPos pos = blockRayTraceResult.getPos();
            BlockState state = this.world.getBlockState(pos);
            Block block = state.getBlock();

            if(Config.COMMON.enableGunGriefing.get() && (block instanceof BreakableBlock || block instanceof PaneBlock) && state.getMaterial() == Material.GLASS)
            {
                this.world.destroyBlock(blockRayTraceResult.getPos(), false);
            }

            if(!state.getMaterial().isReplaceable())
            {
                this.remove();
            }

            if(block instanceof IDamageable)
            {
                ((IDamageable) block).onBlockDamaged(this.world, state, pos, (int) Math.ceil(getDamage() / 2.0) + 1);
            }

            this.onHitBlock(state, pos, result.getHitVec().x, result.getHitVec().y, result.getHitVec().z);

            return;
        }

        if(result instanceof EntityRayTraceResult)
        {
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult) result;
            Entity entity = entityRayTraceResult.getEntity();
            if(entity.getEntityId() == this.shooterId)
            {
                return;
            }
            this.onHitEntity(entity, result.getHitVec().x, result.getHitVec().y, result.getHitVec().z);
            this.remove();
            entity.hurtResistantTime = 0;
        }
    }

    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        boolean headShot = false;
        float damage = this.getDamage();
        if(Config.COMMON.enableHeadShots.get() && entity instanceof PlayerEntity)
        {
            AxisAlignedBB boundingBox = entity.getBoundingBox().expand(0, 0.0625, 0);
            if(boundingBox.maxY - y <= 8.0 * 0.0625)
            {
                headShot = true;
                damage *= Config.COMMON.headShotDamageMultiplier.get();
            }
        }

        DamageSource source = new DamageSourceProjectile("bullet", this, shooter, weapon).setProjectile();
        entity.attackEntityFrom(source, damage);

        if(entity instanceof PlayerEntity && shooter instanceof ServerPlayerEntity)
        {
            SoundEvent event = headShot ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.ENTITY_PLAYER_HURT;
            ServerPlayerEntity shooterPlayer = (ServerPlayerEntity) this.shooter;
            shooterPlayer.connection.sendPacket(new SPlaySoundPacket(event.getRegistryName(), SoundCategory.PLAYERS, new Vec3d(this.shooter.getPosX(), this.shooter.getPosY(), this.shooter.getPosZ()), 0.75F, 3.0F));
        }
    }

    protected void onHitBlock(BlockState state, BlockPos pos, double x, double y, double z)
    {
        ((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), x, y, z, (int) this.projectile.damage, 0.0, 0.0, 0.0, 0.05);
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        this.projectile = new Gun.Projectile();
        this.projectile.deserializeNBT(compound.getCompound("Projectile"));
        this.general = new Gun.General();
        this.general.deserializeNBT(compound.getCompound("General"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        compound.put("Projectile", this.projectile.serializeNBT());
        compound.put("General", this.general.serializeNBT());
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeCompoundTag(this.projectile.serializeNBT());
        buffer.writeCompoundTag(this.general.serializeNBT());
        buffer.writeInt(this.shooterId);
        ItemStackUtil.writeItemStackToBufIgnoreTag(buffer, this.item);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.projectile = new Gun.Projectile();
        this.projectile.deserializeNBT(buffer.readCompoundTag());
        this.general = new Gun.General();
        this.general.deserializeNBT(buffer.readCompoundTag());
        this.shooterId = buffer.readInt();
        this.item = ItemStackUtil.readItemStackFromBufIgnoreTag(buffer);
        this.entitySize = new EntitySize(this.projectile.size, this.projectile.size, false);
    }

    public void updateHeading()
    {
        float f = MathHelper.sqrt(this.getMotion().getX() * this.getMotion().getX() + this.getMotion().getZ() * this.getMotion().getZ());
        this.rotationYaw = (float) (MathHelper.atan2(this.getMotion().getX(), this.getMotion().getZ()) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.getMotion().getY(), (double) f) * (180D / Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    public Projectile getProjectile()
    {
        return projectile;
    }

    private Vec3d getVectorFromRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    public LivingEntity getShooter()
    {
        return this.shooter;
    }

    public float getDamage()
    {
        float damage = (this.projectile.damage + this.additionalDamage) * this.damageModifier;
        if(this.projectile.damageReduceOverLife)
        {
            float modifier = ((float) this.projectile.life - (float) (this.ticksExisted - 1)) / (float) this.projectile.life;
            damage *= modifier;
        }
        return damage / this.general.projectileAmount;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        return true;
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
