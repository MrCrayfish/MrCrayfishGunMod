package com.mrcrayfish.guns.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * Author: MrCrayfish
 */
public abstract class EntityThrowableItem extends ThrowableEntity implements IEntityAdditionalSpawnData
{
    private ItemStack item = ItemStack.EMPTY;
    private boolean shouldBounce;
    private float gravityVelocity = 0.03F;

    /* The max life of the entity. If -1, will stay alive forever and will need to be explicitly removed. */
    private int maxLife = 20 * 10;

    public EntityThrowableItem(EntityType<? extends EntityThrowableItem> entityType, World worldIn)
    {
        super(entityType, worldIn);
    }

    public EntityThrowableItem(EntityType<? extends EntityThrowableItem> entityType, World world, PlayerEntity player)
    {
        super(entityType, player, world);
    }

    public EntityThrowableItem(EntityType<? extends EntityThrowableItem> entityType, World world, double x, double y, double z)
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
    protected float getGravityVelocity()
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
        if(this.shouldBounce && this.ticksExisted >= this.maxLife)
        {
            this.remove();
            this.onDeath();
        }
    }

    public void onDeath() {}

    @Override
    protected void onImpact(RayTraceResult result)
    {
        //TODO add this back LOL i just want to compile already
        /*switch(result.typeOfHit)
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
        }*/
    }

    @Override
    public boolean hasNoGravity()
    {
        return false;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.shouldBounce);
        buffer.writeFloat(this.gravityVelocity);
        buffer.writeItemStack(this.item);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer)
    {
        this.shouldBounce = buffer.readBoolean();
        this.gravityVelocity = buffer.readFloat();
        this.item = buffer.readItemStack();
    }
}
