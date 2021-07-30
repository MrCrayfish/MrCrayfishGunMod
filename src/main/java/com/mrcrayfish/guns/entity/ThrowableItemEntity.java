package com.mrcrayfish.guns.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * Author: MrCrayfish
 */
public abstract class ThrowableItemEntity extends ThrowableEntity implements IEntityAdditionalSpawnData
{
    private ItemStack item = ItemStack.EMPTY;
    private boolean shouldBounce;
    private float gravityVelocity = 0.03F;

    /* The max life of the entity. If -1, will stay alive forever and will need to be explicitly removed. */
    private int maxLife = 20 * 10;

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, LivingEntity player)
    {
        super(entityType, player, world);
    }

    public ThrowableItemEntity(EntityType<? extends ThrowableItemEntity> entityType, World world, double x, double y, double z)
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
            this.remove();
            this.onDeath();
        }
    }

    public void onDeath() {}

    @Override
    protected void onHit(RayTraceResult result)
    {
        switch(result.getType())
        {
            case BLOCK:
                BlockRayTraceResult blockResult = (BlockRayTraceResult) result;
                if(this.shouldBounce)
                {
                    BlockPos resultPos = blockResult.getBlockPos();
                    BlockState state = this.level.getBlockState(resultPos);
                    SoundEvent event = state.getBlock().getSoundType(state, this.level, resultPos, this).getStepSound();
                    double speed = this.getDeltaMovement().length();
                    if(speed > 0.1)
                    {
                        this.level.playSound(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, event, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }
                    Direction direction = blockResult.getDirection();
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
                else
                {
                    this.remove();
                    this.onDeath();
                }
                break;
            case ENTITY:
                EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
                Entity entity = entityResult.getEntity();
                if(entity != null)
                {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isNoGravity()
    {
        return false;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.shouldBounce);
        buffer.writeFloat(this.gravityVelocity);
        buffer.writeItem(this.item);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.shouldBounce = buffer.readBoolean();
        this.gravityVelocity = buffer.readFloat();
        this.item = buffer.readItem();
    }

    @Override
    public IPacket<?> getAddEntityPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
