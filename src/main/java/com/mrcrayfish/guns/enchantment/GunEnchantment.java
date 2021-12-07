package com.mrcrayfish.guns.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.entity.EquipmentSlot;

import net.minecraft.world.item.enchantment.Enchantment.Rarity;

/**
 * Author: MrCrayfish
 */
public abstract class GunEnchantment extends Enchantment
{
    private Type type;

    protected GunEnchantment(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot[] slots, Type type)
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
