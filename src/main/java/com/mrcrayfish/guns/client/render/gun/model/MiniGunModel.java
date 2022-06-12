package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.WeakHashMap;

/**
 * Author: MrCrayfish
 */
public class MiniGunModel implements IOverrideModel
{
    private WeakHashMap<LivingEntity, Rotations> rotationMap = new WeakHashMap<>();

    @Override
    public void tick(PlayerEntity entity)
    {
        this.rotationMap.putIfAbsent(entity, new Rotations());
        Rotations rotations = this.rotationMap.get(entity);
        rotations.prevRotation = rotations.rotation;

        boolean shooting = SyncedPlayerData.instance().get(entity, ModSyncedDataKeys.SHOOTING);
        ItemStack heldItem = entity.getMainHandItem();
        if(!Gun.hasAmmo(heldItem) && !entity.isCreative())
        {
            shooting = false;
        }

        if(shooting)
        {
            rotations.rotation += 20;
        }
        else
        {
            rotations.rotation += 1;
        }
    }

    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        Rotations rotations = this.rotationMap.computeIfAbsent(entity, uuid -> new Rotations());
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, light, overlay, SpecialModels.MINI_GUN_BASE.getModel());
        matrixStack.pushPose();
        RenderUtil.rotateZ(matrixStack, 0.0F, -0.375F, rotations.prevRotation + (rotations.rotation - rotations.prevRotation) * partialTicks);
        Minecraft.getInstance().getItemRenderer().render(stack, ItemCameraTransforms.TransformType.NONE, false, matrixStack, renderTypeBuffer, light, overlay, SpecialModels.MINI_GUN_BARRELS.getModel());
        matrixStack.popPose();
    }

    private static class Rotations
    {
        private int rotation;
        private int prevRotation;
    }

    @SubscribeEvent
    public void onClientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.rotationMap.clear();
    }
}
