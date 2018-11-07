package com.mrcrayfish.guns.init;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.item.*;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ModGuns
{
	static final List<ItemGun> GUNS = new ArrayList<>();

	public static final Item PARTS;
	public static final Item AMMO;
	public static final Item SCOPES;
	public static final Item SILENCER;

	static
	{
		GunConfig.ID_TO_GUN.forEach((s, gun) -> GUNS.add(new ItemGun(gun)));
		PARTS = new ItemPart();
		AMMO = new ItemAmmo();
		SCOPES = new ItemScope();
		SILENCER = new ItemAttachment("silencer", IAttachment.Type.BARREL);
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
		register(SILENCER);
	}

	private static void register(Item item)
	{
		RegistrationHandler.Items.add(item);
	}
}
