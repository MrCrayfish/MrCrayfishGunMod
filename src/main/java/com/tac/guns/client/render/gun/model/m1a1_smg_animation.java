package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.item.GunItem;
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
public class m1a1_smg_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {

        /**/
        RenderUtil.renderModel(SpecialModels.M1A1_SMG_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always push
        matrices.push();

        //We're getting the cooldown tracker for the item - items like the sword, ender pearl, and chorus fruit all have this too.
        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());


        //matrices.translate(0, 0, 0.22f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, -0.22f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, -0.22f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else
            {
                matrices.translate(0, 0, -0.22f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            }
        }
        RenderUtil.renderModel(SpecialModels.M1A1_SMG_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always pop
        matrices.pop();
    }

     

    //TODO comments
}
