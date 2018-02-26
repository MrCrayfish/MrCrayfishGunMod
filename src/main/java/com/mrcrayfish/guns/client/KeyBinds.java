package com.mrcrayfish.guns.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Author: MrCrayfish
 */
public class KeyBinds
{
    public static final KeyBinding KEY_AIM = new KeyBinding("key.aim", Keyboard.KEY_LMENU, "key.categories.gun");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(KEY_AIM);
    }
}
