package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.recipe.RecipeAttachment;
import com.mrcrayfish.guns.recipe.RecipeColorItem;
import com.mrcrayfish.guns.util.ItemStackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkbenchRegistry
{
	private static final Map<ItemStack, List<ItemStack>> RECIPE_MAP = new HashMap<>();
	
	public static void registerRecipe(ItemStack stack, ItemStack ... materials)
	{
		for(ItemStack key : RECIPE_MAP.keySet())
		{
			if(ItemStackHelper.areItemStackEqualIgnoreTag(key, stack))
			{
				return;
			}
		}
		RECIPE_MAP.put(stack, Arrays.asList(materials));
	}

	@SuppressWarnings({"ConstantConditions"})
	public static void register()
	{

	}

	public static ImmutableMap<ItemStack, List<ItemStack>> getRecipeMap()
	{
		return ImmutableMap.copyOf(RECIPE_MAP);
	}

	@Nullable
	public static List<ItemStack> getMaterialsForStack(ItemStack find)
	{
		for(ItemStack stack : RECIPE_MAP.keySet())
		{
			if(ItemStackHelper.areItemStackEqualIgnoreTag(stack, find))
			{
				return RECIPE_MAP.get(stack);
			}
		}
		return null;
	}
}
