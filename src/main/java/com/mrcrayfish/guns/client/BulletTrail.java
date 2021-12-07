package com.mrcrayfish.guns.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

/**
 * Author: MrCrayfish
 */
public class BulletTrail
{
    private int entityId;
    private Vec3 position;
    private Vec3 motion;
    private float yaw;
    private float pitch;
    private boolean dead;
    private ItemStack item;
    private int trailColor;
    private double trailLengthMultiplier;
    private int age;
    private int maxAge;
    private double gravity;
    private int shooterId;
    private WeakReference<Entity> shooter;
    private boolean enchanted;
    private ParticleOptions particleData;

    public BulletTrail(int entityId, Vec3 position, Vec3 motion, ItemStack item, int trailColor, double trailMultiplier, int maxAge, double gravity, int shooterId, boolean enchanted, ParticleOptions particleData)
    {
        this.entityId = entityId;
        this.position = position;
        this.motion = motion;
        this.item = item;
        this.trailColor = trailColor;
        this.trailLengthMultiplier = trailMultiplier;
        this.maxAge = maxAge;
        this.gravity = gravity;
        this.shooterId = shooterId;
        this.enchanted = enchanted;
        this.particleData = particleData;
        this.updateYawPitch();
    }

    private void updateYawPitch()
    {
        float horizontalLength = Mth.sqrt((float) (this.motion.x * this.motion.x + this.motion.z * this.motion.z));
        this.yaw = (float) Math.toDegrees(Mth.atan2(this.motion.x, this.motion.z));
        this.pitch = (float) Math.toDegrees(Mth.atan2(this.motion.y, (double) horizontalLength));
    }

    public void tick()
    {
        this.age++;

        this.position = this.position.add(this.motion);

        if(this.gravity != 0)
        {
            this.motion = this.motion.add(0, this.gravity, 0);
            this.updateYawPitch();
        }

        Entity shooter = this.getShooter();
        if(shooter instanceof Player && ((Player) shooter).isLocalPlayer())
        {
            Level world = shooter.level;
            world.addAlwaysVisibleParticle(this.particleData, true, this.position.x(), this.position.y(), this.position.z(), this.motion.x, this.motion.y, this.motion.z);
        }

        Entity entity = Minecraft.getInstance().getCameraEntity();
        double distance = entity != null ? Math.sqrt(entity.distanceToSqr(this.position)) : Double.MAX_VALUE;
        if(this.age >= this.maxAge || distance > 256)
        {
            this.dead = true;
        }
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public Vec3 getPosition()
    {
        return this.position;
    }

    public Vec3 getMotion()
    {
        return this.motion;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public boolean isDead()
    {
        return this.dead;
    }

    public int getAge()
    {
        return this.age;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public int getTrailColor()
    {
        return this.trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return this.trailLengthMultiplier;
    }

    public int getShooterId()
    {
        return this.shooterId;
    }

    /**
     * Gets the instance of the entity that shot the bullet. The entity is cached to avoid searching
     * for it every frame, especially when lots of bullet trails are being rendered.
     *
     * @return the shooter entity
     */
    @Nullable
    public Entity getShooter()
    {
        if(this.shooter == null)
        {
            Level world = Minecraft.getInstance().level;
            if(world != null)
            {
                Entity entity = world.getEntity(this.shooterId);
                if(entity != null)
                {
                    this.shooter = new WeakReference<>(entity);
                }
            }
        }
        if(this.shooter != null)
        {
            Entity entity = this.shooter.get();
            if(entity != null && !entity.isAlive())
            {
                return null;
            }
            return entity;
        }
        return null;
    }

    public boolean isTrailVisible()
    {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        return entity != null && entity.getId() != this.shooterId;
    }

    @Override
    public int hashCode()
    {
        return this.entityId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof BulletTrail)
        {
            return ((BulletTrail) obj).entityId == this.entityId;
        }
        return false;
    }
}
