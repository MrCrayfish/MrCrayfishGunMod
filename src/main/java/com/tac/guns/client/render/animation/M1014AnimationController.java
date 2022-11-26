package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.*;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class M1014AnimationController extends PumpShotgunAnimationController {

    public static final int INDEX_BOLT = 0;

    public static final int INDEX_BULLET = 1;

    public static final int INDEX_BODY = 2;

    public static final int INDEX_RIGHT_HAND = 3;

    public static final int INDEX_LEFT_HAND = 6;

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/m1014_static.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/m1014_draw.gltf"));

    public static final AnimationMeta INTRO = new AnimationMeta(new ResourceLocation("tac","animations/m1014_reload_intro.gltf"));

    public static final AnimationMeta LOOP = new AnimationMeta(new ResourceLocation("tac","animations/m1014_reload_loop.gltf"));

    public static final AnimationMeta NORMAL_END = new AnimationMeta(new ResourceLocation("tac","animations/m1014_reload_norm_end.gltf"));

    public static final AnimationMeta EMPTY_END = new AnimationMeta(new ResourceLocation("tac","animations/m1014_reload_empty_end.gltf"));

    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/m1014_inspect.gltf"));


    private static final M1014AnimationController instance = new M1014AnimationController();

    public static M1014AnimationController getInstance(){return instance;}
    private M1014AnimationController() {
        try {
            Animations.load(STATIC);
            Animations.load(DRAW);
            Animations.load(INTRO);
            Animations.load(LOOP);
            Animations.load(NORMAL_END);
            Animations.load(INSPECT);
            Animations.load(EMPTY_END);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        enableStaticState();
        GunAnimationController.setAnimationControllerMap(ModItems.M1014.getId(),this);
    }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case STATIC: return STATIC;
            case DRAW: return DRAW;
            case RELOAD_INTRO: return INTRO;
            case RELOAD_LOOP: return LOOP;
            case RELOAD_NORMAL_END: return NORMAL_END;
            case RELOAD_EMPTY_END: return EMPTY_END;
            case INSPECT: return INSPECT;
            default: return null;
        }
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.M1014.get(), label);
    }

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
}
