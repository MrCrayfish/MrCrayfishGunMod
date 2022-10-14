package com.tac.guns.client.handler.command;

import static com.tac.guns.GunMod.LOGGER;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import com.google.gson.GsonBuilder;
import com.tac.guns.Config;
import com.tac.guns.client.InputHandler;
import com.tac.guns.common.Gun;
import com.tac.guns.common.tooling.CommandsHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ObjectRenderEditor
{
    private static ObjectRenderEditor instance;

    public static ObjectRenderEditor get()
    {
        if(instance == null)
        {
            instance = new ObjectRenderEditor();
        }
        return instance;
    }

    private ObjectRenderEditor() {}

    public int currElement = 0;
    private HashMap<Integer, RENDER_Element> elements = new HashMap<>();

    public RENDER_Element GetFromElements(int index)
    {
        return this.elements.get(index);
    }

    public static class RENDER_Element
    {
        private float xMod = 0;
        private float yMod = 0;
        private float zMod = 0;
        private float sizeMod = 0;

        public RENDER_Element(float x, float y, float z, float sizeX)
        {
            this.xMod = x;
            this.zMod = z;
            this.yMod = y;
            this.sizeMod = sizeX;
        }
        public float getxMod() {return xMod;}
        public float getyMod() {return yMod;}
        public float getzMod() {return zMod;}
        public float getSizeMod() {return sizeMod;}
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        // Basics overview
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if (!Config.COMMON.development.enableTDev.get() && CommandsHandler.get().getCatCurrentIndex() == 4)
            return;
        if(event.getKey() == GLFW.GLFW_KEY_1)
        {
            this.currElement = 1;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_2)
        {
            this.currElement = 2;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_3)
        {
            this.currElement = 3;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_4)
        {
            this.currElement = 4;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_5)
        {
            this.currElement = 5;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_6)
        {
            this.currElement = 6;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_7)
        {
            this.currElement = 7;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_8)
        {
            this.currElement = 8;
            //event.setCanceled(true);
            return;
        }
        else if(event.getKey() == GLFW.GLFW_KEY_9)
        {
            this.currElement = 9;
            //event.setCanceled(true);
            return;
        }

        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;

        boolean isControlDown = InputHandler.CONTROLLY.down || InputHandler.CONTROLLYR.down; // Increase Module Size
        boolean isShiftDown = event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT; // Increase Step Size
        boolean isAltDown = InputHandler.ALTY.down || InputHandler.ALTYR.down; // Swap X -> Z modify
        boolean isPeriodDown = InputHandler.SIZE_OPT.down;

        RENDER_Element element = this.elements.size() == 0 || !this.elements.containsKey(this.currElement) ? new RENDER_Element(0, 0,0, 0) : this.elements.get(this.currElement);
        float xMod = element.xMod;
        float yMod = element.yMod;
        float zMod = element.zMod;
        float sizeMod = element.sizeMod;
        float stepModifier = 1;
        /*if(isShiftDown)
            stepModifier*=10;*/
        if(isShiftDown)
            stepModifier/=10;

        if (isPeriodDown && isUp) {
            sizeMod += 0.05*stepModifier;
        }
        else if (isPeriodDown && isDown) {
            sizeMod -= 0.05*stepModifier;
        }
        else if (isControlDown && isUp) {
            zMod += 0.05*stepModifier;
        }
        else if (isControlDown && isDown) {
            zMod -= 0.05*stepModifier;
        }
        else if (isLeft) {
            xMod -= 0.05*stepModifier;
        }
        else if (isRight) {
            xMod += 0.05*stepModifier;
        }
        else if (isUp) {
            yMod += 0.05*stepModifier;
        }
        else if (isDown) {
            yMod -= 0.05*stepModifier;
        }

        this.elements.put(this.currElement, new RENDER_Element(xMod,yMod,zMod,sizeMod));
    }
    public void exportData() {
        this.elements.forEach((name, data) ->
        {
            if(this.elements.get(name) == null) {LOGGER.log(Level.ERROR, "OBJ_RENDER EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN."); return;}
            GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();

            String jsonString = gsonB.create().toJson(data);
            this.writeExport(jsonString, "OBJ_RENDER"+name);
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
            LOGGER.log(Level.INFO, "OBJ_RENDER EDITOR EXPORTED FILE ( "+name + "export.txt ). BE PROUD!");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.ERROR, "OBJ_RENDER EDITOR FAILED TO EXPORT, NO FILE CREATED!!! NO ACCESS IN PATH?. CONTACT CLUMSYALIEN.");
        }
    }
}
