package com.mrcrayfish.guns.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSourceIndirect;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class DamageSourceProjectile extends EntityDamageSourceIndirect
{
    private ItemStack weapon = ItemStack.EMPTY;

    public DamageSourceProjectile(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn, ItemStack weapon)
    {
        super(damageTypeIn, source, indirectEntityIn);
        this.weapon = weapon;
    }

    public ItemStack getWeapon()
    {
        return weapon;
    }
}
