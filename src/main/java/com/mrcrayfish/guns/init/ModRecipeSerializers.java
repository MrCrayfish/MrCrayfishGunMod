package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.recipe.AttachmentRecipe;
import com.mrcrayfish.guns.recipe.DyeItemRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Author: MrCrayfish
 */
public class ModRecipeSerializers
{
    public static final DeferredRegister<IRecipeSerializer<?>> REGISTER = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Reference.MOD_ID);

    public static final RegistryObject<SpecialRecipeSerializer<AttachmentRecipe>> ATTACHMENT = REGISTER.register("attachment", () -> new SpecialRecipeSerializer<>(AttachmentRecipe::new));
    public static final RegistryObject<SpecialRecipeSerializer<DyeItemRecipe>> DYE_ITEM = REGISTER.register("dye_item", () -> new SpecialRecipeSerializer<>(DyeItemRecipe::new));
}
