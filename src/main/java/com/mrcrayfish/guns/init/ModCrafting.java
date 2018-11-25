package com.mrcrayfish.guns.init;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.gui.DisplayProperty;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.recipe.RecipeAttachment;
import com.mrcrayfish.guns.recipe.RecipeColorItem;
import com.mrcrayfish.guns.util.ItemStackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ModCrafting
{
	private static ImmutableMap<ItemStack, List<ItemStack>> recipeMaterials;

	public static void register()
	{
		RegistrationHandler.Recipes.add(new RecipeAttachment());
		RegistrationHandler.Recipes.add(new RecipeColorItem());

		ImmutableMap.Builder<ItemStack, List<ItemStack>> mapBuilder = ImmutableMap.builder();
		registerGunRecipe(mapBuilder, "handgun",
				new ItemStack(Items.IRON_INGOT, 14));
		registerGunRecipe(mapBuilder, "shotgun",
				new ItemStack(Items.IRON_INGOT, 24));
		registerGunRecipe(mapBuilder, "rifle",
				new ItemStack(Items.IRON_INGOT, 28));
		registerGunRecipe(mapBuilder, "grenade_launcher",
				new ItemStack(Items.IRON_INGOT, 32));
		registerGunRecipe(mapBuilder, "bazooka",
				new ItemStack(Items.IRON_INGOT, 44),
				new ItemStack(Items.REDSTONE, 4),
				new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		registerGunRecipe(mapBuilder, "chain_gun",
				new ItemStack(Items.IRON_INGOT, 38));
		registerGunRecipe(mapBuilder, "assault_rifle",
				new ItemStack(Items.IRON_INGOT, 28));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 8, ItemAmmo.Type.BASIC.ordinal()),
				new ItemStack(Items.GUNPOWDER, 1),
				new ItemStack(Items.IRON_NUGGET, 8));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 2, ItemAmmo.Type.ADVANCED.ordinal()),
				new ItemStack(Items.GUNPOWDER, 1),
				new ItemStack(Items.IRON_NUGGET, 4));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 4, ItemAmmo.Type.SHELL.ordinal()),
				new ItemStack(Items.GUNPOWDER, 1),
				new ItemStack(Items.GOLD_NUGGET, 2),
				new ItemStack(Items.IRON_NUGGET, 4));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE.ordinal()),
				new ItemStack(Items.GUNPOWDER, 4),
				new ItemStack(Items.IRON_INGOT, 2));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.MISSILE.ordinal()),
				new ItemStack(Items.GUNPOWDER, 8),
				new ItemStack(Items.IRON_INGOT, 4));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.AMMO, 1, ItemAmmo.Type.GRENADE_STUN.ordinal()),
				new ItemStack(Items.GLOWSTONE_DUST, 4),
				new ItemStack(Items.GUNPOWDER, 2),
				new ItemStack(Items.IRON_INGOT, 2));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.SMALL.ordinal()),
				new ItemStack(Items.IRON_INGOT, 4),
				new ItemStack(Blocks.GLASS_PANE, 1),
				new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.MEDIUM.ordinal()),
				new ItemStack(Items.IRON_INGOT, 6),
				new ItemStack(Blocks.GLASS_PANE, 2),
				new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.SCOPES, 1, ItemScope.Type.LONG.ordinal()),
				new ItemStack(Items.IRON_INGOT, 8),
				new ItemStack(Blocks.GLASS_PANE, 3),
				new ItemStack(Items.DYE, 2, EnumDyeColor.BLACK.getDyeDamage()));
		registerRecipe(mapBuilder, new ItemStack(ModGuns.SILENCER),
				new ItemStack(Items.IRON_INGOT, 12));
		recipeMaterials = mapBuilder.build();
	}

	private static void registerGunRecipe(ImmutableMap.Builder<ItemStack, List<ItemStack>> mapBuilder, String id, ItemStack ... materials)
	{
		ItemGun gun = ModGuns.getGun(id);
		if(gun != null)
		{
			mapBuilder.put(new ItemStack(gun), ImmutableList.copyOf(materials));
		}
	}

	private static void registerRecipe(ImmutableMap.Builder<ItemStack, List<ItemStack>> mapBuilder, ItemStack stack, ItemStack ... materials)
	{
		mapBuilder.put(stack, ImmutableList.copyOf(materials));
	}

	public static ImmutableMap<ItemStack, List<ItemStack>> getRecipeMaterials()
	{
		return recipeMaterials;
	}

	@Nullable
	public static List<ItemStack> getMaterialsForStack(ItemStack find)
	{
		for(ItemStack stack : recipeMaterials.keySet())
		{
			if(ItemStackHelper.areItemStackEqualIgnoreTag(stack, find))
			{
				return recipeMaterials.get(stack);
			}
		}
		return null;
	}
}
