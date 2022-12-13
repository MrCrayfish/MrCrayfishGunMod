package com.tac.guns.inventory.gear;

import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;


// Will be one for backpacks too
public class ArmorRigSlot extends SlotItemHandler {

    public ArmorRigSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof ArmorRigItem;
    }
}
