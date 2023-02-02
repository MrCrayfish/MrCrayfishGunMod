package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.RPKAnimationController;
import com.tac.guns.client.render.animation.SKSTacticalAnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
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
import net.minecraft.util.math.vector.Vector3f;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class sks_tactical_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        
        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

        SKSTacticalAnimationController controller = SKSTacticalAnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.SKS_TACTICAL.getModel(), SKSTacticalAnimationController.INDEX_BODY,transformType,matrices);

            if (Gun.getScope(stack) != null) {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_SCOPE_RAIL.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_RSIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_LIGHT_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.TACTICAL_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_TACTICAL_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_HEAVY_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_TACTICAL_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_LIGHT_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.SKS_TACTICAL.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.SKS_TACTICAL.getModel(), SKSTacticalAnimationController.INDEX_MAGAZINE,transformType,matrices);

            if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.SKS_TACTICAL.getModel(), SKSTacticalAnimationController.INDEX_BOLT,transformType,matrices);
            if(transformType.isFirstPerson()) {
                AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
                boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
                if (Gun.hasAmmo(stack) || shouldOffset) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.245f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack)) {
                    matrices.translate(0, 0, 0.245f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
            }
            matrices.translate(0, 0, 0.0225f);
            RenderUtil.renderModel(SpecialModels.SKS_TACTICAL_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
