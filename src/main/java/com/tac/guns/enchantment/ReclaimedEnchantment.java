package com.tac.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ReclaimedEnchantment extends GunEnchantment
{
    public ReclaimedEnchantment()
    {
        super(Rarity.VERY_RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.AMMO);
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 15 + (level - 1) * 10;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 10;
    }
}
