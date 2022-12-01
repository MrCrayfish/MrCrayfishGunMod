package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.*;
import com.tac.guns.init.ModItems;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class TtiG34AnimationController extends PistalAnimationController {

    public static int INDEX_BODY = 6;

    public static int INDEX_SLIDE = 12;

    public static int INDEX_MAG = 1;

    public static int INDEX_EXTRA_MAG = 3;

    public static int INDEX_LEFT_HAND = 20;

    public static int INDEX_RIGHT_HAND = 17;

    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/glock_tac_2mag_norm_reload.gltf"));

    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/glock_tac_draw.gltf"));

    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/glock_tac_empty_reload.gltf"));

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/glock_tac_static.gltf"));

    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/glock_tac_inspect.gltf"));

    private static final TtiG34AnimationController instance = new TtiG34AnimationController();

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

    private TtiG34AnimationController() {
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
        GunAnimationController.setAnimationControllerMap(ModItems.TTI_G34.getId(),this);
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.TTI_G34.get(), label);
    }


    public static TtiG34AnimationController getInstance() { return instance; }

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
