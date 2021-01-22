package com.mrcrayfish.guns.client.settings;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.interfaces.IResourceLocation;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class GunListOption<E extends IResourceLocation> extends AbstractOption
{
    private String title;
    private ResourceLocation selected;
    private Supplier<List<E>> supplier;
    private Supplier<ResourceLocation> getter;
    private Consumer<ResourceLocation> setter;
    private Function<E, ITextComponent> displayNameGetter;

    public GunListOption(String title, Supplier<List<E>> supplier, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter, Function<E, ITextComponent> displayNameGetter)
    {
        super(title);
        this.title = title;
        this.supplier = supplier;
        this.getter = getter;
        this.setter = setter;
        this.displayNameGetter = displayNameGetter;
    }

    @Override
    public Widget createWidget(GameSettings options, int x, int y, int width)
    {
        return new OptionButton(x, y, width, 20, this, this.getTitle(), (button) -> {
            List<E> list = this.supplier.get();
            if(list.isEmpty())
                return;
            this.nextItem();
            button.setMessage(this.getTitle());
        }) {
            @Override
            public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
            {
                List<E> list = GunListOption.this.supplier.get();
                this.active = !list.isEmpty();
                super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
            }
        };
    }

    @Nullable
    public E get()
    {
        if(this.selected == null)
        {
            this.selected = this.getter.get();
        }
        List<E> list = this.supplier.get();
        E e = list.stream().filter(c -> c.getLocation().equals(this.selected)).findFirst().orElse(null);
        if(e == null && list.size() > 0)
        {
            e = list.get(0);
            this.selected = e.getLocation();
        }
        return e;
    }

    public void set(E e)
    {
        List<E> list = this.supplier.get();
        if(list.indexOf(e) != -1)
        {
            this.setter.accept(e.getLocation());
            this.selected = e.getLocation();
        }
    }

    private void nextItem()
    {
        List<E> list = this.supplier.get();
        E current = this.get();
        if(current != null)
        {
            int nextIndex = (list.indexOf(current) + 1) % list.size();
            E next = list.get(nextIndex);
            this.set(next);
        }
    }

    public ITextComponent getTitle()
    {
        ITextComponent component = new TranslationTextComponent("cgm.option_list.no_items");
        E e = this.get();
        if(e != null)
        {
            component = this.displayNameGetter.apply(e);
        }
        return new TranslationTextComponent(this.title + ".format", component);
    }
}
