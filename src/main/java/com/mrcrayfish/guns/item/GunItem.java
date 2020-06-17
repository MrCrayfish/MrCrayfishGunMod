package com.mrcrayfish.guns.item;

import com.google.common.annotations.Beta;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.ItemStackUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
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
    private Gun gun = new Gun();

    public GunItem(Item.Properties properties)
    {
        super(properties);
    }

    public void setGun(Gun gun)
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

        tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("info.cgm.damage", TextFormatting.RESET + Float.toString(modifiedGun.projectile.damage) + additionalDamageText)));

        if(tagCompound != null)
        {
            if(tagCompound.getBoolean("IgnoreAmmo"))
            {
                tooltip.add(new StringTextComponent(TextFormatting.AQUA + I18n.format("info.cgm.ignore_ammo")));
            }
            else
            {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add(new StringTextComponent(TextFormatting.GRAY + I18n.format("info.cgm.ammo", TextFormatting.RESET + Integer.toString(ammoCount), modifiedGun.general.maxAmmo)));
            }
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
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
        Gun modifiedGun = this.getModifiedGun(stack);
        return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != modifiedGun.general.maxAmmo;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        CompoundNBT tagCompound = ItemStackUtil.createTagCompound(stack);
        Gun modifiedGun = this.getModifiedGun(stack);
        return 1.0 - (tagCompound.getInt("AmmoCount") / (double) modifiedGun.general.maxAmmo);
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
            if(tagCompound.getBoolean("Custom"))
            {
                return Gun.create(tagCompound.getCompound("Gun"));
            }
            else
            {
                Gun gunCopy = this.gun.copy();
                gunCopy.deserializeNBT(tagCompound.getCompound("Gun"));
                return gunCopy;
            }
        }
        return this.gun;
    }
}
