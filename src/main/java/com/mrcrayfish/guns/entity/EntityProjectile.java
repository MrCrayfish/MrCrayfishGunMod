package com.mrcrayfish.guns.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.SpreadHandler;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.item.AmmoItem;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageSound;
import com.mrcrayfish.guns.object.EntityResult;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.object.Gun.Projectile;
import com.mrcrayfish.guns.util.ItemStackUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.List;

public class EntityProjectile extends Entity implements IEntityAdditionalSpawnData
{
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith);

    protected int shooterId;
    protected EntityLivingBase shooter;
    protected Gun.General general;
    protected Gun.Projectile projectile;
    private ItemStack weapon = ItemStack.EMPTY;
    private ItemStack item = ItemStack.EMPTY;
    protected float damageModifier = 1.0F;
    protected float additionalDamage = 0.0F;

    public EntityProjectile(EntityType<? extends Entity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public EntityProjectile(World worldIn, EntityLivingBase shooter, GunItem item, Gun modifiedGun)
    {
        this(worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.general = modifiedGun.general;
        this.projectile = modifiedGun.projectile;

        Vec3d dir = this.getDirection(shooter, item, modifiedGun);
        this.motionX = dir.x * this.projectile.speed;
        this.motionY = dir.y * this.projectile.speed;
        this.motionZ = dir.z * this.projectile.speed;
        this.updateHeading();

        this.setSize(this.projectile.size, this.projectile.size);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);

        AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(this.projectile.item);
        if(ammo != null)
        {
            this.item = new ItemStack(ammo);
        }
    }

    private Vec3d getDirection(EntityLivingBase shooter, GunItem item, Gun modifiedGun)
    {
        float gunSpread = modifiedGun.general.spread;

        if(gunSpread == 0F)
        {
            return this.getVectorFromRotation(shooter.rotationPitch, shooter.getRotationYawHead());
        }

        if(!modifiedGun.general.alwaysSpread)
        {
            gunSpread *= SpreadHandler.getSpreadTracker(shooter.getUniqueID()).getSpread(item);
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
        return item;
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
    public void onUpdate()
    {
        super.onUpdate();
        updateHeading();

        this.onTick();

        Vec3d startVec = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d endVec = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult result = this.world.rayTraceBlocks(startVec, endVec, false, true, false);
        startVec = new Vec3d(this.posX, this.posY, this.posZ);
        endVec = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if(result != null)
        {
            endVec = new Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z);
        }

        EntityResult entityResult = this.findEntityOnPath(startVec, endVec);
        if(entityResult != null)
        {
            result = new RayTraceResult(entityResult.entity, entityResult.hitVec);
        }

        if(result != null && result.entityHit instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) result.entityHit;

            if(this.shooter instanceof PlayerEntity && !((PlayerEntity) this.shooter).canAttackPlayer(player))
            {
                result = null;
            }
        }

        if(result != null && !world.isRemote)
        {
            this.onHit(result);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        this.setPosition(this.posX, this.posY, this.posZ);

        if(this.projectile.gravity)
        {
            this.motionY -= 0.05;
        }

        if(this.ticksExisted >= this.projectile.life)
        {
            if(!this.isDead)
            {
                this.onExpired();
            }
            this.setDead();
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
        List<Entity> entities = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0), ARROW_TARGETS);
        double closestDistance = 0.0D;
        for(Entity entity : entities)
        {
            if(!entity.equals(this.shooter))
            {
                AxisAlignedBB boundingBox = entity.getEntityBoundingBox().expand(0, 0.0625, 0); //Player bounding box is one pixel off from the top of the head
                RayTraceResult result = boundingBox.calculateIntercept(start, end);
                if(result == null)
                {
                    boundingBox = entity.getEntityBoundingBox().grow(Config.SERVER.growBoundingBoxAmount, 0.0625, Config.SERVER.growBoundingBoxAmount);
                    result = boundingBox.calculateIntercept(start, end);
                    if(result == null)
                    {
                        continue;
                    }

                    Vec3d entityVec = new Vec3d(entity.posX, result.hitVec.y, entity.posZ);
                    RayTraceResult blockResult = this.world.rayTraceBlocks(result.hitVec, entityVec, false, true, false);
                    if(blockResult != null)
                    {
                        continue;
                    }
                }

                double distanceToHit = start.squareDistanceTo(result.hitVec);
                if(distanceToHit < closestDistance || closestDistance == 0.0D)
                {
                    hitVec = result.hitVec;
                    foundEntity = entity;
                    closestDistance = distanceToHit;
                }
            }
        }
        return foundEntity != null ? new EntityResult(foundEntity, hitVec) : null;
    }

    private void onHit(RayTraceResult result)
    {
        if(result.getBlockPos() != null)
        {
            BlockPos pos = result.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            boolean canGunGrief = world.getGameRules().getBoolean("gunGriefing");
            if(canGunGrief && (block instanceof BlockBreakable || block instanceof BlockPane) && state.getMaterial() == Material.GLASS)
            {
                world.destroyBlock(result.getBlockPos(), false);
            }

            if(!block.isReplaceable(world, result.getBlockPos()))
            {
                this.setDead();
            }

            if(block instanceof IDamageable)
            {
                ((IDamageable) block).onBlockDamaged(world, state, pos, (int) Math.ceil(getDamage() / 2.0) + 1);
            }

            this.onHitBlock(state, pos, result.hitVec.x, result.hitVec.y, result.hitVec.z);

            return;
        }

        Entity entity = result.entityHit;
        if(entity != null)
        {
            if(entity.getEntityId() == this.shooterId)
            {
                return;
            }

            this.onHitEntity(entity, result.hitVec.x, result.hitVec.y, result.hitVec.z);
            this.setDead();

            entity.hurtResistantTime = 0;
        }
    }

    protected void onHitEntity(Entity entity, double x, double y, double z)
    {
        boolean headShot = false;
        float damage = this.getDamage();
        if(Config.SERVER.enableHeadShots && entity instanceof PlayerEntity)
        {
            AxisAlignedBB boundingBox = entity.getEntityBoundingBox().expand(0, 0.0625, 0);
            if(boundingBox.maxY - y <= 8.0 * 0.0625)
            {
                headShot = true;
                damage *= Config.SERVER.headShotDamageMultiplier;
            }
        }

        DamageSource source = new DamageSourceProjectile("bullet", this, shooter, weapon).setProjectile();
        entity.attackEntityFrom(source, damage);

        if(entity instanceof PlayerEntity && shooter instanceof PlayerEntityMP)
        {
            SoundEvent event = headShot ? SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP : SoundEvents.ENTITY_PLAYER_HURT;
            PacketHandler.INSTANCE.sendTo(new MessageSound(event, SoundCategory.PLAYERS, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, 0.75F, 3.0F), (PlayerEntityMP) shooter);
        }
    }

    protected void onHitBlock(IBlockState state, BlockPos pos, double x, double y, double z)
    {
        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, x, y, z, (int) this.projectile.damage, 0.0, 0.0, 0.0, 0.05, Block.getStateId(state));
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return this.projectile.visible;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.projectile = new Gun.Projectile();
        this.projectile.deserializeNBT(compound.getCompoundTag("projectile"));
        this.general = new Gun.General();
        this.general.deserializeNBT(compound.getCompoundTag("general"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setTag("projectile", this.projectile.serializeNBT());
        compound.setTag("general", this.general.serializeNBT());
    }

    @Override
    public void writeSpawnData(ByteBuf additionalData)
    {
        ByteBufUtils.writeTag(additionalData, this.projectile.serializeNBT());
        ByteBufUtils.writeTag(additionalData, this.general.serializeNBT());
        additionalData.writeInt(this.shooterId);
        ItemStackUtil.writeItemStackToBufIgnoreTag(additionalData, this.item);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        this.projectile = new Gun.Projectile();
        this.projectile.deserializeNBT(ByteBufUtils.readTag(additionalData));
        this.general = new Gun.General();
        this.general.deserializeNBT(ByteBufUtils.readTag(additionalData));
        this.shooterId = additionalData.readInt();
        this.item = ItemStackUtil.readItemStackFromBufIgnoreTag(additionalData);
        this.setSize(this.projectile.size, this.projectile.size);
    }

    public void updateHeading()
    {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
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

    public EntityLivingBase getShooter()
    {
        return shooter;
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
}
