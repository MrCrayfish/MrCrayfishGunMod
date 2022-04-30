package com.mrcrayfish.guns.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.PlayerItemInHandLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: MrCrayfish
 */
@Mixin(PlayerItemInHandLayer.class)
public class PlayerItemInHandLayerMixin
{
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "renderArmWithItem", at = @At(value = "HEAD"), cancellable = true)
    private void renderArmWithItemHead(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType transformType, HumanoidArm arm, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci)
    {
        InteractionHand hand = Minecraft.getInstance().options.mainHand == arm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if(hand == InteractionHand.OFF_HAND)
        {
            if(stack.getItem() instanceof GunItem)
            {
                ci.cancel();
                return;
            }

            if(entity.getMainHandItem().getItem() instanceof GunItem gunItem)
            {
                Gun modifiedGun = gunItem.getModifiedGun(entity.getMainHandItem());
                if(!modifiedGun.getGeneral().getGripType().getHeldAnimation().canRenderOffhandItem())
                {
                    ci.cancel();
                    return;
                }
            }
        }

        if(stack.getItem() instanceof GunItem gunItem)
        {
            ci.cancel();
            PlayerItemInHandLayer<?, ?> layer = (PlayerItemInHandLayer<?, ?>) (Object) this;
            renderArmWithGun(layer, (Player) entity, stack, gunItem, transformType, hand, arm, poseStack, source, light, Minecraft.getInstance().getFrameTime());
        }
    }

    private static void renderArmWithGun(PlayerItemInHandLayer<?, ?> layer, Player player, ItemStack stack, GunItem item, ItemTransforms.TransformType transformType, InteractionHand hand, HumanoidArm arm, PoseStack poseStack, MultiBufferSource source, int light, float deltaTicks)
    {
        poseStack.pushPose();
        layer.getParentModel().translateToHand(arm, poseStack);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-90F));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180F));
        poseStack.translate(((float) (arm == HumanoidArm.LEFT ? -1 : 1) / 16F), 0.125, -0.625);
        GunRenderingHandler.get().applyWeaponScale(stack, poseStack);
        Gun gun = item.getModifiedGun(stack);
        gun.getGeneral().getGripType().getHeldAnimation().applyHeldItemTransforms(player, hand, AimingHandler.get().getAimProgress(player, deltaTicks), poseStack, source);
        GunRenderingHandler.get().renderWeapon(player, stack, transformType, poseStack, source, light, deltaTicks);
        poseStack.popPose();
    }
}
