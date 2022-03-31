package com.mrcrayfish.guns.mixin.client;

import com.mrcrayfish.guns.client.handler.AimingHandler;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * I eventually want to get rid of this.
 *
 * Author: MrCrayfish
 */
@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity>
{
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Inject(method = "setupAnim", at = @At(value = "TAIL"))
    private void setupAnimTail(T entity, float animationPos, float animationSpeed, float animationBob, float deltaHeadYaw, float headPitch, CallbackInfo ci)
    {
        if(!(entity instanceof Player player))
            return;

        PlayerModel<T> model = (PlayerModel<T>) (Object) this;
        ItemStack heldItem = player.getMainHandItem();
        if(heldItem.getItem() instanceof GunItem gunItem)
        {
            if(player.isLocalPlayer() && animationPos == 0.0F)
            {
                model.rightArm.xRot = 0;
                model.rightArm.yRot = 0;
                model.rightArm.zRot = 0;
                model.leftArm.xRot = 0;
                model.leftArm.yRot = 0;
                model.leftArm.zRot = 0;
                copyModelAngles(model.rightArm, model.rightSleeve);
                copyModelAngles(model.leftArm, model.leftSleeve);
                return;
            }

            Gun gun = gunItem.getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerModelRotation(player, model.rightArm, model.leftArm, model.head, InteractionHand.MAIN_HAND, AimingHandler.get().getAimProgress(player, Minecraft.getInstance().getFrameTime()));
            copyModelAngles(model.rightArm, model.rightSleeve);
            copyModelAngles(model.leftArm, model.leftSleeve);
            copyModelAngles(model.head, model.hat);
        }
    }

    private static void copyModelAngles(ModelPart source, ModelPart target)
    {
        target.xRot = source.xRot;
        target.yRot = source.yRot;
        target.zRot = source.zRot;
    }
}
