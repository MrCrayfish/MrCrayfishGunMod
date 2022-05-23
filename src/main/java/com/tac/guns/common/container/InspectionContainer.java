package com.tac.guns.common.container;

import com.tac.guns.common.Gun;
import com.tac.guns.init.ModContainers;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class InspectionContainer extends Container
{
    private ItemStack weapon;
    private IInventory playerInventory;
    private IInventory weaponInventory = new Inventory(IAttachment.Type.values().length)
    {
        @Override
        public void markDirty()
        {
            super.markDirty();
            InspectionContainer.this.onCraftMatrixChanged(this);
        }
    };
    private boolean loaded = false;

    public InspectionContainer(int windowId, PlayerInventory playerInventory, ItemStack stack)
    {
        this(windowId, playerInventory);
        ItemStack[] attachments = new ItemStack[IAttachment.Type.values().length];
        for(int i = 0; i < attachments.length; i++)
        {
            attachments[i] = Gun.getAttachment(IAttachment.Type.values()[i], stack);
        }
        for(int i = 0; i < attachments.length; i++)
        {
            this.weaponInventory.setInventorySlotContents(i, attachments[i]);
        }
        this.loaded = true;
    }

    public InspectionContainer(int windowId, PlayerInventory playerInventory)
    {
        super(ModContainers.INSPECTION.get(), windowId);
        /*this.weapon = playerInventory.getSelected();
        this.playerInventory = playerInventory;

        for(int i = 0; i < IAttachment.Type.values().length; i++)
        {
            this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.values()[i], playerInventory.player, i, 8, 17 + i * 18));
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
            if(i == playerInventory.selected)
            {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160)
                {
                    @Override
                    public boolean mayPickup(PlayerEntity playerIn)
                    {
                        return false;
                    }
                });
            }
            else
            {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 160));
            }
        }*/
    }

    public boolean isLoaded()
    {
        return this.loaded;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    /*@Override
    public void slotsChanged(IInventory inventoryIn)
    {
        CompoundNBT attachments = new CompoundNBT();

        for(int i = 0; i < this.getWeaponInventory().getContainerSize(); i++)
        {
            ItemStack attachment = this.getSlot(i).getItem();
            if(attachment.getItem() instanceof IAttachment)
            {
                attachments.put(((IAttachment) attachment.getItem()).getType().getTagKey(), attachment.save(new CompoundNBT()));
            }
        }

        CompoundNBT tag = this.weapon.getOrCreateTag();
        tag.put("Attachments", attachments);
        super.broadcastChanges();
    }*/

    /*@Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index)
    {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            copyStack = slotStack.copy();
            if(index < this.weaponInventory.getContainerSize())
            {
                if(!this.moveItemStackTo(slotStack, this.weaponInventory.getContainerSize(), this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(slotStack, 0, this.weaponInventory.getContainerSize(), false))
            {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }
        }

        return copyStack;
    }
*/
    public IInventory getPlayerInventory()
    {
        return this.playerInventory;
    }

    public IInventory getWeaponInventory()
    {
        return this.weaponInventory;
    }
}
