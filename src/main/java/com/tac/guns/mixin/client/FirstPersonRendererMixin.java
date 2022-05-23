package com.tac.guns.mixin.client;

import com.tac.guns.client.render.animation.Animations;
import com.tac.guns.client.render.animation.GunAnimationController;
import de.javagl.jgltf.model.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonRenderer.class)
public class FirstPersonRendererMixin {
    @Shadow
    private ItemStack itemStackMainHand;

    @Inject(method = "tick",at = @At("HEAD"), cancellable = true)
    public void applyDrawAndHolster(CallbackInfo ci){
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getHeldItemMainhand();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        if(controller == null ) return;
        if(controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW) != null) {
            if( Animations.isAnimationRunning(controller.getPreviousAnimation()) )  return;
            if( this.itemStackMainHand.getItem().equals(mainHandItemStack.getItem()) ) return;
            ci.cancel();
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
        }
    }
}
