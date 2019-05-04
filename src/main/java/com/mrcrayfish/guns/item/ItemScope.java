package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ItemScope extends ItemAttachment implements ISubItems
{
    public ItemScope()
    {
        super("scope", IAttachment.Type.SCOPE);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey(stack) + "_" + Type.values()[stack.getItemDamage()].name;
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
        SMALL("small", 0.1F, 1.0F, 2F),
        MEDIUM("medium", 0.25F, 2.1F, 3F),
        LONG("long", 0.4F, 1.59F, 4F);

        private String name;
        private float additionalZoom;
        private double heightToCenter;
        private float length;

        Type(String name, float additionalZoom, double heightToCenter, float length)
        {
            this.name = name;
            this.additionalZoom = additionalZoom;
            this.heightToCenter = heightToCenter;
            this.length = length;
        }

        public float getAdditionalZoom()
        {
            return additionalZoom;
        }

        public double getHeightToCenter()
        {
            return heightToCenter;
        }

        public float getLength()
        {
            return length;
        }

        @Nullable
        public static Type getFromStack(ItemStack stack)
        {
            if(stack != null && stack.getItem() instanceof ItemScope)
            {
                if(stack.getMetadata() >= 0 && stack.getMetadata() < values().length)
                {
                    return values()[stack.getMetadata()];
                }
            }
            return null;
        }
    }
}
