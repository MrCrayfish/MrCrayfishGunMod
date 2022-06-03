package com.tac.guns.client.handler.command;

import com.google.gson.*;
import com.tac.guns.Config;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.common.Gun;
import com.tac.guns.common.NetworkGunManager;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageUpdateGuns;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.Level;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static com.tac.guns.GunMod.LOGGER;

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
    private String previousWeaponTag = "";
    private TaCWeaponDevModes prevMode;
    private TaCWeaponDevModes mode;
    private HashMap<String, Gun> map = new HashMap<>();

    public TaCWeaponDevModes getMode() {return this.mode;}
    public void setMode(TaCWeaponDevModes mode) {this.mode = mode;}

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(!Config.COMMON.development.enableTDev.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        CommandsHandler ch = CommandsHandler.get();
        if(ch == null || ch.getCatCurrentIndex() != 1)
            return;
        if ((mc.player.getHeldItemMainhand() == null || mc.player.getHeldItemMainhand() == ItemStack.EMPTY || !(mc.player.getHeldItemMainhand().getItem() instanceof TimelessGunItem)))
            return;
        TimelessGunItem gunItem = (TimelessGunItem) mc.player.getHeldItemMainhand().getItem();
        if(this.previousWeaponTag == "")
            this.previousWeaponTag = gunItem.getTranslationKey();
        if(this.previousWeaponTag != gunItem.getTranslationKey())
            resetData();

        if(this.prevMode == null)
            this.prevMode = this.mode;
        else if(this.prevMode != this.mode)
        {
            resetData();
            this.prevMode = this.mode;
            ensureData(ch);
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event)
    {
        if(!Config.COMMON.development.enableTDev.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        if ((mc.player.getHeldItemMainhand() == null || mc.player.getHeldItemMainhand() == ItemStack.EMPTY || !(mc.player.getHeldItemMainhand().getItem() instanceof TimelessGunItem)))
            return;
        CommandsHandler ch = CommandsHandler.get();
        if(ch == null || ch.getCatCurrentIndex() != 1)
            return;
        TimelessGunItem gunItem = (TimelessGunItem) mc.player.getHeldItemMainhand().getItem();
        if(this.previousWeaponTag != gunItem.getTranslationKey())
            resetData();
        ensureData(ch); // Always ensure we are modifying, based on stored instance, instead of resetting everytime we swap weapons TODO: SWAP INTO USING PRIVATE MAP
        if(ch.getCatGlobal(1) != null && this.mode != null)
        {
            //TODO: HANDLE FOR PER MODULE, BEFORE APPLICATION, SAVE DATA ON INSTANCE TO SERIALIZE LATER.
            switch (this.mode)
            {
                case general:
                    this.handleGeneralMod(event, gunItem);

                case reloads:
                    break;

                case projectile:
                    break;

                case display:
                    break;

                case flash:
                    this.handleFlashMod(event, gunItem);

                case zoom:
                    this.handleZoomMod(event, gunItem);

                case scope:
                    this.handleScopeMod(event, gunItem);

                case barrel:
                    this.handleBarrelMod(event, gunItem);

                case oldScope:
                    this.handleOldScopeMod(event, gunItem);

                case pistolScope:
                    this.handlePistolScopeMod(event, gunItem);

                case pistolBarrel:
                    this.handlePistolBarrelMod(event, gunItem);

                default:
                    break;
            }
            NetworkGunManager manager = NetworkGunManager.get();
            if (manager != null)
                PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());
        }

    }

    int rateMod = 0;
    float recoilAngleMod=0;
    float recoilKickMod = 0;
    float horizontalRecoilAngleMod = 0;
    float cameraRecoilModifierMod = 0;
    float weaponRecoilDurationMod = 0;
    float recoilAdsReductionMod = 0;
    int projectileAmountMod = 0;
    float spreadMod = 0;
    float weightKiloMod = 0;
    private void handleGeneralMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem)
    {
        //TODO: After each handle we must save our data, this is how we can maintain simple usage of our pos data across multiple modules
        //ch.getCatGlobal(1).put(gun.getTranslationKey(), weaponData);
    }

    private void handleZoomMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handlePositionedMod(event, gunItem);
    }

    private void handleFlashMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private void handleScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handleBarrelMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handleOldScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handlePistolScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handlePistolBarrelMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) // "zoom" extends positioned
    {
        this.handleScaledPositionedMod(event, gunItem);
    }

    private double xMod = 0; public double getxMod() {return this.xMod;}
    private double yMod = 0; public double getyMod() {return this.yMod;}
    private double zMod = 0; public double getzMod() {return this.zMod;}
    private void handlePositionedMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem)
    {
        double stepModifier = 1;
        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        boolean isControlDown = KeyBinds.CONTROLLY.isKeyDown() || KeyBinds.CONTROLLYR.isKeyDown(); // Increase Module Size
        boolean isShiftDown = KeyBinds.SHIFTY.isKeyDown() || KeyBinds.SHIFTYR.isKeyDown(); // Increase Step Size
        boolean isAltDown = KeyBinds.ALTY.isKeyDown() || KeyBinds.ALTYR.isKeyDown(); // Swap X -> Z modify

        if(isShiftDown)
            stepModifier*=10;
        if(isControlDown)
            stepModifier/=10;

        if (isLeft)
            this.xMod -= 0.1*stepModifier;
        else if (isRight)
            this.xMod += 0.1*stepModifier;
        else if (isUp && isAltDown)
            this.zMod -= 0.1*stepModifier; // Forward
        else if (isDown && isAltDown)
            this.zMod += 0.1*stepModifier; // Backward
        else if (isUp)
            this.yMod += 0.1*stepModifier;
        else if (isDown)
            this.yMod -= 0.1*stepModifier;

        NetworkGunManager manager = NetworkGunManager.get();
        if (manager != null) {
            PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());
        }

        CompoundNBT gun = gunItem.getGun().copy().serializeNBT(); // Copy to ensure we are grabbing a copy of this data.
        if(this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }
        else {
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }

        Gun gunUpd = new Gun();
        gunUpd.deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunUpd);
    }

    private double sizeMod = 0;
    public double getSizeMod() {return this.sizeMod;}
    private void handleScaledPositionedMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem)
    {
        this.handlePositionedMod(event, gunItem);
        boolean isPeriodDown = KeyBinds.SIZE_OPT.isKeyDown(); // Increase Step Size

        if(isPeriodDown) {
            double stepModifier = 1;
            boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
            boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
            boolean isShiftDown = KeyBinds.SHIFTY.isKeyDown() || KeyBinds.SHIFTYR.isKeyDown(); // Increase Step Size

            if (isShiftDown)
                stepModifier *= 10;

            else if (isUp)
                this.sizeMod += 0.015 * stepModifier;
            else if (isDown)
                this.sizeMod -= 0.015 * stepModifier;
        }
        CompoundNBT gun = gunItem.getGun().copy().serializeNBT(); // Ensure we are grabbing a copy of this data.
        if(this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        else {
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        Gun gunUpd = new Gun();
        gunUpd.deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunUpd);
    }

    private double casedGetScale() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
        switch (this.mode)
        {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getSize();
            case scope:
                return gunItem.getGun().canAttachType(IAttachment.Type.SCOPE) ? gunItem.getGun().getModules().getAttachments().getScope().getScale() : 0;
            case barrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.BARREL) ? gunItem.getGun().getModules().getAttachments().getBarrel().getScale() : 0;
            case oldScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.OLD_SCOPE) ? gunItem.getGun().getModules().getAttachments().getOldScope().getScale() : 0;
            case pistolScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE) ? gunItem.getGun().getModules().getAttachments().getPistolScope().getScale() : 0;
            case pistolBarrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_BARREL) ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getScale() : 0;
        }
        return 0;
    }
    private double casedGetX() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
        switch (this.mode)
        {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getXOffset();
            case scope:
                return gunItem.getGun().canAttachType(IAttachment.Type.SCOPE) ? gunItem.getGun().getModules().getAttachments().getScope().getXOffset() : 0;
            case barrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.BARREL) ? gunItem.getGun().getModules().getAttachments().getBarrel().getXOffset() : 0;
            case oldScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.OLD_SCOPE) ? gunItem.getGun().getModules().getAttachments().getOldScope().getXOffset() : 0;
            case pistolScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE) ? gunItem.getGun().getModules().getAttachments().getPistolScope().getXOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_BARREL) ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getXOffset() : 0;
        }
        return 0;
    }
    private double casedGetY() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
        switch (this.mode)
        {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getYOffset();
            case scope:
                return gunItem.getGun().canAttachType(IAttachment.Type.SCOPE) ? gunItem.getGun().getModules().getAttachments().getScope().getYOffset() : 0;
            case barrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.BARREL) ? gunItem.getGun().getModules().getAttachments().getBarrel().getYOffset() : 0;
            case oldScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.OLD_SCOPE) ? gunItem.getGun().getModules().getAttachments().getOldScope().getYOffset() : 0;
            case pistolScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE) ? gunItem.getGun().getModules().getAttachments().getPistolScope().getYOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_BARREL) ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getYOffset() : 0;
        }
        return 0;
    }
    private double casedGetZ() {
        TimelessGunItem gunItem = (TimelessGunItem) Minecraft.getInstance().player.getHeldItemMainhand().getItem();
        switch (this.mode)
        {
            case flash:
                return gunItem.getGun().getDisplay().getFlash().getZOffset();
            case scope:
                return gunItem.getGun().canAttachType(IAttachment.Type.SCOPE) ? gunItem.getGun().getModules().getAttachments().getScope().getZOffset() : 0;
            case barrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.BARREL) ? gunItem.getGun().getModules().getAttachments().getBarrel().getZOffset() : 0;
            case oldScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.OLD_SCOPE) ? gunItem.getGun().getModules().getAttachments().getOldScope().getZOffset() : 0;
            case pistolScope:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_SCOPE) ? gunItem.getGun().getModules().getAttachments().getPistolScope().getZOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().canAttachType(IAttachment.Type.PISTOL_BARREL) ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getZOffset() : 0;
        }
        return 0;
    }

    private void resetData() {
        this.previousWeaponTag = "";
        this.xMod = 0;
        this.yMod = 0;
        this.zMod = 0;
        this.sizeMod = 0;
    }
    private void ensureData(CommandsHandler ch) {return;}
    public void exportData() {
        if(this.map.get(this.previousWeaponTag) == null) {LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN."); return;}
        GsonBuilder gsonB = new GsonBuilder().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting().disableJdkUnsafe().setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).serializeSpecialFloatingPointValues();;
        String jsonString = gsonB.create().toJson(this.map.get(this.previousWeaponTag));//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
        this.writeExport(jsonString);
    }
    private void writeExport(String jsonString) {
        try
        {
            File dir = new File(Config.COMMON.development.TDevPath.get()+"\\tac_export\\");
            dir.mkdir();
            FileWriter dataWriter = new FileWriter (dir.getAbsolutePath() +"\\"+ this.previousWeaponTag + "_export.json");
            dataWriter.write(jsonString);
            dataWriter.close();
            LOGGER.log(Level.INFO, "WEAPON EDITOR EXPORTED FILE ( "+this.previousWeaponTag + "export.txt ). BE PROUD!");
        }
        catch (IOException e)
        {
            LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT, NO FILE CREATED!!! NO ACCESS IN PATH?. CONTACT CLUMSYALIEN.");
        }
    }
    // This is built manually, ensuring that I have both tested and built the new system around any new category.
    public enum TaCWeaponDevModes {
        general("General"),
        reloads("Reloads"),
        projectile("Projectile"),
        /*SOUNDS(""),*/
        display("Display"),
        flash("Flash"),
        zoom("Zoom0"),
        scope("Scope"),
        barrel("Barrel"),
        oldScope("OldScope"),
        pistolScope("PistolScope"),
        pistolBarrel("PistolBarrel");

        public String getTagName() {return tagName;}
        TaCWeaponDevModes(String name) {this.tagName = name;}

        private String tagName;
    }
    public static String formattedModeContext() {
        String result = "\n";
        for (TaCWeaponDevModes mode : TaCWeaponDevModes.values())
        {
            result += mode.tagName +"\n";
        }
        return result;
    }
}
