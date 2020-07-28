package com.mrcrayfish.guns.init;

import com.mojang.serialization.Codec;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.particles.BulletHoleData;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class ModParticleTypes
{
    public static final DeferredRegister<ParticleType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Reference.MOD_ID);

    public static final RegistryObject<ParticleType<BulletHoleData>> BULLET_HOLE = REGISTER.register("bullet_hole",() -> new ParticleType<BulletHoleData>(false, BulletHoleData.DESERIALIZER)
    {
        @Override
        public Codec<BulletHoleData> func_230522_e_()
        {
            return BulletHoleData.CODEC;
        }
    });
    public static final RegistryObject<BasicParticleType> BLOOD = REGISTER.register("blood", () -> new BasicParticleType(true));
}
