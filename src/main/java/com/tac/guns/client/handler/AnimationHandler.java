package com.tac.guns.client.handler;

import com.tac.guns.Reference;
import com.tac.guns.client.render.animation.impl.AnimationMeta;
import com.tac.guns.client.render.animation.impl.GunAnimationController;
import com.tac.guns.common.Gun;
import com.tac.guns.event.GunReloadEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Mainly controls when the animation should play.
 * */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public enum AnimationHandler {
    INSTANCE;

    @SubscribeEvent
    public void onGunReload(GunReloadEvent.Post event) {
        GunAnimationController controller = GunAnimationController.fromItem(event.getStack().getItem());
        if(controller == null) return;
        AnimationMeta reloadEmptyMeta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
        AnimationMeta reloadNormalMeta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
        if(Gun.hasAmmo(event.getStack())) {
            if(!controller.getPreviousAnimation().equals(reloadNormalMeta))
                controller.stopAnimation();
            controller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_NORMAL);
        }else {
            if(!controller.getPreviousAnimation().equals(reloadEmptyMeta))
                controller.stopAnimation();
            controller.runAnimation(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
        }
    }


}
