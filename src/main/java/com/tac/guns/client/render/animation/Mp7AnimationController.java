package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.impl.AnimationMeta;
import com.tac.guns.client.render.animation.impl.Animations;
import com.tac.guns.client.render.animation.impl.GunAnimationController;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class Mp7AnimationController extends GunAnimationController{

    public static int INDEX_BODY = 1;

    public static int INDEX_MAG = 0;

    public static int INDEX_LEFT_HAND = 4;

    public static int INDEX_RIGHT_HAND = 2;

    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/mp7_reload_norm.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/mp7_draw.gltf"));

    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/mp7_reload_empty.gltf"));

    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/mp7_inspect.gltf"));

    private static final Mp7AnimationController instance = new Mp7AnimationController();

    private Mp7AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
            Animations.load(RELOAD_EMPTY);
            Animations.load(DRAW);
            Animations.load(INSPECT);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        GunAnimationController.setAnimationControllerMap(ModItems.MP7.getId(),this);
    }

    public static Mp7AnimationController getInstance(){ return instance; }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_NORMAL: return RELOAD_NORM;
            case RELOAD_EMPTY: return RELOAD_EMPTY;
            case DRAW: return DRAW;
            case INSPECT: return INSPECT;
            default: return null;
        }
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
