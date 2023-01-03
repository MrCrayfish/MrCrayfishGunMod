package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.AimingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
import com.tac.guns.util.OptifineHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: ClumsyAlien, codebase and design based off Mr.Pineapple's original addon
 */
public class m1_garand_animation implements IOverrideModel {

    /*
        I plan on making a very comprehensive description on my render / rendering methods, currently I am unable to give a good explanation on each part and will be supplying one later one in development!

        If you are just starting out I don't recommend attempting to create an animated part of your weapon is as much as I can comfortably give at this point!
    */
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        if((Gun.getScope(stack) != null) && (!(OptifineHelper.isShadersEnabled() || !Config.CLIENT.display.scopeDoubleRender.get()) || !(AimingHandler.get().getNormalisedAdsProgress() > 0.5)))
        {
            RenderUtil.renderModel(SpecialModels.M1_GARAND_MOUNT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        RenderUtil.renderModel(SpecialModels.M1_GARAND.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.push();

        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        float cooldown = (float) easeInOutBack(cooldownOg);

        if(Gun.hasAmmo(stack))
        {
            //RenderUtil.renderModel(SpecialModels.M1_GARAND.getModel(), stack, matrices, renderBuffer, light, overlay);
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else
            {
                matrices.translate(0, 0, 0.205f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            }
        }
        RenderUtil.renderModel(SpecialModels.M1_GARAND_BOLT_HANDLE.getModel(), stack, matrices, renderBuffer, light, overlay); // HANDLE

        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, -0.0335f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0), 0);
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, -0.0335f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0), 0);
            }
            else
            {
                matrices.translate(0, -0.0335f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0), 0);
            }
        }
        RenderUtil.renderModel(SpecialModels.M1_GARAND_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay); // BOLT
        matrices.pop();
    }
    //Same method from GrenadeLauncherModel, to make a smooth rotation of the chamber.
    private double easeInOutBack(double x) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return (x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    }
}
