package com.tac.guns.client;

import com.tac.guns.Config;
import net.minecraft.client.MouseHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class KeyBinds
{
    public static final KeyBinding KEY_RELOAD = new KeyBinding("key.tac.reload", GLFW.GLFW_KEY_R, "key.categories.tac");
    public static final KeyBinding KEY_UNLOAD = new KeyBinding("key.tac.unload", GLFW.GLFW_KEY_U, "key.categories.tac");
    public static final KeyBinding KEY_ATTACHMENTS = new KeyBinding("key.tac.attachments", GLFW.GLFW_KEY_Z, "key.categories.tac");

    public static final KeyBinding KEY_FIRESELECT = new KeyBinding("key.tac.fireSelect", GLFW.GLFW_KEY_G, "key.categories.tac");
    public static final KeyBinding KEY_INSPECT = new KeyBinding("key.tac.inspect", GLFW.GLFW_KEY_H, "key.categories.tac");
    public static final KeyBinding KEY_SIGHT_SWITCH = new KeyBinding("key.tac.sight_switch", GLFW.GLFW_KEY_V, "key.categories.tac");
    public static final KeyBinding KEY_ACTIVATE_SIDE_RAIL = new KeyBinding("key.tac.activateSideRail", GLFW.GLFW_KEY_B, "key.categories.tac");

    public static final KeyBinding KEY_ADS = new KeyBinding("key.tac.aim_sights", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.tac");
    public static final KeyBinding COLOR_BENCH = new KeyBinding("key.tac.color_bench", GLFW.GLFW_KEY_PAGE_DOWN, "key.categories.tac");

    // ALL DEVELOPMENT TEMPS.
    public static final KeyBinding SHIFTY = new KeyBinding("key.tac.ss", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories.tac");
    public static final KeyBinding CONTROLLY = new KeyBinding("key.tac.cc", GLFW.GLFW_KEY_LEFT_CONTROL, "key.categories.tac");
    public static final KeyBinding ALTY = new KeyBinding("key.tac.aa", GLFW.GLFW_KEY_LEFT_ALT, "key.categories.tac");
    public static final KeyBinding SHIFTYR = new KeyBinding("key.tac.ssr", GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.tac");
    public static final KeyBinding CONTROLLYR = new KeyBinding("key.tac.ccr", GLFW.GLFW_KEY_RIGHT_CONTROL, "key.categories.tac");
    public static final KeyBinding ALTYR = new KeyBinding("key.tac.aar", GLFW.GLFW_KEY_RIGHT_ALT, "key.categories.tac");
    public static final KeyBinding SIZE_OPT = new KeyBinding("key.tac.sizer", GLFW.GLFW_KEY_PERIOD, "key.categories.tac");


    public static final KeyBinding P = new KeyBinding("key.tac.p", GLFW.GLFW_KEY_P, "key.categories.tac");
    public static final KeyBinding L = new KeyBinding("key.tac.l", GLFW.GLFW_KEY_L, "key.categories.tac");
    public static final KeyBinding O = new KeyBinding("key.tac.o", GLFW.GLFW_KEY_O, "key.categories.tac");
    public static final KeyBinding K = new KeyBinding("key.tac.k", GLFW.GLFW_KEY_K, "key.categories.tac");
    public static final KeyBinding M = new KeyBinding("key.tac.m", GLFW.GLFW_KEY_M, "key.categories.tac");
    public static final KeyBinding I = new KeyBinding("key.tac.i", GLFW.GLFW_KEY_I, "key.categories.tac");
    public static final KeyBinding J = new KeyBinding("key.tac.j", GLFW.GLFW_KEY_J, "key.categories.tac");
    public static final KeyBinding N = new KeyBinding("key.tac.n", GLFW.GLFW_KEY_N, "key.categories.tac");

    public static void register()
    {
        ClientRegistry.registerKeyBinding(KEY_RELOAD);
        ClientRegistry.registerKeyBinding(KEY_UNLOAD);
        ClientRegistry.registerKeyBinding(KEY_ATTACHMENTS);
        ClientRegistry.registerKeyBinding(KEY_FIRESELECT);
        ClientRegistry.registerKeyBinding(KEY_INSPECT);
        ClientRegistry.registerKeyBinding(KEY_SIGHT_SWITCH);
        ClientRegistry.registerKeyBinding(KEY_ADS);
        ClientRegistry.registerKeyBinding(COLOR_BENCH);
        ClientRegistry.registerKeyBinding(KEY_ACTIVATE_SIDE_RAIL);
        if(Config.COMMON.development.enableTDev.get())
        {
            ClientRegistry.registerKeyBinding(SHIFTY);
            ClientRegistry.registerKeyBinding(CONTROLLY);
            ClientRegistry.registerKeyBinding(ALTY);
            ClientRegistry.registerKeyBinding(SHIFTYR);
            ClientRegistry.registerKeyBinding(CONTROLLYR);
            ClientRegistry.registerKeyBinding(ALTYR);
            ClientRegistry.registerKeyBinding(SIZE_OPT);

            ClientRegistry.registerKeyBinding(P);
            ClientRegistry.registerKeyBinding(L);
            ClientRegistry.registerKeyBinding(O);
            ClientRegistry.registerKeyBinding(K);
            ClientRegistry.registerKeyBinding(M);
            ClientRegistry.registerKeyBinding(I);
            ClientRegistry.registerKeyBinding(J);
            ClientRegistry.registerKeyBinding(N);
        }
    }
}
