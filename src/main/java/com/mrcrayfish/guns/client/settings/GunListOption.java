package com.mrcrayfish.guns.client.settings;

import com.mrcrayfish.guns.interfaces.IResourceLocation;

/**
 * Author: MrCrayfish
 */
public class GunListOption<E extends IResourceLocation>
{
    /*private final Supplier<List<E>> supplier;
    private final Supplier<ResourceLocation> getter;
    private final Consumer<ResourceLocation> setter;
    private final Function<E, Component> displayNameGetter;
    private IAdditionalRenderer renderer = (button, poseStack, partialTicks) -> {};

    public GunListOption(String translationKey, Supplier<List<E>> supplier, Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter, Function<E, Component> displayNameGetter)
    {
        super(translationKey);
        this.supplier = supplier;
        this.getter = getter;
        this.setter = setter;
        this.displayNameGetter = displayNameGetter;
    }

    public GunListOption<E> setRenderer(@Nullable IAdditionalRenderer renderer)
    {
        this.renderer = renderer;
        return this;
    }

    @Override
    public AbstractWidget createButton(Options options, int x, int y, int width)
    {
        return new CycleButton.Builder<E>(this.displayNameGetter)
                .withValues(this.supplier.get())
                .withInitialValue(this.getter.get())
                .create(x, y, width, 20, new TextComponent(""));

        *//*return new CycleButton.Builder<E>(x, y, width, 20, this, this.getTitle(), (button) -> {
            List<E> list = this.supplier.get();
            if(list.isEmpty())
                return;
            this.nextItem(Screen.hasShiftDown() ? -1 : 1);
            button.setMessage(this.getTitle());
        }) {
            @Override
            public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
            {
                List<E> list = GunListOption.this.supplier.get();
                this.active = !list.isEmpty();
                super.renderButton(poseStack, mouseX, mouseY, partialTicks);
                GunListOption.this.renderer.render(this, poseStack, partialTicks);
            }
        };*//*
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

    private void nextItem(int offset)
    {
        List<E> list = this.supplier.get();
        E current = this.get();
        if(current != null)
        {
            int nextIndex =  Math.floorMod(list.indexOf(current) + offset, list.size());
            E next = list.get(nextIndex);
            this.set(next);
        }
    }

    public Component getTitle()
    {
        Component component = Component.translatable("cgm.option_list.no_items");
        E e = this.get();
        if(e != null)
        {
            component = this.displayNameGetter.apply(e);
        }
        return Component.translatable(this.title + ".format", component);
    }

    public interface IAdditionalRenderer
    {
        void render(OptionButton button, PoseStack poseStack, float partialTicks);
    }*/
}
