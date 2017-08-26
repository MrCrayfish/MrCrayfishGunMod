package com.mrcrayfish.guns.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MrCrayfish
 */
public class ItemPart extends Item
{
    public static final String[] PARTS = { "chain_gun_base", "chain_gun_barrels", "flash", "scope" };

    public ItemPart()
    {
        this.setUnlocalizedName("part");
        this.setRegistryName("part");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "_" + PARTS[stack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        for (int i = 0; i < PARTS.length; ++i)
        {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
}
