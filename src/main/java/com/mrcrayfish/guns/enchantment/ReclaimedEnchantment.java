package com.mrcrayfish.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: MrCrayfish
 */
public class ReclaimedEnchantment extends GunEnchantment
{
    public ReclaimedEnchantment()
    {
        super(Rarity.VERY_RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.AMMO);
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 2;
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
