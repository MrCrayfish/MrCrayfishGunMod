package com.tac.guns.client.settings;

import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class GunEnumOption<E extends Enum<E> & IStringSerializable> extends AbstractOption
{
    private Class<E> enumClass;
    private int ordinal = 0;
    private Function<GameSettings, E> getter;
    private BiConsumer<GameSettings, E> setter;
    private BiFunction<GameSettings, GunEnumOption<E>, ITextComponent> displayNameGetter;

    public GunEnumOption(String title, Class<E> enumClass, Function<GameSettings, E> getter, BiConsumer<GameSettings, E> setter, BiFunction<GameSettings, GunEnumOption<E>, ITextComponent> displayNameGetter)
    {
        super(title);
        this.enumClass = enumClass;
        this.getter = getter;
        this.setter = setter;
        this.displayNameGetter = displayNameGetter;
    }

    private void nextEnum(GameSettings options)
    {
        this.set(options, this.getEnum(++this.ordinal));
    }

    public void set(GameSettings options, E e)
    {
        this.setter.accept(options, e);
        this.ordinal = e.ordinal();
    }

    public E get(GameSettings options)
    {
        E e = this.getter.apply(options);
        this.ordinal = e.ordinal();
        return e;
    }

    public Widget createWidget(GameSettings options, int x, int y, int width)
    {
        return new OptionButton(x, y, width, 20, this, this.getTitle(options), (button) -> {
            this.nextEnum(options);
            button.setMessage(this.getTitle(options));
        });
    }

    public ITextComponent getTitle(GameSettings options)
    {
        return this.displayNameGetter.apply(options, this);
    }

    private E getEnum(int ordinal)
    {
        E[] e = this.enumClass.getEnumConstants();
        if(ordinal >= e.length)
        {
            ordinal = 0;
        }
        return e[ordinal];
    }
}
