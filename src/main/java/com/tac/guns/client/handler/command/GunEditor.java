package com.tac.guns.client.handler.command;

import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageUpdateGuns;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

public class GunEditor
{
    private static GunEditor instance;

    public static GunEditor get()
    {
        if(instance == null)
        {
            instance = new GunEditor();
        }
        return instance;
    }

    private GunEditor() {}

    private double xMod = 0;
    private double yMod = 0;
    private double zMod = 0;
    private double sizeMod = 0;

    private String previousWeaponTag = "";
    private TaCDevModes mode;

    public TaCDevModes getMode() {return this.mode;}
    public void setMode(TaCDevModes mode) {this.mode = mode;}
    public double getxMod() {return this.xMod;}
    public double getyMod() {return this.yMod;}
    public double getzMod() {return this.zMod;}
    public double getSizeMod() {return this.sizeMod;}
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        // Basics overview
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;

        if ((mc.player.getHeldItemMainhand() == null ||
                mc.player.getHeldItemMainhand() == ItemStack.EMPTY))
            return;
        if(!(mc.player.getHeldItemMainhand().getItem() instanceof TimelessGunItem))
            return;
        CommandsHandler ch = CommandsHandler.get();
        if(ch == null || ch.getCatCurrentIndex() != 1)
            return;
        /*if (!Config.COMMON.development.guiElementEditor.get())
            return;*/
        TimelessGunItem gun = (TimelessGunItem) mc.player.getHeldItemMainhand().getItem();
        if(this.previousWeaponTag != gun.getTranslationKey())
            resetData();
        ensureData(ch); // Always ensure we are modifying, based on stored instance, instead of resetting everytime we swap weapons

        Gun weaponData;
        if(ch.getCatGlobal(1) != null)
        {
            weaponData = gun.getGun().copy();

            double stepModifier = 1;
            boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
            boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
            boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
            boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
            boolean isControlDown = event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT || event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT; // Increase Module Size
            boolean isShiftDown = event.getKey() == GLFW.GLFW_KEY_LEFT_SHIFT || event.getKey() == GLFW.GLFW_KEY_RIGHT_SHIFT; // Increase Step Size
            boolean isAltDown = event.getKey() == GLFW.GLFW_KEY_LEFT_ALT || event.getKey() == GLFW.GLFW_KEY_RIGHT_ALT; // Swap X -> Z modify

            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;

            if (isLeft)
                this.xMod -= 0.1*stepModifier;
            else if (isRight)
                this.xMod += 0.1*stepModifier;
            else if (isUp)
                this.yMod += 0.1*stepModifier;
            else if (isDown)
                this.yMod -= 0.1*stepModifier;
            else if (isUp && isAltDown)
                this.zMod -= 0.1*stepModifier; // Forward
            else if (isDown && isAltDown)
                this.zMod += 0.1*stepModifier; // Backward

            //TODO: HANDLE FOR PER MODULE, BEFORE APPLICATION, SAVE DATA ON INSTANCE TO SERIALIZE LATER.

            NetworkGunManager manager = NetworkGunManager.get();
            if (manager != null) {
                PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());
            }
            ch.getCatGlobal(1).put(gun.getTranslationKey(), weaponData);
        }
    }

    //
    private void handleZoomModification(InputEvent.KeyInputEvent event)
    {

    }

    private void resetData()
    {
        this.previousWeaponTag = "";
        this.xMod = 0;
        this.yMod = 0;
        this.zMod = 0;
    }
    private void ensureData(CommandsHandler ch)
    {

        this.xMod = 0;
        this.yMod = 0;
        this.zMod = 0;
    }

    public enum TaCDevModes
    {
        general("general"),
        reloads("reloads"),
        projectile("projectile"),/*
        SOUNDS(""),*/
        display("display"),
        flash("flash"),
        zoom("zoom"),
        scope("scope"),
        barrel("barrel"),
        oldScope("oldScope"),
        pistolScope("pistolScope"),
        pistolBarrel("pistolBarrel");

        public String getName() {return name;}
        TaCDevModes(String name)
        {
            this.name = name;
        }

        private String name;
    }

    public static String formattedModeContext()
    {
        String result = "\n";
        for (TaCDevModes mode : TaCDevModes.values())
        {
            result += mode.name+"\n";
        }
        return result+"\n";
    }
}
