package com.mrcrayfish.guns.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WorkbenchRecipe>
{
    @Override
    public WorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject parent)
    {
        ImmutableList.Builder<WorkbenchIngredient> builder = ImmutableList.builder();
        JsonArray input = JSONUtils.getAsJsonArray(parent, "materials");
        for(int i = 0; i < input.size(); i++)
        {
            JsonObject object = input.get(i).getAsJsonObject();
            builder.add(WorkbenchIngredient.fromJson(object));
        }
        if(!parent.has("result"))
        {
            throw new JsonSyntaxException("Missing result item entry");
        }
        JsonObject resultObject = JSONUtils.getAsJsonObject(parent, "result");
        ItemStack resultItem = ShapedRecipe.itemFromJson(resultObject);
        return new WorkbenchRecipe(recipeId, resultItem, builder.build());
    }

    @Nullable
    @Override
    public WorkbenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
    {
        ItemStack result = buffer.readItem();
        ImmutableList.Builder<WorkbenchIngredient> builder = ImmutableList.builder();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++)
        {
            builder.add((WorkbenchIngredient) Ingredient.fromNetwork(buffer));
        }
        return new WorkbenchRecipe(recipeId, result, builder.build());
    }

    @Override
    public void toNetwork(PacketBuffer buffer, WorkbenchRecipe recipe)
    {
        buffer.writeItem(recipe.getItem());
        buffer.writeVarInt(recipe.getMaterials().size());
        for(WorkbenchIngredient ingredient : recipe.getMaterials())
        {
            ingredient.toNetwork(buffer);
        }
    }
}
