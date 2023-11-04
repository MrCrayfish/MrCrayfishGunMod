package com.mrcrayfish.guns.common.container.slot;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.container.AttachmentContainer;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Author: MrCrayfish
 */
public class AttachmentSlot extends Slot
{
    private AttachmentContainer container;
    private ItemStack weapon;
    private IAttachment.Type type;
    private Player player;

    public AttachmentSlot(AttachmentContainer container, Container weaponInventory, ItemStack weapon, IAttachment.Type type, Player player, int index, int x, int y)
    {
        super(weaponInventory, index, x, y);
        this.container = container;
        this.weapon = weapon;
        this.type = type;
        this.player = player;
    }

    public IAttachment.Type getType()
    {
        return this.type;
    }

    @Override
    public boolean isActive()
    {
        if(!(this.weapon.getItem() instanceof GunItem))
        {
            return false;
        }
        GunItem item = (GunItem) this.weapon.getItem();
        Gun modifiedGun = item.getModifiedGun(this.weapon);
        return modifiedGun.canAttachType(this.type);
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        if(!(this.weapon.getItem() instanceof GunItem))
        {
            return false;
        }
        GunItem item = (GunItem) this.weapon.getItem();
        Gun modifiedGun = item.getModifiedGun(this.weapon);
        if (!(stack.getItem() instanceof IAttachment attachment))
        {
            return false;
        }
        return attachment.getType() == this.type && modifiedGun.canAttachType(this.type) && attachment.canAttachTo(this.weapon);
    }

    @Override
    public void setChanged()
    {
        if(this.container.isLoaded())
        {
            this.player.level().playSound(null, this.player.getX(), this.player.getY() + 1.0, this.player.getZ(), ModSounds.UI_WEAPON_ATTACH.get(), SoundSource.PLAYERS, 0.5F, this.hasItem() ? 1.0F : 0.75F);
        }
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public boolean mayPickup(Player player)
    {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.mayPickup(player);
    }
}
