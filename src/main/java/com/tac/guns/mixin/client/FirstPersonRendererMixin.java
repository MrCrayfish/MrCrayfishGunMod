package com.tac.guns.mixin.client;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.client.handler.AnimationHandler;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.CommonStateBox;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageToClientRigInv;
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
    private ItemStack prevItemStack = ItemStack.EMPTY;
    private int prevSlot = 0;
    @Shadow
    private float equippedProgressMainHand;
    @Shadow
    private float prevEquippedProgressMainHand;

    @Inject(method = "tick",at = @At("HEAD"))
    public void applyDrawAndHolster(CallbackInfo ci){
        if(Minecraft.getInstance().player == null) return;
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getHeldItemMainhand();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        GunAnimationController controller1 = GunAnimationController.fromItem(this.prevItemStack.getItem());
        if(prevItemStack.isItemEqual(mainHandItemStack)
                && (prevSlot == Minecraft.getInstance().player.inventory.currentItem && !CommonStateBox.isSwapped ) )
            return;
        prevItemStack = mainHandItemStack;
        prevSlot = Minecraft.getInstance().player.inventory.currentItem;
        CommonStateBox.isSwapped = false;
        //if(isSameWeapon(Minecraft.getInstance().player)) return;
        if(controller1 != null) {
            controller1.stopAnimation();
        }
        if(controller != null && controller == controller1){
            //Stop the previous item's animation
            AnimationMeta meta = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW);
            if(!controller.getPreviousAnimation().equals(meta)) controller.stopAnimation();
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
        }else if(controller != null && controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.DRAW) != null) {
            this.itemStackMainHand = mainHandItemStack;
            controller.runAnimation(GunAnimationController.AnimationLabel.DRAW);
            PacketHandler.getPlayChannel().sendToServer(new MessageToClientRigInv(((GunItem)mainHandItemStack.getItem()).getGun().getProjectile().getItem()));
        }
    }
    /*
             */
    @Inject(method = "tick",at = @At("RETURN"))
    public void cancelEquippedProgress(CallbackInfo ci){
        if(Minecraft.getInstance().player == null) return;
        ItemStack mainHandItemStack = Minecraft.getInstance().player.getHeldItemMainhand();
        GunAnimationController controller = GunAnimationController.fromItem(mainHandItemStack.getItem());
        if(controller == null ) return;
        equippedProgressMainHand = 1.0f;
        prevEquippedProgressMainHand = 1.0f;
    }
}
