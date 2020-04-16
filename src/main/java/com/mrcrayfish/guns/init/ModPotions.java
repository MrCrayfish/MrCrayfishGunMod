package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions
{
    public static final DeferredRegister<Potion> REGISTER = new DeferredRegister<>(ForgeRegistries.POTION_TYPES, Reference.MOD_ID);

    public static final RegistryObject<Potion> BLINDED = REGISTER.register("blinded", Potion::new);
    public static final RegistryObject<Potion> DEAFENED = REGISTER.register("deafened", Potion::new);
}