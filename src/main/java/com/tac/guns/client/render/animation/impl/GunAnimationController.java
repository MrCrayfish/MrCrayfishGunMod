package com.tac.guns.client.render.animation.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageAnimationSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public abstract class GunAnimationController {
    public enum AnimationLabel{
        RELOAD_NORMAL,
        RELOAD_EMPTY,
        INSPECT,
        DRAW,
    }
    private AnimationMeta previousAnimation;

    private AnimationSoundMeta previousSound;

    /*A map to obtain AnimationController through Item, the key value should put the RegistryName of the Item.*/
    private static final Map<ResourceLocation, GunAnimationController> animationControllerMap = new HashMap<>();

    private void runAnimation(AnimationMeta animationMeta, AnimationSoundMeta soundMeta){
        if(animationMeta != null) {
            Animations.runAnimation(animationMeta);
            previousAnimation = animationMeta;
        }
        if(animationMeta != null && soundMeta != null) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if(player == null) return;
            MessageAnimationSound message = new MessageAnimationSound(
                    animationMeta.getResourceLocation(),
                    soundMeta.getResourceLocation(),
                    true,
                    player.getUniqueID());
            PacketHandler.getPlayChannel().sendToServer(message);
            previousSound = soundMeta;
        }
    }

    public boolean isAnimationRunning(){
        return Animations.isAnimationRunning(previousAnimation);
    }

    public AnimationMeta getPreviousAnimation(){
        return previousAnimation;
    }

    public void stopAnimation() {
        if(previousAnimation != null) {
            Animations.stopAnimation(previousAnimation);
        }
        if(previousAnimation != null && previousSound != null){
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if(player == null) return;
            MessageAnimationSound message = new MessageAnimationSound(
                    previousAnimation.getResourceLocation(),
                    previousSound.getResourceLocation(),
                    false,
                    player.getUniqueID());
            PacketHandler.getPlayChannel().sendToServer(message);
        }
    }

    public void runAnimation(AnimationLabel label){
        runAnimation(getAnimationFromLabel(label), getSoundFromLabel(label));
    }

    public abstract AnimationMeta getAnimationFromLabel(AnimationLabel label);
    protected abstract int getAttachmentsNodeIndex();
    protected abstract int getRightHandNodeIndex();
    protected abstract int getLeftHandNodeIndex();

    public AnimationSoundMeta getSoundFromLabel(AnimationLabel label){
        return null;
    }

    protected AnimationSoundMeta getSoundFromLabel(Item item, AnimationLabel label){
        if(item instanceof GunItem){
            GunItem gunItem = (GunItem) item;
            Gun.Sounds sounds = gunItem.getGun().getSounds();
            switch (label){
                case RELOAD_EMPTY: return new AnimationSoundMeta(sounds.getReloadEmpty());
                case RELOAD_NORMAL: return new AnimationSoundMeta(sounds.getReloadNormal());
                case DRAW: return new AnimationSoundMeta(sounds.getDraw());
                case INSPECT: return new AnimationSoundMeta(sounds.getInspect());
                default: return null;
            }
        }
        return null;
    }

    public void applyAttachmentsTransform(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, LivingEntity entity, MatrixStack matrixStack){
        boolean isFirstPerson = transformType.isFirstPerson();
        if( isFirstPerson ) Animations.pushNode(previousAnimation, getAttachmentsNodeIndex());
        Animations.applyAnimationTransform(itemStack, ItemCameraTransforms.TransformType.NONE, entity, matrixStack);
        if( isFirstPerson ) Animations.popNode();
    }

    public void applySpecialModelTransform(IBakedModel model, int index, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack){
        boolean isFirstPerson = transformType.isFirstPerson();
        if( isFirstPerson ) Animations.pushNode(previousAnimation, index);
        Animations.applyAnimationTransform(model, ItemCameraTransforms.TransformType.NONE, matrixStack);
        if( isFirstPerson ) Animations.popNode();
    }

    public void applyTransform(ItemStack itemStack, int index, ItemCameraTransforms.TransformType transformType, LivingEntity entity, MatrixStack matrixStack){
        boolean isFirstPerson = transformType.isFirstPerson();
        if( isFirstPerson ) Animations.pushNode(previousAnimation, index);
        Animations.applyAnimationTransform(itemStack, ItemCameraTransforms.TransformType.NONE, entity, matrixStack);
        if( isFirstPerson ) Animations.popNode();
    }

    public void applyRightHandTransform(MatrixStack matrixStack)
    {
        if(previousAnimation != null) {
            Animations.pushNode(previousAnimation,getRightHandNodeIndex());
            matrixStack.translate(-0.5,-0.5,-0.5);
            Matrix4f animationTransition = new Matrix4f(Animations.peekNodeModel().computeGlobalTransform(null));
            animationTransition.transpose();
            matrixStack.getLast().getMatrix().mul(animationTransition);
            Animations.popNode();
        }
    }

    public void applyLeftHandTransform(MatrixStack matrixStack)
    {
        if(previousAnimation != null) {
            Animations.pushNode(previousAnimation,getLeftHandNodeIndex());
            matrixStack.translate(-0.5,-0.5,-0.5);
            Matrix4f animationTransition = new Matrix4f(Animations.peekNodeModel().computeGlobalTransform(null));
            animationTransition.transpose();
            matrixStack.getLast().getMatrix().mul(animationTransition);
            Animations.popNode();
        }
    }

    /**
     * @param itemRegistryName The RegistryName of the Item.
     * @param animationController The animationController instance you want to register.
     */
    public static void setAnimationControllerMap(ResourceLocation itemRegistryName, GunAnimationController animationController){
        animationControllerMap.put(itemRegistryName, animationController);
    }

    public static GunAnimationController fromItem(Item item){
        return animationControllerMap.get(item.getRegistryName());
    }

    public static GunAnimationController fromRegistryName(ResourceLocation registryName){
        return animationControllerMap.get(registryName);
    }
}
