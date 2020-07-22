package com.mrcrayfish.guns.entity;

import com.google.common.base.Predicate;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.BoundingBoxTracker;
import com.mrcrayfish.guns.common.SpreadTracker;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBlood;
import com.mrcrayfish.guns.network.message.MessageBulletHole;
import com.mrcrayfish.guns.object.EntityResult;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Gun.Projectile;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.network.play.server.SPlaySoundPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> PROJECTILE_TARGETS = input -> input != null && !input.isSpectator() && input.canBeCollidedWith();
    private static final Predicate<BlockState> IGNORE_LEAVES = input -> input != null && Config.COMMON.gameplay.ignoreLeaves.get() && input.getBlock() instanceof LeavesBlock;

    protected int shooterId;
    protected LivingEntity shooter;
    protected Gun modifiedGun;
    protected Gun.General general;
    protected Gun.Projectile projectile;
    private ItemStack weapon = ItemStack.EMPTY;
    private ItemStack item = ItemStack.EMPTY;
    protected float additionalDamage = 0.0F;
    protected EntitySize entitySize;
    protected double modifiedGravity;
    protected int life;

    public ProjectileEntity(EntityType<? extends Entity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public ProjectileEntity(EntityType<? extends Entity> entityType, World worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        this(entityType, worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.modifiedGun = modifiedGun;
        this.general = modifiedGun.general;
        this.projectile = modifiedGun.projectile;
        this.entitySize = new EntitySize(this.projectile.size, this.projectile.size, false);
        this.modifiedGravity = GunModifierHelper.getModifiedProjectileGravity(weapon, -0.05);
        this.life = GunModifierHelper.getModifiedProjectileLife(weapon, this.projectile.life);

        Vec3d dir = this.getDirection(shooter, weapon, item, modifiedGun);
        double speed = GunModifierHelper.getModifiedProjectileSpeed(weapon, this.projectile.speed);
        this.setMotion(dir.x * speed, dir.y * speed, dir.z * speed);
        this.updateHeading();

        /* Spawn the projectile half way between the previous and current position */
        double posX = shooter.lastTickPosX + (shooter.getPosX() - shooter.lastTickPosX) / 2.0;
        double posY = shooter.lastTickPosY + (shooter.getPosY() - shooter.lastTickPosY) / 2.0 + shooter.getEyeHeight();
        double posZ = shooter.lastTickPosZ + (shooter.getPosZ() - shooter.lastTickPosZ) / 2.0;
        this.setPosition(posX, posY, posZ);

        Item ammo = ForgeRegistries.ITEMS.getValue(this.projectile.item);
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

    private Vec3d getDirection(LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        float gunSpread = GunModifierHelper.getModifiedSpread(weapon, modifiedGun.general.spread);

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
        return this.item;
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

        if(!this.world.isRemote())
        {
            Vec3d startVec = this.getPositionVec();
            Vec3d endVec = startVec.add(this.getMotion());
            RayTraceResult result = rayTraceBlocks(this.world, new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this), IGNORE_LEAVES);
            if(result.getType() != RayTraceResult.Type.MISS)
            {
                endVec = result.getHitVec();
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

            if(result != null)
            {
                this.onHit(result);
            }
        }

        double nextPosX = this.getPosX() + this.getMotion().getX();
        double nextPosY = this.getPosY() + this.getMotion().getY();
        double nextPosZ = this.getPosZ() + this.getMotion().getZ();
        this.setPosition(nextPosX, nextPosY, nextPosZ);

        if(this.projectile.gravity)
        {
            this.setMotion(this.getMotion().add(0, this.modifiedGravity, 0));
        }

        if(this.ticksExisted >= this.life)
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
    protected EntityResult findEntityOnPath(Vec3d startVec, Vec3d endVec)
    {
        Vec3d hitVec = null;
        Entity hitEntity = null;
        List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0), PROJECTILE_TARGETS);
        double closestDistance = Double.MAX_VALUE;
        for(Entity entity : entities)
        {
            if(!entity.equals(this.shooter))
            {
                double expandHeight = entity instanceof PlayerEntity && !entity.isCrouching() ? 0.0625 : 0.0;
                AxisAlignedBB boundingBox = entity.getBoundingBox();
                if(Config.COMMON.gameplay.improvedHitboxes.get() && entity instanceof ServerPlayerEntity && this.shooter != null)
                {
                    int ping = (int) Math.floor((((ServerPlayerEntity) this.shooter).ping / 1000.0) * 20.0 + 0.5);
                    boundingBox = BoundingBoxTracker.getBoundingBox(entity, ping); //TODO this is actually the last position
                }
                boundingBox = boundingBox.expand(0, expandHeight, 0);
                Optional<Vec3d> hitPos = boundingBox.rayTrace(startVec, endVec);
                Optional<Vec3d> grownHitPos = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get()).rayTrace(startVec, endVec);
                if(!hitPos.isPresent() && grownHitPos.isPresent())
                {
                    RayTraceResult raytraceresult = rayTraceBlocks(this.world, new RayTraceContext(startVec, grownHitPos.get(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this), IGNORE_LEAVES);
                    if(raytraceresult.getType() == RayTraceResult.Type.BLOCK)
                    {
                        continue;
                    }
                    hitPos = grownHitPos;
                }

                if(!hitPos.isPresent())
                {
                    continue;
                }

                double distanceToHit = startVec.distanceTo(hitPos.get());
                if(distanceToHit < closestDistance)
                {
                    hitVec = hitPos.get();
                    hitEntity = entity;
                    closestDistance = distanceToHit;
                }
            }
        }
        return hitEntity != null ? new EntityResult(hitEntity, hitVec) : null;
    }

    private void onHit(RayTraceResult result)
    {
        if(result instanceof BlockRayTraceResult)
        {
            BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) result;
            if(blockRayTraceResult.getType() == RayTraceResult.Type.MISS)
            {
                return;
            }

            BlockPos pos = blockRayTraceResult.getPos();
            BlockState state = this.world.getBlockState(pos);
            Block block = state.getBlock();

            if(Config.COMMON.gameplay.enableGunGriefing.get() && (block instanceof BreakableBlock || block instanceof PaneBlock) && state.getMaterial() == Material.GLASS)
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

            Vec3d hitVec = blockRayTraceResult.getHitVec();
            double holeX = hitVec.getX() + 0.005 * blockRayTraceResult.getFace().getXOffset();
            double holeY = hitVec.getY() + 0.005 * blockRayTraceResult.getFace().getYOffset();
            double holeZ = hitVec.getZ() + 0.005 * blockRayTraceResult.getFace().getZOffset();
            Direction direction = blockRayTraceResult.getFace();
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> this.world.getChunkAt(blockRayTraceResult.getPos())), new MessageBulletHole(holeX, holeY, holeZ, direction, pos));

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
        if(Config.COMMON.gameplay.enableHeadShots.get() && entity instanceof PlayerEntity)
        {
            AxisAlignedBB boundingBox = entity.getBoundingBox().expand(0, !entity.isCrouching() ? 0.0625 : 0, 0);
            if(boundingBox.maxY - y <= 8.0 * 0.0625 && boundingBox.grow(0.001).contains(new Vec3d(x, y, z)))
            {
                headShot = true;
                damage *= Config.COMMON.gameplay.headShotDamageMultiplier.get();
            }
        }

        DamageSource source = new DamageSourceProjectile("bullet", this, shooter, weapon).setProjectile();
        entity.attackEntityFrom(source, damage);

        if(entity instanceof PlayerEntity && this.shooter instanceof ServerPlayerEntity)
        {
            SoundEvent event = headShot ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.ENTITY_PLAYER_HURT;
            ServerPlayerEntity shooterPlayer = (ServerPlayerEntity) this.shooter;
            shooterPlayer.connection.sendPacket(new SPlaySoundPacket(event.getRegistryName(), SoundCategory.PLAYERS, new Vec3d(this.shooter.getPosX(), this.shooter.getPosY(), this.shooter.getPosZ()), 0.75F, 1.0F));
        }

        /* Send blood particle to tracking clients. */
        //TODO maybe make clients send settings to server to prevent sending unnecessary packets
        PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageBlood(x, y, z));
    }

    protected void onHitBlock(BlockState state, BlockPos pos, double x, double y, double z)
    {
        ((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), x, y, z, (int) this.projectile.damage, 0.0, 0.0, 0.0, 0.05);
        this.world.playSound(null, x, y, z, state.getSoundType().getBreakSound(), SoundCategory.BLOCKS, 0.75F, 2.0F);
    }

    @Override
    protected void readAdditional(CompoundNBT compound)
    {
        this.projectile = new Gun.Projectile();
        this.projectile.deserializeNBT(compound.getCompound("Projectile"));
        this.general = new Gun.General();
        this.general.deserializeNBT(compound.getCompound("General"));
        this.modifiedGravity = compound.getDouble("ModifiedGravity");
        this.life = compound.getInt("MaxLife");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound)
    {
        compound.put("Projectile", this.projectile.serializeNBT());
        compound.put("General", this.general.serializeNBT());
        compound.putDouble("ModifiedGravity", this.modifiedGravity);
        compound.putInt("MaxLife", this.life);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeCompoundTag(this.projectile.serializeNBT());
        buffer.writeCompoundTag(this.general.serializeNBT());
        buffer.writeInt(this.shooterId);
        ItemStackUtil.writeItemStackToBufIgnoreTag(buffer, this.item);
        buffer.writeDouble(this.modifiedGravity);
        buffer.writeVarInt(this.life);
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
        this.modifiedGravity = buffer.readDouble();
        this.life = buffer.readVarInt();
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

    public int getShooterId()
    {
        return shooterId;
    }

    public float getDamage()
    {
        float initialDamage = (this.projectile.damage + this.additionalDamage);
        if(this.projectile.damageReduceOverLife)
        {
            float modifier = ((float) this.projectile.life - (float) (this.ticksExisted - 1)) / (float) this.projectile.life;
            initialDamage *= modifier;
        }
        float damage = initialDamage / this.general.projectileAmount;
        return GunModifierHelper.getModifiedDamage(this.weapon, this.modifiedGun, damage);
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

    /**
     * A custom implementation of {@link net.minecraft.world.IWorldReader#rayTraceBlocks(RayTraceContext)}
     * that allows you to pass a predicate to ignore certain blocks when checking for collisions.
     *
     * @param world     the world to perform the ray trace
     * @param context   the ray trace context
     * @param predicate the block state predicate
     * @return a result of the raytrace
     */
    private static BlockRayTraceResult rayTraceBlocks(World world, RayTraceContext context, Predicate<BlockState> predicate)
    {
        return func_217300_a(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            if(predicate.test(blockState)) return null;
            IFluidState fluidState = world.getFluidState(blockPos);
            Vec3d startVec = rayTraceContext.func_222253_b();
            Vec3d endVec = rayTraceContext.func_222250_a();
            VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
            BlockRayTraceResult blockResult = world.rayTraceBlocks(startVec, endVec, blockPos, blockShape, blockState);
            VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
            BlockRayTraceResult fluidResult = fluidShape.rayTrace(startVec, endVec, blockPos);
            double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.func_222253_b().squareDistanceTo(blockResult.getHitVec());
            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.func_222253_b().squareDistanceTo(fluidResult.getHitVec());
            return blockDistance <= fluidDistance ? blockResult : fluidResult;
        }, (rayTraceContext) -> {
            Vec3d vec3d = rayTraceContext.func_222253_b().subtract(rayTraceContext.func_222250_a());
            return BlockRayTraceResult.createMiss(rayTraceContext.func_222250_a(), Direction.getFacingFromVector(vec3d.x, vec3d.y, vec3d.z), new BlockPos(rayTraceContext.func_222250_a()));
        });
    }

    private static <T> T func_217300_a(RayTraceContext context, BiFunction<RayTraceContext, BlockPos, T> hitFunction, Function<RayTraceContext, T> p_217300_2_)
    {
        Vec3d startVec = context.func_222253_b();
        Vec3d endVec = context.func_222250_a();
        if(startVec.equals(endVec))
        {
            return p_217300_2_.apply(context);
        }
        else
        {
            double d0 = MathHelper.lerp(-1.0E-7D, endVec.x, startVec.x);
            double d1 = MathHelper.lerp(-1.0E-7D, endVec.y, startVec.y);
            double d2 = MathHelper.lerp(-1.0E-7D, endVec.z, startVec.z);
            double d3 = MathHelper.lerp(-1.0E-7D, startVec.x, endVec.x);
            double d4 = MathHelper.lerp(-1.0E-7D, startVec.y, endVec.y);
            double d5 = MathHelper.lerp(-1.0E-7D, startVec.z, endVec.z);
            int i = MathHelper.floor(d3);
            int j = MathHelper.floor(d4);
            int k = MathHelper.floor(d5);
            BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(i, j, k);
            T t = hitFunction.apply(context, blockpos$mutable);
            if(t != null)
            {
                return t;
            }
            else
            {
                double d6 = d0 - d3;
                double d7 = d1 - d4;
                double d8 = d2 - d5;
                int l = MathHelper.signum(d6);
                int i1 = MathHelper.signum(d7);
                int j1 = MathHelper.signum(d8);
                double d9 = l == 0 ? Double.MAX_VALUE : (double) l / d6;
                double d10 = i1 == 0 ? Double.MAX_VALUE : (double) i1 / d7;
                double d11 = j1 == 0 ? Double.MAX_VALUE : (double) j1 / d8;
                double d12 = d9 * (l > 0 ? 1.0D - MathHelper.frac(d3) : MathHelper.frac(d3));
                double d13 = d10 * (i1 > 0 ? 1.0D - MathHelper.frac(d4) : MathHelper.frac(d4));
                double d14 = d11 * (j1 > 0 ? 1.0D - MathHelper.frac(d5) : MathHelper.frac(d5));

                while(d12 <= 1.0D || d13 <= 1.0D || d14 <= 1.0D)
                {
                    if(d12 < d13)
                    {
                        if(d12 < d14)
                        {
                            i += l;
                            d12 += d9;
                        }
                        else
                        {
                            k += j1;
                            d14 += d11;
                        }
                    }
                    else if(d13 < d14)
                    {
                        j += i1;
                        d13 += d10;
                    }
                    else
                    {
                        k += j1;
                        d14 += d11;
                    }

                    T t1 = hitFunction.apply(context, blockpos$mutable.setPos(i, j, k));
                    if(t1 != null)
                    {
                        return t1;
                    }
                }

                return p_217300_2_.apply(context);
            }
        }
    }

    /**
     * Creates a projectile explosion for the specified entity.
     *
     * @param entity The entity to explode
     * @param radius The amount of radius the entity should deal
     */
    public static void createExplosion(Entity entity, float radius)
    {
        World world = entity.world;
        if (world.isRemote())
            return;

        Explosion explosion = new Explosion(world, entity, entity.getPosX(), entity.getPosY(), entity.getPosZ(), radius, false, Explosion.Mode.NONE);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        explosion.clearAffectedBlockPositions();

        for (ServerPlayerEntity serverplayerentity : ((ServerWorld) world).getPlayers())
        {
            if (serverplayerentity.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ()) < 4096.0D)
            {
                serverplayerentity.connection.sendPacket(new SExplosionPacket(entity.getPosX(), entity.getPosY(), entity.getPosZ(), radius / 5f, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(serverplayerentity)));
            }
        }
    }
}
