package com.mrcrayfish.guns.compat;

import com.mrcrayfish.backpacked.Backpacked;
import com.mrcrayfish.backpacked.core.ModEnchantments;
import com.mrcrayfish.backpacked.inventory.BackpackInventory;
import com.mrcrayfish.backpacked.inventory.BackpackedInventoryAccess;
import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public class BackpackHelper
{
    public static AmmoContext findAmmo(PlayerEntity player, ResourceLocation id)
    {
        ItemStack backpack = Backpacked.getBackpackStack(player);
        if(backpack.isEmpty())
            return AmmoContext.NONE;

        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.MARKSMAN.get(), backpack) <= 0)
            return AmmoContext.NONE;

        BackpackInventory inventory = ((BackpackedInventoryAccess) player).getBackpackedInventory();
        if(inventory == null)
            return AmmoContext.NONE;

        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(Gun.isAmmo(stack, id))
            {
                return new AmmoContext(stack, inventory);
            }
        }

        return AmmoContext.NONE;
    }
}
