package com.tac.guns.inventory.gear.armor;

import com.tac.guns.common.Gun;
import com.tac.guns.init.ModContainers;
import com.tac.guns.inventory.gear.GearSlotsHandler;
import com.tac.guns.inventory.gear.InventoryListener;
import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import com.tac.guns.network.PacketHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;

public class ArmorRigContainer extends Container {

    private ItemStack item;
    private int numRows = 2;

    public ArmorRigContainer(int windowId, PlayerInventory inv, ItemStack item) {
        super(ModContainers.ARMOR_TEST.get(), windowId);
        this.item = item;

        RigSlotsHandler itemHandler = (RigSlotsHandler) this.item.getCapability(InventoryListener.RIG_HANDLER_CAPABILITY).resolve().get();
        int maxSlots = ((ArmorRigItem)inv.player.getHeldItemMainhand().getItem()).getSlots();
        int slots = maxSlots;
        int i = (this.numRows - 4) * 18;
        this.numRows = maxSlots % 9 > 0 ? maxSlots / 9 + 1 : maxSlots / 9;
        //RigSlotsHandler itemHandler = new RigSlotsHandler(maxSlots);

        for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                if(slots > 0) {
                    this.addSlot(new AmmoSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
                    slots--;
                }
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

    public ArmorRigContainer(int windowId, PlayerInventory inv) {
        super(ModContainers.ARMOR_TEST.get(), windowId);
        this.item = item;
        int i = (this.numRows - 4) * 18;

        ItemStackHandler itemHandler = new ItemStackHandler(18);
        int maxSlots = ((ArmorRigItem)inv.player.getHeldItemMainhand().getItem()).getSlots();
        int slots = maxSlots;
        for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                if(slots > 0) {
                    this.addSlot(new AmmoSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
                    slots--;
                }
            }
        }
        /*for(int j = 0; j < this.numRows; ++j) {
            for(int k = 0; k < 9; ++k) {
                this.addSlot(new AmmoSlot(itemHandler, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }*/

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
    private static boolean isAmmo(ItemStack stack, ResourceLocation id)
    {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }
    public int getNumRows() {
        return numRows;
    }


}
