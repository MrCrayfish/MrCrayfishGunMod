package com.mrcrayfish.guns.potion;

import com.mrcrayfish.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionMod extends Potion
{
    private ResourceLocation icon;

    public PotionMod(boolean isBadEffect, boolean hasIcon, int liquidColor, String name)
    {
        super(isBadEffect, liquidColor);
        setRegistryName(name);
        setPotionName("effect." + Reference.MOD_ID + "." + name);
        if (hasIcon)
            icon = new ResourceLocation(Reference.MOD_ID, "textures/effect/" + name + ".png");
    }

    @Override
    public boolean shouldRender(PotionEffect effect)
    {
        return icon != null;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc)
    {
        if (mc.currentScreen == null)
            return;

        mc.getTextureManager().bindTexture(icon);
        Gui.drawScaledCustomSizeModalRect(x + 6, y + 7, 0, 0, 100, 100, 18, 18, 100, 100);

        // If level is less than 4, allow InventoryEffectRenderer#drawActivePotionEffects to render it
        int amplifier = effect.getAmplifier();
        if (amplifier < 3)
            return;

        // If level is less than 11, render it as a Roman numeral, otherwise render it normally
        String level = " " + (amplifier > 9 ? amplifier + 1 : I18n.format("enchantment.level." + (amplifier + 1)));
        mc.fontRenderer.drawStringWithShadow(level, x + 10 + 18 + mc.fontRenderer.getStringWidth(I18n.format(getName())), y + 6, 16777215);
    }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect)
    {
        return icon != null;
    }

    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha)
    {
        mc.getTextureManager().bindTexture(icon);
        Gui.drawScaledCustomSizeModalRect(x + 3, y + 3, 0, 0, 100, 100, 18, 18, 100, 100);
    }
}