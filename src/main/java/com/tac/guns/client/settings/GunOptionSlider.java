package com.tac.guns.client.settings;

import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.settings.SliderPercentageOption;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunOptionSlider extends OptionSlider
{
    private final SliderPercentageOption option;

    public GunOptionSlider(GameSettings settings, int x, int y, int width, int height, SliderPercentageOption option)
    {
        super(settings, x, y, width, height, option);
        this.option = option;
    }

    @Override
    protected void func_230972_a_()
    {
        this.option.set(this.settings, this.option.denormalizeValue(this.sliderValue));
    }
}
