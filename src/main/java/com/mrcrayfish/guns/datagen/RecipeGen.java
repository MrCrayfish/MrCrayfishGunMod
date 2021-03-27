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
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        // Dye Item
        consumer.accept(new IFinishedRecipe()
        {
            @Override
            public void serialize(JsonObject json)
            {
            }

            @Override
            public IRecipeSerializer<?> getSerializer()
            {
                return ModRecipeSerializers.DYE_ITEM.get();
            }

            @Override
            public ResourceLocation getID()
            {
                return new ResourceLocation(Reference.MOD_ID, "dye_item");
            }

            @Override
            @Nullable
            public JsonObject getAdvancementJson()
            {
                return null;
            }

            @Override
            public ResourceLocation getAdvancementID()
            {
                return new ResourceLocation("");
            }
        });

        ShapedRecipeBuilder.shapedRecipe(ModBlocks.WORKBENCH.get())
                .patternLine("CCC")
                .patternLine("III")
                .patternLine("I I")
                .key('C', Blocks.LIGHT_GRAY_CONCRETE)
                .key('I', Tags.Items.INGOTS_IRON)
                .addCriterion("has_concrete", hasItem(Blocks.LIGHT_GRAY_CONCRETE))
                .addCriterion("has_iron", hasItem(Tags.Items.INGOTS_IRON))
                .build(consumer);

        // Guns
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.PISTOL.get())
                .addIngredient(Items.IRON_INGOT, 14)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHOTGUN.get())
                .addIngredient(Items.IRON_INGOT, 24)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 24)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.GRENADE_LAUNCHER.get())
                .addIngredient(Items.IRON_INGOT, 32)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.BAZOOKA.get())
                .addIngredient(Items.IRON_INGOT, 44)
                .addIngredient(Items.REDSTONE, 4)
                .addIngredient(Items.RED_DYE)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_redstone", hasItem(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MINI_GUN.get())
                .addIngredient(Items.IRON_INGOT, 38)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.ASSAULT_RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 28)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MACHINE_PISTOL.get())
                .addIngredient(Items.IRON_INGOT, 20)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.HEAVY_RIFLE.get())
                .addIngredient(Items.IRON_INGOT, 36)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);

        // Ammo
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.BASIC_BULLET.get(), 32) // Why is the basic bullet the same price as the advanced?
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", hasItem(Items.IRON_NUGGET))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.ADVANCED_AMMO.get(), 16)
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", hasItem(Items.IRON_NUGGET))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHELL.get(), 24)
                .addIngredient(Items.IRON_NUGGET, 4)
                .addIngredient(Items.GOLD_NUGGET)
                .addIngredient(Items.GUNPOWDER)
                .addCriterion("has_iron_nugget", hasItem(Items.IRON_NUGGET))
                .addCriterion("has_gold_nugget", hasItem(Items.GOLD_NUGGET))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MISSILE.get())
                .addIngredient(Items.IRON_INGOT, 2)
                .addIngredient(Items.GUNPOWDER, 4)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.GRENADE.get(), 2)
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.GUNPOWDER, 4)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.STUN_GRENADE.get(), 2)
                .addIngredient(Items.IRON_INGOT)
                .addIngredient(Items.GUNPOWDER, 2)
                .addIngredient(Items.GLOWSTONE_DUST, 4)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_gunpowder", hasItem(Items.GUNPOWDER))
                .addCriterion("has_glowstone", hasItem(Items.GLOWSTONE_DUST))
                .build(consumer);

        // Scope Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SHORT_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 2)
                .addIngredient(Items.GLASS_PANE)
                .addIngredient(Items.REDSTONE, 2)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_redstone", hasItem(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.MEDIUM_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.GLASS_PANE)
                .addIngredient(Items.REDSTONE, 4)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .addCriterion("has_redstone", hasItem(Items.REDSTONE))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LONG_SCOPE.get())
                .addIngredient(Items.IRON_INGOT, 6)
                .addIngredient(Items.GLASS_PANE, 2)
                .addIngredient(Items.BLACK_DYE)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);

        // Barrel Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SILENCER.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.SPONGE)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);

        // Stock Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LIGHT_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 6)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.TACTICAL_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 8)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.WEIGHTED_STOCK.get())
                .addIngredient(Items.IRON_INGOT, 12)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);

        // Under Barrel Attachments
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.LIGHT_GRIP.get())
                .addIngredient(Items.IRON_INGOT, 4)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
        WorkbenchRecipeBuilder.workbenchRecipe(ModItems.SPECIALISED_GRIP.get())
                .addIngredient(Items.IRON_INGOT, 8)
                .addIngredient(Items.GRAY_WOOL)
                .addCriterion("has_iron_ingot", hasItem(Items.IRON_INGOT))
                .build(consumer);
    }
}