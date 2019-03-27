package com.mrcrayfish.guns.entity;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mrcrayfish.guns.interfaces.IDamageable;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageSound;
import com.mrcrayfish.guns.object.Gun.Projectile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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

    private int shooterId;
    private EntityLivingBase shooter;
    private Projectile projectile;
    private ItemStack weapon = ItemStack.EMPTY;
    private ItemStack item = ItemStack.EMPTY;
    private float damageModifier = 1.0F;
    private float additionalDamage = 0.0F;

    public EntityProjectile(World worldIn)
    {
        super(worldIn);
    }

    public EntityProjectile(World worldIn, EntityLivingBase shooter, Projectile projectile)
    {
        this(worldIn);
        this.shooterId = shooter.getEntityId();
        this.shooter = shooter;
        this.projectile = projectile;

        Vec3d dir = getVectorFromRotation(shooter.rotationPitch, shooter.getRotationYawHead());
        this.motionX = dir.x * projectile.speed;
        this.motionY = dir.y * projectile.speed;
        this.motionZ = dir.z * projectile.speed;
        updateHeading();

        this.setSize(projectile.size, projectile.size);
        this.setPosition(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ);

        switch(projectile.type)
        {
            case GRENADE:
                this.item = ItemAmmo.getAmmo(ItemAmmo.Type.GRENADE, 1);
                break;
            case MISSILE:
                this.item = ItemAmmo.getAmmo(ItemAmmo.Type.MISSILE, 1);
                break;
        }
    }

    public void setWeapon(ItemStack weapon)
    {
        this.weapon = weapon.copy();
    }

    public ItemStack getWeapon()
    {
        return weapon;
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

        if(world.isRemote && projectile.type == ItemAmmo.Type.MISSILE)
        {
            for(int i = 5; i > 0; i--)
            {
                world.spawnParticle(EnumParticleTypes.CLOUD, true, this.posX - (this.motionX / i), this.posY - (this.motionY / i), this.posZ - (this.motionZ / i), 0, 0, 0);
            }
            if(world.rand.nextInt(2) == 0)
            {
                world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX, this.posY, this.posZ, 0, 0, 0);
                world.spawnParticle(EnumParticleTypes.FLAME, true, this.posX, this.posY, this.posZ, 0, 0, 0);
            }
        }

        Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
        vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if(raytraceresult != null)
        {
            vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }

        Entity entity = this.findEntityOnPath(vec3d1, vec3d);

        if(entity != null)
        {
            raytraceresult = new RayTraceResult(entity);
        }

        if(raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) raytraceresult.entityHit;

            if(this.shooter instanceof EntityPlayer && !((EntityPlayer) this.shooter).canAttackPlayer(entityplayer))
            {
                raytraceresult = null;
            }
        }

        if(raytraceresult != null && !world.isRemote)
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

        if(this.ticksExisted >= this.projectile.life)
        {
            if(!this.isDead)
            {
                if(projectile.type == ItemAmmo.Type.MISSILE)
                {
                    world.createExplosion(shooter, this.posX, this.posY, this.posZ, 3F, true);
                    if(world instanceof WorldServer)
                    {
                        WorldServer worldServer = (WorldServer) world;
                        worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, this.posX, this.posY, this.posZ, 0, 0.0, 0.0, 0.0, 0);
                    }
                }
            }
            this.setDead();
        }
    }

    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end)
    {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ), ARROW_TARGETS);
        double closestDistance = 0.0D;

        for(int i = 0; i < list.size(); ++i)
        {
            Entity hitEntity = list.get(i);
            if(!hitEntity.equals(this.shooter))
            {
                AxisAlignedBB axisalignedbb = hitEntity.getEntityBoundingBox().grow(0.3);
                RayTraceResult result = axisalignedbb.calculateIntercept(start, end);

                if(result != null)
                {
                    double distanceToHit = start.squareDistanceTo(result.hitVec);

                    if(distanceToHit < closestDistance || closestDistance == 0.0D)
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
            if(entity.getEntityId() == this.shooterId)
                return;

            switch(projectile.type)
            {
                case BASIC:
                case SHELL:
                case ADVANCED:
                    DamageSource source = new DamageSourceProjectile("bullet", this, shooter, weapon).setProjectile();
                    entity.attackEntityFrom(source, getDamage());
                    entity.hurtResistantTime = 0;

                    if(entity instanceof EntityPlayer && shooter instanceof EntityPlayerMP)
                    {
                        PacketHandler.INSTANCE.sendTo(new MessageSound(SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.PLAYERS, shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, 0.75F, 3.0F), (EntityPlayerMP) shooter);
                    }
                    break;
                case MISSILE:
                    world.createExplosion(shooter, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 3F, true);
                    break;
                case GRENADE:
                    world.createExplosion(shooter, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 1.5F, true);
                    break;
            }

            this.setDead();
            return;
        }

        if(raytraceResultIn.getBlockPos() != null)
        {
            BlockPos pos = raytraceResultIn.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            boolean canGunGrief = world.getGameRules().getBoolean("gunGriefing");
            if(canGunGrief && (block instanceof BlockBreakable || block instanceof BlockPane) && state.getMaterial() == Material.GLASS)
            {
                world.destroyBlock(raytraceResultIn.getBlockPos(), false);
            }

            if(!block.isReplaceable(world, raytraceResultIn.getBlockPos()))
            {
                this.setDead();
            }

            if(block instanceof IDamageable)
            {
                ((IDamageable) block).onBlockDamaged(world, state, pos, (int) Math.ceil(getDamage() / 2.0) + 1);
            }

            if(projectile.type == ItemAmmo.Type.GRENADE)
            {
                world.createExplosion(shooter, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 1.5F, true);
            }
            else if(projectile.type == ItemAmmo.Type.MISSILE)
            {
                world.createExplosion(shooter, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 3F, true);
                WorldServer worldServer = (WorldServer) world;
                worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, raytraceResultIn.hitVec.x, raytraceResultIn.hitVec.y, raytraceResultIn.hitVec.z, 0, 0.0, 0.0, 0.0, 0);
            }
        }
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
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
        ByteBufUtils.writeItemStack(buffer, item);
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
        this.rotationYaw = additionalData.readFloat();
        this.prevRotationYaw = this.rotationYaw;
        this.rotationPitch = additionalData.readFloat();
        this.prevRotationPitch = this.rotationPitch;
        this.item = ByteBufUtils.readItemStack(additionalData);
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
            float modifier = ((float) this.projectile.life - (float) this.ticksExisted) / (float) this.projectile.life;
            return damage * modifier;
        }
        return damage;
    }
}
