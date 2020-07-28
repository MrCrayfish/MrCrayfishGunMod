package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public interface IColored
{
    default boolean canColor()
    {
        return true;
    }

    default boolean hasColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return tagCompound.contains("Color", Constants.NBT.TAG_INT);
    }

    default int getColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return tagCompound.getInt("Color");
    }

    default void setColor(ItemStack stack, int color)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        tagCompound.putInt("Color", color);
    }

    default void removeColor(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        tagCompound.remove("Color");
    }

    static ItemStack dye(ItemStack stack, List<DyeItem> dyes)
    {
        ItemStack resultStack = ItemStack.EMPTY;
        int[] combinedColors = new int[3];
        int maxColor = 0;
        int colorCount = 0;
        IColored coloredItem = null;
        if(stack.getItem() instanceof IColored)
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
            coloredItem.setColor(resultStack, finalColor);
            return resultStack;
        }
    }
}
