package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.AA12AnimationController;
import com.tac.guns.client.render.animation.M16A4AnimationController;
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
public class m16a4_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        
        M16A4AnimationController controller = M16A4AnimationController.getInstance();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M16A4_BODY.getModel(), M16A4AnimationController.INDEX_BODY,transformType,matrices);
            if(Gun.getScope(stack) == null)
            {
                RenderUtil.renderModel(SpecialModels.M16A4_CARRY.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack) == ItemStack.EMPTY && Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack) == ItemStack.EMPTY)
            {
                RenderUtil.renderModel(SpecialModels.M16A4_DEFAULT_HANDGUARD.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                // Use when more rail implementations are ready.
                //RenderUtil.renderModel(SpecialModels.M4_EXTENDED_HANDGUARD.getModel(), stack, matrices, renderBuffer, light, overlay);
                RenderUtil.renderModel(SpecialModels.M16A4_EXTENDED_HANDGUARD.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                // M16a4 light grip missing, updated with m4 grip place holder, same model based on showcase images
                RenderUtil.renderModel(SpecialModels.M4_L_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.M16A4_TAC_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem()))
            {
                matrices.push();
                matrices.translate(0, 0, -0.3f);
                RenderUtil.renderModel(SpecialModels.M16A4_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
                matrices.translate(0, 0, 0.3f);
                matrices.pop();
            }
            else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.M16A4_COMPENSATOR.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.M16A4_BRAKE.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
                RenderUtil.renderModel(SpecialModels.M16A4_DEFAULT_BARREL.getModel(), stack, matrices, renderBuffer, light, overlay);

            RenderUtil.renderModel(SpecialModels.M16A4_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M16A4_BODY.getModel(), M16A4AnimationController.INDEX_MAGAZINE,transformType,matrices);
            if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
            {
                RenderUtil.renderModel(SpecialModels.M16A4_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                RenderUtil.renderModel(SpecialModels.M16A4_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M16A4_BODY.getModel(), M16A4AnimationController.INDEX_HANDLE,transformType,matrices);
            RenderUtil.renderModel(SpecialModels.M16A4_PULL_HANDLE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        controller.applySpecialModelTransform(SpecialModels.M16A4_BODY.getModel(), M16A4AnimationController.INDEX_BODY,transformType,matrices);

        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        if(transformType.isFirstPerson()) {
            if (Gun.hasAmmo(stack)) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
            } else if (!Gun.hasAmmo(stack)) {
                if (cooldownOg > 0.5) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else {
                    matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
            }
        }
        RenderUtil.renderModel(SpecialModels.M16A4_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
