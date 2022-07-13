package com.mrcrayfish.guns.client;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
public class KeyBinds
{
    public static final KeyMapping KEY_RELOAD = new KeyMapping("key.cgm.reload", GLFW.GLFW_KEY_R, "key.categories.cgm");
    public static final KeyMapping KEY_UNLOAD = new KeyMapping("key.cgm.unload", GLFW.GLFW_KEY_U, "key.categories.cgm");
    public static final KeyMapping KEY_ATTACHMENTS = new KeyMapping("key.cgm.attachments", GLFW.GLFW_KEY_Z, "key.categories.cgm");

    public static void registerKeyMappings(RegisterKeyMappingsEvent event)
    {
        event.register(KEY_RELOAD);
        event.register(KEY_UNLOAD);
        event.register(KEY_ATTACHMENTS);
    }
}
