package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.recipe.RecipeAttachment;
import com.mrcrayfish.guns.recipe.RecipeColorItem;

public class ModCrafting 
{
	public static void register()
	{
		//GameRegistry.addShapedRecipe(new ItemStack(ModGuns.shotgun), "IIR", " LC", 'I', Items.IRON_INGOT, 'R', Items.REDSTONE, 'L', Blocks.LEVER, 'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 9));
		//GameRegistry.addShapedRecipe(new ItemStack(ModGuns.ammo, 8), "I", "G", "C", 'I', Items.GOLD_INGOT, 'G', Items.GUNPOWDER, 'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 14));
		RegistrationHandler.Recipes.add(new RecipeAttachment());
		RegistrationHandler.Recipes.add(new RecipeColorItem());
	}
}
