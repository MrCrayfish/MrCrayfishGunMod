package com.tac.guns.enchantment;

import com.tac.guns.Reference;
import com.tac.guns.item.GunItem;
import net.minecraft.enchantment.EnchantmentType;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class EnchantmentTypes
{
    public static final EnchantmentType GUN = EnchantmentType.create(Reference.MOD_ID + ":gun", item -> item.getItem() instanceof GunItem);
    public static final EnchantmentType SEMI_AUTO_GUN = EnchantmentType.create(Reference.MOD_ID + ":semi_auto_gun", item -> item instanceof GunItem && !((GunItem) item).getGun().getGeneral().isAuto());
}
