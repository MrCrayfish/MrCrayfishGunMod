package com.mrcrayfish.guns.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class WorkbenchRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<WorkbenchRecipe>
{
    @Override
    public WorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject json)
    {
        String group = JSONUtils.getAsString(json, "group", "");
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        JsonArray input = JSONUtils.getAsJsonArray(json, "materials");
        for(int i = 0; i < input.size(); i++)
        {
            JsonObject itemObject = input.get(i).getAsJsonObject();
            ItemStack stack = ShapedRecipe.itemFromJson(itemObject);
            builder.add(stack);
        }
        if(!json.has("result"))
            throw new JsonSyntaxException("Missing vehicle entry");

        JsonObject resultObject = JSONUtils.getAsJsonObject(json, "result");
        ItemStack resultItem = ShapedRecipe.itemFromJson(resultObject);
        return new WorkbenchRecipe(recipeId, resultItem, builder.build(), group);
    }

    @Nullable
    @Override
    public WorkbenchRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
    {
        String group = buffer.readUtf();
        ItemStack result = buffer.readItem();
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++)
            builder.add(buffer.readItem());
        return new WorkbenchRecipe(recipeId, result, builder.build(), group);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, WorkbenchRecipe recipe)
    {
        buffer.writeUtf(recipe.getGroup());
        buffer.writeItem(recipe.getItem());
        buffer.writeVarInt(recipe.getMaterials().size());
        for(ItemStack stack : recipe.getMaterials())
        {
            buffer.writeItem(stack);
        }
    }
}
