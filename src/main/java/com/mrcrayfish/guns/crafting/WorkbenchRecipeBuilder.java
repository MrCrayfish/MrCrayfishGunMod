package com.mrcrayfish.guns.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.init.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

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
    private final List<ItemStack> materials;
    private final Advancement.Builder advancementBuilder;
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
        this.materials.add(new ItemStack(item, quantity));
        return this;
    }

    /**
     * Adds a criterion needed to unlock the recipe.
     */
    public WorkbenchRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn)
    {
        this.advancementBuilder.withCriterion(name, criterionIn);
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
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        if(new ResourceLocation(Reference.MOD_ID, save).equals(resourcelocation))
        {
            throw new IllegalStateException("Workbench Recipe " + save + " should remove its 'save' argument");
        }
        else
        {
            this.build(consumerIn, new ResourceLocation(Reference.MOD_ID, save));
        }
    }

    /**
     * Builds this recipe into an {@link IFinishedRecipe}.
     */
    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id)
    {
        this.validate(id);
        this.advancementBuilder.withParentId(new ResourceLocation("recipes/root")).withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id)).withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
        consumerIn.accept(new WorkbenchRecipeBuilder.Result(id, this.result, this.count, this.group == null ? "" : this.group, this.materials, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getGroup().getPath() + "/" + id.getPath())));
    }

    /**
     * Makes sure that this recipe is valid and obtainable.
     */
    private void validate(ResourceLocation id)
    {
        if(this.advancementBuilder.getCriteria().isEmpty())
        {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements IFinishedRecipe
    {
        private final ResourceLocation id;
        private final Item item;
        private final int count;
        private final String group;
        private final List<ItemStack> materials;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, IItemProvider item, int count, String group, List<ItemStack> materials, Advancement.Builder advancement, ResourceLocation advancementId)
        {
            this.id = id;
            this.item = item.asItem();
            this.count = count;
            this.group = group;
            this.materials = materials;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json)
        {
            if(!this.group.isEmpty())
                json.addProperty("group", this.group);

            JsonArray input = new JsonArray();
            for(ItemStack material : this.materials)
            {
                JsonObject resultObject = new JsonObject();
                resultObject.addProperty("item", Registry.ITEM.getKey(material.getItem()).toString());
                if(material.getCount() > 1)
                    resultObject.addProperty("count", material.getCount());
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
            return this.advancement.serialize();
        }

        @Override
        public ResourceLocation getAdvancementID()
        {
            return advancementId;
        }
    }
}
