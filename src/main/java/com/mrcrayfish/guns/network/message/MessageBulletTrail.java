package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class MessageBulletTrail implements IMessage
{
    private int[] entityIds;
    private Vec3[] positions;
    private Vec3[] motions;
    private ItemStack item;
    private int trailColor;
    private double trailLengthMultiplier;
    private int life;
    private double gravity;
    private int shooterId;
    private boolean enchanted;
    private ParticleOptions particleData;

    public MessageBulletTrail() {}

    public <T extends ParticleOptions> MessageBulletTrail(ProjectileEntity[] spawnedProjectiles, Gun.Projectile projectileProps, int shooterId, T particleData)
    {
        this.positions = new Vec3[spawnedProjectiles.length];
        this.motions = new Vec3[spawnedProjectiles.length];
        this.entityIds = new int[spawnedProjectiles.length];
        for(int i = 0; i < spawnedProjectiles.length; i++)
        {
            ProjectileEntity projectile = spawnedProjectiles[i];
            this.positions[i] = projectile.position();
            this.motions[i] = projectile.getDeltaMovement();
            this.entityIds[i] = projectile.getId();
        }
        this.item = spawnedProjectiles[0].getItem();
        this.enchanted = spawnedProjectiles[0].getWeapon().isEnchanted();
        this.trailColor = this.enchanted ? 0x9C71FF : projectileProps.getTrailColor();
        this.trailLengthMultiplier = projectileProps.getTrailLengthMultiplier();
        this.life = projectileProps.getLife();
        this.gravity = spawnedProjectiles[0].getModifiedGravity(); //It's possible that projectiles have different gravity
        this.shooterId = shooterId;
        this.particleData = particleData;
    }

    @Override
    public void encode(FriendlyByteBuf buffer)
    {
        buffer.writeInt(this.entityIds.length);
        for(int i = 0; i < this.entityIds.length; i++)
        {
            buffer.writeInt(this.entityIds[i]);

            Vec3 position = this.positions[i];
            buffer.writeDouble(position.x);
            buffer.writeDouble(position.y);
            buffer.writeDouble(position.z);

            Vec3 motion = this.motions[i];
            buffer.writeDouble(motion.x);
            buffer.writeDouble(motion.y);
            buffer.writeDouble(motion.z);
        }
        buffer.writeItem(this.item);
        buffer.writeVarInt(this.trailColor);
        buffer.writeDouble(this.trailLengthMultiplier);
        buffer.writeInt(this.life);
        buffer.writeDouble(this.gravity);
        buffer.writeInt(this.shooterId);
        buffer.writeBoolean(this.enchanted);
        buffer.writeInt(Registry.PARTICLE_TYPE.getId(this.particleData.getType()));
        this.particleData.writeToNetwork(buffer);
    }

    @Override
    public void decode(FriendlyByteBuf buffer)
    {
        int size = buffer.readInt();
        this.entityIds = new int[size];
        this.positions = new Vec3[size];
        this.motions = new Vec3[size];
        for(int i = 0; i < size; i++)
        {
            this.entityIds[i] = buffer.readInt();
            this.positions[i] = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
            this.motions[i] = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
        this.item = buffer.readItem();
        this.trailColor = buffer.readVarInt();
        this.trailLengthMultiplier = buffer.readDouble();
        this.life = buffer.readInt();
        this.gravity = buffer.readDouble();
        this.shooterId = buffer.readInt();
        this.enchanted = buffer.readBoolean();
        ParticleType<?> type = Registry.PARTICLE_TYPE.byId(buffer.readInt());
        if (type == null) {
            type = ParticleTypes.CRIT;
        }
        this.particleData = this.readParticle(buffer, type);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf buffer, ParticleType<T> type)
    {
        return type.getDeserializer().fromNetwork(type, buffer);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleMessageBulletTrail(this));
        supplier.get().setPacketHandled(true);
    }

    public int getCount()
    {
        return this.entityIds.length;
    }

    public int[] getEntityIds()
    {
        return this.entityIds;
    }

    public Vec3[] getPositions()
    {
        return this.positions;
    }

    public Vec3[] getMotions()
    {
        return this.motions;
    }

    public int getTrailColor()
    {
        return this.trailColor;
    }

    public double getTrailLengthMultiplier()
    {
        return this.trailLengthMultiplier;
    }

    public int getLife()
    {
        return this.life;
    }

    public ItemStack getItem()
    {
        return this.item;
    }

    public double getGravity()
    {
        return this.gravity;
    }

    public int getShooterId()
    {
        return this.shooterId;
    }

    public boolean isEnchanted()
    {
        return this.enchanted;
    }

    public ParticleOptions getParticleData()
    {
        return this.particleData;
    }
}
