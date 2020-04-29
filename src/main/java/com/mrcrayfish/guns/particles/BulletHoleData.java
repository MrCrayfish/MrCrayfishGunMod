package com.mrcrayfish.guns.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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
            reader.expect(' ');
            int x = reader.readInt();
            reader.expect(' ');
            int y = reader.readInt();
            reader.expect(' ');
            int z = reader.readInt();
            return new BulletHoleData(particleType, direction, new BlockPos(x, y, z));
        }

        public BulletHoleData read(ParticleType<BulletHoleData> particleType, PacketBuffer buffer)
        {
            return new BulletHoleData(particleType, buffer.readEnumValue(Direction.class), buffer.readBlockPos());
        }
    };

    private final ParticleType<BulletHoleData> particleType;
    private final Direction direction;
    private final BlockPos pos;

    public BulletHoleData(ParticleType<BulletHoleData> particleType, Direction direction, BlockPos pos)
    {
        this.particleType = particleType;
        this.direction = direction;
        this.pos = pos;
    }

    @Override
    public ParticleType<?> getType()
    {
        return this.particleType;
    }

    public Direction getDirection()
    {
        return this.direction;
    }

    public BlockPos getPos()
    {
        return this.pos;
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeEnumValue(this.direction);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public String getParameters()
    {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " + this.direction.getName();
    }
}
