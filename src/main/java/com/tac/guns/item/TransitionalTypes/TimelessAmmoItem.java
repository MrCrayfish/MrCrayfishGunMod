package com.tac.guns.item.TransitionalTypes;

import com.tac.guns.GunMod;
import com.tac.guns.item.AmmoItem;
import com.tac.guns.util.Process;
import net.minecraft.item.Item;


public class TimelessAmmoItem extends AmmoItem {
	public TimelessAmmoItem() {
		this(properties -> properties);
	}
	
	public TimelessAmmoItem(Process<Item.Properties> properties) {
		super(properties.process(new Item.Properties().maxStackSize(64).group(GunMod.AMMO)));
	}
}
