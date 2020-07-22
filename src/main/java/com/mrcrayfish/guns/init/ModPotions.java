package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions
{
    public static final DeferredRegister<Potion> REGISTER = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);

    public static final RegistryObject<Potion> BLINDED = REGISTER.register("blinding", () -> new Potion(new EffectInstance(ModEffects.BLINDED.get(), 100)));
    public static final RegistryObject<Potion> DEAFENED = REGISTER.register("deafening", () -> new Potion(new EffectInstance(ModEffects.DEAFENED.get(), 100)));
}