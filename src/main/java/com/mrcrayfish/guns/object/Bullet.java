package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class Bullet
{
    private EntityProjectile projectile;
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

    public Bullet(@Nullable EntityProjectile projectile, int entityId, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, int trailColor)
    {
        this.projectile = projectile;
        this.entityId = entityId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.trailColor = trailColor;
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
            if(entity instanceof EntityProjectile)
            {
                this.projectile = (EntityProjectile) entity;
                /*this.posX = this.projectile.posX;
                this.posY = this.projectile.posY;
                this.posZ = this.projectile.posZ;
                this.motionX = this.projectile.motionX;
                this.motionY = this.projectile.motionY;
                this.motionZ = this.projectile.motionZ;*/
            }
        }
        else if(this.projectile.isDead)
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

    public EntityProjectile getProjectile()
    {
        return projectile;
    }

    public double getPosX()
    {
        return posX;
    }

    public double getPosY()
    {
        return posY;
    }

    public double getPosZ()
    {
        return posZ;
    }

    public double getMotionX()
    {
        return motionX;
    }

    public double getMotionY()
    {
        return motionY;
    }

    public double getMotionZ()
    {
        return motionZ;
    }

    public float getRotationYaw()
    {
        return rotationYaw;
    }

    public float getRotationPitch()
    {
        return rotationPitch;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public int getTrailColor()
    {
        return trailColor;
    }
}
