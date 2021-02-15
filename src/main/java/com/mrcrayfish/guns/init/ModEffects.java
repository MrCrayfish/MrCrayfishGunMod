package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.effect.IncurableEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class ModEffects
{
    public static final DeferredRegister<Effect> REGISTER = DeferredRegister.create(ForgeRegistries.POTIONS, Reference.MOD_ID);

    public static final RegistryObject<IncurableEffect> BLINDED = REGISTER.register("blinded", () -> new IncurableEffect(EffectType.HARMFUL, 0));
    public static final RegistryObject<IncurableEffect> DEAFENED = REGISTER.register("deafened", () -> new IncurableEffect(EffectType.HARMFUL, 0));
}
