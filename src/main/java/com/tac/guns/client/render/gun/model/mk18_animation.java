package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mk18_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        if(Gun.getScope(stack) == null)
        {
            RenderUtil.renderModel(SpecialModels.MK18_SIGHTS.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        if(Gun.getAttachment(IAttachment.Type.STOCK, stack).equals(ItemStack.EMPTY))
        {
            RenderUtil.renderModel(SpecialModels.MK18_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        //if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), entity.getHeldItemMainhand()) > 0 && entity.getHeldItemMainhand().isItemEqual(stack))
        //{
        //    RenderUtil.renderModel(SpecialModels.MK_A1_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        //}
        //else
        //{
        RenderUtil.renderModel(SpecialModels.MK18_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        //}
        RenderUtil.renderModel(SpecialModels.MK18_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
    }
}
