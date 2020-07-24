package com.mrcrayfish.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: MrCrayfish
 */
public class PuncturingEnchantment extends GunEnchantment
{
    public PuncturingEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }

    @Override
    public int getMinLevel()
    {
        return 1;
    }

    @Override
    public int getMaxLevel()
    {
        return 4;
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 10;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return 30;
    }
}
