package com.tac.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class PuncturingEnchantment extends GunEnchantment
{
    public PuncturingEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.PROJECTILE);
    }

    @Override
    public int getMaxLevel()
    {
        return 5;
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 1 + (level - 1) * 10;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 10;
    }
}
