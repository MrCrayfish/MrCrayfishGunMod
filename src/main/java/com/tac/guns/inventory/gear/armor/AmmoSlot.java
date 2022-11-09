package com.tac.guns.inventory.gear.armor;

import com.tac.guns.item.IAmmo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AmmoSlot extends SlotItemHandler {

    public AmmoSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof IAmmo;
    }
}
