package com.mrcrayfish.guns.entity;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.BoundingBoxManager;
import com.mrcrayfish.guns.common.SpreadTracker;
import com.mrcrayfish.guns.hook.GunProjectileHitEvent;
import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.interfaces.IExplosionDamageable;
import com.mrcrayfish.guns.interfaces.IHeadshotBox;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageBlood;
import com.mrcrayfish.guns.network.message.MessageBulletHole;
import com.mrcrayfish.guns.object.EntityResult;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Gun.Projectile;
import com.mrcrayfish.guns.object.HitResult;
import com.mrcrayfish.guns.util.ExtendedEntityRayTraceResult;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.guns.util.ItemStackUtil;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
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
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProjectileEntity extends Entity implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> PROJECTILE_TARGETS = input -> input != null && input.isAlive() && !input.isSpectator() && input.canBeCollidedWith();
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
        this.general = modifiedGun.getGeneral();
        this.projectile = modifiedGun.getProjectile();
        this.entitySize = new EntitySize(this.projectile.getSize(), this.projectile.getSize(), false);
        this.modifiedGravity = GunModifierHelper.getModifiedProjectileGravity(weapon, -0.05);
        this.life = GunModifierHelper.getModifiedProjectileLife(weapon, this.projectile.getLife());

        /* Get speed and set motion */
        Vector3d dir = this.getDirection(shooter, weapon, item, modifiedGun);
        double speedModifier = GunEnchantmentHelper.getProjectileSpeedModifier(weapon, modifiedGun);
        double speed = GunModifierHelper.getModifiedProjectileSpeed(weapon, this.projectile.getSpeed() * speedModifier);
        this.setMotion(dir.x * speed, dir.y * speed, dir.z * speed);
        this.updateHeading();

        /* Spawn the projectile half way between the previous and current position */
        double posX = shooter.lastTickPosX + (shooter.getPosX() - shooter.lastTickPosX) / 2.0;
        double posY = shooter.lastTickPosY + (shooter.getPosY() - shooter.lastTickPosY) / 2.0 + shooter.getEyeHeight();
        double posZ = shooter.lastTickPosZ + (shooter.getPosZ() - shooter.lastTickPosZ) / 2.0;
        this.setPosition(posX, posY, posZ);

        Item ammo = ForgeRegistries.ITEMS.getValue(this.projectile.getItem());
        if(ammo != null)
        {
            this.item = new ItemStack(ammo);
        }
    }

    @Override
    protected void registerData() {}

    @Override
    public EntitySize getSize(Pose pose)
    {
        return this.entitySize;
    }

    private Vector3d getDirection(LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        float gunSpread = GunModifierHelper.getModifiedSpread(weapon, modifiedGun.getGeneral().getSpread());

        if(gunSpread == 0F)
        {
            return this.getVectorFromRotation(shooter.rotationPitch, shooter.rotationYaw);
        }

        if(shooter instanceof PlayerEntity)
        {
            if(!modifiedGun.getGeneral().isAlwaysSpread())
            {
                gunSpread *= SpreadTracker.get(shooter.getUniqueID()).getSpread(item);
            }

            if(SyncedPlayerData.instance().get((PlayerEntity) shooter, ModSyncedDataKeys.AIMING))
            {
                gunSpread *= 0.5F;
            }
        }

        return this.getVectorFromRotation(shooter.rotationPitch - (gunSpread / 2.0F) + rand.nextFloat() * gunSpread, shooter.getRotationYawHead() - (gunSpread / 2.0F) + rand.nextFloat() * gunSpread);
    }

    public void setWeapon(ItemStack weapon)
    {
        this.weapon = weapon.copy();
    }

    public ItemStack getWeapon()
    {
        return this.weapon;
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
        this.onProjectileTick();

        if(!this.world.isRemote())
        {
            Vector3d startVec = this.getPositionVec();
            Vector3d endVec = startVec.add(this.getMotion());
            RayTraceResult result = rayTraceBlocks(this.world, new RayTraceContext(startVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this), IGNORE_LEAVES);
            if(result.getType() != RayTraceResult.Type.MISS)
            {
                endVec = result.getHitVec();
            }

            List<EntityResult> hitEntities = null;
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.COLLATERAL.get(), this.weapon);
            if(level == 0)
            {
                EntityResult entityResult = this.findEntityOnPath(startVec, endVec);
                if(entityResult != null)
                {
                    hitEntities = Collections.singletonList(entityResult);
                }
            }
            else
            {
                hitEntities = this.findEntitiesOnPath(startVec, endVec);
            }

            if(hitEntities != null && hitEntities.size() > 0)
            {
                for(EntityResult entityResult : hitEntities)
                {
                    result = new ExtendedEntityRayTraceResult(entityResult);
                    if(((EntityRayTraceResult) result).getEntity() instanceof PlayerEntity)
                    {
                        PlayerEntity player = (PlayerEntity) ((EntityRayTraceResult) result).getEntity();

                        if(this.shooter instanceof PlayerEntity && !((PlayerEntity) this.shooter).canAttackPlayer(player))
                        {
                            result = null;
                        }
                    }
                    if(result != null)
                    {
                        this.onHit(result, startVec, endVec);
                    }
                }
            }
            else
            {
                this.onHit(result, startVec, endVec);
            }
        }

        double nextPosX = this.getPosX() + this.getMotion().getX();
        double nextPosY = this.getPosY() + this.getMotion().getY();
        double nextPosZ = this.getPosZ() + this.getMotion().getZ();
        this.setPosition(nextPosX, nextPosY, nextPosZ);

        if(this.projectile.isGravity())
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

    /**
     * A simple method to perform logic on each tick of the projectile. This method is appropriate
     * for spawning particles. Override {@link #tick()} to make changes to physics
     */
    protected void onProjectileTick()
    {
    }

    /**
     * Called when the projectile has run out of it's life. In other words, the projectile managed
     * to not hit any blocks and instead aged. The grenade uses this to explode in the air.
     */
    protected void onExpired()
    {
    }

    @Nullable
    protected EntityResult findEntityOnPath(Vector3d startVec, Vector3d endVec)
    {
        Vector3d hitVec = null;
        Entity hitEntity = null;
        boolean headshot = false;
        List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0), PROJECTILE_TARGETS);
        double closestDistance = Double.MAX_VALUE;
        for(Entity entity : entities)
        {
            if(!entity.equals(this.shooter))
            {
                HitResult result = this.getHitResult(entity, startVec, endVec);
                Optional<Vector3d> hitPos = result.getHitPos();
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
                    headshot = result.isHeadshot();
                }
            }
        }
        return hitEntity != null ? new EntityResult(hitEntity, hitVec, headshot) : null;
    }

    @Nullable
    protected List<EntityResult> findEntitiesOnPath(Vector3d startVec, Vector3d endVec)
    {
        List<EntityResult> hitEntities = new ArrayList<>();
        List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0), PROJECTILE_TARGETS);
        for(Entity entity : entities)
        {
            if(!entity.equals(this.shooter))
            {
                HitResult result = this.getHitResult(entity, startVec, endVec);
                Optional<Vector3d> hitPos = result.getHitPos();
                if(!hitPos.isPresent())
                {
                    continue;
                }
                hitEntities.add(new EntityResult(entity, hitPos.get(), result.isHeadshot()));
            }
        }
        return hitEntities;
    }

    @SuppressWarnings("unchecked")
    private HitResult getHitResult(Entity entity, Vector3d startVec, Vector3d endVec)
    {
        double expandHeight = entity instanceof PlayerEntity && !entity.isCrouching() ? 0.0625 : 0.0;
        AxisAlignedBB boundingBox = entity.getBoundingBox();
        if(Config.COMMON.gameplay.improvedHitboxes.get() && entity instanceof ServerPlayerEntity && this.shooter != null)
        {
            int ping = (int) Math.floor((((ServerPlayerEntity) this.shooter).ping / 1000.0) * 20.0 + 0.5);
            boundingBox = BoundingBoxManager.getBoundingBox((PlayerEntity) entity, ping); //TODO this is actually the last position
        }
        boundingBox = boundingBox.expand(0, expandHeight, 0);

        Vector3d hitPos = boundingBox.rayTrace(startVec, endVec).orElse(null);
        Vector3d grownHitPos = boundingBox.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get()).rayTrace(startVec, endVec).orElse(null);
        if(hitPos == null && grownHitPos != null)
        {
            RayTraceResult raytraceresult = rayTraceBlocks(this.world, new RayTraceContext(startVec, grownHitPos, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this), IGNORE_LEAVES);
            if(raytraceresult.getType() == RayTraceResult.Type.BLOCK)
            {
                return new HitResult(null, false);
            }
            hitPos = grownHitPos;
        }

        /* Check for headshot */
        boolean headshot = false;
        if(Config.COMMON.gameplay.enableHeadShots.get() && entity instanceof LivingEntity)
        {
            IHeadshotBox<LivingEntity> headshotBox = (IHeadshotBox<LivingEntity>) BoundingBoxManager.getHeadshotBoxes(entity.getType());
            if(headshotBox != null)
            {
                AxisAlignedBB box = headshotBox.getHeadshotBox((LivingEntity) entity);
                if(box != null)
                {
                    box = box.offset(boundingBox.getCenter().x, boundingBox.minY, boundingBox.getCenter().z);
                    Optional<Vector3d> headshotHitPos = box.rayTrace(startVec, endVec);
                    if(!headshotHitPos.isPresent())
                    {
                        box = box.grow(Config.COMMON.gameplay.growBoundingBoxAmount.get(), 0, Config.COMMON.gameplay.growBoundingBoxAmount.get());
                        headshotHitPos = box.rayTrace(startVec, endVec);
                    }
                    if(headshotHitPos.isPresent() && (hitPos == null || headshotHitPos.get().distanceTo(hitPos) < 0.5))
                    {
                        hitPos = headshotHitPos.get();
                        headshot = true;
                    }
                }
            }
        }
        return new HitResult(hitPos, headshot);
    }

    private void onHit(RayTraceResult result, Vector3d startVec, Vector3d endVec)
    {
        MinecraftForge.EVENT_BUS.post(new GunProjectileHitEvent(result, this));

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
                ((IDamageable) block).onBlockDamaged(world, state, pos, this, this.getDamage(), (int) Math.ceil(this.getDamage() / 2.0) + 1);
            }

            Vector3d hitVec = blockRayTraceResult.getHitVec();
            double holeX = hitVec.getX() + 0.005 * blockRayTraceResult.getFace().getXOffset();
            double holeY = hitVec.getY() + 0.005 * blockRayTraceResult.getFace().getYOffset();
            double holeZ = hitVec.getZ() + 0.005 * blockRayTraceResult.getFace().getZOffset();
            Direction direction = blockRayTraceResult.getFace();
            PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_CHUNK.with(() -> this.world.getChunkAt(blockRayTraceResult.getPos())), new MessageBulletHole(holeX, holeY, holeZ, direction, pos));

            this.onHitBlock(state, pos, result.getHitVec().x, result.getHitVec().y, result.getHitVec().z);

            int fireStarterLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FIRE_STARTER.get(), this.weapon);
            if(fireStarterLevel > 0)
            {
                BlockPos offsetPos = pos.offset(blockRayTraceResult.getFace());
                if(AbstractFireBlock.canLightBlock(this.world, offsetPos, blockRayTraceResult.getFace()))
                {
                    BlockState fireState = AbstractFireBlock.getFireForPlacement(this.world, offsetPos);
                    this.world.setBlockState(offsetPos, fireState, 11);
                }
            }
            return;
        }

        if(result instanceof ExtendedEntityRayTraceResult)
        {
            ExtendedEntityRayTraceResult entityRayTraceResult = (ExtendedEntityRayTraceResult) result;
            Entity entity = entityRayTraceResult.getEntity();
            if(entity.getEntityId() == this.shooterId)
            {
                return;
            }

            int fireStarterLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FIRE_STARTER.get(), this.weapon);
            if(fireStarterLevel > 0)
            {
                entity.setFire(2);
            }

            this.onHitEntity(entity, result.getHitVec(), startVec, endVec, entityRayTraceResult.isHeadshot());

            int collateralLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.COLLATERAL.get(), weapon);
            if(collateralLevel == 0)
            {
                this.remove();
            }

            entity.hurtResistantTime = 0;
        }
    }

    protected void onHitEntity(Entity entity, Vector3d hitVec, Vector3d startVec, Vector3d endVec, boolean headshot)
    {
        float damage = this.getDamage();
        float newDamage = this.getCriticalDamage(this.weapon, this.rand, damage);
        boolean critical = damage != newDamage;
        damage = newDamage;

        if(headshot)
        {
            damage *= Config.COMMON.gameplay.headShotDamageMultiplier.get();
        }

        DamageSource source = new DamageSourceProjectile("bullet", this, shooter, weapon).setProjectile();
        entity.attackEntityFrom(source, damage);

        if(entity instanceof PlayerEntity && this.shooter instanceof ServerPlayerEntity)
        {
            SoundEvent event = headshot ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.ENTITY_PLAYER_HURT;
            if(critical)
            {
                event = SoundEvents.ENTITY_ITEM_BREAK; //TODO change
            }
            ServerPlayerEntity shooterPlayer = (ServerPlayerEntity) this.shooter;
            shooterPlayer.connection.sendPacket(new SPlaySoundPacket(event.getRegistryName(), SoundCategory.PLAYERS, new Vector3d(this.shooter.getPosX(), this.shooter.getPosY(), this.shooter.getPosZ()), 0.75F, 1.0F));
        }
        else if((critical || headshot) && this.shooter instanceof ServerPlayerEntity)
        {
            SoundEvent event = headshot ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.ENTITY_ITEM_BREAK;
            ServerPlayerEntity shooterPlayer = (ServerPlayerEntity) this.shooter;
            shooterPlayer.connection.sendPacket(new SPlaySoundPacket(event.getRegistryName(), SoundCategory.PLAYERS, new Vector3d(this.shooter.getPosX(), this.shooter.getPosY(), this.shooter.getPosZ()), 0.75F, 1.0F));
        }

        /* Send blood particle to tracking clients. */
        //TODO maybe make clients send settings to server to prevent sending unnecessary packets
        PacketHandler.getPlayChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageBlood(hitVec.x, hitVec.y, hitVec.z));
    }

    protected void onHitBlock(BlockState state, BlockPos pos, double x, double y, double z)
    {
        ((ServerWorld) this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, state), x, y, z, (int) this.projectile.getDamage(), 0.0, 0.0, 0.0, 0.05);
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
        this.entitySize = new EntitySize(this.projectile.getSize(), this.projectile.getSize(), false);
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
        return this.projectile;
    }

    private Vector3d getVectorFromRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vector3d((double) (f1 * f2), (double) f3, (double) (f * f2));
    }

    /**
     * Gets the entity who spawned the projectile
     */
    public LivingEntity getShooter()
    {
        return this.shooter;
    }

    /**
     * Gets the id of the entity who spawned the projectile
     */
    public int getShooterId()
    {
        return this.shooterId;
    }

    public float getDamage()
    {
        float initialDamage = (this.projectile.getDamage() + this.additionalDamage);
        if(this.projectile.isDamageReduceOverLife())
        {
            float modifier = ((float) this.projectile.getLife() - (float) (this.ticksExisted - 1)) / (float) this.projectile.getLife();
            initialDamage *= modifier;
        }
        float damage = initialDamage / this.general.getProjectileAmount();
        damage = GunModifierHelper.getModifiedDamage(this.weapon, this.modifiedGun, damage);
        return Math.max(0F, damage);
    }

    private float getCriticalDamage(ItemStack weapon, Random rand, float damage)
    {
        float chance = GunModifierHelper.getCriticalChance(weapon);
        if(rand.nextFloat() < chance)
        {
            return (float) (damage * Config.COMMON.gameplay.criticalDamageMultiplier.get());
        }
        return damage;
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
     * @param ignorePredicate the block state predicate
     * @return a result of the raytrace
     */
    private static BlockRayTraceResult rayTraceBlocks(World world, RayTraceContext context, Predicate<BlockState> ignorePredicate)
    {
        return performRayTrace(context, (rayTraceContext, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            if(ignorePredicate.test(blockState)) return null;
            FluidState fluidState = world.getFluidState(blockPos);
            Vector3d startVec = rayTraceContext.getStartVec();
            Vector3d endVec = rayTraceContext.getEndVec();
            VoxelShape blockShape = rayTraceContext.getBlockShape(blockState, world, blockPos);
            BlockRayTraceResult blockResult = world.rayTraceBlocks(startVec, endVec, blockPos, blockShape, blockState);
            VoxelShape fluidShape = rayTraceContext.getFluidShape(fluidState, world, blockPos);
            BlockRayTraceResult fluidResult = fluidShape.rayTrace(startVec, endVec, blockPos);
            double blockDistance = blockResult == null ? Double.MAX_VALUE : rayTraceContext.getStartVec().squareDistanceTo(blockResult.getHitVec());
            double fluidDistance = fluidResult == null ? Double.MAX_VALUE : rayTraceContext.getStartVec().squareDistanceTo(fluidResult.getHitVec());
            return blockDistance <= fluidDistance ? blockResult : fluidResult;
        }, (rayTraceContext) -> {
            Vector3d Vector3d = rayTraceContext.getStartVec().subtract(rayTraceContext.getEndVec());
            return BlockRayTraceResult.createMiss(rayTraceContext.getEndVec(), Direction.getFacingFromVector(Vector3d.x, Vector3d.y, Vector3d.z), new BlockPos(rayTraceContext.getEndVec()));
        });
    }

    private static <T> T performRayTrace(RayTraceContext context, BiFunction<RayTraceContext, BlockPos, T> hitFunction, Function<RayTraceContext, T> p_217300_2_)
    {
        Vector3d startVec = context.getStartVec();
        Vector3d endVec = context.getEndVec();
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
     * @param forceNone If true, forces the explosion mode to be NONE instead of config value
     */
    public static void createExplosion(Entity entity, float radius, boolean forceNone)
    {
        World world = entity.world;
        if(world.isRemote())
            return;

        Explosion.Mode mode = Config.COMMON.gameplay.enableGunGriefing.get() && !forceNone ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        Explosion explosion = new Explosion(world, entity, null, null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), radius, false, Explosion.Mode.NONE);
        explosion.doExplosionA();
        explosion.getAffectedBlockPositions().forEach(pos ->
        {
            if(world.getBlockState(pos).getBlock() instanceof IExplosionDamageable)
            {
                ((IExplosionDamageable) world.getBlockState(pos).getBlock()).onProjectileExploded(world, world.getBlockState(pos), pos, entity);
            }
        });
        explosion.doExplosionB(true);

        if(mode == Explosion.Mode.NONE)
        {
            explosion.clearAffectedBlockPositions();
        }

        for(ServerPlayerEntity serverplayerentity : ((ServerWorld) world).getPlayers())
        {
            if(serverplayerentity.getDistanceSq(entity.getPosX(), entity.getPosY(), entity.getPosZ()) < 4096.0D)
            {
                serverplayerentity.connection.sendPacket(new SExplosionPacket(entity.getPosX(), entity.getPosY(), entity.getPosZ(), radius / 5f, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(serverplayerentity)));
            }
        }
    }
}
