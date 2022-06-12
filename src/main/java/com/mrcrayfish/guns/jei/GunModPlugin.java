package com.mrcrayfish.guns.jei;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.crafting.WorkbenchRecipe;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.init.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * Author: MrCrayfish
 */
@JeiPlugin
public class GunModPlugin implements IModPlugin
{
    public static final RecipeType<WorkbenchRecipe> WORKBENCH = RecipeType.create(Reference.MOD_ID, "workbench", WorkbenchRecipe.class);

    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(Reference.MOD_ID, "crafting");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new WorkbenchCategory(helper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
        registration.addRecipes(WORKBENCH, WorkbenchRecipes.getAll(world));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WORKBENCH.get()), WORKBENCH);
    }
}
