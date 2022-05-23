package com.tac.guns.client.render.animation;


import com.tac.guns.GunMod;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class Ppsh41AnimationController extends GunAnimationController {
    public static int INDEX_BODY = 1;
    public static int INDEX_LEFT_HAND = 2;
    public static int INDEX_RIGHT_HAND = 2;
    public static int INDEX_MAGAZINE = 4;
    //public static int INDEX_BOLT = 5;
    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/ppsh_41.gltf"));

    private static final Ppsh41AnimationController instance = new Ppsh41AnimationController();

    private Ppsh41AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        //GunAnimationController.setAnimationControllerMap(ModItems.PPSH_41.getId(),this);
    }

    public static Ppsh41AnimationController getInstance(){
        return instance;
    }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_NORMAL: return RELOAD_NORM;
            default: return null;
        }
    }

    @Override
    protected int getAttachmentsNodeIndex() {
        return INDEX_BODY;
    }

    @Override
    protected int getRightHandNodeIndex() {
        return INDEX_LEFT_HAND;
    }

    @Override
    protected int getLeftHandNodeIndex() {
        return INDEX_RIGHT_HAND;
    }

    protected int getIndexMagazine(){return INDEX_MAGAZINE;}
}
