package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.*;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class M870AnimationController extends PumpShotgunAnimationController {

    public static final int INDEX_PUMP = 0;

    public static final int INDEX_BULLET = 1;

    public static final int INDEX_BODY = 2;

    public static final int INDEX_RIGHT_HAND = 3;

    public static final int INDEX_LEFT_HAND = 6;

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/m870_static.gltf"));

    public static final AnimationMeta PUMP = new AnimationMeta(new ResourceLocation("tac","animations/m870_pump.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/m870_draw.gltf"));

    public static final AnimationMeta INTRO = new AnimationMeta(new ResourceLocation("tac","animations/m870_reload_intro.gltf"));

    public static final AnimationMeta LOOP = new AnimationMeta(new ResourceLocation("tac","animations/m870_reload_loop.gltf"));

    public static final AnimationMeta NORMAL_END = new AnimationMeta(new ResourceLocation("tac","animations/m870_reload_normal_end.gltf"));

    private static final M870AnimationController instance = new M870AnimationController();

    public static M870AnimationController getInstance(){return instance;}
    private M870AnimationController() {
        try {
            Animations.load(STATIC);
            Animations.load(PUMP);
            Animations.load(DRAW);
            Animations.load(INTRO);
            Animations.load(LOOP);
            Animations.load(NORMAL_END);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        enableStaticState();
        GunAnimationController.setAnimationControllerMap(ModItems.M870_CLASSIC.getId(),this);
    }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        switch (label){
            case STATIC: return STATIC;
            case DRAW: return DRAW;
            case PUMP: return PUMP;
            case RELOAD_INTRO: return INTRO;
            case RELOAD_LOOP: return LOOP;
            case RELOAD_NORMAL_END: return NORMAL_END;
            default: return null;
        }
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.M870_CLASSIC.get(), label);
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
