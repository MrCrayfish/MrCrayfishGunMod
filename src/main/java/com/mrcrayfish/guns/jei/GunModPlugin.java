package com.mrcrayfish.guns.jei;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.crafting.WorkbenchRecipes;
import com.mrcrayfish.guns.init.ModBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * Author: MrCrayfish
 */
@JeiPlugin
public class GunModPlugin implements IModPlugin
{
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
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
        registration.addRecipes(WorkbenchRecipes.getAll(world), WorkbenchCategory.ID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WORKBENCH.get()), WorkbenchCategory.ID);
    }
}
