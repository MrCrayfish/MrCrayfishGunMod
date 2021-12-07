package com.mrcrayfish.guns.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.guns.init.ModParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class BulletHoleData implements ParticleOptions
{
    public static final Codec<BulletHoleData> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.INT.fieldOf("dir").forGetter((data) -> {
            return data.direction.ordinal();
        }), Codec.LONG.fieldOf("pos").forGetter((p_239806_0_) -> {
            return p_239806_0_.pos.asLong();
        })).apply(builder, BulletHoleData::new);
    });

    public static final ParticleOptions.Deserializer<BulletHoleData> DESERIALIZER = new ParticleOptions.Deserializer<BulletHoleData>()
    {
        @Override
        public BulletHoleData fromCommand(ParticleType<BulletHoleData> particleType, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            int dir = reader.readInt();
            reader.expect(' ');
            long pos = reader.readLong();
            return new BulletHoleData(dir, pos);
        }

        @Override
        public BulletHoleData fromNetwork(ParticleType<BulletHoleData> particleType, FriendlyByteBuf buffer)
        {
            return new BulletHoleData(buffer.readInt(), buffer.readLong());
        }
    };

    private final Direction direction;
    private final BlockPos pos;

    public BulletHoleData(int dir, long pos)
    {
        this.direction = Direction.values()[dir];
        this.pos = BlockPos.of(pos);
    }

    public BulletHoleData(Direction dir, BlockPos pos)
    {
        this.direction = dir;
        this.pos = pos;
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
    public ParticleType<?> getType()
    {
        return ModParticleTypes.BULLET_HOLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer)
    {
        buffer.writeEnum(this.direction);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public String writeToString()
    {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " + this.direction.getName();
    }

    public static Codec<BulletHoleData> codec(ParticleType<BulletHoleData> type)
    {
        return CODEC;
    }
}
