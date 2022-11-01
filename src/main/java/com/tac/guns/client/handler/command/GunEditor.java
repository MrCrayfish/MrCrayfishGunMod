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
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.tac.guns.item.attachment.IAttachment;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
    public void onClientTick(TickEvent.ClientTickEvent event) {
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
            ensureData(getMapItem(gunItem.getTranslationKey(),gunItem.getGun()), gunItem.getGun().copy());
            this.prevMode = this.mode;
        }
        if(this.previousWeaponTag == "")
            this.previousWeaponTag = gunItem.getTranslationKey();
        else if(this.previousWeaponTag != gunItem.getTranslationKey())
        {
            this.previousWeaponTag = gunItem.getTranslationKey();
            resetData();
            /*if(this.map.containsKey(gunItem.getTranslationKey()))
                ensureData(this.map.get(gunItem.getTranslationKey()), gunItem.getGun().copy());*/
            ensureData(getMapItem(gunItem.getTranslationKey(),gunItem.getGun()), gunItem.getGun().copy());
        }
    }
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
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
        if(ch.catInGlobal(1) && this.mode != null)
        {
            //TODO: HANDLE FOR PER MODULE, BEFORE APPLICATION, SAVE DATA ON INSTANCE TO SERIALIZE LATER.
            switch (this.mode) {
                case general:
                    this.handleGeneralMod(event, gunItem);
                    break;
                case reloads:
                    this.handleReloadsMod(event, gunItem);
                    break;
                case projectile:
                    this.handleProjectileMod(event, gunItem);
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
            /*NetworkGunManager manager = NetworkGunManager.get();
            if (manager != null)
                PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());*/
        }

    }

    public double getRateMod() {return rateMod;}
    public double getBurstRateMod() {return burstRateMod;}
    public float getRecoilAngleMod() {return recoilAngleMod;}
    public float getRecoilKickMod() {return recoilKickMod;}
    public float getHorizontalRecoilAngleMod() {return horizontalRecoilAngleMod;}
    public float getCameraRecoilModifierMod() {return cameraRecoilModifierMod;}
    public float getWeaponRecoilDurationMod() {return weaponRecoilDurationMod;}
    public float getcameraRecoilDurationMod() {return cameraRecoilDurationMod;}
    public float getRecoilDurationMod() {return recoilDurationMod;}
    public float getRecoilAdsReductionMod() {return recoilAdsReductionMod;}
    public double getProjectileAmountMod() {return projectileAmountMod;}
    public float getSpreadMod() {return spreadMod;}
    public float getWeightKiloMod() {return weightKiloMod;}
    private double rateMod = 0;
    private double burstRateMod = 0;
    private float recoilAngleMod = 0;
    private float recoilKickMod = 0;
    private float horizontalRecoilAngleMod = 0;
    private float cameraRecoilModifierMod = 0;
    private float weaponRecoilDurationMod = 0;
    private float cameraRecoilDurationMod = 0;
    private float recoilDurationMod = 0;
    private float recoilAdsReductionMod = 0;
    private double projectileAmountMod = 0;
    private float spreadMod = 0;
    private float weightKiloMod = 0;
    private void handleGeneralMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        boolean isControlDown = InputHandler.CONTROLLY.down || InputHandler.CONTROLLYR.down; // Increase Module Size
        boolean isShiftDown = InputHandler.SHIFTY.down || InputHandler.SHIFTYR.down; // Increase Step Size
        boolean isAltDown = InputHandler.ALTY.down || InputHandler.ALTYR.down; // Swap X -> Z modify
        PlayerEntity player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if(InputHandler.P.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;
            player.sendStatusMessage(new TranslationTextComponent("VerticalRecoilAngle: "+gunTmp.getGeneral().getRecoilAngle()+" | HorizontalRecoilAngle: "+gunTmp.getGeneral().getHorizontalRecoilAngle()+" | RecoilKick: "+gunTmp.getGeneral().getRecoilKick()), true);
            if (isLeft) {
                this.horizontalRecoilAngleMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("HorizontalRecoilAngle: "+gunTmp.getGeneral().getHorizontalRecoilAngle()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isRight) {
                this.horizontalRecoilAngleMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("HorizontalRecoilAngle: "+gunTmp.getGeneral().getHorizontalRecoilAngle()).mergeStyle(TextFormatting.DARK_RED), true);
            }
            else if (isUp && isAltDown) {
                this.recoilKickMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilKick: "+gunTmp.getGeneral().getRecoilKick()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown && isAltDown) {
                this.recoilKickMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilKick: "+gunTmp.getGeneral().getRecoilKick()).mergeStyle(TextFormatting.DARK_RED), true);
            }
            else if (isUp) {
                this.recoilAngleMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("VerticalRecoilAngle: "+gunTmp.getGeneral().getRecoilAngle()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.recoilAngleMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("VerticalRecoilAngle: "+gunTmp.getGeneral().getRecoilAngle()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.L.down)
        {
            if(isShiftDown)
                stepModifier*=5;
            player.sendStatusMessage(new TranslationTextComponent("weaponRecoilOffset: "+gunTmp.getGeneral().getWeaponRecoilOffset()+" | RecoilDuration: "+gunTmp.getGeneral().getRecoilDuration()), true);
            if (isLeft) {
                this.weaponRecoilDurationMod += 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("weaponRecoilOffset: "+gunTmp.getGeneral().getWeaponRecoilOffset()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isRight) {
                this.weaponRecoilDurationMod -= 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("weaponRecoilOffset: "+gunTmp.getGeneral().getWeaponRecoilOffset()).mergeStyle(TextFormatting.DARK_RED), true);
            }
            else if (isUp) {
                this.recoilDurationMod += 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilDuration: "+gunTmp.getGeneral().getRecoilDuration()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.recoilDurationMod -= 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilDuration: "+gunTmp.getGeneral().getRecoilDuration()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.O.down)
        {
            if(isShiftDown)
                stepModifier*=5;
            player.sendStatusMessage(new TranslationTextComponent("CameraRecoilModifier: "+gunTmp.getGeneral().getCameraRecoilModifier()),true);
            if (isUp) {
                this.cameraRecoilModifierMod += 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("CameraRecoilModifier: "+gunTmp.getGeneral().getCameraRecoilModifier()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.cameraRecoilModifierMod -= 0.0025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("CameraRecoilModifier: "+gunTmp.getGeneral().getCameraRecoilModifier()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.K.down)
        {
            if(isShiftDown)
                stepModifier*=5;
            player.sendStatusMessage(new TranslationTextComponent("RecoilAdsReduction: "+gunTmp.getGeneral().getRecoilAdsReduction()),true);
            if (isUp) {
                this.recoilAdsReductionMod += 0.001 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilAdsReduction: "+gunTmp.getGeneral().getRecoilAdsReduction()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.recoilAdsReductionMod -= 0.001 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("RecoilAdsReduction: "+gunTmp.getGeneral().getRecoilAdsReduction()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.M.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;
            player.sendStatusMessage(new TranslationTextComponent("Inaccuracy in Degrees: "+gunTmp.getGeneral().getSpread()),true);
            if (isUp) {
                this.spreadMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Inaccuracy in Degrees: "+gunTmp.getGeneral().getSpread()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.spreadMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Inaccuracy in Degrees: "+gunTmp.getGeneral().getSpread()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.I.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;
            player.sendStatusMessage(new TranslationTextComponent("Weight in Kilograms: "+gunTmp.getGeneral().getWeightKilo()),true);
            if (isUp) {
                this.weightKiloMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Weight in Kilograms: "+gunTmp.getGeneral().getWeightKilo()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.weightKiloMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Weight in Kilograms: "+gunTmp.getGeneral().getWeightKilo()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.J.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("Rate in Ticks: "+gunTmp.getGeneral().getRate()+" | Burst Rate in Ticks:: "+gunTmp.getGeneral().getBurstRate()),true);
            if (isLeft) {
                this.burstRateMod += 0.5 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Burst Rate in Ticks: "+gunTmp.getGeneral().getBurstRate()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isRight) {
                this.burstRateMod -= 0.5 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Burst Rate in Ticks: "+gunTmp.getGeneral().getBurstRate()).mergeStyle(TextFormatting.DARK_RED), true);
            }
            else if (isUp) {
                this.rateMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Rate in Ticks: "+gunTmp.getGeneral().getRate()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.rateMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Rate in Ticks: "+gunTmp.getGeneral().getRate()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.N.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("Projectile Amount: "+gunTmp.getGeneral().getProjectileAmount()),true);
            if (isUp) {
                this.projectileAmountMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Projectile Amount: "+gunTmp.getGeneral().getProjectileAmount()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.projectileAmountMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Projectile Amount: "+gunTmp.getGeneral().getProjectileAmount()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }

        CompoundNBT gun = getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("General").remove("Rate");
        gun.getCompound("General").remove("RecoilAngle");
        gun.getCompound("General").remove("RecoilKick");
        gun.getCompound("General").remove("HorizontalRecoilAngle");
        gun.getCompound("General").remove("CameraRecoilModifier");
        gun.getCompound("General").remove("RecoilDurationOffset");
        gun.getCompound("General").remove("weaponRecoilOffset");
        gun.getCompound("General").remove("RecoilAdsReduction");
        gun.getCompound("General").remove("ProjectileAmount");
        gun.getCompound("General").remove("Spread");
        gun.getCompound("General").remove("WeightKilo");
        gun.getCompound("General").putDouble("Rate", gunItem.getGun().getGeneral().getRate());
        gun.getCompound("General").putDouble("RecoilAngle", gunItem.getGun().getGeneral().getRecoilAngle());
        gun.getCompound("General").putDouble("RecoilKick", gunItem.getGun().getGeneral().getRecoilKick());
        gun.getCompound("General").putDouble("HorizontalRecoilAngle", gunItem.getGun().getGeneral().getHorizontalRecoilAngle());
        gun.getCompound("General").putDouble("CameraRecoilModifier", gunItem.getGun().getGeneral().getCameraRecoilModifier());
        gun.getCompound("General").putDouble("RecoilDurationOffset", gunItem.getGun().getGeneral().getRecoilDuration());
        gun.getCompound("General").putDouble("weaponRecoilOffset", gunItem.getGun().getGeneral().getWeaponRecoilOffset());
        gun.getCompound("General").putDouble("RecoilAdsReduction", gunItem.getGun().getGeneral().getRecoilAdsReduction());
        gun.getCompound("General").putDouble("ProjectileAmount", gunItem.getGun().getGeneral().getProjectileAmount());
        gun.getCompound("General").putDouble("Spread", gunItem.getGun().getGeneral().getSpread());
        gun.getCompound("General").putDouble("WeightKilo", gunItem.getGun().getGeneral().getWeightKilo());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun);
    }

    public float getDamageMod() {return this.damageMod;}
    public float getSizePrjMod() {return this.sizePrjMod;}
    public double getSpeedMod() {return this.speedMod;}
    public double getLifeMod() {return this.lifeMod;}
    private float damageMod = 0;
    private float sizePrjMod = 0;
    private double speedMod = 0;
    private double lifeMod = 0;
    private void handleProjectileMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        boolean isControlDown = InputHandler.CONTROLLY.down || InputHandler.CONTROLLYR.down; // Increase Module Size
        boolean isShiftDown = InputHandler.SHIFTY.down || InputHandler.SHIFTYR.down; // Increase Step Size
        PlayerEntity player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if(InputHandler.P.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;
            player.sendStatusMessage(new TranslationTextComponent("Damage: "+gunTmp.getProjectile().getDamage()), true);
            if (isUp) {
                this.damageMod += 0.025f * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Damage: "+gunTmp.getProjectile().getDamage()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.damageMod -= 0.025f * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Damage: "+gunTmp.getProjectile().getDamage()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.L.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            if(isControlDown)
                stepModifier/=10;
            player.sendStatusMessage(new TranslationTextComponent("Projectile size: "+gunTmp.getProjectile().getSize()), true);
            if (isUp) {
                this.sizePrjMod += 0.025f * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Projectile size: "+gunTmp.getProjectile().getSize()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.sizePrjMod -= 0.025f * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Projectile size: "+gunTmp.getProjectile().getSize()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.O.down)
        {
            if(isShiftDown)
                stepModifier*=10;
            player.sendStatusMessage(new TranslationTextComponent("Speed: "+gunTmp.getProjectile().getSpeed()), true);
            if (isUp) {
                this.speedMod += 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Speed: "+gunTmp.getProjectile().getSpeed()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.speedMod -= 0.025 * stepModifier;
                player.sendStatusMessage(new TranslationTextComponent("Speed: "+gunTmp.getProjectile().getSpeed()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.K.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("Ticks bullet Exists for: "+gunTmp.getProjectile().getLife()), true);
            if (isUp) {
                this.lifeMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Ticks bullet Exists for: "+gunTmp.getProjectile().getLife()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.lifeMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("Ticks bullet Exists for: "+gunTmp.getProjectile().getLife()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }

        CompoundNBT gun = getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("Projectile").remove("Damage");
        gun.getCompound("Projectile").remove("Size");
        gun.getCompound("Projectile").remove("Speed");
        gun.getCompound("Projectile").remove("Life");
        gun.getCompound("Projectile").putDouble("Damage", gunItem.getGun().getProjectile().getDamage());
        gun.getCompound("Projectile").putDouble("Size", gunItem.getGun().getProjectile().getSize());
        gun.getCompound("Projectile").putDouble("Speed", gunItem.getGun().getProjectile().getSpeed());
        gun.getCompound("Projectile").putDouble("Life", gunItem.getGun().getProjectile().getLife());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun);
    }

    public double getReloadMagTimerMod() {return reloadMagTimerMod;}
    public double getAdditionalReloadEmptyMagTimerMod() {return additionalReloadEmptyMagTimerMod;}
    public double getReloadAmountMod() {return reloadAmountMod;}
    public double getPreReloadPauseTicksMod() {return preReloadPauseTicksMod;}
    public double getInterReloadPauseTicksMod() {return interReloadPauseTicksMod;}
    double reloadMagTimerMod = 0;
    double additionalReloadEmptyMagTimerMod = 0;
    double reloadAmountMod = 0;
    double preReloadPauseTicksMod = 0;
    double interReloadPauseTicksMod = 0;
    private void handleReloadsMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        PlayerEntity player = Minecraft.getInstance().player;
        Gun gunTmp = gunItem.getGun();
        if(InputHandler.P.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("ReloadMagTimer: "+gunTmp.getReloads().getReloadMagTimer()), true);
            if (isUp) {
                this.reloadMagTimerMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("ReloadMagTimer: "+gunTmp.getReloads().getReloadMagTimer()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.reloadMagTimerMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("ReloadMagTimer: "+gunTmp.getReloads().getReloadMagTimer()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.L.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("AdditionalReloadEmptyMagTimer: "+gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()), true);
            if (isUp) {
                this.additionalReloadEmptyMagTimerMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("AdditionalReloadEmptyMagTimer: "+gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.additionalReloadEmptyMagTimerMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("AdditionalReloadEmptyMagTimer: "+gunTmp.getReloads().getAdditionalReloadEmptyMagTimer()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.O.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("ReloadAmount: "+gunTmp.getReloads().getReloadAmount()), true);
            if (isUp) {
                this.reloadAmountMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("ReloadAmount: "+gunTmp.getReloads().getReloadAmount()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.reloadAmountMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("ReloadAmount: "+gunTmp.getReloads().getReloadAmount()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.K.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("PreReloadPauseTicks: "+gunTmp.getReloads().getPreReloadPauseTicks()), true);
            if (isUp) {
                this.preReloadPauseTicksMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("PreReloadPauseTicks: "+gunTmp.getReloads().getPreReloadPauseTicks()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.preReloadPauseTicksMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("PreReloadPauseTicks: "+gunTmp.getReloads().getPreReloadPauseTicks()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        else if(InputHandler.M.down)
        {
            player.sendStatusMessage(new TranslationTextComponent("InterReloadPauseTicks: "+gunTmp.getReloads().getinterReloadPauseTicks()), true);
            if (isUp) {
                this.interReloadPauseTicksMod += 0.5;
                player.sendStatusMessage(new TranslationTextComponent("InterReloadPauseTicks: "+gunTmp.getReloads().getinterReloadPauseTicks()).mergeStyle(TextFormatting.GREEN), true);
            }
            else if (isDown) {
                this.interReloadPauseTicksMod -= 0.5;
                player.sendStatusMessage(new TranslationTextComponent("InterReloadPauseTicks: "+gunTmp.getReloads().getinterReloadPauseTicks()).mergeStyle(TextFormatting.DARK_RED), true);
            }
        }
        CompoundNBT gun = getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
        gun.getCompound("Reloads").remove("ReloadSpeed");
        gun.getCompound("Reloads").remove("ReloadMagTimer");
        gun.getCompound("Reloads").remove("AdditionalReloadEmptyMagTimer");
        gun.getCompound("Reloads").remove("ReloadPauseTicks");
        gun.getCompound("Reloads").remove("InterReloadPauseTicks");
        gun.getCompound("Reloads").putDouble("ReloadSpeed", gunItem.getGun().getReloads().getReloadAmount());
        gun.getCompound("Reloads").putDouble("ReloadMagTimer", gunItem.getGun().getReloads().getReloadMagTimer());
        gun.getCompound("Reloads").putDouble("AdditionalReloadEmptyMagTimer", gunItem.getGun().getReloads().getAdditionalReloadEmptyMagTimer());
        gun.getCompound("Reloads").putDouble("ReloadPauseTicks", gunItem.getGun().getReloads().getPreReloadPauseTicks());
        gun.getCompound("Reloads").putDouble("InterReloadPauseTicks", gunItem.getGun().getReloads().getinterReloadPauseTicks());
        /*gunItem.getGun().getGeneral().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun);
    }

    private void handleZoomMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handlePositionedMod(event, gunItem);
    }
    private void handleFlashMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handleScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handleBarrelMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handleOldScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handlePistolScopeMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handleScaledPositionedMod(event, gunItem);
    }
    private void handlePistolBarrelMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem){ // "zoom" extends positioned
        this.handleScaledPositionedMod(event, gunItem);
    }

    private double xMod = 0; public double getxMod() {return this.xMod;}
    private double yMod = 0; public double getyMod() {return this.yMod;}
    private double zMod = 0; public double getzMod() {return this.zMod;}

    private boolean controlToggle = false;
    private boolean altToggle = false;
    private void handlePositionedMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        double stepModifier = 1;
        boolean isLeft = event.getKey() == GLFW.GLFW_KEY_LEFT;
        boolean isRight = event.getKey() == GLFW.GLFW_KEY_RIGHT;
        boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
        boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
        //boolean isControlDown = InputHandler.CONTROLLY.down || InputHandler.CONTROLLYR.down; // Increase Module Size
        //boolean isShiftDown = InputHandler.SHIFTY.down || InputHandler.SHIFTYR.down; // Increase Step Size
        //boolean isAltDown = InputHandler.ALTY.down || InputHandler.ALTYR.down; // Swap X -> Z modify

        /*if(isShiftDown)
            stepModifier*=10;*/
        if(event.getKey() == GLFW.GLFW_KEY_LEFT_CONTROL)
        {controlToggle = true;}
        if(event.getKey() == GLFW.GLFW_KEY_LEFT_ALT)
        {altToggle = true;}

        if(controlToggle)
            stepModifier/=10;

        if (isLeft)
            this.xMod -= 0.1*stepModifier;
        else if (isRight)
            this.xMod += 0.1*stepModifier;
        else if (isUp && altToggle)
            this.zMod -= 0.1*stepModifier; // Forward
        else if (isDown && altToggle)
            this.zMod += 0.1*stepModifier; // Backward
        else if (isUp)
            this.yMod += 0.1*stepModifier;
        else if (isDown)
            this.yMod -= 0.1*stepModifier;

        CompoundNBT gun = this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT(); // Copy to ensure we are grabbing a copy of this data. new CompoundNBT();//
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
            //this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).getModules().getAttachments().getScope().deserializeNBT(gun.getCompound("Modules").getCompound("Zoom"));
        }
        if(this.mode == TaCWeaponDevModes.scope)
        {
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("XOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("YOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").remove("ZOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").putDouble("ZOffset", this.casedGetZ());
            //LOGGER.log(Level.FATAL, gun.getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
            //LOGGER.log(Level.FATAL, gun.getCompound("Modules").getCompound("Attachments").toString());
        }
        else {
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("XOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("YOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).remove("ZOffset");
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("XOffset", this.casedGetX());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("YOffset", this.casedGetY());
            gun.getCompound("Modules").getCompound("Attachments").getCompound(this.mode.tagName).putDouble("ZOffset", this.casedGetZ());

        }
        //gunItem.getGun().deserializeNBT(gun);
        this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun);
        //LOGGER.log(Level.FATAL, this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
        //this.map.put(gunItem.getTranslationKey(), getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun));
    }
    private double sizeMod = 0;
    public double getSizeMod() {return this.sizeMod;}
    private void handleScaledPositionedMod(InputEvent.KeyInputEvent event, TimelessGunItem gunItem) {
        this.handlePositionedMod(event, gunItem);
        boolean isPeriodDown = InputHandler.SIZE_OPT.down; // Increase Step Size

        if(isPeriodDown) {
            double stepModifier = 1;
            boolean isUp = event.getKey() == GLFW.GLFW_KEY_UP;
            boolean isDown = event.getKey() == GLFW.GLFW_KEY_DOWN;
            boolean isShiftDown = InputHandler.SHIFTY.down || InputHandler.SHIFTYR.down; // Increase Step Size

            if (isShiftDown)
                stepModifier *= 10;

            else if (isUp)
                this.sizeMod += 0.0075 * stepModifier;
            else if (isDown)
                this.sizeMod -= 0.0075 * stepModifier;
        }
        CompoundNBT gun = getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).serializeNBT(); //new CompoundNBT();//
        if(this.mode == TaCWeaponDevModes.flash) // Flash was apparently brought out of Display, I don't wish to break every gun at this point in time.
        {
            gun.getCompound("Display").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Display").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        else {
            gun.getCompound("Modules").getCompound(this.mode.tagName).remove("Scale");
            gun.getCompound("Modules").getCompound(this.mode.tagName).putDouble("Scale", this.casedGetScale());
        }
        /*gunItem.getGun().deserializeNBT(gun);
        this.map.put(gunItem.getTranslationKey(), gunItem.getGun());*/
        this.getMapItem(gunItem.getTranslationKey(), gunItem.getGun()).deserializeNBT(gun);
    }

    private Gun getMapItem(String gunTagName, Gun gun) {
        if(this.map.containsKey(gunTagName))
            return this.map.get(gunTagName);
        else {
            this.map.put(gunTagName, gun.copy());
            return this.map.get(gunTagName);
        }
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
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getXOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getXOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getXOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getXOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getXOffset() : 0;
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
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getYOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getYOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getYOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getYOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getYOffset() : 0;
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
                return gunItem.getGun().getModules().getAttachments().getScope() != null ? gunItem.getGun().getModules().getAttachments().getScope().getZOffset() : 0;
            case barrel:
                return gunItem.getGun().getModules().getAttachments().getBarrel() != null ? gunItem.getGun().getModules().getAttachments().getBarrel().getZOffset() : 0;
            case oldScope:
                return gunItem.getGun().getModules().getAttachments().getOldScope() != null ? gunItem.getGun().getModules().getAttachments().getOldScope().getZOffset() : 0;
            case pistolScope:
                return gunItem.getGun().getModules().getAttachments().getPistolScope() != null ? gunItem.getGun().getModules().getAttachments().getPistolScope().getZOffset() : 0;
            case pistolBarrel:
                return gunItem.getGun().getModules().getAttachments().getPistolBarrel() != null ? gunItem.getGun().getModules().getAttachments().getPistolBarrel().getZOffset() : 0;
        }
        return 0;
    }
    public void resetData() {
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
                this.reloadMagTimerMod = 0;
                this.additionalReloadEmptyMagTimerMod = 0;
                this.reloadAmountMod = 0;
                this.preReloadPauseTicksMod = 0;
                this.interReloadPauseTicksMod = 0;
                break;

            case projectile:
                this.damageMod = 0;
                this.sizePrjMod = 0;
                this.speedMod = 0;
                this.lifeMod = 0;
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
    //TODO: ENSURE WE ARE EDITING "TO EXPORT DATA", I DON'T WANT TO MANAGE MULTIPLE DATA STORES AND ATTRIBUTES, HERE WE JUST ADJUST BASED ON THE GUNS MODIFIED AND EXISTING!
    private void ensureData(Gun gun, Gun toUpdate) {
        switch (this.mode) {
            case general:
                if(gun.getGeneral() != null) {
                    this.rateMod = gun.getGeneral().getRate()-toUpdate.getGeneral().getRate();
                    this.recoilAngleMod = gun.getGeneral().getRecoilAngle()-toUpdate.getGeneral().getRecoilAngle();
                    this.recoilKickMod = gun.getGeneral().getRecoilKick()-toUpdate.getGeneral().getRecoilKick();
                    this.horizontalRecoilAngleMod = gun.getGeneral().getHorizontalRecoilAngle()-toUpdate.getGeneral().getHorizontalRecoilAngle();
                    this.cameraRecoilModifierMod = gun.getGeneral().getCameraRecoilModifier()-toUpdate.getGeneral().getCameraRecoilModifier();
                    this.weaponRecoilDurationMod = gun.getGeneral().getWeaponRecoilOffset()-toUpdate.getGeneral().getWeaponRecoilOffset();
                    this.recoilDurationMod = gun.getGeneral().getRecoilDuration()-toUpdate.getGeneral().getRecoilDuration();
                    this.recoilAdsReductionMod = gun.getGeneral().getRecoilAdsReduction()-toUpdate.getGeneral().getRecoilAdsReduction();
                    this.projectileAmountMod = gun.getGeneral().getProjectileAmount()-toUpdate.getGeneral().getProjectileAmount();
                    this.spreadMod = gun.getGeneral().getSpread()-toUpdate.getGeneral().getSpread();
                    this.weightKiloMod = gun.getGeneral().getWeightKilo()-toUpdate.getGeneral().getWeightKilo();
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
                    this.xMod = gun.getDisplay().getFlash().getXOffset()-toUpdate.getDisplay().getFlash().getXOffset();
                    this.yMod = gun.getDisplay().getFlash().getYOffset()-toUpdate.getDisplay().getFlash().getYOffset();
                    this.zMod = gun.getDisplay().getFlash().getZOffset()-toUpdate.getDisplay().getFlash().getZOffset();
                    this.sizeMod = gun.getDisplay().getFlash().getSize()-toUpdate.getDisplay().getFlash().getSize();
                }

                break;
            case zoom:
                if(toUpdate.getModules().getZoom() != null && gun.getModules().getZoom() != null) {
                    this.xMod = gun.getModules().getZoom().getXOffset()-toUpdate.getModules().getZoom().getXOffset();
                    this.yMod = gun.getModules().getZoom().getYOffset()-toUpdate.getModules().getZoom().getYOffset();
                    this.zMod = gun.getModules().getZoom().getZOffset()-toUpdate.getModules().getZoom().getZOffset();
                }
                break;
            case scope:
                if(toUpdate.getModules().getAttachments().getScope() != null && gun.getModules().getAttachments().getScope() != null){
                    this.xMod = gun.getModules().getAttachments().getScope().getXOffset()-toUpdate.getModules().getAttachments().getScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getScope().getYOffset()-toUpdate.getModules().getAttachments().getScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getScope().getZOffset()-toUpdate.getModules().getAttachments().getScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getScope().getScale()-toUpdate.getModules().getAttachments().getScope().getScale();
                }
                break;
            case barrel:
                if(toUpdate.getModules().getAttachments().getBarrel() != null && gun.getModules().getAttachments().getBarrel() != null) {
                    this.xMod = gun.getModules().getAttachments().getBarrel().getXOffset()-toUpdate.getModules().getAttachments().getBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getBarrel().getYOffset()-toUpdate.getModules().getAttachments().getBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getBarrel().getZOffset()-toUpdate.getModules().getAttachments().getBarrel().getZOffset();
                }
                break;
            case oldScope:
                if(toUpdate.getModules().getAttachments().getOldScope() != null && gun.getModules().getAttachments().getOldScope() != null) {
                    this.xMod = gun.getModules().getAttachments().getOldScope().getXOffset()-toUpdate.getModules().getAttachments().getOldScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getOldScope().getYOffset()-toUpdate.getModules().getAttachments().getOldScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getOldScope().getZOffset()-toUpdate.getModules().getAttachments().getOldScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getOldScope().getScale()-toUpdate.getModules().getAttachments().getOldScope().getScale();
                }
                break;
            case pistolScope:
                if(toUpdate.getModules().getAttachments().getPistolScope() != null && gun.getModules().getAttachments().getPistolScope() != null) {
                    this.xMod = gun.getModules().getAttachments().getPistolScope().getXOffset()-toUpdate.getModules().getAttachments().getPistolScope().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolScope().getYOffset()-toUpdate.getModules().getAttachments().getPistolScope().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolScope().getZOffset()-toUpdate.getModules().getAttachments().getPistolScope().getZOffset();
                    this.sizeMod = gun.getModules().getAttachments().getPistolScope().getScale()-toUpdate.getModules().getAttachments().getPistolScope().getScale();
                }
                break;
            case pistolBarrel:
                if(toUpdate.getModules().getAttachments().getPistolBarrel() != null && gun.getModules().getAttachments().getPistolBarrel() != null) {
                    this.xMod = gun.getModules().getAttachments().getPistolBarrel().getXOffset()-toUpdate.getModules().getAttachments().getPistolBarrel().getXOffset();
                    this.yMod = gun.getModules().getAttachments().getPistolBarrel().getYOffset()-toUpdate.getModules().getAttachments().getPistolBarrel().getYOffset();
                    this.zMod = gun.getModules().getAttachments().getPistolBarrel().getZOffset()-toUpdate.getModules().getAttachments().getPistolBarrel().getZOffset();
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
            LOGGER.log(Level.FATAL, gun.serializeNBT().getCompound("Modules").getCompound("Attachments").toString());
            GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();//.setNumberToNumberStrategy(ToNumberPolicy.DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).serializeSpecialFloatingPointValues();;

            String jsonString = gsonB.create().toJson(gun);//gson.toJson(ch.getCatGlobal(1).get(this.previousWeaponTag));
            jsonString += "\nSCOPE"+gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Scope").toString());
            jsonString += "\nBARREL"+gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("Barrel").toString());
            jsonString += "\nOLD_SCOPE"+gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("OldScope").toString());
            jsonString += "\nPISTOL_SCOPE"+gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("PistolScope").toString());
            jsonString += "\nPISTOL_BARREL"+gsonB.create().toJson(gun.serializeNBT().getCompound("Modules").getCompound("Attachments").getCompound("PistolBarrel").toString());
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
            LOGGER.log(Level.INFO, "WEAPON EDITOR EXPORTED FILE ( "+name + "_export.txt ). BE PROUD!");
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
