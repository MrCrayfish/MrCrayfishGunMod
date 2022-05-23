package com.tac.guns.item;

import com.tac.guns.item.IColored;
import com.tac.guns.item.attachment.IBarrel;
import com.tac.guns.item.attachment.impl.Barrel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A basic barrel attachment item implementation with color support
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class PistolBarrelItem extends Item implements IBarrel, IColored
{
    private final Barrel barrel;
    private final boolean colored;

    public PistolBarrelItem(Barrel barrel, Properties properties)
    {
        super(properties);
        this.barrel = barrel;
        this.colored = true;
    }

    public PistolBarrelItem(Barrel barrel, Properties properties, boolean colored)
    {
        super(properties);
        this.barrel = barrel;
        this.colored = colored;
    }

    @Override
    public Barrel getProperties()
    {
        return this.barrel;
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
    @Override
    public Type getType()
    {
        return Type.PISTOL_BARREL;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, worldIn, tooltip, flag);
        tooltip.add((new TranslationTextComponent("info.tac.pistolBarrel_type").mergeStyle(TextFormatting.LIGHT_PURPLE).mergeStyle(TextFormatting.BOLD)));
    }
}
