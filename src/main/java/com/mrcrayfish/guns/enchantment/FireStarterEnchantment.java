package com.mrcrayfish.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: MrCrayfish
 */
public class FireStarterEnchantment extends GunEnchantment
{
    public FireStarterEnchantment()
    {
        super(Rarity.VERY_RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 20;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return 50;
    }
}
