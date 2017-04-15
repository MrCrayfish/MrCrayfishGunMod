package com.mrcrayfish.guns.init;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModGuns 
{
	public static final List<ItemGun> GUNS = new ArrayList<ItemGun>();
	public static Item shotgun_ammo;
	public static Item grenade;
	
	public static void init()
	{
		Reader reader = new InputStreamReader(ModGuns.class.getResourceAsStream("/assets/cgm/guns.json"));
		Gson gson = new Gson();
		ArrayList<Gun> guns = gson.fromJson(reader, new TypeToken<ArrayList<Gun>>(){}.getType());
		for(Gun gun : guns)
		{
			GUNS.add(new ItemGun(gun));
		}
		shotgun_ammo = new Item().setUnlocalizedName("shotgun_ammo").setRegistryName("shotgun_ammo").setCreativeTab(MrCrayfishGunMod.GUN_TAB);
		grenade = new Item().setUnlocalizedName("grenade").setRegistryName("grenade").setCreativeTab(MrCrayfishGunMod.GUN_TAB);
	}
	
	public static void register()
	{
		for(Item item : GUNS) 
		{
			GameRegistry.register(item);
		}
		GameRegistry.register(shotgun_ammo);
		GameRegistry.register(grenade);
	}
	
	public static void registerRenders()
	{
		for(Item item : GUNS) 
		{
			registerRender(item);
		}
		registerRender(shotgun_ammo);
		registerRender(grenade);
	}
	
	private static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
	
	
}
