package com.mrcrayfish.guns.debug;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugButton;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugToggle;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.ScopeItem;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class Debug
{
    private static final Map<Item, Gun> GUNS = new HashMap<>();
    private static final Map<Item, Scope> SCOPES = new HashMap<>();
    private static boolean forceAim = false;

    @SubscribeEvent
    public static void onServerStarting(ServerStartedEvent event)
    {
        // Resets the cache every time when joining a world
        event.getServer().execute(() ->
        {
            GUNS.clear();
            SCOPES.clear();
        });
    }

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
            return new TextComponent("Editor Menu");
        }

        @Override
        public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
        {
            ItemStack heldItem = Objects.requireNonNull(Minecraft.getInstance().player).getMainHandItem();
            if(heldItem.getItem() instanceof GunItem gunItem)
            {
                widgets.add(Pair.of(new TranslatableComponent(gunItem.getDescriptionId()), () -> new DebugButton(new TextComponent("Edit"), btn -> {
                    Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, getGun(gunItem)));
                })));
            }
            widgets.add(Pair.of(new TextComponent("Settings"), () -> new DebugButton(new TextComponent(">"), btn -> {
                Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, new Settings()));
            })));
        }
    }

    public static class Settings implements IEditorMenu
    {
        @Override
        public Component getEditorLabel()
        {
            return new TextComponent("Settings");
        }

        @Override
        public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
        {
            widgets.add(Pair.of(new TextComponent("Force Aim"), () -> new DebugToggle(Debug.forceAim, value -> Debug.forceAim = value)));
        }
    }
}
