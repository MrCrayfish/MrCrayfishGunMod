package com.tac.guns.common.container.slot;

import com.tac.guns.common.Gun;
import com.tac.guns.common.container.AttachmentContainer;
import com.tac.guns.init.ModSounds;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.ScopeItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class AttachmentSlot extends Slot
{
    private AttachmentContainer container;
    private ItemStack weapon;
    private IAttachment.Type type;
    private PlayerEntity player;

    public AttachmentSlot(AttachmentContainer container, IInventory weaponInventory, ItemStack weapon, IAttachment.Type type, PlayerEntity player, int index, int x, int y)
    {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.weapon = weapon;
        this.type = type;
        this.player = player;
    }

    @Override
    public boolean isEnabled()
    {
        if(!(this.weapon.getItem() instanceof GunItem) && !(this.weapon.getItem() instanceof ScopeItem))
        {
            return false;
        }
        if(this.weapon.getItem() instanceof ScopeItem)
        {
            return true;
        }
        else
        {
            GunItem item = (GunItem) this.weapon.getItem();
            Gun modifiedGun = item.getModifiedGun(this.weapon);
            return modifiedGun.canAttachType(this.type);
        }
        /*GunItem item = (GunItem) this.weapon.getItem();
        Gun modifiedGun = item.getModifiedGun(this.weapon);
        return modifiedGun.canAttachType(this.type);*/
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if(!(this.weapon.getItem() instanceof GunItem) && !(this.weapon.getItem() instanceof ScopeItem))
        {
            return false;
        }
        if(this.weapon.getItem() instanceof ScopeItem)
        {
            return true;
        }
        else
        {
            GunItem item = (GunItem) this.weapon.getItem();
            Gun modifiedGun = item.getModifiedGun(this.weapon);
            return stack.getItem() instanceof IAttachment && ((IAttachment) stack.getItem()).getType() == this.type && modifiedGun.canAttachType(this.type);
        }
    }

    @Override
    public void onSlotChanged()
    {
        if(this.container.isLoaded())
        {
            this.player.world.playSound(null, this.player.getPosX(), this.player.getPosY() + 1.0, this.player.getPosZ(), ModSounds.UI_WEAPON_ATTACH.get(), SoundCategory.PLAYERS, 0.5F, this.getHasStack() ? 1.0F : 0.75F);
        }
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean canTakeStack(PlayerEntity player)
    {
        //ItemStack itemstack = this.getStack();
        return true;
    }
}
