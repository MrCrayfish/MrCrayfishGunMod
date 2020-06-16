package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.network.message.MessageBullet;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class Bullet
{
    private ProjectileEntity projectile;
    private int entityId;
    private double posX;
    private double posY;
    private double posZ;
    private double motionX;
    private double motionY;
    private double motionZ;
    private float rotationYaw;
    private float rotationPitch;
    private boolean finished = false;
    private int trailColor;
    private double trailLengthMultiplier;

    public Bullet(@Nullable ProjectileEntity projectile, MessageBullet message)
    {
        this.projectile = projectile;
        this.entityId = message.getEntityId();
        this.posX = message.getPosX();
        this.posY = message.getPosY();
        this.posZ = message.getPosZ();
        this.motionX = message.getMotionX();
        this.motionY = message.getMotionY();
        this.motionZ = message.getMotionZ();
        this.trailColor = message.getTrailColor();
        this.trailLengthMultiplier = message.getTrailLengthMultiplier();
        this.updateHeading();
    }

    private void updateHeading()
    {
        float d = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) d) * (180D / Math.PI));
    }

    public void tick(World world)
    {
        if(this.projectile == null)
        {
            Entity entity = world.getEntityByID(this.entityId);
            if(entity instanceof ProjectileEntity)
            {
                this.projectile = (ProjectileEntity) entity;
                /*this.posX = this.projectile.posX;
                this.posY = this.projectile.posY;
                this.posZ = this.projectile.posZ;
                this.motionX = this.projectile.motionX;
                this.motionY = this.projectile.motionY;
                this.motionZ = this.projectile.motionZ;*/
            }
        }
        else if(!this.projectile.isAlive())
        {
            this.finished = true;
        }
        else
        {
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;

            if(this.projectile.getProjectile().gravity)
            {
                this.motionY -= 0.05;
                this.updateHeading();
            }
        }
    }

    public ProjectileEntity getProjectile()
    {
        return this.projectile;
    }

    public double getPosX()
    {
        return this.posX;
    }

    public double getPosY()
    {
        return this.posY;
    }

    public double getPosZ()
    {
        return this.posZ;
    }

    public double getMotionX()
    {
        return this.motionX;
    }

    public double getMotionY()
    {
        return this.motionY;
    }

    public double getMotionZ()
    {
        return this.motionZ;
    }

    public float getRotationYaw()
    {
        return this.rotationYaw;
    }

    public float getRotationPitch()
    {
        return this.rotationPitch;
    }

    public boolean isFinished()
    {
        return this.finished;
    }

    public int getTrailColor()
    {
        return this.trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return this.trailLengthMultiplier;
    }
}
