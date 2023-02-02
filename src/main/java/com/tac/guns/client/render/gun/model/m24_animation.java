package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.M24AnimationController;
import com.tac.guns.client.render.animation.SPR15AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.item.GunItem;
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
 * Author: ClumsyAlien, codebase and design based off Mr.Pineapple's original addon
 */
public class m24_animation implements IOverrideModel {

    /*
        I plan on making a very comprehensive description on my render / rendering methods, currently I am unable to give a good explanation on each part and will be supplying one later one in development!

        If you are just starting out I don't recommend attempting to create an animated part of your weapon is as much as I can comfortably give at this point!
    */
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        M24AnimationController controller = M24AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M24_BODY.getModel(), M24AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(SpecialModels.M24_IRON_SIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.M24_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M24_BODY.getModel(), M24AnimationController.INDEX_BOLT, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M24_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M24_BODY.getModel(), M24AnimationController.INDEX_MAGAZINE, transformType, matrices);
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0) {
                RenderUtil.renderModel(SpecialModels.M24_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }else {
                RenderUtil.renderModel(SpecialModels.M24_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M24_BODY.getModel(), M24AnimationController.INDEX_BULLET, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M24_BULLET_SHELL.getModel(), stack, matrices, renderBuffer, light, overlay);
            AnimationMeta bolt = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.PULL_BOLT);
            boolean onlyShell = bolt != null && bolt.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if(!onlyShell) RenderUtil.renderModel(SpecialModels.M24_BULLET_HEAD.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
    }

    //Same method from GrenadeLauncherModel, to make a smooth rotation of the chamber.
    private double easeInOutBack(double x) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return (x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    }
}
