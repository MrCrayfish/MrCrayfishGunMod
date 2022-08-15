package com.tac.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class LightweightEnchantment extends GunEnchantment
{
    public LightweightEnchantment()
    {
        super(Rarity.RARE, EnchantmentTypes.GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}, Type.WEAPON);
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 15;
    }
    @Override
    public int getMaxLevel()
    {
        return 3;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 20;
    }
}
