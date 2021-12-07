package com.mrcrayfish.guns.crafting;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipes
{
    public static boolean isEmpty(Level world)
    {
        return world.getRecipeManager().getRecipes().stream().noneMatch(recipe -> recipe.getType() == ModRecipeType.WORKBENCH);
    }

    public static NonNullList<WorkbenchRecipe> getAll(Level world)
    {
        return world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == ModRecipeType.WORKBENCH)
                .map(recipe -> (WorkbenchRecipe) recipe)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Nullable
    public static WorkbenchRecipe getRecipeById(Level world, ResourceLocation id)
    {
        return world.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == ModRecipeType.WORKBENCH)
                .map(recipe -> (WorkbenchRecipe) recipe)
                .filter(recipe -> recipe.getId().equals(id))
                .findFirst().orElse(null);
    }
}
