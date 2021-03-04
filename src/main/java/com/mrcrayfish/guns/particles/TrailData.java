package com.mrcrayfish.guns.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrcrayfish.guns.init.ModParticleTypes;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class TrailData implements IParticleData
{
    public static final Codec<TrailData> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(Codec.BOOL.fieldOf("enchanted").forGetter((data) -> {
            return data.enchanted;
        })).apply(builder, TrailData::new);
    });

    public static final IParticleData.IDeserializer<TrailData> DESERIALIZER = new IParticleData.IDeserializer<TrailData>()
    {
        @Override
        public TrailData deserialize(ParticleType<TrailData> particleType, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            return new TrailData(reader.readBoolean());
        }

        @Override
        public TrailData read(ParticleType<TrailData> particleType, PacketBuffer buffer)
        {
            return new TrailData(buffer.readBoolean());
        }
    };

    private boolean enchanted;

    public TrailData(boolean enchanted)
    {
        this.enchanted = enchanted;
    }

    public boolean isEnchanted()
    {
        return this.enchanted;
    }

    @Override
    public ParticleType<?> getType()
    {
        return ModParticleTypes.TRAIL.get();
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.enchanted);
    }

    @Override
    public String getParameters()
    {
        return ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()) + " " + this.enchanted;
    }
}
