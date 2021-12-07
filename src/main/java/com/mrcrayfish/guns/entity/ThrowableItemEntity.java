package com.mrcrayfish.guns.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

/**
 * Author: MrCrayfish
 */
public abstract class ThrowableItemEntity extends ThrowableProjectile implements IEntityAdditionalSpawnData
{
    private ItemStack item = ItemStack.EMPTY;
    private boolean shouldBounce;
    private float gravityVelocity = 0.03F;

    /* The max life of the entity. If -1, will stay alive forever and will need to be explicitly removed. */
    private int maxLife = 20 * 10;

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, Level worldIn)
    {
        super(entityType, worldIn);
    }

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, Level world, LivingEntity player)
    {
        super(entityType, player, world);
    }

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, Level world, double x, double y, double z)
    {
        super(entityType, x, y, z, world);
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    protected void setShouldBounce(boolean shouldBounce)
    {
        this.shouldBounce = shouldBounce;
    }

    protected void setGravityVelocity(float gravity)
    {
        this.gravityVelocity = gravity;
    }

    @Override
    protected float getGravity()
    {
        return this.gravityVelocity;
    }

    public void setMaxLife(int maxLife)
    {
        this.maxLife = maxLife;
    }

    @Override
    public void tick()
    {
        super.tick();
        if(this.shouldBounce && this.tickCount >= this.maxLife)
        {
            this.remove(RemovalReason.KILLED);
            this.onDeath();
        }
    }

    public void onDeath() {}

    @Override
    protected void onHit(HitResult result)
    {
        switch(result.getType())
        {
            case BLOCK:
                BlockHitResult blockResult = (BlockHitResult) result;
                if(this.shouldBounce)
                {
                    BlockPos resultPos = blockResult.getBlockPos();
                    BlockState state = this.level.getBlockState(resultPos);
                    SoundEvent event = state.getBlock().getSoundType(state, this.level, resultPos, this).getStepSound();
                    double speed = this.getDeltaMovement().length();
                    if(speed > 0.1)
                    {
                        this.level.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundSource.AMBIENT, 1.0F, 1.0F);
                    }
                    this.bounce(blockResult.getDirection());
                }
                else
                {
                    this.remove(RemovalReason.KILLED);
                    this.onDeath();
                }
                break;
            case ENTITY:
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity entity = entityResult.getEntity();
                if(this.shouldBounce)
                {
                    double speed = this.getDeltaMovement().length();
                    if(speed > 0.1)
                    {
                        entity.hurt(DamageSource.thrown(this, this.getOwner()), 1.0F);
                    }
                    this.bounce(Direction.getNearest(this.getDeltaMovement().x(), this.getDeltaMovement().y(), this.getDeltaMovement().z()).getOpposite());
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.25, 1.0, 0.25));
                }
                else
                {
                    this.remove(RemovalReason.KILLED);
                    this.onDeath();
                }
                break;
            default:
                break;
        }
    }

    private void bounce(Direction direction)
    {
        switch(direction.getAxis())
        {
            case X:
                this.setDeltaMovement(this.getDeltaMovement().multiply(-0.5, 0.75, 0.75));
                break;
            case Y:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, -0.25, 0.75));
                if(this.getDeltaMovement().y() < this.getGravity())
                {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
                }
                break;
            case Z:
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.75, 0.75, -0.5));
                break;
        }
    }

    @Override
    public boolean isNoGravity()
    {
        return false;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer)
    {
        buffer.writeBoolean(this.shouldBounce);
        buffer.writeFloat(this.gravityVelocity);
        buffer.writeItem(this.item);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer)
    {
        this.shouldBounce = buffer.readBoolean();
        this.gravityVelocity = buffer.readFloat();
        this.item = buffer.readItem();
    }

    @Override
    public Packet<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
