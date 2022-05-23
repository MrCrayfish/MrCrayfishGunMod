package com.tac.guns.item;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A simple interface to allow items to be colored. Implementing this on an item will automatically
 * register an {@link IItemColor} into {@link ItemColors}. If the item this is implemented on is an
 * attachment, it will colored automatically by the color of the weapon if the item does not explicitly
 * have a color set.
 *
 * Timeless changes - IColored will now also provide methods for the 8 new color segments.
 *
 *
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs, Timeless Development team (ClumsyAlien)
 */
public interface IWeaponColorable
{
    /**
     * @return Handle color segment type.
     */
    WeaponColorSegment getColorSegment();
    enum WeaponColorSegment
    {
        BARREL_COLOR("barrel_color"),
        IRON_SIGHTS_COLOR("iron_sights_color"),
        HANDGUARD_COLOR("handguard_color"),
        BODY_COLOR("body_color"),
        MAGAZINE_COLOR("magazine_color"),
        GRIP_COLOR("grip_color"),
        STOCK_COLOR("stock_color"),
        EXTRA_PIECES_COLOR("extra_color");

        private String colorTranslationKey;

        WeaponColorSegment(String colorTranslationKey)
        {
            this.colorTranslationKey = colorTranslationKey;
        }

        public String getColorTranslationKey()
        {
            return this.colorTranslationKey;
        }
        @Nullable
        public static WeaponColorSegment byTagKey(String colorTranslationKey)
        {
            for(WeaponColorSegment colorSegment : values())
            {
                if(colorSegment.colorTranslationKey.equalsIgnoreCase(colorTranslationKey))
                {
                    return colorSegment;
                }
            }
            return null;
        }
    }

    /**
     * Gets whether or not this item can be colored
     *
     * @param stack the ItemStack of the colored item
     * @return If this item can be colored
     */
    default boolean canWeaponColor(ItemStack stack)
    {
        return true;
    }

    /**
     * Gets whether or not this item has a color applied
     *
     * @param stack the ItemStack of the colored item
     * @return If this item has any color or color segment applied
     */
    default boolean hasWeaponColor(ItemStack stack)
    {
        CompoundNBT tagCompound = stack.getOrCreateTag();
        boolean hasColor = false;
        for (WeaponColorSegment colorSegment: IWeaponColorable.WeaponColorSegment.values())
        {
            if(tagCompound.contains(colorSegment.colorTranslationKey, Constants.NBT.TAG_INT))
                hasColor = true;
        }
        return hasColor;
    }

    /**
     * Gets all color segments, only for weapon coloring.
     *
     * @param stack the ItemStack of the colored item
     * @return the color in rgba integer format
     */
    default int[] getWeaponColors(ItemStack stack)
    {
        CompoundNBT tagCompound = stack.getOrCreateTag();
        int[] colors = new int[IWeaponColorable.WeaponColorSegment.values().length];
        int iterator = 0;
        for (WeaponColorSegment colorSegment: IWeaponColorable.WeaponColorSegment.values())
        {
            colors[iterator] = tagCompound.getInt(colorSegment.colorTranslationKey);
        }
        return colors;
    }
    /**
     * Sets the color of this item
     *
     * @param stack the ItemStack of the colored item
     * @param color the color in rgba integer format
     */
    default void setWeaponColor(ItemStack stack, IWeaponColorable.WeaponColorSegment segment, int color)
    {
        CompoundNBT tagCompound = stack.getOrCreateTag();
        tagCompound.putInt(segment.colorTranslationKey, color);
    }

    /**
     * Removes the color from this item
     *
     * @param stack the ItemStack of the colored item
     */
    default void removeWeaponColor(ItemStack stack, IWeaponColorable.WeaponColorSegment segment)
    {
        CompoundNBT tagCompound = stack.getOrCreateTag();
        tagCompound.remove(segment.colorTranslationKey);
    }

    /**
     * Gets the color of this item
     *
     * @param stack the ItemStack of the colored item
     * @return the color in rgba integer format
     */
    default int getWeaponColorBySegment(ItemStack stack, IWeaponColorable.WeaponColorSegment segment)
    {
        CompoundNBT tagCompound = stack.getOrCreateTag();
        return tagCompound.getInt(segment.colorTranslationKey);
    }

    /**
     * Combines the color values of a list of dyes and applies the color to the colored item. If the
     * colored item already has a color, this will be included into combining the dye color values.
     *
     * @param stack the ItemStack of the colored item
     * @param dyes  a list of {@link DyeItem}
     * @return a new ItemStack with the combined color
     */
    static ItemStack dye(ItemStack stack, IWeaponColorable.WeaponColorSegment colorSegment, List<DyeItem> dyes)
    {
        ItemStack resultStack = ItemStack.EMPTY;
        int[] combinedColors = new int[3];
        int maxColor = 0;
        int colorCount = 0;
        IWeaponColorable coloredItem = null;
        if(stack.getItem() instanceof IWeaponColorable && ((IWeaponColorable) stack.getItem()).canWeaponColor(stack))
        {
            coloredItem = (IWeaponColorable) stack.getItem();
            resultStack = stack.copy();
            resultStack.setCount(1);
            if(coloredItem.hasWeaponColor(stack))
            {
                int color = coloredItem.getWeaponColorBySegment(resultStack,colorSegment);
                float red = (float) (color >> 16 & 255) / 255.0F;
                float green = (float) (color >> 8 & 255) / 255.0F;
                float blue = (float) (color & 255) / 255.0F;
                maxColor = (int) ((float) maxColor + Math.max(red, Math.max(green, blue)) * 255.0F);
                combinedColors[0] = (int) ((float) combinedColors[0] + red * 255.0F);
                combinedColors[1] = (int) ((float) combinedColors[1] + green * 255.0F);
                combinedColors[2] = (int) ((float) combinedColors[2] + blue * 255.0F);
                colorCount++;
            }

            for(DyeItem dyeitem : dyes)
            {
                float[] colorComponents = dyeitem.getDyeColor().getColorComponentValues();
                int red = (int) (colorComponents[0] * 255.0F);
                int green = (int) (colorComponents[1] * 255.0F);
                int blue = (int) (colorComponents[2] * 255.0F);
                maxColor += Math.max(red, Math.max(green, blue));
                combinedColors[0] += red;
                combinedColors[1] += green;
                combinedColors[2] += blue;
                colorCount++;
            }
        }

        if(coloredItem == null)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            int red = combinedColors[0] / colorCount;
            int green = combinedColors[1] / colorCount;
            int blue = combinedColors[2] / colorCount;
            float averageColor = (float) maxColor / (float) colorCount;
            float maxValue = (float) Math.max(red, Math.max(green, blue));
            red = (int) ((float) red * averageColor / maxValue);
            green = (int) ((float) green * averageColor / maxValue);
            blue = (int) ((float) blue * averageColor / maxValue);
            int finalColor = (red << 8) + green;
            finalColor = (finalColor << 8) + blue;
            coloredItem.setWeaponColor(resultStack, colorSegment, finalColor);
            return resultStack;
        }
    }
}
