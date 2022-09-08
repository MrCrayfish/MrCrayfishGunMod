package com.mrcrayfish.guns.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
public class KeyBinds
{
    public static final KeyMapping KEY_RELOAD = new KeyMapping("key.cgm.reload", GLFW.GLFW_KEY_2, "key.categories.cgm");
    public static final KeyMapping KEY_UNLOAD = new KeyMapping("key.cgm.unload", GLFW.GLFW_KEY_3, "key.categories.cgm");
    public static final KeyMapping KEY_ATTACHMENTS = new KeyMapping("key.cgm.attachments", GLFW.GLFW_KEY_1, "key.categories.cgm");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(KEY_RELOAD);
        ClientRegistry.registerKeyBinding(KEY_UNLOAD);
        ClientRegistry.registerKeyBinding(KEY_ATTACHMENTS);
    }
}
