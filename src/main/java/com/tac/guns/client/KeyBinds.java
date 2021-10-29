package com.tac.guns.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * Author: MrCrayfish
 */
public class KeyBinds
{
    public static final KeyBinding KEY_RELOAD = new KeyBinding("key.tac.reload", GLFW.GLFW_KEY_R, "key.categories.tac");
    public static final KeyBinding KEY_UNLOAD = new KeyBinding("key.tac.unload", GLFW.GLFW_KEY_U, "key.categories.tac");
    public static final KeyBinding KEY_ATTACHMENTS = new KeyBinding("key.tac.attachments", GLFW.GLFW_KEY_Z, "key.categories.tac");

    public static final KeyBinding KEY_FIRESELECT = new KeyBinding("key.tac.fireSelect", GLFW.GLFW_KEY_G, "key.categories.tac");
    public static final KeyBinding KEY_INSPECT = new KeyBinding("key.tac.inspect", GLFW.GLFW_KEY_H, "key.categories.tac");
    public static final KeyBinding KEY_SIGHT_SWITCH = new KeyBinding("key.tac.sight_switch", GLFW.GLFW_KEY_V, "key.categories.tac");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(KEY_RELOAD);
        ClientRegistry.registerKeyBinding(KEY_UNLOAD);
        ClientRegistry.registerKeyBinding(KEY_ATTACHMENTS);
        ClientRegistry.registerKeyBinding(KEY_FIRESELECT);
        ClientRegistry.registerKeyBinding(KEY_INSPECT);
        ClientRegistry.registerKeyBinding(KEY_SIGHT_SWITCH);
    }
}
