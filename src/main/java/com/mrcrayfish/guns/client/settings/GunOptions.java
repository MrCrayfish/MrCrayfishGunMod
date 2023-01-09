package com.mrcrayfish.guns.client.settings;

import java.text.DecimalFormat;

/**
 * Author: MrCrayfish
 */
public class GunOptions
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0#");

    /*public static final ProgressOption ADS_SENSITIVITY = new GunSliderPercentageOption("cgm.options.adsSensitivity", 0.0, 1.0, 0.01F, gameSettings -> {
        return Config.CLIENT.controls.aimDownSightSensitivity.get();
    }, (gameSettings, value) -> {
        Config.CLIENT.controls.aimDownSightSensitivity.set(Mth.clamp(value, 0.0, 1.0));
        Config.saveClientConfig();
    }, (gameSettings, option) -> {
        double adsSensitivity = Config.CLIENT.controls.aimDownSightSensitivity.get();
        return Component.translatable("cgm.options.adsSensitivity.format", FORMAT.format(adsSensitivity));
    });

    public static final CycleOption<ResourceLocation> CROSSHAIR = CycleOption.create("cgm.options.crosshair", () -> {
        return CrosshairHandler.get().getRegisteredCrosshairs().stream().map(Crosshair::getLocation).collect(Collectors.toList());
    }, id -> {
        return Component.translatable(id.getNamespace() + ".crosshair." + id.getPath());
    }, (options) -> {
        return ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
    }, (options, option, id) -> {
        Config.CLIENT.display.crosshair.set(id.toString());
        Config.saveClientConfig();
        CrosshairHandler.get().setCrosshair(id);
    });*/

    /*public static final Option CROSSHAIR = CycleOption.create("cgm.options.crosshair", () -> {
        return CrosshairHandler.get().getRegisteredCrosshairs();
    }, (t) -> {
        return ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
    }, (value) -> {
        Config.CLIENT.display.crosshair.set(value.toString());
        Config.saveClientConfig();
        CrosshairHandler.get().setCrosshair(value);
    }, (value) -> {
        ResourceLocation id = value.getLocation();
        return Component.translatable(id.getNamespace() + ".crosshair." + id.getPath());
    }).setRenderer((button, poseStack, partialTicks) -> {
        poseStack.translate(button.x, button.y, 0);
        poseStack.translate(button.getWidth() + 2, 2, 0);
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if(crosshair != null)
        {
            if(crosshair.isDefault())
            {
                Minecraft mc = Minecraft.getInstance();
                mc.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
                GuiComponent.blit(poseStack, (16 - 15) / 2, (16 - 15) / 2, 0, 0, 0, 15, 15, 256, 256);
            }
            else
            {
                crosshair.render(Minecraft.getInstance(), poseStack, 16, 16, partialTicks);
            }
        }
    });*/
}