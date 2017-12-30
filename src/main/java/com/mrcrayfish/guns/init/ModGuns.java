package com.mrcrayfish.guns.init;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.item.ItemAmmo;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemPart;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModGuns
{
	static final List<ItemGun> GUNS = new ArrayList<>();

	public static final Item PARTS;
	public static final Item AMMO;
	public static final Item SCOPES;

	static
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

		PARTS = new ItemPart();
		AMMO = new ItemAmmo();
		SCOPES = new ItemScope();
	}

	public static void register()
	{
		for(Item item : GUNS)
		{
			register(item);
		}
		register(PARTS);
		register(AMMO);
		register(SCOPES);
	}

	private static void register(Item item)
	{
		RegistrationHandler.Items.add(item);
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
