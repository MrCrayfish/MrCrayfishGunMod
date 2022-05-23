package com.tac.guns.item;

import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.impl.Scope;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A basic scope attachment item implementation with color support
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class PistolScopeItem extends ScopeItem
{
    public PistolScopeItem(Scope scope, Properties properties)
    {
        super(scope,properties);
    }
    @Override
    public Type getType()
    {
        return Type.PISTOL_SCOPE;
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, worldIn, tooltip, flag);
        tooltip.add((new TranslationTextComponent("info.tac.pistolScope_type").mergeStyle(TextFormatting.LIGHT_PURPLE).mergeStyle(TextFormatting.BOLD)));
    }
}
