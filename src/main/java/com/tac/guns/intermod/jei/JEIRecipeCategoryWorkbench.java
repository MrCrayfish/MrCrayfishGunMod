package com.tac.guns.intermod.jei;

import com.tac.guns.Reference;
import com.tac.guns.crafting.WorkbenchRecipe;
import com.tac.guns.init.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class JEIRecipeCategoryWorkbench implements IRecipeCategory<WorkbenchRecipe> {

    private final IDrawable icon;
    private final IDrawable background;
    public static final ResourceLocation UID = new ResourceLocation(Reference.MOD_ID, "jei_workbench");

    public JEIRecipeCategoryWorkbench(IGuiHelper helper){
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.WORKBENCH.get()));
        ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID, "textures/gui/jeiworkbench.png");
        this.background = helper.drawableBuilder(TEXTURE, 0, 0, 148, 86).setTextureSize(148, 86).build();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends WorkbenchRecipe> getRecipeClass() {
        return WorkbenchRecipe.class;
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("recipe.gunworkbench");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(WorkbenchRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM ,recipe.getIngredientStacks());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, WorkbenchRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemstacks = recipeLayout.getItemStacks();
        List<List<ItemStack>> list = ingredients.getInputs(VanillaTypes.ITEM);
        int size = list.size();
        int index = 0;
        for(int j=0; j<Math.min(size, 5); j++){
            for(int k=0; k<Math.min(4, size % 4); k++){
                index = j*4+k;
                itemstacks.init(index, true, 3+16*k, 3+16*j);
            }
        }
        itemstacks.init(index+1, false, 109, 34);
        itemstacks.set(ingredients);
    }
}
