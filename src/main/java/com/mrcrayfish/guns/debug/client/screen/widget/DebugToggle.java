package com.mrcrayfish.guns.debug.client.screen.widget;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class DebugToggle extends DebugButton
{
    private boolean enabled;
    private final Consumer<Boolean> callback;

    public DebugToggle(boolean initialValue, Consumer<Boolean> callback)
    {
        super(Component.empty(), btn -> ((DebugToggle) btn).toggle());
        this.enabled = initialValue;
        this.callback = callback;
        this.updateMessage();
    }

    private void toggle()
    {
        this.enabled = !this.enabled;
        this.updateMessage();
        this.callback.accept(this.enabled);
    }

    private void updateMessage()
    {
        this.setMessage(this.enabled ? Component.literal("On") : Component.literal("Off"));
    }
}
