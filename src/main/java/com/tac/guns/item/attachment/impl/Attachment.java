package com.tac.guns.item.attachment.impl;

import com.tac.guns.Reference;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

/**
 * The base attachment object
 *
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public abstract class Attachment
{
    private final IGunModifier[] modifiers;
    private List<ITextComponent> perks = null;

    Attachment(IGunModifier... modifiers)
    {
        this.modifiers = modifiers;
    }

    public IGunModifier[] getModifiers()
    {
        return this.modifiers;
    }

    void setPerks(List<ITextComponent> perks)
    {
        if(this.perks == null)
        {
            this.perks = perks;
        }
    }

    List<ITextComponent> getPerks()
    {
        return this.perks;
    }

    /* Determines the perks of attachments and caches them */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void addInformationEvent(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        if(stack.getItem() instanceof IAttachment<?>)
        {
            IAttachment<?> attachment = (IAttachment<?>) stack.getItem();
            List<ITextComponent> perks = attachment.getProperties().getPerks();

            if (perks != null && perks.size() > 0) {
                event.getToolTip().add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GOLD, TextFormatting.BOLD));
                event.getToolTip().addAll(perks);
                return;
            }

            IGunModifier[] modifiers = attachment.getProperties().getModifiers();
            List<ITextComponent> positivePerks = new ArrayList<>();
            List<ITextComponent> negativePerks = new ArrayList<>();

            /* Test for fire sound volume *//*
            float inputSound = 1.0F;
            float outputSound = inputSound;
            for (IGunModifier modifier : modifiers) {
                outputSound = modifier.modifyFireSoundVolume(outputSound);
            }
            if (outputSound > inputSound) {
                addPerk(negativePerks, "perk.tac.fire_volume.negative", new TranslationTextComponent("+" + String.valueOf((1.0F - Math.round(outputSound)) * 100) + "% Volume").mergeStyle(TextFormatting.RED));
            } else if (outputSound < inputSound) {
                addPerk(negativePerks, "perk.tac.fire_volume.negative", new TranslationTextComponent("" + String.valueOf((1.0F - Math.round(outputSound)) * 100) + "% Volume").mergeStyle(TextFormatting.GREEN));
                //addPerk(positivePerks, "perk.tac.fire_volume.positive", TextFormatting.GREEN, "-" + String.valueOf((1.0F - outputSound) * 100) + new TranslationTextComponent("perk.tac.vol"));
            }*/

            /* Test for silenced */
            for (IGunModifier modifier : modifiers) {
                if (modifier.silencedFire()) {
                    addPerk(positivePerks, "perk.tac.silenced.positive", new TranslationTextComponent("+Silenced").mergeStyle(TextFormatting.GREEN));
                    break;
                }
            }

            /* Test for sound radius */
            double inputRadius = 10.0;
            double outputRadius = inputRadius;
            for (IGunModifier modifier : modifiers) {
                outputRadius = modifier.modifyFireSoundRadius(outputRadius);
            }
            if (outputRadius > inputRadius) {
                addPerk(negativePerks, "perk.tac.sound_radius.negative", new TranslationTextComponent("+" + Math.round(outputRadius) + " Sound Radius").mergeStyle(TextFormatting.RED));
            } else if (outputRadius < inputRadius) {
                addPerk(positivePerks, "perk.tac.sound_radius.positive", new TranslationTextComponent("-" + Math.round(outputRadius) + " Sound Radius").mergeStyle(TextFormatting.GREEN));
            }

            /* Test for additional damage */
            float additionalDamage = 0.0F;
            for (IGunModifier modifier : modifiers) {
                additionalDamage += modifier.additionalDamage();
            }
            if (additionalDamage > 0.0F) {
                addPerk(positivePerks, "perk.tac.additional_damage.positive", ItemStack.DECIMALFORMAT.format(additionalDamage / 2.0));
            } else if (additionalDamage < 0.0F) {
                addPerk(negativePerks, "perk.tac.additional_damage.negative", ItemStack.DECIMALFORMAT.format(additionalDamage / 2.0));
            }

            /* Test for additional headshot damage */
            float additionalHeadshotDamage = 0.0F;
            for (IGunModifier modifier : modifiers) {
                additionalHeadshotDamage += modifier.additionalHeadshotDamage();
            }
            if (additionalHeadshotDamage > 0.0F) {
                addPerk(positivePerks, "perk.tac.additional_damage.positive", ItemStack.DECIMALFORMAT.format(additionalHeadshotDamage / 2.0));
            } else if (additionalHeadshotDamage < 0.0F) {
                addPerk(negativePerks, "perk.tac.additional_damage.negative", ItemStack.DECIMALFORMAT.format(additionalHeadshotDamage / 2.0));
            }

            /* Test for modified damage */
            float inputDamage = 10.0F;
            float outputDamage = inputDamage;
            for (IGunModifier modifier : modifiers) {
                outputDamage = modifier.modifyProjectileDamage(outputDamage);
            }
            if (outputDamage > inputDamage) {
                addPerk(positivePerks, "perk.tac.modified_damage.positive", new TranslationTextComponent("+" + String.valueOf(outputDamage) + " Damage").mergeStyle(TextFormatting.GREEN));
            } else if (outputDamage < inputDamage) {
                addPerk(positivePerks, "perk.tac.modified_damage.negative", new TranslationTextComponent("-" + String.valueOf(outputDamage) + " Damage").mergeStyle(TextFormatting.RED));
            }

            /* Test for modified damage */
            double inputSpeed = 10.0;
            double outputSpeed = inputSpeed;
            for (IGunModifier modifier : modifiers) {
                outputSpeed = modifier.modifyProjectileSpeed(outputSpeed);
            }
            if (outputSpeed > inputSpeed) {
                addPerk(positivePerks, "perk.tac.projectile_speed.positive", new TranslationTextComponent("+" + Math.round((10.0F - outputSpeed) * 10) + "% Bullet Speed").mergeStyle(TextFormatting.GREEN));
            } else if (outputSpeed < inputSpeed) {
                addPerk(negativePerks, "perk.tac.projectile_speed.negative", new TranslationTextComponent("-" + Math.round((10.0F - outputSpeed) * 10) + "% Bullet Speed").mergeStyle(TextFormatting.RED));
            }

            /* Test for modified projectile spread */
            float inputSpread = 10.0F;
            float outputSpread = inputSpread;
            for (IGunModifier modifier : modifiers) {
                outputSpread = modifier.modifyProjectileSpread(outputSpread);
            }
            if (outputSpread > inputSpread) {
                addPerk(negativePerks, "perk.tac.projectile_spread.negative", new TranslationTextComponent("" + String.valueOf(Math.round((10.0F - outputSpread) * 10f)) + "% Accuracy").mergeStyle(TextFormatting.RED));
            } else if (outputSpread < inputSpread) {
                addPerk(positivePerks, "perk.tac.projectile_spread.positive", new TranslationTextComponent("+" + String.valueOf(Math.round((10.0F - outputSpread) * 10f)) + "% Accuracy").mergeStyle(TextFormatting.GREEN));
            }

            /* Test for modified projectile life */
            int inputLife = 100;
            int outputLife = inputLife;
            for (IGunModifier modifier : modifiers) {
                outputLife = modifier.modifyProjectileLife(outputLife);
            }
            if (outputLife > inputLife) {
                addPerk(positivePerks, "perk.tac.projectile_life.positive", new TranslationTextComponent("+" + String.valueOf(outputLife) + " Bullet Life").mergeStyle(TextFormatting.GREEN));
            } else if (outputLife < inputLife) {
                addPerk(negativePerks, "perk.tac.projectile_life.negative", new TranslationTextComponent("-" + String.valueOf(outputLife) + " Bullet Life").mergeStyle(TextFormatting.RED));
            }

            /* Test for modified recoil */
            float inputRecoil = 10.0F;
            float outputRecoil = inputRecoil;
            for (IGunModifier modifier : modifiers) {
                outputRecoil *= modifier.recoilModifier();
            }
            if (outputRecoil > inputRecoil) {
                addPerk(negativePerks, "perk.tac.recoil.negative", new TranslationTextComponent("+" + String.valueOf(Math.round((10.0F - outputRecoil) * -10f)) + "% Vertical Recoil").mergeStyle(TextFormatting.RED));
            } else if (outputRecoil < inputRecoil) {
                addPerk(positivePerks, "perk.tac.recoil.positive", new TranslationTextComponent("-" + String.valueOf(Math.round((10.0F - outputRecoil) * 10f)) + "% Vertical Recoil").mergeStyle(TextFormatting.GREEN));
            }

            float inputHRecoil = 10.0F;
            float outputHRecoil = inputHRecoil;
            for (IGunModifier modifier : modifiers) {
                outputHRecoil *= modifier.horizontalRecoilModifier();
            }
            if (outputHRecoil > inputHRecoil) {
                addPerk(negativePerks, "perk.tac.recoil.negative", new TranslationTextComponent("+" + String.valueOf(Math.round((10.0F - outputHRecoil) * -10f)) + "% Horizontal Recoil").mergeStyle(TextFormatting.RED));
            } else if (outputHRecoil < inputHRecoil) {
                addPerk(positivePerks, "perk.tac.recoil.positive", new TranslationTextComponent("-" + String.valueOf(Math.round((10.0F - outputHRecoil) * 10f)) + "% Horizontal Recoil").mergeStyle(TextFormatting.GREEN));
            }

            /* Test for aim down sight speed */
            double inputAdsSpeed = 10.0;
            double outputAdsSpeed = inputAdsSpeed;
            for (IGunModifier modifier : modifiers) {
                outputAdsSpeed = modifier.modifyAimDownSightSpeed(outputAdsSpeed);
            }
            if (outputAdsSpeed > inputAdsSpeed) {
                addPerk(positivePerks, "perk.tac.ads_speed.positive", new TranslationTextComponent("+" + -Math.round((10.0 - outputAdsSpeed) * 10) + "% ADS Speed").mergeStyle(TextFormatting.GREEN));
            } else if (outputAdsSpeed < inputAdsSpeed) {
                addPerk(negativePerks, "perk.tac.ads_speed.negative", new TranslationTextComponent("-" + Math.round((10.0 - outputAdsSpeed) * 10) + "% ADS Speed").mergeStyle(TextFormatting.RED));
            }

            /* Test for fire rate */
            int inputRate = 10;
            int outputRate = inputRate;
            for (IGunModifier modifier : modifiers) {
                outputRate = modifier.modifyFireRate(outputRate);
            }
            if (outputRate > inputRate) {
                addPerk(negativePerks, "perk.tac.rate.negative");
            } else if (outputRate < inputRate) {
                addPerk(positivePerks, "perk.tac.rate.positive");
            }

            positivePerks.addAll(negativePerks);
            attachment.getProperties().setPerks(positivePerks);
            if (positivePerks.size() > 0) {
                event.getToolTip().add(new TranslationTextComponent("perk.tac.title").mergeStyle(TextFormatting.GRAY, TextFormatting.BOLD));
                event.getToolTip().addAll(positivePerks);
            }

        }
    }

    private static void addPerk(List<ITextComponent> components, String id, Object... params)
    {
        //TextFormatting format,   components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(format)));
        components.add(new TranslationTextComponent("perk.tac.entry.negative", new TranslationTextComponent(id, params).mergeStyle(TextFormatting.AQUA)));
    }
}
