package com.tac.guns.mixin.client;

import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.animation.AnimationMeta;
import com.tac.guns.client.render.animation.Animations;
import com.tac.guns.client.render.animation.GunAnimationController;
import de.javagl.jgltf.model.animation.AnimationRunner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(FirstPersonRenderer.class)
public class FirstPersonRendererMixin {
    @Shadow
    private ItemStack itemStackMainHand;
    private ItemStack prevItemStack = ItemStack.EMPTY;
    @Shadow
    private float equippedProgressMainHand;
    @Shadow
    private float prevEquippedProgressMainHand;

    @Inject(method = "tick",at = @At("HEAD"))
    public void applyDrawAndHolster(CallbackInfo ci){
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getHeldItemMainhand();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        GunAnimationController controller1 = GunAnimationController.fromItem(this.prevItemStack.getItem());
        if(prevItemStack.hasTag()&&prevItemStack.getTag().contains("ID")){
            UUID prevUuid = prevItemStack.getTag().getUniqueId("ID");
            if(mainHandItemStack.hasTag()&&mainHandItemStack.getTag().contains("ID")){
                UUID uuid = mainHandItemStack.getTag().getUniqueId("ID");
                if(uuid.equals(prevUuid)) return;
            }
        }
        prevItemStack = mainHandItemStack;
        if(controller1 != null && controller != controller1) {
            controller1.stopAnimation();
        }
        if(controller != null && controller == controller1){
            AnimationMeta meta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW);
            if(!controller.getPreviousAnimation().equals(meta)) controller.stopAnimation();
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
            AnimationRunner runner = Animations.getAnimationRunner(
                    controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW).getResourceLocation());
            runner.getAnimationManager().setCurrentTimeS(0.1f);
            GunRenderingHandler.get().sprintTransition = 0;
        }else if(controller != null && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW) != null) {
            this.itemStackMainHand = mainHandItemStack;
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
            //Skip the beginning of the draw animation to prevent flickering
            AnimationRunner runner = Animations.getAnimationRunner(
                    controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW).getResourceLocation());
            if(runner.getAnimationManager().getCurrentTimeS() < 0.1f)
                runner.getAnimationManager().setCurrentTimeS(0.1f);
            //Restore sprint gesture
            GunRenderingHandler.get().sprintTransition = 0;
        }
    }
    /*
             */
    @Inject(method = "tick",at = @At("RETURN"))
    public void cancelEquippedProgress(CallbackInfo ci){
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getHeldItemMainhand();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        if(controller == null ) return;
        equippedProgressMainHand = 1.0f;
        prevEquippedProgressMainHand = 1.0f;
    }
}