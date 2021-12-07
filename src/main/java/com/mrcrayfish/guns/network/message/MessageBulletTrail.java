package com.mrcrayfish.guns.network.message;

import com.mrcrayfish.framework.api.network.PlayMessage;
import com.mrcrayfish.guns.client.network.ClientPlayHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.network.BufferUtil;
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
public class MessageBulletTrail extends PlayMessage<MessageBulletTrail>
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

    public MessageBulletTrail(int[] entityIds, Vec3[] positions, Vec3[] motions, ItemStack item, int trailColor, double trailLengthMultiplier, int life, double gravity, int shooterId, boolean enchanted, ParticleOptions particleData)
    {
        this.entityIds = entityIds;
        this.positions = positions;
        this.motions = motions;
        this.item = item;
        this.trailColor = trailColor;
        this.trailLengthMultiplier = trailLengthMultiplier;
        this.life = life;
        this.gravity = gravity;
        this.shooterId = shooterId;
        this.enchanted = enchanted;
        this.particleData = particleData;
    }

    @Override
    public void encode(MessageBulletTrail message, FriendlyByteBuf buffer)
    {
        buffer.writeInt(message.entityIds.length);
        for(int i = 0; i < message.entityIds.length; i++)
        {
            buffer.writeInt(message.entityIds[i]);
            BufferUtil.writeVec3(buffer, message.positions[i]);
            BufferUtil.writeVec3(buffer, message.motions[i]);
        }
        buffer.writeItem(message.item);
        buffer.writeVarInt(message.trailColor);
        buffer.writeDouble(message.trailLengthMultiplier);
        buffer.writeInt(message.life);
        buffer.writeDouble(message.gravity);
        buffer.writeInt(message.shooterId);
        buffer.writeBoolean(message.enchanted);
        buffer.writeInt(Registry.PARTICLE_TYPE.getId(message.particleData.getType()));
        message.particleData.writeToNetwork(buffer);
    }

    @Override
    public MessageBulletTrail decode(FriendlyByteBuf buffer)
    {
        int size = buffer.readInt();
        int[] entityIds = new int[size];
        Vec3[] positions = new Vec3[size];
        Vec3[] motions = new Vec3[size];
        for(int i = 0; i < size; i++)
        {
            entityIds[i] = buffer.readInt();
            positions[i] = BufferUtil.readVec3(buffer);
            motions[i] = BufferUtil.readVec3(buffer);
        }
        ItemStack item = buffer.readItem();
        int trailColor = buffer.readVarInt();
        double trailLengthMultiplier = buffer.readDouble();
        int life = buffer.readInt();
        double gravity = buffer.readDouble();
        int shooterId = buffer.readInt();
        boolean enchanted = buffer.readBoolean();
        ParticleType<?> type = Registry.PARTICLE_TYPE.byId(buffer.readInt());
        if (type == null) type = ParticleTypes.CRIT;
        ParticleOptions particleData = this.readParticle(buffer, type);
        return new MessageBulletTrail(entityIds, positions, motions, item, trailColor, trailLengthMultiplier, life, gravity,shooterId, enchanted, particleData);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf buffer, ParticleType<T> type)
    {
        return type.getDeserializer().fromNetwork(type, buffer);
    }

    @Override
    public void handle(MessageBulletTrail message, Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> ClientPlayHandler.handleMessageBulletTrail(message));
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
