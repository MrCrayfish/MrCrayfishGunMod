package com.mrcrayfish.guns.debug.client.screen.widget;

import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class DebugSlider extends ForgeSlider
{
    private final Consumer<Double> callback;

    public DebugSlider(double minValue, double maxValue, double currentValue, double stepSize, int precision, Consumer<Double> callback)
    {
        super(0, 0, 0, 20, Component.empty(), Component.empty(), minValue, maxValue, currentValue, stepSize, precision, true);
        this.callback = callback;
    }

    @Override
    protected void applyValue()
    {
        this.callback.accept(this.getValue());
    }
}
