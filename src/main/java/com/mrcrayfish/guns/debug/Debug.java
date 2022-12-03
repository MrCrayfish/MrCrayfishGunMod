package com.mrcrayfish.guns.debug;

import com.mrcrayfish.guns.client.util.Easings;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugButton;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugEnum;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugSlider;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugToggle;
import com.mrcrayfish.guns.interfaces.IGunModifier;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
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
    private static final Map<Item, Gun> GUNS = new HashMap<>();
    private static final Map<Item, Scope> SCOPES = new HashMap<>();
    private static boolean forceAim = false;

    public static Gun getGun(GunItem item)
    {
        return GUNS.computeIfAbsent(item, item1 -> item.getGun().copy());
    }

    public static Scope getScope(ScopeItem item)
    {
        return SCOPES.computeIfAbsent(item, item1 -> item.getProperties().copy());
    }

    public static boolean isForceAim()
    {
        return forceAim;
    }

    public static void setForceAim(boolean forceAim)
    {
        Debug.forceAim = forceAim;
    }

    public static class Menu implements IEditorMenu
    {
        @Override
        public Component getEditorLabel()
        {
            return Component.literal("Editor Menu");
        }

        @Override
        public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
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
        public Component getEditorLabel()
        {
            return Component.literal("Settings");
        }

        @Override
        public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
        {
            widgets.add(Pair.of(Component.literal("Force Aim"), () -> new DebugToggle(Debug.forceAim, value -> Debug.forceAim = value)));
        }
    }
}
