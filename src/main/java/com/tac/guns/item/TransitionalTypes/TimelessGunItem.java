package com.tac.guns.item.TransitionalTypes;


import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.client.handler.MovementAdaptationsHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.IColored;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.tac.guns.util.Process;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.minecraft.entity.ai.attributes.Attributes.MOVEMENT_SPEED;


public class TimelessGunItem extends GunItem
{
    public TimelessGunItem(Process<Item.Properties> properties, IGunModifier... modifiers)
    {
        super(properties.process(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
        this.modifiers = modifiers;
    }
    
    public TimelessGunItem() {
        this(properties -> properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = (Item)ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
        if (ammo != null) {
            tooltip.add((new TranslationTextComponent("info.tac.ammo_type", new TranslationTextComponent(ammo.getTranslationKey()).mergeStyle(TextFormatting.GOLD)).mergeStyle(TextFormatting.DARK_GRAY)));
        }

        String additionalDamageText = "";
        CompoundNBT tagCompound = stack.getTag();
        float additionalDamage;
        if (tagCompound != null && tagCompound.contains("AdditionalDamage", 99)) {
            additionalDamage = tagCompound.getFloat("AdditionalDamage");
            additionalDamage += GunModifierHelper.getAdditionalDamage(stack);
            if (additionalDamage > 0.0F) {
                additionalDamageText = TextFormatting.GREEN + " +" + ItemStack.DECIMALFORMAT.format((double)additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = TextFormatting.RED + " " + ItemStack.DECIMALFORMAT.format((double)additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslationTextComponent("info.tac.damage", new Object[]{TextFormatting.GOLD + ItemStack.DECIMALFORMAT.format((double)additionalDamage) + additionalDamageText})).mergeStyle(TextFormatting.DARK_GRAY));
        if (tagCompound != null) {
            if (tagCompound.getBoolean("IgnoreAmmo")) {
                tooltip.add((new TranslationTextComponent("info.tac.ignore_ammo")).mergeStyle(TextFormatting.AQUA));
            } else {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add((new TranslationTextComponent("info.tac.ammo", new Object[]{TextFormatting.GOLD.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun)})).mergeStyle(TextFormatting.DARK_GRAY));
            }
        }

        if(tagCompound != null) {
            if (tagCompound.get("CurrentFireMode") == null) {
            } else if (tagCompound.getInt("CurrentFireMode") == 0)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_safe", new Object[]{(new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).mergeStyle(TextFormatting.GREEN));
            else if (tagCompound.getInt("CurrentFireMode") == 1)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_semi", new Object[]{(new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).mergeStyle(TextFormatting.RED));
            else if (tagCompound.getInt("CurrentFireMode") == 2)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_auto", new Object[]{(new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH)})).mergeStyle(TextFormatting.RED));
        }

        if(tagCompound != null)
        {
            GunItem gun = (GunItem)stack.getItem();
            float speed = 0.1f / (1+((gun.getModifiedGun(stack).getGeneral().getWeightKilo()*(1+GunModifierHelper.getModifierOfWeaponWeight(stack)) + GunModifierHelper.getAdditionalWeaponWeight(stack)) * 0.0275f));
            speed = Math.max(Math.min(speed, 0.095F), 0.075F);
            if(speed > 0.09)
                tooltip.add((new TranslationTextComponent("info.tac.lightWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_AQUA)));
            else if(speed < 0.09 && speed > 0.0825)
                tooltip.add((new TranslationTextComponent("info.tac.standardWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_GREEN)));
            else
                tooltip.add((new TranslationTextComponent("info.tac.heavyWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_RED)));
        }

        tooltip.add((new TranslationTextComponent("info.tac.attachment_help", new Object[]{(new KeybindTextComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH)})).mergeStyle(TextFormatting.YELLOW));
    }

    private final IGunModifier[] modifiers;

    public IGunModifier[] getModifiers()
    {
        return this.modifiers;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return (Integer)Objects.requireNonNull(TextFormatting.GOLD.getColor());
    }
    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        if (Config.CLIENT.display.weaponAmmoBar.get()) {
            CompoundNBT tagCompound = stack.getOrCreateTag();
            Gun modifiedGun = this.getModifiedGun(stack);
            return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun);
        }
        else
            return false;
    }
    @Override
    public boolean hasEffect(ItemStack gunItem)
    {
        return false;
    }
}