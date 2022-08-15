package com.tac.guns.client.render.animation;

import com.tac.guns.client.render.animation.module.GunAnimationController;

public abstract class PistalAnimationController extends GunAnimationController {
    public abstract int getSlideNodeIndex();
    public abstract int getMagazineNodeIndex();
}
