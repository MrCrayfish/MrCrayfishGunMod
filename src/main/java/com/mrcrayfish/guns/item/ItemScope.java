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

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ItemScope extends Item implements ISubItems
{
    public ItemScope()
    {
        this.setUnlocalizedName("scope");
        this.setRegistryName("scope");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(MrCrayfishGunMod.GUN_TAB);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName(stack) + "_" + Type.values()[stack.getItemDamage()].name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
    {
        if(!isInCreativeTab(tab))
            return;

        for (int i = 0; i < Type.values().length; i++)
        {
            subItems.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public NonNullList<ResourceLocation> getModels()
    {
        NonNullList<ResourceLocation> modelLocations = NonNullList.create();
        for(Type type : Type.values())
        {
            modelLocations.add(new ResourceLocation(Reference.MOD_ID, "scope_" + type.name));
        }
        return modelLocations;
    }

    public enum Type
    {
        SMALL("small", 1F, 0.021),
        MEDIUM("medium", 2F, 0.105),
        LONG("long", 3F, 0.0);

        private String name;
        private float additionalZoom;
        private double offset;

        Type(String name, float additionalZoom, double offset)
        {
            this.name = name;
            this.additionalZoom = additionalZoom;
            this.offset = offset;
        }

        public float getAdditionalZoom()
        {
            return additionalZoom;
        }

        public double getOffset()
        {
            return offset;
        }

        @Nullable
        public static Type getFromStack(ItemStack stack)
        {
            if(stack.getItem() instanceof ItemScope)
            {
                return values()[stack.getMetadata()];
            }
            return null;
        }
    }
}
