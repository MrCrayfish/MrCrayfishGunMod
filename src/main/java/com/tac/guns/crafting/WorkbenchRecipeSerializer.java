package com.tac.guns.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class WorkbenchRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WorkbenchRecipe>
{
    @Override
    public WorkbenchRecipe read(ResourceLocation recipeId, JsonObject json)
    {
        String group = JSONUtils.getString(json, "group", "");
        ImmutableList.Builder<Pair<Ingredient, Integer>> builder = ImmutableList.builder();
        JsonArray input = JSONUtils.getJsonArray(json, "materials");
        for(int i = 0; i < input.size(); i++)
        {
            JsonObject itemObject = input.get(i).getAsJsonObject();
            Ingredient ingredient = Ingredient.deserialize(itemObject.get("item"));

            int count;
            try {
                count = JSONUtils.getInt(itemObject, "count");
            }
            catch (JsonSyntaxException e){
                count = 1;
            }
            builder.add(new Pair<>(ingredient, count));
        }if(!json.has("result"))
            throw new JsonSyntaxException("Missing result entry");

        JsonObject resultObject = JSONUtils.getJsonObject(json, "result");
        ItemStack resultItem = ShapedRecipe.deserializeItem(resultObject);
        return new WorkbenchRecipe(recipeId, resultItem, builder.build(), group);
    }

    @Nullable
    @Override
    public WorkbenchRecipe read(ResourceLocation recipeId, PacketBuffer buffer)
    {
        String group = buffer.readString();
        ItemStack result = buffer.readItemStack();
        ImmutableList.Builder<Pair<Ingredient, Integer>> builder = ImmutableList.builder();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++) {
            Ingredient ingredient = Ingredient.read(buffer);
            int count = buffer.readByte();
            builder.add(new Pair<>(ingredient, count));
        }
        return new WorkbenchRecipe(recipeId, result, builder.build(), group);
    }

    @Override
    public void write(PacketBuffer buffer, WorkbenchRecipe recipe)
    {
        buffer.writeString(recipe.getGroup());
        buffer.writeItemStack(recipe.getItem());
        buffer.writeVarInt(recipe.getMaterials().size());
        for(Pair<Ingredient, Integer> stack : recipe.getMaterials())
        {
            stack.getFirst().write(buffer);
            buffer.writeByte(stack.getSecond());
        }
    }
}
