package com.mrcrayfish.guns.init;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemPart;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ModGuns
{
	public static final List<ItemGun> GUNS = new ArrayList<>();

	public static Item parts;
	public static Item ammo;

	public static void init()
	{
		Reader reader = new InputStreamReader(ModGuns.class.getResourceAsStream("/assets/cgm/guns.json"));
		Gson gson = new Gson();
		ArrayList<Gun> guns = gson.fromJson(reader, new TypeToken<ArrayList<Gun>>(){}.getType());
		for(Gun gun : guns)
		{
			GUNS.add(new ItemGun(gun));
		}

		parts = new ItemPart().setCreativeTab(MrCrayfishGunMod.GUN_TAB);
		ammo = new ItemAmmo();
	}

	public static void register()
	{
		for(Item item : GUNS)
		{
			GameRegistry.register(item);
		}
		GameRegistry.register(parts);
		GameRegistry.register(ammo);
	}

	public static void registerRenders()
	{
		for(Item item : GUNS)
		{
			registerRender(item);
		}
		registerMeta();
	}

	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}

	private static void registerMeta()
	{
		for(int i = 0; i < ItemPart.PARTS.length; i++)
		{
			ModelLoader.setCustomModelResourceLocation(ModGuns.parts, i, new ModelResourceLocation(Reference.MOD_ID + ":" + "part_" + ItemPart.PARTS[i], "inventory"));
		}

		for(int i = 0; i < ItemAmmo.Type.values().length; i++)
        {
            ModelLoader.setCustomModelResourceLocation(ModGuns.ammo, i, new ModelResourceLocation(Reference.MOD_ID + ":" + "ammo_" + ItemAmmo.Type.values()[i].name, "inventory"));
        }
	}
}
