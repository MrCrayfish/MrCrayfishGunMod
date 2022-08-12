package com.tac.guns.client.handler.command;

import com.google.gson.GsonBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.common.Gun;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.common.tooling.CommandsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static com.tac.guns.GunMod.LOGGER;

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

    public int currElement = 0;
    private HashMap<Integer, GUI_Element> elements = new HashMap<>();

    public GUI_Element GetFromElements(int index)
    {
        return this.elements.get(index);
    }

    public static class GUI_Element
    {
        private double xMod = 0;
        private double yMod = 0;
        private double sizeXMod = 0;
        private double sizeYMod = 0;
        public GUI_Element(double x, double y, double sizeX, double sizeY)
        {
            this.xMod = x;
            this.yMod = y;
            this.sizeXMod = sizeX;
            this.sizeYMod = sizeY;
        }
        public int getxMod() {return (int)xMod;}
        public int getyMod() {return (int)yMod;}
        public int getSizeXMod() {return (int)sizeYMod;}
        public int getSizeYMod() {return (int)sizeXMod;}
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onKeyPressed(GuiScreenEvent.KeyboardKeyEvent event) {
        // Basics overview
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if (!Config.COMMON.development.enableTDev.get() && CommandsHandler.get().getCatCurrentIndex() == 3)
            return;
        if(event.getKeyCode() == GLFW.GLFW_KEY_1)
        {
            this.currElement = 1;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_2)
        {
            this.currElement = 2;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_3)
        {
            this.currElement = 3;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_4)
        {
            this.currElement = 4;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_5)
        {
            this.currElement = 5;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_6)
        {
            this.currElement = 6;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_7)
        {
            this.currElement = 7;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_8)
        {
            this.currElement = 8;
            event.setCanceled(true);
            return;
        }
        else if(event.getKeyCode() == GLFW.GLFW_KEY_9)
        {
            this.currElement = 9;
            event.setCanceled(true);
            return;
        }

        boolean isLeft = event.getKeyCode() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKeyCode() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKeyCode() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKeyCode() == GLFW.GLFW_KEY_DOWN;

        boolean isShiftDown = event.getKeyCode() == GLFW.GLFW_KEY_LEFT_SHIFT; // Increase Step Size
        boolean isPeriodDown = KeyBinds.SIZE_OPT.isKeyDown();

        GUI_Element element = this.elements.size() == 0 || !this.elements.containsKey(this.currElement) ? new GUI_Element(0, 0, 0, 0) : this.elements.get(this.currElement);
        double xMod = element.xMod;
        double yMod = element.yMod;
        double sizeXMod = element.sizeXMod;
        double sizeYMod = element.sizeYMod;

        if (isPeriodDown && isUp) {
            sizeYMod += isPeriodDown ? 2.5 : 0.5;
        }
        else if (isPeriodDown && isDown) {
            sizeYMod -= isPeriodDown ? 2.5 : 0.5;
        }
        else if (isPeriodDown && isRight) {
            sizeXMod += isPeriodDown ? 2.5 : 0.5;
        }
        else if (isPeriodDown && isLeft) {
            sizeXMod -= isPeriodDown ? 2.5 : 0.5;
        }
        else if (isLeft) {
            xMod -= isShiftDown ? 2.5 : 0.5;
        }
        else if (isRight) {
            xMod += isShiftDown ? 2.5 : 0.5;
        }
        else if (isUp) {
            yMod -= isShiftDown ? 2.5 : 0.5;
        }
        else if (isDown) {
            yMod += isShiftDown ? 2.5 : 0.5;
        }

        this.elements.put(this.currElement, new GUI_Element(xMod,yMod,sizeXMod,sizeYMod));
        //Minecraft.getInstance().player.s
    }
    public void exportData() {
        this.elements.forEach((name, data) ->
        {
            if(this.elements.get(name) == null) {LOGGER.log(Level.ERROR, "GUI EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN."); return;}
            GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();

            String jsonString = gsonB.create().toJson(data);//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
            this.writeExport(jsonString, "gui_"+name);
        });
    }
    private void writeExport(String jsonString, String name) {
        try
        {
            File dir = new File(Config.COMMON.development.TDevPath.get()+"\\tac_export\\");
            dir.mkdir();
            FileWriter dataWriter = new FileWriter (dir.getAbsolutePath() +"\\"+ name + "_export.json");
            dataWriter.write(jsonString);
            dataWriter.close();
            LOGGER.log(Level.INFO, "WEAPON EDITOR EXPORTED FILE ( "+name + "export.txt ). BE PROUD!");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT, NO FILE CREATED!!! NO ACCESS IN PATH?. CONTACT CLUMSYALIEN.");
        }
    }
}
