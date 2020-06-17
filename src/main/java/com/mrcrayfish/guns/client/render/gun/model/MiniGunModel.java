package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class MiniGunModel implements IOverrideModel
{
    private Map<UUID, Rotations> rotationMap = new HashMap<>();

    @Override
    public void tick(PlayerEntity entity)
    {
        this.rotationMap.putIfAbsent(entity.getUniqueID(), new Rotations());
        Rotations rotations = this.rotationMap.get(entity.getUniqueID());
        rotations.prevRotation = rotations.rotation;

        boolean shooting = SyncedPlayerData.instance().get(entity, ModSyncedDataKeys.SHOOTING);
        ItemStack heldItem = entity.getHeldItemMainhand();
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
        Rotations rotations = this.rotationMap.computeIfAbsent(entity.getUniqueID(), uuid -> new Rotations());
        RenderUtil.renderModel(SpecialModels.MINI_GUN_BASE.getModel(), stack,matrixStack, renderTypeBuffer, light, overlay);
        RenderUtil.renderModel(SpecialModels.MINI_GUN_BARRELS.getModel(), ItemCameraTransforms.TransformType.NONE, () -> {
            RenderUtil.rotateZ(matrixStack, 0.5F, 0.125F, rotations.prevRotation + (rotations.rotation - rotations.prevRotation) * partialTicks);
        }, stack, parent, matrixStack, renderTypeBuffer, light, overlay);
    }

    private class Rotations
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
