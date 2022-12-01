package com.tac.guns.client.render.animation.module;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class PumpShotgunAnimationController extends GunAnimationController {

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    private boolean empty = false;
    @Override
    protected AnimationSoundMeta getSoundFromLabel(Item item, AnimationLabel label) {
        if (item instanceof GunItem) {
            GunItem gunItem = (GunItem) item;
            Gun.Sounds sounds = gunItem.getGun().getSounds();
            switch (label) {
                case PUMP:
                    return new AnimationSoundMeta(sounds.getPump());
                case RELOAD_INTRO:
                    return new AnimationSoundMeta(sounds.getReloadIntro());
                case RELOAD_LOOP:
                    return new AnimationSoundMeta(sounds.getReloadLoop());
                case RELOAD_NORMAL_END:
                    return new AnimationSoundMeta(sounds.getReloadEnd());
                default:
                    return super.getSoundFromLabel(item, label);
            }
        }
        return null;
    }

    @Override
    public void runAnimation(AnimationLabel label) {

        switch (label) {
            case RELOAD_EMPTY:
            case RELOAD_NORMAL:
                super.runAnimation(AnimationLabel.RELOAD_INTRO , ()-> {
                    if(!this.isAnimationRunning()) super.runAnimation(AnimationLabel.RELOAD_LOOP);
                });
            default:
                super.runAnimation(label);
        }
    }

    @Override
    public void runAnimation(AnimationLabel label, Runnable callback) {
        switch (label) {
            case RELOAD_EMPTY:
            case RELOAD_NORMAL:
                super.runAnimation(AnimationLabel.RELOAD_INTRO, () -> {
                    if(!this.isAnimationRunning())  super.runAnimation(AnimationLabel.RELOAD_LOOP, callback);
                });
            default:
                super.runAnimation(label, callback);
        }
    }

    public boolean isEmpty() {
        return empty;
    }
}
