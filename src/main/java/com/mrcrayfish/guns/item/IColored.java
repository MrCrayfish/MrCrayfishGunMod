package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.Config;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * A simple interface to allow items to be colored. Implementing this on an item will automatically
 * register an {@link ItemColor} into {@link ItemColors}. If the item this is implemented on is an
 * attachment, it will colored automatically by the color of the weapon if the item does not explicitly
 * have a color set.
 * <p>
 * Author: MrCrayfish
 */
public interface IColored
{
    /**
     * Gets whether or not this item can be colored
     *
     * @param stack the ItemStack of the colored item
     * @return If this item can be colored
     */
    default boolean canColor(ItemStack stack)
    {
        return true;
    }

    /**
     * Gets whether or not this item has a color applied
     *
     * @param stack the ItemStack of the colored item
     * @return If this item has a color applied
     */
    default boolean hasColor(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return tagCompound.contains("Color", Tag.TAG_INT);
    }

    /**
     * Gets the color of this item
     *
     * @param stack the ItemStack of the colored item
     * @return the color in rgba integer format
     */
    default int getColor(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        return tagCompound.getInt("Color");
    }

    /**
     * Sets the color of this item
     *
     * @param stack the ItemStack of the colored item
     * @param color the color in rgba integer format
     */
    default void setColor(ItemStack stack, int color)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.putInt("Color", color);
    }

    /**
     * Removes the color from this item
     *
     * @param stack the ItemStack of the colored item
     */
    default void removeColor(ItemStack stack)
    {
        CompoundTag tagCompound = stack.getOrCreateTag();
        tagCompound.remove("Color");
    }

    /**
     * Combines the color values of a list of dyes and applies the color to the colored item. If the
     * colored item already has a color, this will be included into combining the dye color values.
     *
     * @param stack the ItemStack of the colored item
     * @param dyes  a list of {@link DyeItem}
     * @return a new ItemStack with the combined color
     */
    static ItemStack dye(ItemStack stack, List<DyeItem> dyes)
    {
        ItemStack resultStack = ItemStack.EMPTY;
        int[] combinedColors = new int[3];
        int maxColor = 0;
        int colorCount = 0;
        IColored coloredItem = null;
        if(IColored.isDyeable(stack))
        {
            coloredItem = (IColored) stack.getItem();
            resultStack = stack.copy();
            resultStack.setCount(1);
            if(coloredItem.hasColor(stack))
            {
                int color = coloredItem.getColor(resultStack);
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
                float[] colorComponents = dyeitem.getDyeColor().getTextureDiffuseColors();
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
            coloredItem.setColor(resultStack, finalColor);
            return resultStack;
        }
    }

    static boolean isDyeable(ItemStack stack)
    {
        if(stack.getItem() instanceof IColored)
        {
            IColored colored = ((IColored) stack.getItem());
            return colored.canColor(stack) || Config.SERVER.experimental.forceDyeableAttachments.get();
        }
        return false;
    }
}
