package com.tac.guns.item;

import com.tac.guns.item.attachment.IScope;
import com.tac.guns.item.attachment.impl.Scope;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * A basic scope attachment item implementation with color support
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ScopeItem extends Item implements IScope, IColored
{
    private final Scope scope;
    private final boolean colored;

    public ScopeItem(Scope scope, Item.Properties properties)
    {
        super(properties);
        this.scope = scope;
        this.colored = true;
    }

    public ScopeItem(Scope scope, Item.Properties properties, boolean colored)
    {
        super(properties);
        this.scope = scope;
        this.colored = colored;
    }

    @Override
    public Scope getProperties()
    {
        return this.scope;
    }

    @Override
    public boolean canColor(ItemStack stack)
    {
        return this.colored;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
