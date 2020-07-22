package com.mrcrayfish.guns.client.settings;

import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class GunSliderPercentageOption extends SliderPercentageOption
{
    public GunSliderPercentageOption(String title, double minValue, double maxValue, float stepSize, Function<GameSettings, Double> getter, BiConsumer<GameSettings, Double> setter, BiFunction<GameSettings, SliderPercentageOption, ITextComponent> displayNameGetter)
    {
        super(title, minValue, maxValue, stepSize, getter, setter, displayNameGetter);
    }

    @Override
    public Widget createWidget(GameSettings settings, int x, int y, int width)
    {
        return new GunOptionSlider(settings, x, y, width, 20, this);
    }
}
