package com.mrcrayfish.guns.debug.client.screen.widget;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringRepresentable;

import java.util.function.Consumer;

/**
 * Author: MrCrayfish
 */
public class DebugEnum<T extends Enum<T> & StringRepresentable> extends DebugButton
{
    private final Class<T> enumClass;
    private final Consumer<T> callback;
    private int ordinal;

    public DebugEnum(Class<T> enumClass, T currentValue, Consumer<T> callback)
    {
        super(new TextComponent(currentValue.getSerializedName()), btn -> ((DebugEnum<T>) btn).next());
        this.enumClass = enumClass;
        this.callback = callback;
        this.ordinal = currentValue.ordinal();
    }

    private void next()
    {
        int nextIndex = (this.ordinal + 1) % this.enumClass.getEnumConstants().length;
        T value = this.enumClass.getEnumConstants()[nextIndex];
        this.ordinal = value.ordinal();
        this.setMessage(new TextComponent(value.getSerializedName()));
        this.callback.accept(value);
    }
}
