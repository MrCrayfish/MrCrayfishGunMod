package com.mrcrayfish.guns.client.settings;

import com.mrcrayfish.guns.GunMod;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.settings.SliderPercentageOption;

/**
 * Author: MrCrayfish
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
    protected void applyValue()
    {
        this.option.set(this.options, this.option.denormalizeValue(this.value));
        GunMod.getOptions().saveOptions();
    }
}
