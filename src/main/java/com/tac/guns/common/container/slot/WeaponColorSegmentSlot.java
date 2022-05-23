package com.tac.guns.common.container.slot;

import com.tac.guns.common.Gun;
import com.tac.guns.common.container.AttachmentContainer;
import com.tac.guns.common.container.ColorBenchContainer;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.IWeaponColorable;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class WeaponColorSegmentSlot extends Slot
{
    private ColorBenchContainer container;
    private ItemStack weapon;
    private PlayerEntity player;

    // Segment handling with these slots will be based on index
    public WeaponColorSegmentSlot(ColorBenchContainer container, IInventory weaponInventory, ItemStack weapon, PlayerEntity player, int index, int x, int y)
    {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.weapon = weapon;
        this.player = player;
    }

    @Override
    public boolean isEnabled()
    {
        if(this.weapon.getItem() instanceof GunItem)
            return true;
        else
            return false;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if(this.weapon.getItem() instanceof GunItem)
            return stack.getItem() instanceof DyeItem;
        else
            return false;
    }

/*    @Override
    public void onSlotChanged()
    {
        if(this.container.get isLoaded())
        {
            this.player.world.playSound(null, this.player.getPosX(), this.player.getPosY() + 1.0, this.player.getPosZ(), ModSounds.UI_WEAPON_ATTACH.get(), SoundCategory.PLAYERS, 0.5F, this.getHasStack() ? 1.0F : 0.75F);
        }
    }*/
    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }
}
