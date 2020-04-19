package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

@Beta
public class GunItem extends ColoredItem
{
    private Gun gun;

    public GunItem(Item.Properties properties)
    {
        super(properties);
        GunRegistry.getInstance().register(this);
    }

    void setGun(Gun gun)
    {
        this.gun = gun;
    }

    public Gun getGun()
    {
        return this.gun;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        Gun modifiedGun = this.getModifiedGun(stack);

        String additionalDamageText = "";
        CompoundNBT tagCompound = stack.getTag();
        if(tagCompound != null)
        {
            if(tagCompound.contains("AdditionalDamage", Constants.NBT.TAG_FLOAT))
            {
                float additionalDamage = tagCompound.getFloat("AdditionalDamage");
                if(additionalDamage > 0)
                {
                    additionalDamageText = TextFormatting.GREEN + " +" + tagCompound.getFloat("AdditionalDamage");
                }
                else if(additionalDamage < 0)
                {
                    additionalDamageText = TextFormatting.RED + " " + tagCompound.getFloat("AdditionalDamage");
                }
            }
        }

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("info.cgm.damage", TextFormatting.RESET + Float.toString(gun.projectile.getDamage(modifiedGun)) + additionalDamageText)));

        if(tagCompound != null)
        {
            if(tagCompound.getBoolean("IgnoreAmmo"))
            {
                tooltip.add(new StringTextComponent(TextFormatting.AQUA + I18n.format("info.cgm.ignore_ammo")));
            }
            else
            {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("info.cgm.ammo", TextFormatting.RESET + Integer.toString(ammoCount), gun.general.getMaxAmmo(modifiedGun))));
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) //TODO might not need
    {
        return 72000;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }

    /*@Override
    public void onUsingTick(ItemStack stack, LivingEntity entity, int count)
    {
        Gun modifiedGun = getModifiedGun(stack);
        if(!modifiedGun.general.auto)
        {
            return;
        }

        PlayerEntity player = (PlayerEntity) entity;

        if(!player.world.isRemote)
        {
            return;
        }

        if(GunItem.hasAmmo(stack) || player.isCreative())
        {
            CooldownTracker tracker = player.getCooldownTracker();
            if(!tracker.hasCooldown(stack.getItem()))
            {
                tracker.setCooldown(stack.getItem(), modifiedGun.general.rate);
                PacketHandler.getPlayChannel().sendToServer(new MessageShoot());
            }
        }
    }*/

    /*@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, EnumHand handIn)
    {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if(worldIn.isRemote)
        {
            if(!GunMod.proxy.canShoot())
            {
                return new ActionResult<>(EnumActionResult.FAIL, heldItem);
            }

            if(GunItem.hasAmmo(heldItem) || playerIn.capabilities.isCreativeMode)
            {
                if(playerIn.isHandActive())
                {
                    return new ActionResult<>(EnumActionResult.FAIL, heldItem);
                }
                playerIn.setActiveHand(handIn);

                Gun modifiedGun = getModifiedGun(heldItem);
                if(!modifiedGun.general.auto)
                {
                    CooldownTracker tracker = playerIn.getCooldownTracker();
                    if(!tracker.hasCooldown(heldItem.getItem()))
                    {
                        tracker.setCooldown(heldItem.getItem(), modifiedGun.general.rate);
                        PacketHandler.INSTANCE.sendToServer(new MessageShoot());
                    }
                }
            }
            else
            {
                GunMod.proxy.playClientSound(SoundEvents.BLOCK_LEVER_CLICK);
            }
        }
        return new ActionResult<>(EnumActionResult.FAIL, heldItem);
    }*/

    public static ItemStack findAmmo(PlayerEntity player, ResourceLocation id)
    {
        if(player.isCreative())
        {
            AmmoItem ammo = AmmoRegistry.getInstance().getAmmo(id);
            return ammo != null ? new ItemStack(ammo, 64) : ItemStack.EMPTY;
        }
        for(int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if(isAmmo(stack, id))
            {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static boolean isAmmo(ItemStack stack, ResourceLocation id)
    {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }

    public static boolean hasAmmo(ItemStack gunStack)
    {
        CompoundNBT tag = ItemStackUtil.createTagCompound(gunStack);
        return tag.getBoolean("IgnoreAmmo") || tag.getInt("AmmoCount") > 0;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> stacks)
    {
        if(this.isInGroup(group))
        {
            ItemStack stack = new ItemStack(this);
            ItemStackUtil.createTagCompound(stack).putInt("AmmoCount", this.gun.general.maxAmmo);
            stacks.add(stack);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return slotChanged;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != this.gun.general.maxAmmo;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        return 1.0 - (tagCompound.getInt("AmmoCount") / (double) this.gun.general.maxAmmo);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return Color.CYAN.getRGB();
    }

    public Gun getModifiedGun(ItemStack stack)
    {
        CompoundNBT tagCompound = stack.getTag();
        if(tagCompound != null && tagCompound.contains("Gun", Constants.NBT.TAG_COMPOUND))
        {
            Gun gunCopy = this.gun.copy();
            gunCopy.deserializeNBT(tagCompound.getCompound("Gun"));
            return gunCopy;
        }
        return this.gun;
    }
}
