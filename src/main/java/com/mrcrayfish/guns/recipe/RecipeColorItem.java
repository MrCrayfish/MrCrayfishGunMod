package com.mrcrayfish.guns.recipe;

import com.google.common.collect.Lists;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemColored;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class RecipeColorItem extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    public RecipeColorItem()
    {
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "color_weapon"));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack item = ItemStack.EMPTY;
        List<ItemStack> dyes = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemColored)
                {
                    if (!item.isEmpty())
                    {
                        return false;
                    }
                    item = stack;
                }
                else
                {
                    if (!net.minecraftforge.oredict.DyeUtils.isDye(stack))
                    {
                        return false;
                    }
                    dyes.add(stack);
                }
            }
        }

        return !item.isEmpty() && !dyes.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack item = ItemStack.EMPTY;
        ItemColored colored = null;

        int[] combinedValues = new int[3];
        int combinedColor = 0;
        int colorCount = 0;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack stack = inv.getStackInSlot(k);
            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemColored)
                {
                    colored = (ItemColored) stack.getItem();
                    if (!item.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }
                    item = stack.copy();
                    item.setCount(1);

                    if (colored.hasColor(stack))
                    {
                        int color = colored.getColor(item);
                        float red = (float)(color >> 16 & 255) / 255.0F;
                        float green = (float)(color >> 8 & 255) / 255.0F;
                        float blue = (float)(color & 255) / 255.0F;
                        combinedColor = (int)((float)combinedColor + Math.max(red, Math.max(green, blue)) * 255.0F);
                        combinedValues[0] = (int)((float)combinedValues[0] + red * 255.0F);
                        combinedValues[1] = (int)((float)combinedValues[1] + green * 255.0F);
                        combinedValues[2] = (int)((float)combinedValues[2] + blue * 255.0F);
                        colorCount++;
                    }
                }
                else
                {
                    if (!net.minecraftforge.oredict.DyeUtils.isDye(stack))
                    {
                        return ItemStack.EMPTY;
                    }

                    float[] color = net.minecraftforge.oredict.DyeUtils.colorFromStack(stack).get().getColorComponentValues();
                    int red = (int)(color[0] * 255.0F);
                    int green = (int)(color[1] * 255.0F);
                    int blue = (int)(color[2] * 255.0F);
                    combinedColor += Math.max(red, Math.max(green, blue));
                    combinedValues[0] += red;
                    combinedValues[1] += green;
                    combinedValues[2] += blue;
                    colorCount++;
                }
            }
        }

        if (colored == null)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            int red = combinedValues[0] / colorCount;
            int green = combinedValues[1] / colorCount;
            int blue = combinedValues[2] / colorCount;
            float averageColor = (float)combinedColor / (float)colorCount;
            float maxValue = (float)Math.max(red, Math.max(green, blue));
            red = (int)((float)red * averageColor / maxValue);
            green = (int)((float)green * averageColor / maxValue);
            blue = (int)((float)blue * averageColor / maxValue);
            int finalColor = (red << 8) + green;
            finalColor = (finalColor << 8) + blue;
            colored.setColor(item, finalColor);
            return item;
        }
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> remainingItems = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remainingItems.size(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            remainingItems.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(stack));
        }
        return remainingItems;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }
}
