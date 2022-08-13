package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.Reference;
import com.tac.guns.client.KeyBinds;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PumpShotgunAnimationController;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunFireEvent;
import com.tac.guns.event.GunReloadEvent;
import com.tac.guns.init.ModSyncedDataKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

/**
 * Mainly controls when the animation should play.
 * */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public enum AnimationHandler {
    INSTANCE;

    @SubscribeEvent
    public void onGunReload(InputEvent.KeyInputEvent event) {
        if(KeyBinds.KEY_RELOAD.isKeyDown() && event.getAction() == GLFW.GLFW_PRESS) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player == null) return;
            ItemStack itemStack = player.inventory.getCurrentItem();
            GunAnimationController controller = GunAnimationController.fromItem(itemStack.getItem());
            if (controller == null) return;
            if(controller instanceof PumpShotgunAnimationController){
                PumpShotgunAnimationController pcontroller = (PumpShotgunAnimationController) controller;
                if(!pcontroller.isReloading()) {
                    pcontroller.stopAnimation();
                    if (Gun.hasAmmo(itemStack)) pcontroller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
                    else pcontroller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                }
                return;
            }
            AnimationMeta reloadEmptyMeta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            AnimationMeta reloadNormalMeta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
            if (Gun.hasAmmo(itemStack)) {
                if (!controller.getPreviousAnimation().equals(reloadNormalMeta))
                    controller.stopAnimation();
                controller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
            } else {
                if (!controller.getPreviousAnimation().equals(reloadEmptyMeta))
                    controller.stopAnimation();
                controller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            }
        }
    }

    @SubscribeEvent
    public void onGunFire(GunFireEvent.Pre event){
        if(!event.isClient()) return;
        GunAnimationController controller = GunAnimationController.fromItem(event.getStack().getItem());
        if(controller == null) return;
        if(controller.isAnimationRunning()){
            AnimationMeta meta = controller.getPreviousAnimation();
            if(meta.equals(controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.INSPECT)))
                controller.stopAnimation();
            else {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPumpShotgunFire(GunFireEvent.Post event){
        if(!event.isClient()) return;
        GunAnimationController controller = GunAnimationController.fromItem(event.getStack().getItem());
        if(controller instanceof PumpShotgunAnimationController) {
            controller.runAnimation(GunAnimationController.AnimationLabel.PUMP);
        }
    }

    @SubscribeEvent
    public void onInspect(InputEvent.KeyInputEvent event){
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack itemStack = player.inventory.getCurrentItem();
        if(KeyBinds.KEY_INSPECT.isKeyDown() && event.getAction() == GLFW.GLFW_PRESS) {
            GunAnimationController controller =  GunAnimationController.fromItem(itemStack.getItem());
            if(controller != null) {
                controller.stopAnimation();
                controller.runAnimation(GunAnimationController.AnimationLabel.INSPECT);
            }
        }
    }
}
