package com.tac.guns.client.handler.command;

import com.tac.guns.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class GuiEditor
{
    private static GuiEditor instance;

    public static GuiEditor get()
    {
        if(instance == null)
        {
            instance = new GuiEditor();
        }
        return instance;
    }

    private GuiEditor() {}

    private int xMod = 0;
    private int yMod = 0;
    private int sizeMod = 0;

    public int getxMod() {return xMod;}
    public int getyMod() {return yMod;}
    public int getSizeMod() {return sizeMod;}

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        // Basics overview
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if (!Config.COMMON.development.enableTDev.get())
            return;

        // Check our index, TaC will have documentation on which index is being read standard, and how to check

        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;

        //boolean isControlDown = KeyBinds.KEY_ADS.matchesMouseKey(GLFW.GLFW_MOUSE_BUTTON_LEFT); // Increase Module Size
        boolean isShiftDown = event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT; // Increase Step Size

        //if(isControlDown)

        if (isLeft)
            xMod -= isShiftDown ? 10 : 1;
        else if (isRight)
            xMod += isShiftDown ? 10 : 1;
        else if (isUp)
            yMod += isShiftDown ? 10 : 1;
        else if (isDown)
            yMod -= isShiftDown ? 10 : 1;
    }
}
