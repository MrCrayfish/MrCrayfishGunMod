package com.mrcrayfish.guns.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipes
{
    public static boolean isEmpty(World world)
    {
        return world.getRecipeManager().getRecipes().stream().noneMatch(recipe -> recipe.getType() == RecipeType.WORKBENCH);
    }

    public static NonNullList<WorkbenchRecipe> getAll(World world)
    {
        return world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == RecipeType.WORKBENCH)
                .map(recipe -> (WorkbenchRecipe) recipe)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Nullable
    public static WorkbenchRecipe getRecipeById(World world, ResourceLocation id)
    {
        return world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == RecipeType.WORKBENCH)
                .map(recipe -> (WorkbenchRecipe) recipe)
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst().orElse(null);
    }
}
