package com.tac.guns.inventory.gear.backpack;

import com.tac.guns.init.ModContainers;
import com.tac.guns.inventory.gear.GearSlotsHandler;
import com.tac.guns.inventory.gear.armor.AmmoPackCapabilityProvider;
import com.tac.guns.inventory.gear.armor.AmmoSlot;
import com.tac.guns.item.ArmorRigItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BackpackContainer extends Container {

    private ItemStack item;
    private int numRows = 2;

    public BackpackContainer(int windowId, PlayerInventory inv, ItemStack item) {
        super(ModContainers.AMMOPACK.get(), windowId);
        this.item = item;
        GearSlotsHandler itemHandler = (GearSlotsHandler)this.item.getCapability(AmmoPackCapabilityProvider.capability).resolve().get();
        int i = (this.numRows - 4) * 18;

        for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new AmmoSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inv, i1, 8 + i1 * 18, 161 + i));
        }

        //this.setAll(itemHandler.getStacks());
    }

    public BackpackContainer(int windowId, PlayerInventory inv) {
        super(ModContainers.AMMOPACK.get(), windowId);
        this.item = item;
        int i = (this.numRows - 4) * 18;

        ItemStackHandler itemHandler = new ItemStackHandler(18);
        for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new AmmoSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(inv, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(inv, i1, 8 + i1 * 18, 161 + i));
        }
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if(slotId <= 0) return super.slotClick(slotId, dragType, clickTypeIn, player);
        Slot slot = this.inventorySlots.get(slotId);
        if(slot.getHasStack()) {
            if(slot.getStack().getItem() instanceof ArmorRigItem) return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < this.numRows * 9) {
                if (!this.mergeItemStack(itemstack1, this.numRows * 9, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.numRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public int getNumRows() {
        return numRows;
    }
}
