package com.tac.guns.enchantment;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
        return 15;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }
    @Override
    public int getMaxEnchantability(int level)
    {
        return super.getMinEnchantability(level) + 30;
    }
}
