package com.tac.guns.item.TransitionalTypes;


import com.tac.guns.GunMod;
import com.tac.guns.common.Gun;
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


public class TimelessPistolGunItem extends TimelessGunItem {
    public TimelessPistolGunItem(Process<Properties> properties)
    {
        super(properties1 -> properties.process(new Properties().maxStackSize(1).group(GunMod.GROUP)));
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        Item ammo = (Item) ForgeRegistries.ITEMS.getValue(modifiedGun.getProjectile().getItem());
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
            float speed = 0.1f / (1+((gun.getGun().getGeneral().getWeightKilo()*(1+GunModifierHelper.getModifierOfWeaponWeight(stack)) + GunModifierHelper.getAdditionalWeaponWeight(stack)) * 0.0275f));
            speed = Math.max(Math.min(speed, 0.095F), 0.075F);
            if(speed > 0.09)
                tooltip.add((new TranslationTextComponent("info.tac.lightWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_AQUA)));
            else if(speed < 0.09 && speed > 0.0825)
                tooltip.add((new TranslationTextComponent("info.tac.standardWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_GREEN)));
            else
                tooltip.add((new TranslationTextComponent("info.tac.heavyWeightGun", new TranslationTextComponent(-((int)((0.1 - speed)*1000))+"%").mergeStyle(TextFormatting.RED)).mergeStyle(TextFormatting.DARK_RED)));
            //tooltip.add((new TranslationTextComponent("info.tac.oldRifleScope", new TranslationTextComponent("OldScope").mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.LIGHT_PURPLE)));
        }

        if(tagCompound != null)
        {
            //tooltip.add((new TranslationTextComponent("info.tac.oldRifle", new TranslationTextComponent(IAttachment.Type.OLD_SCOPE.getTranslationKey())).mergeStyle(TextFormatting.GREEN)));
            tooltip.add((new TranslationTextComponent("info.tac.pistolScope", new TranslationTextComponent("MiniScope").mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.LIGHT_PURPLE)));
            tooltip.add((new TranslationTextComponent("info.tac.pistolBarrel", new TranslationTextComponent("PistolBarrel").mergeStyle(TextFormatting.BOLD)).mergeStyle(TextFormatting.LIGHT_PURPLE)));
        }
        tooltip.add((new TranslationTextComponent("info.tac.attachment_help", new Object[]{(new KeybindTextComponent("key.tac.attachments")).getString().toUpperCase(Locale.ENGLISH)})).mergeStyle(TextFormatting.YELLOW));
    }
    public TimelessPistolGunItem() {
        this(properties -> properties);
    }
}