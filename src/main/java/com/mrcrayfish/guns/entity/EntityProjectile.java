package com.mrcrayfish.guns.entity;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mrcrayfish.guns.object.Gun.Projectile;
import com.mrcrayfish.guns.object.Gun.Projectile.Type;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityProjectile extends Entity implements IEntityAdditionalSpawnData
{
	private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(new Predicate[] {EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_.canBeCollidedWith();
        }
    }});
	
	private int shooterId;
	private EntityLivingBase shooter;
	private Projectile projectile;
	
	public EntityProjectile(World worldIn) 
	{
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}
	
	public EntityProjectile(World worldIn, EntityLivingBase shooter, Projectile projectile)
    {
        this(worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.projectile = projectile;
        this.setSize(projectile.spread, projectile.spread);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D, shooter.posZ);

        Vec3d dir = shooter.getLookVec();
        this.motionX = dir.xCoord * projectile.speed + shooter.motionX;
        this.motionY = dir.yCoord * projectile.speed;
        this.motionZ = dir.zCoord * projectile.speed + shooter.motionZ;
    }
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		//if(!worldObj.isRemote)
		{
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
	
			if (raytraceresult != null) 
			{
				vec3d = new Vec3d(raytraceresult.hitVec.xCoord, raytraceresult.hitVec.yCoord, raytraceresult.hitVec.zCoord);
			}
	
			Entity entity = this.findEntityOnPath(vec3d1, vec3d);
	
			if (entity != null) 
			{
				raytraceresult = new RayTraceResult(entity);
			}
	
			if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) 
			{
				EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;
	
				if (this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer)) 
				{
					raytraceresult = null;
				}
			}
	
			if (raytraceresult != null && !world.isRemote) 
			{
				this.onHit(raytraceresult);
			}
			
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			
			this.setPosition(this.posX, this.posY, this.posZ);
			
			if(this.projectile.gravity)
			{
				this.motionY -= 0.05;
			}
			
			if(this.ticksExisted >= this.projectile.life) this.setDead();
		}
	}
	
	@Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ), ARROW_TARGETS);
        double closestDistance = 0.0D;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity hitEntity = (Entity) list.get(i);

            if (hitEntity != this.shooter)
            {
                AxisAlignedBB axisalignedbb = hitEntity.getEntityBoundingBox().expandXyz(0.30000001192092896D);
                RayTraceResult result = axisalignedbb.calculateIntercept(start, end);

                if (result != null)
                {
                    double distanceToHit = start.squareDistanceTo(result.hitVec);

                    if (distanceToHit < closestDistance || closestDistance == 0.0D)
                    {
                        entity = hitEntity;
                        closestDistance = distanceToHit;
                    }
                }
            }
        }

        return entity;
    }
	
	protected void onHit(RayTraceResult raytraceResultIn)
    {
		Entity entity = raytraceResultIn.entityHit;

		if(entity != null)
		{
			if(entity.getEntityId() == this.shooterId) return;
			float damage = this.projectile.damage;
			if(this.projectile.damageReduceOverLife)
			{
				float percent = ((float) this.projectile.life - (float) this.ticksExisted) / (float) this.projectile.life;
				damage = this.projectile.damage * percent + this.projectile.damage / this.projectile.life;
			}
			
			switch(projectile.type)
			{
			case BULLET:
				entity.attackEntityFrom(DamageSource.ANVIL, damage);
				break;
			case GRENADE:
				world.createExplosion(shooter, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 5F, true);
				break;
			}
			
			this.setDead();
			return;
		}
		
		if(raytraceResultIn.getBlockPos() != null)
		{
			BlockPos pos = raytraceResultIn.getBlockPos();
			IBlockState state = world.getBlockState(raytraceResultIn.getBlockPos());
			Block block = state.getBlock();
			if((block instanceof BlockBreakable || block instanceof BlockPane) && state.getMaterial() == Material.GLASS)
			{
				world.destroyBlock(raytraceResultIn.getBlockPos(), false);
			}
			if(!block.isReplaceable(world, raytraceResultIn.getBlockPos()))
			{
				this.setDead();
			}
			
			if(projectile.type == Type.GRENADE)
			{
				world.createExplosion(shooter, raytraceResultIn.hitVec.xCoord, raytraceResultIn.hitVec.yCoord, raytraceResultIn.hitVec.zCoord, 5F, true);
			}
		}
    }
	
	@Override
	public boolean shouldRenderInPass(int pass) 
	{
		return this.projectile.visible;
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) 
	{
		this.projectile = new Projectile();
		this.projectile.deserializeNBT(compound.getCompoundTag("projectile"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) 
	{
		compound.setTag("projectile", this.projectile.serializeNBT());
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) 
	{
		ByteBufUtils.writeTag(buffer, this.projectile.serializeNBT());
		buffer.writeInt(this.shooterId);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData) 
	{
		if(this.projectile == null) 
		{
			this.projectile = new Projectile();
		}
		this.projectile.deserializeNBT(ByteBufUtils.readTag(additionalData));
		this.shooterId = additionalData.readInt();
	}
}
