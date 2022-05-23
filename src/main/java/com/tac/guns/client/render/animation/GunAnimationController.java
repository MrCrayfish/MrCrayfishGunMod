package com.tac.guns.client.render.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public abstract class GunAnimationController {
    public enum AnimationLabel{
        RELOAD_NORMAL,
        INSPECT,
        DRAW,
    }
    private AnimationMeta previousAnimation;
    /*A map to obtain AnimationController through Item, the key value should put the RegistryName of the Item.*/
    private static final Map<ResourceLocation, GunAnimationController> animationControllerMap = new HashMap<>();

    public void runAnimation(AnimationMeta animationMeta){
        Animations.runAnimation(animationMeta);
        previousAnimation = animationMeta;
    }

    public boolean isAnimationRunning(){
        return Animations.isAnimationRunning(previousAnimation);
    }

    public AnimationMeta getPreviousAnimation(){
        return previousAnimation;
    }

    public void stopAnimation() {
        if(previousAnimation!=null) {
            Animations.stopAnimation(previousAnimation);
        }
    }

    public void runAnimation(AnimationLabel label){
        runAnimation(getAnimationFromLabel(label));
    }

    public abstract AnimationMeta getAnimationFromLabel(AnimationLabel label);
    protected abstract int getAttachmentsNodeIndex();
    protected abstract int getRightHandNodeIndex();
    protected abstract int getLeftHandNodeIndex();

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

    public void applyRightHandTransform(ItemStack heldItem, LivingEntity entity, MatrixStack matrixStack)
    {
        if(isAnimationRunning()) {
            Animations.pushNode(previousAnimation, getRightHandNodeIndex());
            Animations.applyAnimationTransform(heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, entity, matrixStack);
            matrixStack.translate(-0.56, 0.52, 0.72);
            Animations.popNode();
        }
    }

    public void applyLeftHandTransform(ItemStack heldItem, LivingEntity entity, MatrixStack matrixStack)
    {
        if(isAnimationRunning()) {
            Animations.pushNode(previousAnimation, getLeftHandNodeIndex());
            Animations.applyAnimationTransform(heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, entity, matrixStack);
            matrixStack.translate(-0.56, 0.52, 0.72);
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
