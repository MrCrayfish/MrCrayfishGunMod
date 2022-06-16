package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class CZ75AnimationController extends GunAnimationController {

    public static int INDEX_BODY = 6;

    public static int INDEX_SLIDE = 2;

    public static int INDEX_MAG = 3;

    public static int INDEX_LEFT_HAND = 0;

    public static int INDEX_RIGHT_HAND = 1;

    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/cz75_reload.gltf"));

    private static final CZ75AnimationController instance = new CZ75AnimationController();

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_NORMAL: return RELOAD_NORM;
            default: return null;
        }
    }

    private CZ75AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        GunAnimationController.setAnimationControllerMap(ModItems.CZ75.getId(),this);
    }

    public static CZ75AnimationController getInstance(){ return instance; }

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
