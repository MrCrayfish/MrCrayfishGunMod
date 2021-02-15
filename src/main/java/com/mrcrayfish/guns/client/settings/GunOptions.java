package com.mrcrayfish.guns.client.settings;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.handler.CrosshairHandler;
import com.mrcrayfish.guns.client.render.crosshair.Crosshair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;

/**
 * Author: MrCrayfish
 */
public class GunOptions
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0#");

    public static final SliderPercentageOption ADS_SENSITIVITY = new GunSliderPercentageOption("cgm.options.adsSensitivity", 0.0, 1.0, 0.01F, gameSettings -> {
        return Config.CLIENT.controls.aimDownSightSensitivity.get();
    }, (gameSettings, value) -> {
        Config.CLIENT.controls.aimDownSightSensitivity.set(MathHelper.clamp(value, 0.0, 1.0));
        Config.saveClientConfig();
    }, (gameSettings, option) -> {
        double adsSensitivity = Config.CLIENT.controls.aimDownSightSensitivity.get();
        return new TranslationTextComponent("cgm.options.adsSensitivity.format", FORMAT.format(adsSensitivity)).getFormattedText();
    });

    public static final AbstractOption CROSSHAIR = new GunListOption<>("cgm.options.crosshair", () -> {
        return CrosshairHandler.get().getRegisteredCrosshairs();
    }, () -> {
        return ResourceLocation.tryCreate(Config.CLIENT.display.crosshair.get());
    }, (value) -> {
        Config.CLIENT.display.crosshair.set(value.toString());
        Config.saveClientConfig();
        CrosshairHandler.get().setCrosshair(value);
    }, (value) -> {
        ResourceLocation id = value.getLocation();
        return new TranslationTextComponent(id.getNamespace() + ".crosshair." + id.getPath());
    }).setRenderer((button, matrixStack, partialTicks) -> {
        matrixStack.push();
        matrixStack.translate(button.x, button.y, 0);
        matrixStack.translate(button.getWidth() + 2, 2, 0);
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if(crosshair != null)
        {
            if(crosshair.isDefault())
            {
                Minecraft mc = Minecraft.getInstance();
                mc.getTextureManager().bindTexture(AbstractGui.GUI_ICONS_LOCATION);
                AbstractGui.blit((16 - 15) / 2, (16 - 15) / 2, 0, 0, 0, 15, 15, 256, 256);
            }
            else
            {
                crosshair.render(Minecraft.getInstance(), matrixStack, 16, 16, partialTicks);
            }
        }
        matrixStack.pop();
    });
}