package com.tac.guns.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.Reference;
import com.tac.guns.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.extensions.IForgeAdvancementBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Ocelot
 */
public class WorkbenchRecipeBuilder
{
    private final Item result;
    private final int count;
    private final List<Pair<Ingredient, Integer>> materials;
    private String group;

    public WorkbenchRecipeBuilder(IItemProvider item, int count)
    {
        this.result = item.asItem();
        this.count = count;
        this.materials = new ArrayList<>();
        this.advancementBuilder = Advancement.Builder.builder();
    }

    /**
     * Creates a new builder for a workbench recipe.
     */
    public static WorkbenchRecipeBuilder workbenchRecipe(IItemProvider resultIn)
    {
        return new WorkbenchRecipeBuilder(resultIn, 1);
    }

    /**
     * Creates a new builder for a workbench recipe.
     */
    public static WorkbenchRecipeBuilder workbenchRecipe(IItemProvider resultIn, int countIn)
    {
        return new WorkbenchRecipeBuilder(resultIn, countIn);
    }

    /**
     * Adds an ingredient of the given item.
     */
    public WorkbenchRecipeBuilder addIngredient(IItemProvider itemIn)
    {
        return this.addIngredient(itemIn, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public WorkbenchRecipeBuilder addIngredient(IItemProvider item, int quantity)
    {
        this.materials.add(new Pair<>(Ingredient.fromItems(item), quantity));
        return this;
    }

    public WorkbenchRecipeBuilder addIngredient(ITag.INamedTag<Item> item, int quantity)
    {
        this.materials.add(new Pair<>(Ingredient.fromTag(item), quantity));
        return this;
    }

    public WorkbenchRecipeBuilder addIngredient(ITag.INamedTag<Item> item)
    {
        this.materials.add(new Pair<>(Ingredient.fromTag(item), 1));
        return this;
    }

    /**
     * Adds an ingredient of the given item.
     */
    public WorkbenchRecipeBuilder addIngredient(Ingredient itemIn)
    {
        return this.addIngredient(itemIn, 1);
    }

    /**
     * Adds the given ingredient multiple times.
     */
    public WorkbenchRecipeBuilder addIngredient(Ingredient item, int quantity)
    {
        this.materials.add(new Pair<>(item, quantity));
        return this;
    }
    /**
     * Sets the group to place this recipe in.
     */
    public WorkbenchRecipeBuilder setGroup(String groupIn)
    {
        this.group = groupIn;
        return this;
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn)
    {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
     * the result.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn, String save)
    {
        this.build(consumerIn, Reference.MOD_ID, save);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String modid, String save)
    {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        if(new ResourceLocation(Reference.MOD_ID, save).equals(resourcelocation))
        {
            throw new IllegalStateException("Workbench Recipe " + save + " should remove its 'save' argument");
        }
        else
        {
            this.build(consumerIn, new ResourceLocation(modid, "craft_"+save));
        }
    }
    private final Advancement.Builder advancementBuilder;
    private void validate(ResourceLocation id)
    {
        if(this.advancementBuilder.getCriteria().isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        advancementBuilder.build(id);
        consumerIn.accept(new Result(id, this.result, this.count, this.group == null ? "" : this.group, this.materials));
    }
    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final Item item;
        private final int count;
        private final String group;
        private final List<Pair<Ingredient, Integer>> materials;

        public Result(ResourceLocation id, IItemProvider item, int count, String group, List<Pair<Ingredient, Integer>> materials)
        {
            this.id = id;
            this.item = item.asItem();
            this.count = count;
            this.group = group;
            this.materials = materials;
        }

        @Override
        public void serialize(JsonObject json)
        {
            if(!this.group.isEmpty())
                json.addProperty("group", this.group);

            JsonArray input = new JsonArray();
            for(Pair<Ingredient, Integer> material : this.materials)
            {
                JsonObject resultObject = new JsonObject();
                resultObject.add("item", material.getFirst().serialize());
               /* if(material.getSecond() > 1)
                    */
                resultObject.addProperty("count", material.getSecond());
                input.add(resultObject);
            }
            json.add("materials", input);

            JsonObject resultObject = new JsonObject();
            resultObject.addProperty("item", Registry.ITEM.getKey(this.item).toString());
            if(this.count > 1)
                resultObject.addProperty("count", this.count);
            json.add("result", resultObject);
        }

        @Override
        public ResourceLocation getID()
        {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer()
        {
            return ModRecipeSerializers.WORKBENCH.get();
        }

        @Override
        public JsonObject getAdvancementJson()
        {
            return new JsonObject();
        }

        @Override
        public ResourceLocation getAdvancementID()
        {
            return new ResourceLocation(Reference.MOD_ID, "");
        }
    }
}
