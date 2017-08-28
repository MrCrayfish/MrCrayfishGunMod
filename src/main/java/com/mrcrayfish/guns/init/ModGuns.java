package com.mrcrayfish.guns.init;

import com.google.gson.*;
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

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
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
		JsonParser parser = new JsonParser();
		JsonArray elements = parser.parse(reader).getAsJsonArray();

		try
		{
			Gson gson = new Gson();
			for(JsonElement element : elements)
			{
				Gun gun = gson.fromJson(element, new TypeToken<Gun>(){}.getType());
				if(!validateFields(gun))
				{
					if(gun.id != null)
					{
						throw new NullPointerException("The gun '" + gun.id + "' is missing required attributes");
					}
					else
					{
						throw new NullPointerException("Invalid gun entry");
					}
				}

				GUNS.add(new ItemGun(gun));
			}
		}
		catch(IllegalAccessException e)
		{
			e.printStackTrace();
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
			registerGun(item);
		}
		registerMeta();
	}

	private static void registerGun(Item item)
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

	private static <T> boolean validateFields(@Nonnull T t) throws IllegalAccessException
	{
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields)
		{
			if(field.getDeclaredAnnotation(Gun.Optional.class) == null)
			{
				if(field.get(t) == null)
				{
					return false;
				}

				if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
				{
					return validateFields(field.get(t));
				}
			}
		}
		return true;
	}
}
