package com.tac.guns.client.render.animation.module;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.GunEnchantmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class PumpShotgunAnimationController extends GunAnimationController{
    private PumpReloadingAnimationsFlow flow;
    @Override
    protected AnimationSoundMeta getSoundFromLabel(Item item, AnimationLabel label){
        if(item instanceof GunItem){
            GunItem gunItem = (GunItem) item;
            Gun.Sounds sounds = gunItem.getGun().getSounds();
            switch (label){
                case PUMP: return new AnimationSoundMeta(sounds.getPump());
                default: return super.getSoundFromLabel(item, label);
            }
        }
        return null;
    }

    @Override
    public void runAnimation(AnimationLabel label) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack.getItem() instanceof GunItem) {
            GunItem gunItem = (GunItem) itemStack.getItem();
            CompoundNBT tag = itemStack.getOrCreateTag();
            int reloadingAmount = GunEnchantmentHelper.getAmmoCapacity(itemStack, gunItem.getGun()) - tag.getInt("AmmoCount");
            switch (label) {
                case RELOAD_EMPTY: {
                    flow = new PumpReloadingAnimationsFlow(reloadingAmount, true);
                    flow.run();
                }
                case RELOAD_NORMAL:
                    flow = new PumpReloadingAnimationsFlow(reloadingAmount, false);
                    flow.run();
                default:
                    super.runAnimation(label);
            }
        }
    }

    @Override
    public void stopAnimation(){
        super.stopAnimation();
        stopReloadingFlow();
    }

    public void stopReloadingFlow(){
        if(flow!= null) flow.stop();
    }

    public boolean isReloading(){
        return flow != null;
    }

    public class PumpReloadingAnimationsFlow{
        private AnimationMeta currentAnimation;
        private final int loopTime;

        private int count = 0;

        private final boolean isEmpty;

        private boolean stopped = false;

        private final Runnable introCallback = new Runnable() {
            @Override
            public void run() {
                if(stopped) return;
                if(count >= loopTime) return;
                runAnimation(AnimationLabel.RELOAD_LOOP, loopCallback);
                currentAnimation = getAnimationFromLabel(AnimationLabel.RELOAD_LOOP);
            }
        };

        private final Runnable loopCallback = new Runnable() {
            @Override
            public void run() {
                if(stopped) return;
                count++;
                if(count >= loopTime) {
                    if(isEmpty) {
                        runAnimation(AnimationLabel.RELOAD_EMPTY_END, endCallback);
                        currentAnimation = getAnimationFromLabel(AnimationLabel.RELOAD_EMPTY_END);
                    }
                    else {
                        runAnimation(AnimationLabel.RELOAD_NORMAL_END, endCallback);
                        currentAnimation = getAnimationFromLabel(AnimationLabel.RELOAD_NORMAL_END);
                    }
                }else {
                    runAnimation(AnimationLabel.RELOAD_LOOP, loopCallback);
                    currentAnimation = getAnimationFromLabel(AnimationLabel.RELOAD_LOOP);
                }
            }
        };

        private final Runnable endCallback = new Runnable() {
            @Override
            public void run() {
                currentAnimation = null;
                flow = null;
            }
        };

        public PumpReloadingAnimationsFlow(int loopTime, boolean isEmpty){
            this.loopTime = loopTime;
            this.isEmpty = isEmpty;
        }

        public void run(){
            if(count >= loopTime) {
                runAnimation(AnimationLabel.STATIC);
                return;
            }
            runAnimation(AnimationLabel.RELOAD_INTRO, introCallback);;
            currentAnimation = getAnimationFromLabel(AnimationLabel.RELOAD_INTRO);
        }

        public void stop(){
            stopped = true;
            flow = null;
            stopAnimation();
        }

        public AnimationMeta getCurrentAnimation() {return currentAnimation;}
    }
}
