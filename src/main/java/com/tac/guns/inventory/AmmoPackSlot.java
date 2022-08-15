package com.tac.guns.inventory;

import com.tac.guns.item.AmmoPackItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AmmoPackSlot extends SlotItemHandler {

    public AmmoPackSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof AmmoPackItem;
    }
}
