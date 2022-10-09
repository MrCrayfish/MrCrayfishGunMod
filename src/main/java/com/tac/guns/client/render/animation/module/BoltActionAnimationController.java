package com.tac.guns.client.render.animation.module;

import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.item.Item;

public abstract class BoltActionAnimationController extends GunAnimationController {
    @Override
    protected AnimationSoundMeta getSoundFromLabel(Item item, AnimationLabel label) {
        if (item instanceof GunItem) {
            GunItem gunItem = (GunItem) item;
            Gun.Sounds sounds = gunItem.getGun().getSounds();
            switch (label) {
                case PULL_BOLT:
                    return new AnimationSoundMeta(sounds.getPullBolt());
                default:
                    return super.getSoundFromLabel(item, label);
            }
        }
        return null;
    }
}
