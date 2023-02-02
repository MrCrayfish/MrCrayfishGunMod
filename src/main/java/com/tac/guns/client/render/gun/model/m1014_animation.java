package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.M1014AnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
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
public class m1014_animation implements IOverrideModel {

    /*
        I plan on making a very comprehensive description on my render / rendering methods, currently I am unable to give a good explanation on each part and will be supplying one later one in development!

        If you are just starting out I don't recommend attempting to create an animated part of your weapon is as much as I can comfortably give at this point!
    */
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        

        M1014AnimationController controller = M1014AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M1014.getModel(), M1014AnimationController.INDEX_BODY, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M1014.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            if(transformType.isFirstPerson()) {
                controller.applySpecialModelTransform(SpecialModels.M1014.getModel(), M1014AnimationController.INDEX_BOLT, transformType, matrices);
                Gun gun = ((GunItem) stack.getItem()).getGun();
                float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

                if (controller.isAnimationRunning(GunAnimationController.AnimationLabel.INSPECT) || (Gun.hasAmmo(stack) && !controller.isEmpty())/* || controller.isAnimationRunning()*/) {
                    RenderUtil.renderModel(SpecialModels.M1014_SHELL.getModel(), stack, matrices, renderBuffer, light, overlay);
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.2725f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                } else if (!Gun.hasAmmo(stack) || controller.isEmpty()) {
                    {
                        matrices.translate(0, 0, 0.2725f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    }
                }
            }
            matrices.translate(0, 0, 0.025f);
            RenderUtil.renderModel(SpecialModels.M1014_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay); // BOLT
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M1014.getModel(), M1014AnimationController.INDEX_BULLET, transformType, matrices);
            if(Gun.hasAmmo(stack) || controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_LOOP))
                RenderUtil.renderModel(SpecialModels.M1014_BULLET.getModel(), stack, matrices, renderBuffer, light, overlay);
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
