package com.mrcrayfish.guns.debug;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugButton;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugSlider;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugToggle;
import com.mrcrayfish.guns.interfaces.IGunModifier;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
public class Debug
{
    private static final Map<Item, MutableGun> GUNS = new HashMap<>();
    private static final Map<Item, MutableScope> SCOPES = new HashMap<>();
    private static boolean forceAim = false;

    public static MutableGun getGun(Item item)
    {
        return GUNS.computeIfAbsent(item, item1 -> new MutableGun(item));
    }

    public static MutableScope getScope(Item item)
    {
        return SCOPES.computeIfAbsent(item, item1 -> new MutableScope(item));
    }

    public static boolean isForceAim()
    {
        return forceAim;
    }

    public static class Menu implements IEditorMenu
    {
        @Override
        public Component getLabel()
        {
            return Component.literal("Editor Menu");
        }

        @Override
        public void getWidgets(List<Pair<Component, Supplier<AbstractWidget>>> widgets)
        {
            ItemStack heldItem = Objects.requireNonNull(Minecraft.getInstance().player).getMainHandItem();
            if(heldItem.getItem() instanceof GunItem gunItem)
            {
                widgets.add(Pair.of(Component.translatable(gunItem.getDescriptionId()), () -> new DebugButton(Component.literal("Edit"), btn -> {
                    Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, getGun(gunItem)));
                })));
            }
            widgets.add(Pair.of(Component.literal("Settings"), () -> new DebugButton(Component.literal(">"), btn -> {
                Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, new Settings()));
            })));
        }
    }

    public static class Settings implements IEditorMenu
    {
        @Override
        public Component getLabel()
        {
            return Component.literal("Settings");
        }

        @Override
        public void getWidgets(List<Pair<Component, Supplier<AbstractWidget>>> widgets)
        {
            widgets.add(Pair.of(Component.literal("Force Aim"), () -> new DebugToggle(Debug.forceAim, value -> Debug.forceAim = value)));
        }
    }

    public static class MutableGun implements IEditorMenu
    {
        private final Item item;

        public MutableGun(Item item)
        {
            this.item = item;
        }

        @Override
        public Component getLabel()
        {
            return Component.translatable(this.item.getDescriptionId());
        }

        @Override
        public void getWidgets(List<Pair<Component, Supplier<AbstractWidget>>> widgets)
        {
            ItemStack heldItem = Objects.requireNonNull(Minecraft.getInstance().player).getMainHandItem();
            ItemStack scope = Gun.getScopeStack(heldItem);
            if(!scope.isEmpty())
            {
                widgets.add(Pair.of(scope.getItem().getName(scope), () -> new DebugButton(Component.literal("Edit"), btn -> {
                    Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, getScope(scope.getItem())));
                })));
            }
        }
    }

    public static class MutableScope extends Scope implements IEditorMenu
    {
        public MutableScope(Item item)
        {
            if(item instanceof ScopeItem)
            {
                Scope scope = ((ScopeItem) item).getProperties();
                this.setAdditionalZoom(scope.getAdditionalZoom());
                this.setCenterOffset(scope.getCenterOffset());
                this.setStable(scope.isStable());
                this.setViewFinderOffset(scope.getViewFinderOffset());
                this.setAimFov(scope.getAimFov());
                this.setModifiers(scope.getModifiers());
            }
        }

        public void setAdditionalZoom(float additionalZoom)
        {
            this.additionalZoom = additionalZoom;
        }

        public void setCenterOffset(double centerOffset)
        {
            this.centerOffset = centerOffset;
        }

        public void setStable(boolean stable)
        {
            this.stable = stable;
        }

        public void setViewFinderOffset(double viewFinderOffset)
        {
            this.viewFinderOffset = viewFinderOffset;
        }

        public void setAimFov(double aimFov)
        {
            this.aimFov = aimFov;
        }

        public void setModifiers(IGunModifier ... modifiers)
        {
            this.modifiers = modifiers;
        }

        @Override
        public Component getLabel()
        {
            return Component.literal("Scope");
        }

        @Override
        public void getWidgets(List<Pair<Component, Supplier<AbstractWidget>>> widgets)
        {
            widgets.add(Pair.of(Component.literal("Additional Zoom"), () -> new DebugSlider(0.0, 0.5, this.additionalZoom, 0.05, 3, value -> this.additionalZoom = value.floatValue())));
            widgets.add(Pair.of(Component.literal("Center Offset"), () -> new DebugSlider(0.0, 4.0, this.centerOffset, 0.025, 4, value -> this.centerOffset = value)));
            widgets.add(Pair.of(Component.literal("View Finder Offset"), () -> new DebugSlider(0.0, 5.0, this.viewFinderOffset, 0.05, 3, value -> this.viewFinderOffset = value)));
            widgets.add(Pair.of(Component.literal("Aim FOV"), () -> new DebugSlider(1.0, 100.0, this.aimFov, 1.0, 4, value -> this.aimFov = value)));
        }
    }
}
