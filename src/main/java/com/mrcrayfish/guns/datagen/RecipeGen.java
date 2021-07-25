package com.mrcrayfish.guns.datagen;

import com.google.gson.JsonObject;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.crafting.WorkbenchRecipeBuilder;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModRecipeSerializers;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider
{
    public RecipeGen(DataGenerator generator)
    {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
    {
        // Dye Item
        consumer.accept(new IFinishedRecipe()
        {
            @Override
            public void serializeRecipeData(JsonObject json)
            {
            }

            @Override
            public IRecipeSerializer<?> getType()
            {
                return ModRecipeSerializers.DYE_ITEM.get();
            }

            @Override
            public ResourceLocation getId()
            {
                return new ResourceLocation(Reference.MOD_ID, "dye_item");
            }

            @Override
            @Nullable
            public JsonObject serializeAdvancement()
            {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementId()
            {
                return new ResourceLocation("");
            }
        });

        ShapedRecipeBuilder.shaped(ModBlocks.WORKBENCH.get())
                .pattern("CCC")
                .pattern("III")
                .pattern("I I")
                .define('C', Blocks.LIGHT_GRAY_CONCRETE)
                .define('I', Tags.Items.INGOTS_IRON)
                .unlockedBy("has_concrete", has(Blocks.LIGHT_GRAY_CONCRETE))
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .save(consumer);

        // Guns
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.PISTOL.get())
                .addIngredient(Items.IRON_INGOT, 14)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHOTGUN.get())
                .addIngredient(Items.IRON_INGOT, 24)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 24)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.GRENADE_LAUNCHER.get())
                .addIngredient(Items.IRON_INGOT, 32)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.BAZOOKA.get())
                .addIngredient(Items.IRON_INGOT, 44)
                .addIngredient(Items.REDSTONE, 4)
                .addIngredient(Items.RED_DYE)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_redstone", has(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MINI_GUN.get())
                .addIngredient(Items.IRON_INGOT, 38)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.ASSAULT_RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 28)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MACHINE_PISTOL.get())
                .addIngredient(Items.IRON_INGOT, 20)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.HEAVY_RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 36)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);

        // Ammo
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.BASIC_BULLET.get(), 32) // Why is the basic bullet the same price as the advanced?
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.ADVANCED_AMMO.get(), 16)
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHELL.get(), 24)
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GOLD_NUGGET)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", has(Items.IRON_NUGGET))
                .addCriterion("has_gold_nugget", has(Items.GOLD_NUGGET))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MISSILE.get())
                .addIngredient(Items.IRON_INGOT, 2)
                .addIngredient(Items.GUNPOWDER, 4)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.GRENADE.get(), 2)
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.GUNPOWDER, 4)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.STUN_GRENADE.get(), 2)
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.GUNPOWDER, 2)
                .addIngredient(Items.GLOWSTONE_DUST, 4)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", has(Items.GUNPOWDER))
                .addCriterion("has_glowstone", has(Items.GLOWSTONE_DUST))
                .build(consumer);

        // Scope Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHORT_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 2)
                .addIngredient(Items.GLASS_PANE)
                .addIngredient(Items.REDSTONE, 2)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_redstone", has(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MEDIUM_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.GLASS_PANE)
                .addIngredient(Items.REDSTONE, 4)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .addCriterion("has_redstone", has(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LONG_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 6)
                .addIngredient(Items.GLASS_PANE, 2)
                .addIngredient(Items.BLACK_DYE)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);

        // Barrel Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SILENCER.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.SPONGE)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);

        // Stock Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LIGHT_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 6)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.TACTICAL_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 8)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.WEIGHTED_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 12)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);

        // Under Barrel Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LIGHT_GRIP.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SPECIALISED_GRIP.get())
                .addIngredient(Items.IRON_INGOT, 8)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", has(Items.IRON_INGOT))
                .build(consumer);
    }
}