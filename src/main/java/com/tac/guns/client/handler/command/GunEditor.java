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
import java.util.Locale;

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
    private boolean resetMode;
    public TaCWeaponDevModes getMode() {return this.mode;}
    public void setResetMode(boolean reset){this.resetMode = reset;}
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
        if(this.prevMode == null)
            this.prevMode = this.mode;
        else if(this.prevMode != this.mode && this.resetMode)
        {
            this.resetMode = false;
            resetData();
            if(this.map.containsKey(gunItem.getTranslationKey()))
                ensureData(this.map.get(gunItem.getTranslationKey()), gunItem.getGun().copy());
            this.prevMode = this.mode;
        }

        if(this.previousWeaponTag == "")
            this.previousWeaponTag = gunItem.getTranslationKey();
        else if(this.previousWeaponTag != gunItem.getTranslationKey())
        {
            this.previousWeaponTag = gunItem.getTranslationKey();
            resetData();
            if(this.map.containsKey(gunItem.getTranslationKey()))
                ensureData(this.map.get(gunItem.getTranslationKey()), gunItem.getGun().copy());
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
        if(ch.getCatGlobal(1) != null && this.mode != null)
        {
            //TODO: HANDLE FOR PER MODULE, BEFORE APPLICATION, SAVE DATA ON INSTANCE TO SERIALIZE LATER.
            switch (this.mode)
            {
                case general:
                    this.handleGeneralMod(event, gunItem);
                    break;
                case reloads:
                    break;

                case projectile:
                    break;

                case display:
                    break;

                case flash:
                    this.handleFlashMod(event, gunItem);
                    break;
                case zoom:
                    this.handleZoomMod(event, gunItem);
                    break;
                case scope:
                    this.handleScopeMod(event, gunItem);
                    break;
                case barrel:
                    this.handleBarrelMod(event, gunItem);
                    break;
                case oldScope:
                    this.handleOldScopeMod(event, gunItem);
                    break;
                case pistolScope:
                    this.handlePistolScopeMod(event, gunItem);
                    break;
                case pistolBarrel:
                    this.handlePistolBarrelMod(event, gunItem);
                    break;
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

        CompoundNBT gun = gunItem.getGun().copy().serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        if(this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }
        if(this.mode == TaCWeaponDevModes.zoom) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Modules").getCompound("Zoom").remove("XOffset");
            gun.getCompound("Modules").getCompound("Zoom").remove("YOffset");
            gun.getCompound("Modules").getCompound("Zoom").remove("ZOffset");
            gun.getCompound("Modules").getCompound("Zoom").putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Zoom").putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Zoom").putDouble("ZOffset", this.casedGetZ());

        }
        else {
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }

        /*Gun gunUpd = new Gun();
        gunUpd.deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunUpd);*/
        gunItem.getGun().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());
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
                this.sizeMod += 0.0075 * stepModifier;
            else if (isDown)
                this.sizeMod -= 0.0075 * stepModifier;
        }
        CompoundNBT gun = gunItem.getGun().copy().serializeNBT(); //new CompoundNBT();//
        if(this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        else {
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        gunItem.getGun().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());
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
            case zoom:
                return gunItem.getGun().getModules().getZoom().getXOffset();
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
            case zoom:
                return gunItem.getGun().getModules().getZoom().getYOffset();
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
            case zoom:
                return gunItem.getGun().getModules().getZoom().getZOffset();
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
        switch (this.mode) {
            case general:
                this.rateMod = 0;
                this.recoilAngleMod=0;
                this.recoilKickMod = 0;
                this.horizontalRecoilAngleMod = 0;
                this.cameraRecoilModifierMod = 0;
                this.weaponRecoilDurationMod = 0;
                this.recoilAdsReductionMod = 0;
                this.projectileAmountMod = 0;
                this.spreadMod = 0;
                this.weightKiloMod = 0;
                break;
            case reloads:
                break;

            case projectile:
                break;

            case display:
                break;

            case flash:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case zoom:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case scope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case barrel:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case oldScope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                this.sizeMod = 0;
                break;
            case pistolScope:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            case pistolBarrel:
                this.xMod = 0;
                this.yMod = 0;
                this.zMod = 0;
                break;
            default:
                break;
        }
    }
    //TODO: ENSURE DATA REQUIRES COMPARISON DATA, NOT FINAL DATA! FINAL DATA IS TO BE USED FOR EXPORTER
    private void ensureData(Gun gun, Gun toUpdate) {
        switch (this.mode) {
            case general:
                if(gun.getGeneral() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.rateMod = gun.getGeneral().getRate();
                    this.recoilAngleMod = gun.getGeneral().getRecoilAngle();
                    this.recoilKickMod = gun.getGeneral().getRecoilKick();
                    this.horizontalRecoilAngleMod = gun.getGeneral().getHorizontalRecoilAngle();
                    this.cameraRecoilModifierMod = gun.getGeneral().getCameraRecoilModifier();
                    this.weaponRecoilDurationMod = gun.getGeneral().getWeaponRecoilDuration();
                    this.recoilAdsReductionMod = gun.getGeneral().getRecoilAdsReduction();
                    this.projectileAmountMod = gun.getGeneral().getProjectileAmount();
                    this.spreadMod = gun.getGeneral().getSpread();
                    this.weightKiloMod = gun.getGeneral().getWeightKilo();

                    this.mode = TaCWeaponDevModes.general;

                    this.rateMod -= gun.getGeneral().getRate();
                    this.recoilAngleMod -= gun.getGeneral().getRecoilAngle();
                    this.recoilKickMod -= gun.getGeneral().getRecoilKick();
                    this.horizontalRecoilAngleMod -= gun.getGeneral().getHorizontalRecoilAngle();
                    this.cameraRecoilModifierMod -= gun.getGeneral().getCameraRecoilModifier();
                    this.weaponRecoilDurationMod -= gun.getGeneral().getWeaponRecoilDuration();
                    this.recoilAdsReductionMod -= gun.getGeneral().getRecoilAdsReduction();
                    this.projectileAmountMod -= gun.getGeneral().getProjectileAmount();
                    this.spreadMod -= gun.getGeneral().getSpread();
                    this.weightKiloMod -= gun.getGeneral().getWeightKilo();
                }
                break;
            case reloads:
                break;
            case projectile:
                break;
            case display:
                break;
            case flash:
                if(toUpdate.getDisplay().getFlash() != null && gun.getDisplay().getFlash() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getDisplay().getFlash().getXOffset()-toUpdate.getDisplay().getFlash().getXOffset();
                    this.yMod = gun.getDisplay().getFlash().getYOffset()-toUpdate.getDisplay().getFlash().getYOffset();
                    this.zMod = gun.getDisplay().getFlash().getZOffset()-toUpdate.getDisplay().getFlash().getZOffset();
                    this.sizeMod = gun.getDisplay().getFlash().getSize()-toUpdate.getDisplay().getFlash().getSize();

                    this.mode = TaCWeaponDevModes.flash;
                }

                break;
            case zoom:
                if(toUpdate.getModules().getZoom() != null && gun.getModules().getZoom() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    LOGGER.log(Level.FATAL, gun.getModules().getZoom().getXOffset());
                    LOGGER.log(Level.FATAL, gun.getModules().getZoom().getYOffset());
                    LOGGER.log(Level.FATAL, gun.getModules().getZoom().getZOffset());

                    this.xMod = gun.getModules().getZoom().getXOffset()-toUpdate.getModules().getZoom().getXOffset();
                    this.yMod = gun.getModules().getZoom().getYOffset()-toUpdate.getModules().getZoom().getYOffset();
                    this.zMod = gun.getModules().getZoom().getZOffset()-toUpdate.getModules().getZoom().getZOffset();

                    LOGGER.log(Level.FATAL, toUpdate.getModules().getZoom().getXOffset());
                    LOGGER.log(Level.FATAL, toUpdate.getModules().getZoom().getYOffset());
                    LOGGER.log(Level.FATAL, toUpdate.getModules().getZoom().getZOffset());
                    this.mode = TaCWeaponDevModes.zoom;
                }
                else
                    LOGGER.log(Level.FATAL, "HOW THE FUCK IS IT NOT THERE");
                break;
            case scope:
                if(toUpdate.getModules().getAttachments().getScope() != null && gun.getModules().getAttachments().getScope() != null){
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getModules().getAttachments().getScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getScope().getScale();

                    this.mode = TaCWeaponDevModes.scope;

                    this.xMod -= toUpdate.getModules().getAttachments().getScope().getXOffset();
                    this.yMod -= toUpdate.getModules().getAttachments().getScope().getYOffset();
                    this.zMod -= toUpdate.getModules().getAttachments().getScope().getZOffset();
                    this.sizeMod -= toUpdate.getModules().getAttachments().getScope().getScale();
                }
                break;
            case barrel:
                if(toUpdate.getModules().getAttachments().getBarrel() != null && gun.getModules().getAttachments().getBarrel() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getModules().getAttachments().getBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getBarrel().getZOffset();

                    this.mode = TaCWeaponDevModes.barrel;

                    this.xMod -= gun.getModules().getAttachments().getBarrel().getXOffset();
                    this.yMod -= gun.getModules().getAttachments().getBarrel().getYOffset();
                    this.zMod -= gun.getModules().getAttachments().getBarrel().getZOffset();
                }
                break;
            case oldScope:
                if(toUpdate.getModules().getAttachments().getOldScope() != null && gun.getModules().getAttachments().getOldScope() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getModules().getAttachments().getOldScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getOldScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getOldScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getOldScope().getScale();

                    this.mode = TaCWeaponDevModes.oldScope;

                    this.xMod -= gun.getModules().getAttachments().getOldScope().getXOffset();
                    this.yMod -= gun.getModules().getAttachments().getOldScope().getYOffset();
                    this.zMod -= gun.getModules().getAttachments().getOldScope().getZOffset();
                    this.sizeMod -= gun.getModules().getAttachments().getOldScope().getScale();
                }
                break;
            case pistolScope:
                if(toUpdate.getModules().getAttachments().getPistolScope() != null && gun.getModules().getAttachments().getPistolScope() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getModules().getAttachments().getPistolScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getPistolScope().getScale();

                    this.mode = TaCWeaponDevModes.pistolScope;

                    this.xMod -= gun.getModules().getAttachments().getPistolScope().getXOffset();
                    this.yMod -= gun.getModules().getAttachments().getPistolScope().getYOffset();
                    this.zMod -= gun.getModules().getAttachments().getPistolScope().getZOffset();
                    this.sizeMod -= gun.getModules().getAttachments().getPistolScope().getScale();

                }
                break;
            case pistolBarrel:
                if(toUpdate.getModules().getAttachments().getPistolBarrel() != null && gun.getModules().getAttachments().getPistolBarrel() != null) {
                    this.mode = TaCWeaponDevModes.undef;

                    this.xMod = gun.getModules().getAttachments().getPistolBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolBarrel().getZOffset();

                    this.mode = TaCWeaponDevModes.pistolBarrel;

                    this.xMod -= gun.getModules().getAttachments().getPistolBarrel().getXOffset();
                    this.yMod -= gun.getModules().getAttachments().getPistolBarrel().getYOffset();
                    this.zMod -= gun.getModules().getAttachments().getPistolBarrel().getZOffset();
                }
                break;
            default:
                break;
        }

    }
    public void exportData() {
        this.map.forEach((name, gun) ->
        {
            if(this.map.get(name) == null) {LOGGER.log(Level.ERROR, "WEAPON EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN."); return;}
            GsonBuilder gsonB = new GsonBuilder().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting().disableJdkUnsafe().setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).serializeSpecialFloatingPointValues();;
            String jsonString = gsonB.create().toJson(gun);//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
            this.writeExport(jsonString, name);
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
    // This is built manually, ensuring that I have both tested and built the new system around any new category.
    public enum TaCWeaponDevModes {
        general("General"),
        reloads("Reloads"),
        projectile("Projectile"),
        /*SOUNDS(""),*/
        display("Display"),
        flash("Flash"),
        zoom("Zoom"),
        scope("Scope"),
        barrel("Barrel"),
        oldScope("OldScope"),
        pistolScope("PistolScope"),
        pistolBarrel("PistolBarrel"),
        undef("&undefined&");

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
