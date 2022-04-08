package com.mrcrayfish.guns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

import net.minecraft.enchantment.Enchantment.Rarity;

/**
 * Author: MrCrayfish
 */
public abstract class GunEnchantment extends Enchantment
{
    private Type type;

    protected GunEnchantment(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots, Type type)
    {
        super(rarityIn, typeIn, slots);
        this.type = type;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment)
    {
        if(enchantment instanceof GunEnchantment)
        {
            return ((GunEnchantment) enchantment).type != this.type;
        }
        return super.checkCompatibility(enchantment);
    }

    public enum Type
    {
        WEAPON, AMMO, PROJECTILE
    }
}
