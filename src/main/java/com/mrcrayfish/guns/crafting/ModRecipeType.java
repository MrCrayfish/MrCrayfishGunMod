package com.mrcrayfish.guns.crafting;

import net.minecraft.world.item.crafting.RecipeType;

/**
 * Author: MrCrayfish
 */
public class ModRecipeType
{
    public static final RecipeType<WorkbenchRecipe> WORKBENCH = RecipeType.register("cgm:workbench");

    // Does nothing but trigger loading of static fields
    public static void init() {}
}
