package com.mrcrayfish.guns.common.container;

import com.mrcrayfish.guns.common.container.slot.AttachmentSlot;
import com.mrcrayfish.guns.init.ModContainers;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/**
 * Author: MrCrayfish
 */
public class AttachmentContainer extends Container
{
    private ItemStack weapon;
    private IInventory playerInventory;
    private IInventory weaponInventory = new Inventory(2)
    {
        @Override
        public void markDirty()
        {
            super.markDirty();
            AttachmentContainer.this.onCraftMatrixChanged(this);
        }
    };
    private boolean loaded = false;

    public AttachmentContainer(int windowId, PlayerInventory playerInventory, ItemStack stack)
    {
        this(windowId, playerInventory);
        ItemStack scopeStack = Gun.getAttachment(IAttachment.Type.SCOPE, stack);
        ItemStack barrelStack = Gun.getAttachment(IAttachment.Type.BARREL, stack);
        this.weaponInventory.setInventorySlotContents(0, scopeStack);
        this.weaponInventory.setInventorySlotContents(1, barrelStack);
        this.loaded = true;
    }

    public AttachmentContainer(int windowId, PlayerInventory playerInventory)
    {
        super(ModContainers.ATTACHMENTS.get(), windowId);
        this.weapon = playerInventory.getCurrentItem();
        this.playerInventory = playerInventory;

        this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.SCOPE, playerInventory.player, 0, 8, 17));
        this.addSlot(new AttachmentSlot(this, this.weaponInventory, this.weapon, IAttachment.Type.BARREL, playerInventory.player, 1, 8, 35));

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

    public boolean isLoaded()
    {
        return this.loaded;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn)
    {
        CompoundNBT attachments = new CompoundNBT();

        ItemStack scopeStack = this.getSlot(0).getStack();
        if(scopeStack.getItem() instanceof IAttachment && ((IAttachment) scopeStack.getItem()).getType() == IAttachment.Type.SCOPE)
        {
            attachments.put(((IAttachment) scopeStack.getItem()).getType().getId(), scopeStack.write(new CompoundNBT()));
        }

        ItemStack barrelStack = this.getSlot(1).getStack();
        if(barrelStack.getItem() instanceof IAttachment && ((IAttachment) barrelStack.getItem()).getType() == IAttachment.Type.BARREL)
        {
            attachments.put(((IAttachment) barrelStack.getItem()).getType().getId(), barrelStack.write(new CompoundNBT()));
        }

        CompoundNBT tag = ItemStackUtil.createTagCompound(this.weapon);
        tag.put("Attachments", attachments);
        super.detectAndSendChanges();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack copyStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack())
        {
            ItemStack slotStack = slot.getStack();
            copyStack = slotStack.copy();
            if(index < this.weaponInventory.getSizeInventory())
            {
                if(!this.mergeItemStack(slotStack, this.weaponInventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(slotStack, 0, this.weaponInventory.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return copyStack;
    }

    public IInventory getPlayerInventory()
    {
        return this.playerInventory;
    }

    public IInventory getWeaponInventory()
    {
        return this.weaponInventory;
    }
}
