package com.mrcrayfish.guns.enchantment;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.enchantment.EnchantmentType;

/**
 * Author: MrCrayfish
 */
public class EnchantmentTypes
{
    public static final EnchantmentType GUN = EnchantmentType.create(Reference.MOD_ID + ":gun", item -> item.getItem() instanceof GunItem);
    public static final EnchantmentType SEMI_AUTO_GUN = EnchantmentType.create(Reference.MOD_ID + ":semi_auto_gun", item -> item instanceof GunItem && !((GunItem) item).getGun().general.auto);
}
