package com.tac.guns.item.TransitionalTypes;


import com.tac.guns.Config;
import com.tac.guns.GunMod;
import com.tac.guns.common.Gun;
import com.tac.guns.common.network.ServerPlayHandler;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.GunItem;
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


public class TimelessGunItem extends GunItem {
    private final IGunModifier[] modifiers;
    private Boolean integratedOptic = false;
    public TimelessGunItem(Process<Item.Properties> properties, IGunModifier... modifiers) {
        super(properties.process(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
        this.modifiers = modifiers;
    }

    public TimelessGunItem(Process<Item.Properties> properties, Boolean integratedOptic, IGunModifier... modifiers) {
        super(properties.process(new Item.Properties().maxStackSize(1).group(GunMod.GROUP)));
        this.modifiers = modifiers;
        this.integratedOptic = integratedOptic;
    }

    public TimelessGunItem() {
        this(properties -> properties);
    }

    public Boolean isIntegratedOptic() {
        return integratedOptic;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
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
                additionalDamageText = TextFormatting.GREEN + " +" + ItemStack.DECIMALFORMAT.format(additionalDamage);
            } else if (additionalDamage < 0.0F) {
                additionalDamageText = TextFormatting.RED + " " + ItemStack.DECIMALFORMAT.format(additionalDamage);
            }
        }

        additionalDamage = modifiedGun.getProjectile().getDamage();
        additionalDamage = GunModifierHelper.getModifiedProjectileDamage(stack, additionalDamage);
        additionalDamage = GunEnchantmentHelper.getAcceleratorDamage(stack, additionalDamage);
        tooltip.add((new TranslationTextComponent("info.tac.damage", TextFormatting.GOLD + ItemStack.DECIMALFORMAT.format(additionalDamage) + additionalDamageText)).mergeStyle(TextFormatting.DARK_GRAY));
        if (tagCompound != null) {
            if (tagCompound.getBoolean("IgnoreAmmo")) {
                tooltip.add((new TranslationTextComponent("info.tac.ignore_ammo")).mergeStyle(TextFormatting.AQUA));
            } else {
                int ammoCount = tagCompound.getInt("AmmoCount");
                tooltip.add((new TranslationTextComponent("info.tac.ammo", TextFormatting.GOLD.toString() + ammoCount + "/" + GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun))).mergeStyle(TextFormatting.DARK_GRAY));
            }
        }

        if (tagCompound != null) {
            if (tagCompound.get("CurrentFireMode") == null) {
            } else if (tagCompound.getInt("CurrentFireMode") == 0)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_safe", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).mergeStyle(TextFormatting.GREEN));
            else if (tagCompound.getInt("CurrentFireMode") == 1)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_semi", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).mergeStyle(TextFormatting.RED));
            else if (tagCompound.getInt("CurrentFireMode") == 2)
                tooltip.add((new TranslationTextComponent("info.tac.firemode_auto", (new KeybindTextComponent("key.tac.fireSelect")).getString().toUpperCase(Locale.ENGLISH))).mergeStyle(TextFormatting.RED));
        }
        GunItem gun = (GunItem) stack.getItem();
        if (tagCompound != null) {
            float speed = ServerPlayHandler.calceldGunWeightSpeed(gun.getGun(), stack);
            speed = Math.max(Math.min(speed, 0.1F), 0.075F);
            if (speed > 0.095)
                tooltip.add((new TranslationTextComponent("info.tac.lightWeightGun", new TranslationTextComponent(-((int) ((0.1 - speed) * 1000)) + "%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_AQUA)));
            else if (speed < 0.095 && speed > 0.085)
                tooltip.add((new TranslationTextComponent("info.tac.standardWeightGun", new TranslationTextComponent(-((int) ((0.1 - speed) * 1000)) + "%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_GREEN)));
            else
                tooltip.add((new TranslationTextComponent("info.tac.heavyWeightGun", new TranslationTextComponent(-((int) ((0.1 - speed) * 1000)) + "%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_RED)));

            float percentageToNextLevel =
                    ( tagCompound.getFloat("levelDmg") * 100) / (modifiedGun.getGeneral().getLevelReq()*(((tagCompound.getInt("level"))*3.0f)));
            tooltip.add((new TranslationTextComponent("info.tac.current_level").append(new TranslationTextComponent( " " + tagCompound.getInt("level") + " : " + percentageToNextLevel+"%")))
                    .mergeStyle(TextFormatting.GRAY).mergeStyle(TextFormatting.BOLD));
        }
        tooltip.add((new TranslationTextComponent("info.tac.attachment_help", (new KeybindTextComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH))).mergeStyle(TextFormatting.YELLOW));
    }

    public IGunModifier[] getModifiers() {
        return this.modifiers;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return Objects.requireNonNull(TextFormatting.GOLD.getColor());
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        if (Config.CLIENT.display.weaponAmmoBar.get()) {
            CompoundNBT tagCompound = stack.getOrCreateTag();
            Gun modifiedGun = this.getModifiedGun(stack);
            return !tagCompound.getBoolean("IgnoreAmmo") && tagCompound.getInt("AmmoCount") != GunEnchantmentHelper.getAmmoCapacity(stack, modifiedGun);
        } else
            return false;
    }

    @Override
    public boolean hasEffect(ItemStack gunItem) {
        return false;
    }
}