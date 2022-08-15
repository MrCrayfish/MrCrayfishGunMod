package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.AnimationSoundMeta;
import com.tac.guns.client.render.animation.module.Animations;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class Mp7AnimationController extends GunAnimationController{

    public static int INDEX_BODY = 1;

    public static int INDEX_MAG = 0;

    public static int INDEX_LEFT_HAND = 4;

    public static int INDEX_RIGHT_HAND = 2;

    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/mp7_reload_norm.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/mp7_draw.gltf"));

    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/mp7_reload_empty.gltf"));

    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/mp7_inspect.gltf"));

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/mp7_static.gltf"));

    private static final Mp7AnimationController instance = new Mp7AnimationController();

    private Mp7AnimationController() {
        try {
            Animations.load(RELOAD_NORM);
            Animations.load(RELOAD_EMPTY);
            Animations.load(DRAW);
            Animations.load(INSPECT);
            Animations.load(STATIC);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        enableStaticState();
        GunAnimationController.setAnimationControllerMap(ModItems.MP7.getId(),this);
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.MP7.get(), label);
    }

    public static Mp7AnimationController getInstance(){ return instance; }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case RELOAD_NORMAL: return RELOAD_NORM;
            case RELOAD_EMPTY: return RELOAD_EMPTY;
            case DRAW: return DRAW;
            case INSPECT: return INSPECT;
            case STATIC: return STATIC;
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
