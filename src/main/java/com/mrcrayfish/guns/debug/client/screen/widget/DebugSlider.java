package com.mrcrayfish.guns.debug.client.screen.widget;

import com.mrcrayfish.guns.debug.IDebugWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class DebugSlider extends ForgeSlider implements IDebugWidget {
    private final Consumer<Double> callback;

    public DebugSlider(double minValue, double maxValue, double currentValue, double stepSize, int precision, Consumer<Double> callback) {
        super(0, 0, 0, 14, Component.empty(), Component.empty(), minValue, maxValue, currentValue, stepSize, precision, true);
        this.callback = callback;
    }

    @Override
    protected void applyValue() {
        this.callback.accept(this.getValue());
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = (this.isHoveredOrFocused() ? 2 : 1) * 20;
        graphics.blit(WIDGETS_LOCATION, this.getX() + (int) (this.value * (double) (this.width - 8)), this.getY(), 0, 46 + i, 4, this.height);
        graphics.blit(WIDGETS_LOCATION, this.getX() + (int) (this.value * (double) (this.width - 8)) + 4, this.getY(), 196, 46 + i, 4, this.height);
    }
}
