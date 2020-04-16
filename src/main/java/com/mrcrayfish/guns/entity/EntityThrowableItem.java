package com.mrcrayfish.guns.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Author: MrCrayfish
 */
public abstract class EntityThrowableItem extends EntityThrowable implements IEntityAdditionalSpawnData
{
    private ItemStack item = ItemStack.EMPTY;
    private boolean shouldBounce;
    private float gravityVelocity = 0.03F;

    /* The max life of the entity. If -1, will stay alive forever and will need to be explicitly removed. */
    private int maxLife = 20 * 10;

    public EntityThrowableItem(World worldIn)
    {
        super(worldIn);
    }

    public EntityThrowableItem(World world, PlayerEntity player)
    {
        super(world, player);
    }

    public EntityThrowableItem(World world, double x, double y, double z)
    {
        super(world, x, y, z);
    }

    public void setItem(ItemStack item)
    {
        this.item = item;
    }

    public ItemStack getItem()
    {
        return item;
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
    protected float getGravityVelocity()
    {
        return gravityVelocity;
    }

    public void setMaxLife(int maxLife)
    {
        this.maxLife = maxLife;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if(shouldBounce && ticksExisted >= maxLife)
        {
            this.setDead();
            this.onDeath();
        }
    }

    public void onDeath() {}

    @Override
    protected void onImpact(RayTraceResult result)
    {
        switch(result.typeOfHit)
        {
            case BLOCK:
                if(shouldBounce)
                {
                    IBlockState state = world.getBlockState(result.getBlockPos());
                    SoundEvent event = state.getBlock().getSoundType().getStepSound();
                    double speed = Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionY, 2) + Math.pow(this.motionZ, 2));
                    if(speed > 0.1)
                    {
                        world.playSound(null, result.hitVec.x, result.hitVec.y, result.hitVec.z, event, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }
                    EnumFacing facing = result.sideHit;
                    switch(facing.getAxis())
                    {
                        case X:
                            this.motionX = -this.motionX * 0.5;
                            this.motionY *= 0.75;
                            this.motionZ *= 0.75;
                            break;
                        case Y:
                            this.motionX *= 0.75;
                            this.motionY = -this.motionY * 0.25;
                            if(this.motionY < this.getGravityVelocity())
                            {
                                this.motionY = 0F;
                            }
                            this.motionZ *= 0.75;
                            break;
                        case Z:
                            this.motionX *= 0.75;
                            this.motionY *= 0.75;
                            this.motionZ = -this.motionZ * 0.5;
                            break;
                    }
                }
                else
                {
                    this.setDead();
                    this.onDeath();
                }
                break;
            case ENTITY:
                Entity entity = result.entityHit;
                if(entity != null)
                {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean hasNoGravity()
    {
        return false;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(shouldBounce);
        buffer.writeFloat(gravityVelocity);
        ByteBufUtils.writeItemStack(buffer, item);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        shouldBounce = additionalData.readBoolean();
        gravityVelocity = additionalData.readFloat();
        item = ByteBufUtils.readItemStack(additionalData);
    }
}
