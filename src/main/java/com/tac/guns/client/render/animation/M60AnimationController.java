package com.tac.guns.client.render.animation;

import com.tac.guns.GunMod;
import com.tac.guns.client.render.animation.module.*;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAnimationRun;
import de.javagl.jgltf.model.animation.AnimationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class M60AnimationController extends MachineGunAnimationController {
    public static int INDEX_BODY = 3;
    public static int INDEX_LEFT_HAND = 7;
    public static int INDEX_RIGHT_HAND = 4;
    public static int INDEX_MAGAZINE = 1;
    public static int INDEX_CHAIN = 0;
    public static int INDEX_CAPS = 2;

    public static final AnimationMeta STATIC = new AnimationMeta(new ResourceLocation("tac","animations/m60_static.gltf"));
    public static final AnimationMeta RELOAD_NORM = new AnimationMeta(new ResourceLocation("tac","animations/m60_reload_norm.gltf"));
    public static final AnimationMeta RELOAD_EMPTY = new AnimationMeta(new ResourceLocation("tac","animations/m60_reload_empty.gltf"));
    public static final AnimationMeta RELOAD_NORM_SCOPE = new AnimationMeta(new ResourceLocation("tac","animations/m60_reload_norm_scope.gltf"));
    public static final AnimationMeta RELOAD_EMPTY_SCOPE = new AnimationMeta(new ResourceLocation("tac","animations/m60_reload_empty_scope.gltf"));
    public static final AnimationMeta DRAW = new AnimationMeta(new ResourceLocation("tac","animations/m60_draw.gltf"));
    public static final AnimationMeta INSPECT = new AnimationMeta(new ResourceLocation("tac","animations/m60_inspect.gltf"));
    private static final M60AnimationController instance = new M60AnimationController();

    private M60AnimationController(){
        try {
            Animations.load(RELOAD_NORM);
            Animations.load(RELOAD_EMPTY);
            Animations.load(RELOAD_NORM_SCOPE);
            Animations.load(RELOAD_EMPTY_SCOPE);
            Animations.load(DRAW);
            Animations.load(INSPECT);
            Animations.load(STATIC);
        } catch (IOException e) {
            GunMod.LOGGER.fatal(e.getStackTrace());
        }
        enableStaticState();
        GunAnimationController.setAnimationControllerMap(ModItems.M60.getId(),this);
    }

    public static M60AnimationController getInstance(){
        return instance;
    }

    @Override
    public AnimationMeta getAnimationFromLabel(AnimationLabel label) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        switch (label){
            case RELOAD_EMPTY: {
                if(player != null){
                    ItemStack stack = player.getHeldItemMainhand();
                    if(Gun.getScope(stack) == null) return RELOAD_EMPTY;
                    else return RELOAD_EMPTY_SCOPE;
                }
                return RELOAD_EMPTY;
            }
            case RELOAD_NORMAL: {
                if(player != null){
                    ItemStack stack = player.getHeldItemMainhand();
                    if(Gun.getScope(stack) == null) return RELOAD_NORM;
                    else return RELOAD_NORM_SCOPE;
                }
                return RELOAD_NORM;
            }
            case DRAW: return DRAW;
            case INSPECT: return INSPECT;
            case STATIC: return STATIC;
            default: return null;
        }
    }

    @Override
    protected void enableStaticState(){
        try {
            Animations.specifyInitialModel(RELOAD_NORM_SCOPE, STATIC);
            Animations.specifyInitialModel(RELOAD_EMPTY_SCOPE, STATIC);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        super.enableStaticState();
    }

    @Override
    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return super.getSoundFromLabel(ModItems.M60.get(), label);
    }

    @Override
    protected int getAttachmentsNodeIndex() {
        return INDEX_CAPS;
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
