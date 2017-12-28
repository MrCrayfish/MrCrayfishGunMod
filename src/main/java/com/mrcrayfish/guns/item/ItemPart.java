package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MrCrayfish
 */
public class ItemPart extends Item implements ISubItems
{
    private static final String[] PARTS = { "chain_gun_base", "chain_gun_barrels", "flash", "scope" };

    public ItemPart()
    {
        this.setUnlocalizedName("part");
        this.setRegistryName("part");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
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

    @Override
    public NonNullList<ResourceLocation> getModels()
    {
        NonNullList<ResourceLocation> modelLocations = NonNullList.create();
        for(String part : ItemPart.PARTS)
        {
            modelLocations.add(new ResourceLocation(Reference.MOD_ID, "part_" + part));
        }
        return modelLocations;
    }
}
