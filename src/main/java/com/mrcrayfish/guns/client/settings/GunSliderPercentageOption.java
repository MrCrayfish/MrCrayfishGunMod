package com.mrcrayfish.guns.client.settings;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.ProgressOption;
import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Author: MrCrayfish
 */
public class GunSliderPercentageOption extends ProgressOption
{
    public GunSliderPercentageOption(String title, double minValue, double maxValue, float stepSize, Function<Options, Double> getter, BiConsumer<Options, Double> setter, BiFunction<Options, ProgressOption, Component> displayNameGetter)
    {
        super(title, minValue, maxValue, stepSize, getter, setter, displayNameGetter);
    }
}
