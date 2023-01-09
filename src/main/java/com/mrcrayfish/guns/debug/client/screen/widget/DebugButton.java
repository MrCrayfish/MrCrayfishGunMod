package com.mrcrayfish.guns.debug.client.screen.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.debug.IDebugWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class DebugButton extends Button implements IDebugWidget
{
    private final Supplier<Boolean> enabled;

    public DebugButton(Component label, OnPress onPress)
    {
        super(0, 0, 0, 20, label, onPress);
        this.enabled = () -> true;
    }

    public DebugButton(Component label, OnPress onPress, Supplier<Boolean> enabled)
    {
        super(0, 0, 0, 20, label, onPress);
        this.enabled = enabled;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick)
    {
        this.active = this.enabled.get();
        super.render(poseStack, mouseX, mouseY, partialTick);
    }
}
