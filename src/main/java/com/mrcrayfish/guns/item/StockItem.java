package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.item.attachment.IStock;
import com.mrcrayfish.guns.item.attachment.impl.Stock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.item.Item.Properties;

/**
 * A basic stock attachment item implementation with color support
 *
 * Author: MrCrayfish
 */
public class StockItem extends AttachmentItem implements IStock, IColored
{
    private final Stock stock;
    private final boolean colored;

    public StockItem(Stock stock, Properties properties)
    {
        super(properties);
        this.stock = stock;
        this.colored = true;
    }

    public StockItem(Stock stock, Properties properties, boolean colored)
    {
        super(properties);
        this.stock = stock;
        this.colored = colored;
    }

    @Override
    public Stock getProperties()
    {
        return this.stock;
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
