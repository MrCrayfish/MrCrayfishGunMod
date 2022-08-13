package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.*;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class Glock17AnimationController extends PistalAnimationController {

    public static int INDEX_BODY = 3;

    public static int INDEX_SLIDE = 0;

    public static int INDEX_MAG = 2;

    public static int INDEX_LEFT_HAND = 7;

    public static int INDEX_RIGHT_HAND = 4;

    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/glock_17_reload_norm.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/glock_17_draw.gltf"));

    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/glock_17_reload_empty.gltf"));

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/glock_17_static.gltf"));

    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/glock_17_inspect.gltf"));

    private static final Glock17AnimationController instance = new Glock17AnimationController();

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_NORMAL: return RELOAD_NORM;
            case RELOAD_EMPTY: return RELOAD_EMPTY;
            case DRAW: return DRAW;
            case STATIC: return STATIC;
            case INSPECT: return INSPECT;
            default: return null;
        }
    }

    private Glock17AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
            Animations.load(DRAW);
            Animations.load(RELOAD_EMPTY);
            Animations.load(STATIC);
            Animations.load(INSPECT);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        enableStaticState();
        GunAnimationController.setAnimationControllerMap(ModItems.GLOCK_17.getId(),this);
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.GLOCK_17.get(), label);
    }


    public static Glock17AnimationController getInstance() { return instance; }

    @Override
    protected int getAttachmentsNodeIndex() {
        return INDEX_BODY;
    }

    @Override
    protected int getRightHandNodeIndex() {
        return INDEX_RIGHT_HAND;
    }

    @Override
    protected int getLeftHandNodeIndex() {
        return INDEX_LEFT_HAND;
    }

    @Override
    public int getSlideNodeIndex() {
        return INDEX_SLIDE;
    }

    @Override
    public int getMagazineNodeIndex() {
        return INDEX_MAG;
    }
}
