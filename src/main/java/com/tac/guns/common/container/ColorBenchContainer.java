package com.tac.guns.common.container;

import com.tac.guns.common.Gun;
import com.tac.guns.common.container.slot.AttachmentSlot;
import com.tac.guns.common.container.slot.WeaponColorSegmentSlot;
import com.tac.guns.init.ModContainers;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.IWeaponColorable;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ColorBenchContainer extends Container
{
    private ItemStack weapon;
    private IInventory playerInventory;
    private IInventory weaponInventory = new Inventory(IWeaponColorable.WeaponColorSegment.values().length)
    {
        @Override
        public void markDirty()
        {
            super.markDirty();
            ColorBenchContainer.this.onCraftMatrixChanged(this);
        }
    };
    public ColorBenchContainer(int windowId, PlayerInventory playerInventory)
    {
        super(ModContainers.COLOR_BENCH.get(), windowId);
        this.weapon = playerInventory.getCurrentItem();
        this.playerInventory = playerInventory;

        if(this.weapon.getItem() instanceof GunItem)
        {
            for (int i = 0; i < IWeaponColorable.WeaponColorSegment.values().length; i++)
            {
                this.addSlot(new WeaponColorSegmentSlot(this, this.weaponInventory, this.weapon, playerInventory.player, i, (i * 18)-38, 6));//14- i * 18, 12));
            }
        }

        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }

        for(int i = 0; i < 9; i++)
        {
            if(i == playerInventory.currentItem)
            {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160)
                {
                    @Override
                    public boolean canTakeStack(PlayerEntity playerIn)
                    {
                        return false;
                    }
                });
            }
            else
            {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160));
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

/*    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        CompoundNBT attachments = new CompoundNBT();

        *//*if(this.weapon.getItem() instanceof ScopeItem)
        {
            for (int i = 0; i < this.getWeaponInventory().getSizeInventory()-4; i++) {
                ItemStack attachment = this.getSlot(i).getStack();
                if (attachment.getItem() instanceof DyeItem) {
                    attachments.put(currentStuff[i], attachment.write(new CompoundNBT()));
                }
            }

            *//**//*if (scopeReticleAttachment.getItem() instanceof DyeItem) {
                attachments.put(IAttachment.Type.SCOPE_RETICLE_COLOR.getTagKey(), scopeReticleAttachment.write(new CompoundNBT()));
            }
            if (scopeBodyAttachment.getItem() instanceof DyeItem) {
                attachments.put(IAttachment.Type.SCOPE_BODY_COLOR.getTagKey(), scopeBodyAttachment.write(new CompoundNBT()));
            }
            if (scopeGlassAttachment.getItem() instanceof DyeItem) {
                attachments.put(IAttachment.Type.SCOPE_GLASS_COLOR.getTagKey(), scopeGlassAttachment.write(new CompoundNBT()));
            }*//**//*
        }
        else*//*
            for (int i = 0; i < this.getWeaponInventory().getSizeInventory(); i++) {
                ItemStack attachment = this.getSlot(i).getStack();
                if (attachment.getItem() instanceof IAttachment) {
                    attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.write(new CompoundNBT()));
                }
                else if(attachment.getItem() instanceof DyeItem)
                {
                    if(i == 0)
                        attachments.put(IAttachment.Type.SCOPE_RETICLE_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                    if(i == 1)
                        attachments.put(IAttachment.Type.SCOPE_BODY_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                    if(i == 2)
                        attachments.put(IAttachment.Type.SCOPE_GLASS_COLOR.getTagKey(), attachment.write(new CompoundNBT()));
                }
            }

        CompoundNBT tag = this.weapon.getOrCreateTag();
        tag.put("Attachments", attachments);
        super.detectAndSendChanges();
    }*/

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

/*        if (this.weapon.getItem() instanceof ScopeItem)
        {
            if (slot != null && slot.getHasStack()) {
                ItemStack slotStack = slot.getStack();
                copyStack = slotStack.copy();

                if (index == 0) {
                    if (!this.mergeItemStack(slotStack, 0, 36, true)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (slotStack.getItem() instanceof DyeItem) {
                        if (!this.mergeItemStack(slotStack, 0, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 28) {
                        if (!this.mergeItemStack(slotStack, 28, 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index <= 36 && !this.mergeItemStack(slotStack, 0, 28, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                if (slotStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }

                if (slotStack.getCount() == copyStack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTake(playerIn, slotStack);
            }
        }
        else {*/
            if (slot != null && slot.getHasStack()) {
                ItemStack slotStack = slot.getStack();
                copyStack = slotStack.copy();
                if (index < this.weaponInventory.getSizeInventory()) {
                    if (!this.mergeItemStack(slotStack, this.weaponInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.mergeItemStack(slotStack, 0, this.weaponInventory.getSizeInventory(), false)) {
                    return ItemStack.EMPTY;
                }
                if (slotStack.isEmpty()) {
                    slot.putStack(ItemStack.EMPTY);
                } else {
                    slot.onSlotChanged();
                }
            }//}

        return copyStack;
    }

    public IInventory getPlayerInventory()
    {
        return this.playerInventory;
    }
}
