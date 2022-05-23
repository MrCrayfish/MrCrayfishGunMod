package com.tac.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class AcceleratorEnchantment extends GunEnchantment
{
    public AcceleratorEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 10 + (level - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 20;
    }
}
