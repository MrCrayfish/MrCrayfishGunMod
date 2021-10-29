package com.tac.guns.item.TransitionalTypes;

import com.tac.guns.init.ModEnchantments;
import com.tac.guns.item.IColored;
import com.tac.guns.util.Process;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class AutomaticTimelessGunItem extends TimelessGunItem implements IColored {
	
	private final ForgeConfigSpec.IntValue TRIG_MAX;
	
	public AutomaticTimelessGunItem(Process<Item.Properties> properties, ForgeConfigSpec.IntValue TRIG_MAX) {
		super(properties);
		this.TRIG_MAX = TRIG_MAX;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		Map<Enchantment, Integer> x = EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTagList());
		if (TRIG_MAX.get() == 0) {
			x.remove(ModEnchantments.TRIGGER_FINGER.get());
		} else if (EnchantmentHelper.getMaxEnchantmentLevel(ModEnchantments.TRIGGER_FINGER.get(), (LivingEntity) entityIn) > TRIG_MAX.get()) {
			x.remove(ModEnchantments.TRIGGER_FINGER.get());
			x.put(ModEnchantments.TRIGGER_FINGER.get(), TRIG_MAX.get());
		}
		EnchantmentHelper.setEnchantments(x, stack);
	}
}