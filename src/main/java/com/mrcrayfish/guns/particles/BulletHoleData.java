package com.mrcrayfish.guns.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class BulletHoleData implements IParticleData
{
    public static final IParticleData.IDeserializer<BulletHoleData> DESERIALIZER = new IParticleData.IDeserializer<BulletHoleData>()
    {
        public BulletHoleData deserialize(ParticleType<BulletHoleData> particleType, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            Direction direction = Direction.byName(reader.readString());
            if(direction == null)
            {
                direction = Direction.NORTH;
            }
            return new BulletHoleData(particleType, direction);
        }

        public BulletHoleData read(ParticleType<BulletHoleData> particleType, PacketBuffer buffer)
        {
            return new BulletHoleData(particleType, buffer.readEnumValue(Direction.class));
        }
    };

    private final ParticleType<BulletHoleData> particleType;
    private final Direction direction;

    public BulletHoleData(ParticleType<BulletHoleData> particleType, Direction direction)
    {
        this.particleType = particleType;
        this.direction = direction;
    }

    @Override
    public ParticleType<?> getType()
    {
        return this.particleType;
    }

    public Direction getDirection()
    {
        return direction;
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeEnumValue(this.direction);
    }

    @Override
    public String getParameters()
    {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " + this.direction.getName();
    }
}
