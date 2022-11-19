package com.mrcrayfish.guns.debug.client.screen.widget;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * Author: MrCrayfish
 */
public class DebugButton extends Button
{
    public DebugButton(Component label, OnPress onPress)
    {
        super(0, 0, 0, 20, label, onPress);
    }
}
