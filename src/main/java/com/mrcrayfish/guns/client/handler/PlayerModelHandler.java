package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class PlayerModelHandler
{
    /*@SubscribeEvent
    public void onRenderPlayer(PlayerModelEvent.Render.Post event)
    {
        PoseStack poseStack = event.getPoseStack();
        Player player = event.getPlayer();
        ItemStack heldItem = player.getOffhandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            poseStack.pushPose();
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            if(gun.getGeneral().getGripType().getHeldAnimation().applyOffhandTransforms(player, event.getPlayerModel(), heldItem, poseStack, event.getDeltaTicks()))
            {
                MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
                GunRenderingHandler.get().renderWeapon(player, heldItem, ItemTransforms.TransformType.FIXED, poseStack, buffer, event.getLight(), event.getDeltaTicks());
            }
            poseStack.popPose();
        }
    }*/

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getMainHandItem();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof GunItem)
        {
            Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
            gun.getGeneral().getGripType().getHeldAnimation().applyPlayerPreRender(player, InteractionHand.MAIN_HAND, AimingHandler.get().getAimProgress((Player) event.getEntity(), event.getPartialTick()), event.getPoseStack(), event.getMultiBufferSource());
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event)
    {
        /* Makes sure the model part positions reset back to original definitions */
        PlayerModel<AbstractClientPlayer> model = event.getRenderer().getModel();
        boolean slim = ((AbstractClientPlayer) event.getPlayer()).getModelName().equals("slim");
        model.rightArm.x = -5.0F;
        model.rightArm.y = slim ? 2.5F : 2.0F;
        model.rightArm.z = 0.0F;
        model.leftArm.x = 5.0F;
        model.leftArm.y = slim ? 2.5F : 2.0F;
        model.leftArm.z = 0.0F;
        /*model.head.x = 5.0F;
        model.leftArm.y = slim ? 2.5F : 2.0F;
        model.leftArm.z = 0.0F;*/
    }
}
