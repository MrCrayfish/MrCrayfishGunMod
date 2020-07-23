package com.mrcrayfish.guns.enchantment;

import com.mrcrayfish.guns.GunMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

/**
 * Author: MrCrayfish
 */
public class QuickHandsEnchantment extends Enchantment
{
    public QuickHandsEnchantment()
    {
        super(Rarity.RARE, GunMod.ENCHANTMENT_TYPE_GUN, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return 10;
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return this.getMinEnchantability(level) + 30;
    }
}
