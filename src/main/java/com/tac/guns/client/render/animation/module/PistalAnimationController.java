package com.tac.guns.client.render.animation.module;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class PistalAnimationController extends GunAnimationController {
    public abstract int getSlideNodeIndex();
    public abstract int getMagazineNodeIndex();
}
