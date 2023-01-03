package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.MK47AnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mk47_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        MK47AnimationController controller = MK47AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.MK47_BODY.getModel(),MK47AnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(SpecialModels.MK47_FSU.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.MK47_FS.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.MK47_LIGHT_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.TACTICAL_STOCK.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.MK47_TACTICAL_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if (Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.MK47_HEAVY_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem())) {
                matrices.push();
                matrices.translate(0, 0, -0.1f);
                RenderUtil.renderModel(SpecialModels.MK47_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
                matrices.translate(0, 0, 0.1f);
                matrices.pop();

            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.MK47_COMPENSATOR.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(SpecialModels.MK47_BRAKE.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else // Default
            {
                RenderUtil.renderModel(SpecialModels.MK47_DEFAULT_BARREL.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            RenderUtil.renderModel(SpecialModels.MK47_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.MK47_BODY.getModel(),MK47AnimationController.INDEX_MAGAZINE,transformType,matrices);
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0) {
                RenderUtil.renderModel(SpecialModels.MK47_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.MK47_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.MK47_BODY.getModel(),MK47AnimationController.INDEX_BOLT,transformType,matrices);
            RenderUtil.renderModel(SpecialModels.MK47_PULL.getModel(), stack, matrices, renderBuffer, light, overlay);

            Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        
            matrices.translate(0, 0, 0.225f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1));
            matrices.translate(0, 0, 0.025f);
            RenderUtil.renderModel(SpecialModels.MK47_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}