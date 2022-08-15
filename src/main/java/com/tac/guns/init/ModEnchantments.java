package com.tac.guns.init;

import com.tac.guns.Reference;
import com.tac.guns.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ModEnchantments
{
    public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Reference.MOD_ID);

    public static final RegistryObject<Enchantment> TRIGGER_FINGER = REGISTER.register("trigger_finger", TriggerFingerEnchantment::new); // LEGACY, ONLY BOLT ACTION?
    public static final RegistryObject<Enchantment> LIGHTWEIGHT = REGISTER.register("lightweight", LightweightEnchantment::new); // !
    public static final RegistryObject<Enchantment> COLLATERAL = REGISTER.register("collateral", CollateralEnchantment::new); // LEGACY

    public static final RegistryObject<Enchantment> OVER_CAPACITY = REGISTER.register("over_capacity", OverCapacityEnchantment::new); // TO-RENAME
    public static final RegistryObject<Enchantment> RECLAIMED = REGISTER.register("reclaimed", ReclaimedEnchantment::new); // LEGACY
    public static final RegistryObject<Enchantment> ACCELERATOR = REGISTER.register("accelerator", AcceleratorEnchantment::new); // TO-RENAME
    public static final RegistryObject<Enchantment> PUNCTURING = REGISTER.register("puncturing", PuncturingEnchantment::new); // TO-RENAME
    public static final RegistryObject<Enchantment> FIRE_STARTER = REGISTER.register("fire_starter", FireStarterEnchantment::new); // LEGACY
    public static final RegistryObject<Enchantment> RIFLING = REGISTER.register("rifling", AdvancedRiflingEnchantment::new);
    public static final RegistryObject<Enchantment> BUFFERED = REGISTER.register("buffered", BufferedEnchantment::new);
    //public static final RegistryObject<Enchantment> Reliability = REGISTER.register("reliability", AcceleratorEnchantment::new); // TO-RENAME
}
