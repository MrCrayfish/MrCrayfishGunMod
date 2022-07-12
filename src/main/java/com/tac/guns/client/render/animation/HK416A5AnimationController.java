package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class HK416A5AnimationController extends GunAnimationController{
    public static int INDEX_BODY = 6;
    public static int INDEX_LEFT_HAND = 0;
    public static int INDEX_RIGHT_HAND = 2;
    public static int INDEX_MAGAZINE = 4;
    public static int INDEX_EXTRA_MAGAZINE = 5;
    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/hk416_a5_reload_norm.gltf"));
    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/hk416_a5_reload_empty.gltf"));
    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/hk416_a5_draw.gltf"));
    private static final HK416A5AnimationController instance = new HK416A5AnimationController();

    private HK416A5AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
            Animations.load(RELOAD_EMPTY);
            Animations.load(DRAW);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        GunAnimationController.setAnimationControllerMap(ModItems.HK416_A5.getId(),this);
    }

    public static HK416A5AnimationController getInstance(){
        return instance;
    }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_EMPTY: return RELOAD_EMPTY;
            case RELOAD_NORMAL: return RELOAD_NORM;
            case DRAW: return DRAW;
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
