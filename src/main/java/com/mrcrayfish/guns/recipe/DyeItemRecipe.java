package com.mrcrayfish.guns.recipe;

import com.mrcrayfish.guns.init.ModRecipeSerializers;
import com.mrcrayfish.guns.item.IColored;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
public class DyeItemRecipe extends SpecialRecipe
{
    public DyeItemRecipe(ResourceLocation id)
    {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inventory, World worldIn)
    {
        ItemStack item = ItemStack.EMPTY;
        List<ItemStack> dyes = new ArrayList<>();

        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof IColored)
                {
                    if(!item.isEmpty())
                    {
                        return false;
                    }
                    item = stack;
                }
                else
                {
                    if(!(stack.getItem() instanceof DyeItem))
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
    public ItemStack getCraftingResult(CraftingInventory inventory)
    {
        ItemStack item = ItemStack.EMPTY;
        List<DyeItem> dyes = new ArrayList<>();

        for(int i = 0; i < inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!stack.isEmpty())
            {
                if(stack.getItem() instanceof IColored)
                {
                    if(!item.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }
                    item = stack.copy();
                }
                else
                {
                    if(!(stack.getItem() instanceof DyeItem))
                    {
                        return ItemStack.EMPTY;
                    }
                    dyes.add((DyeItem) stack.getItem());
                }
            }
        }

        return !item.isEmpty() && !dyes.isEmpty() ? IColored.dye(item, dyes) : ItemStack.EMPTY;
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
    public IRecipeSerializer<?> getSerializer()
    {
        return ModRecipeSerializers.DYE_ITEM.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInventory inventory)
    {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);
        for(int i = 0; i < remainingItems.size(); ++i)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            remainingItems.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(stack));
        }
        return remainingItems;
    }
}
