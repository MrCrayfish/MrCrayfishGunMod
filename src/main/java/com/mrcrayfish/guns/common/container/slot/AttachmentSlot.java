package com.mrcrayfish.guns.common.container.slot;

import com.mrcrayfish.guns.client.event.SoundEvents;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.AttachmentItem;
import com.mrcrayfish.guns.item.IAttachment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Author: MrCrayfish
 */
public class AttachmentSlot extends Slot
{
    private AttachmentContainer container;
    private IAttachment.Type type;
    private PlayerEntity player;

    public AttachmentSlot(AttachmentContainer container, IInventory weaponInventory, IAttachment.Type type, PlayerEntity player, int index, int x, int y)
    {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.type = type;
        this.player = player;
    }

    public boolean isItemValid(ItemStack stack)
    {
        return stack.getItem() instanceof AttachmentItem && ((AttachmentItem) stack.getItem()).getType() == this.type;
    }

    @Override
    public void onSlotChanged()
    {
        if(this.container.isLoaded() && this.getHasStack())
        {
            this.player.world.playSound(null, this.player.getPosX(), this.player.getPosY() + 1.0, this.player.getPosZ(), ModSounds.UI_WEAPON_ATTACH.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
        }
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }
}
