package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.vector.Vector3f;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Mr. Pineapple
 */
public class spas_12_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI) && Config.CLIENT.quality.reducedGuiWeaponQuality.get())
        {
            matrices.push();
            matrices.rotate(Vector3f.XP.rotationDegrees(-60.0F));
            matrices.rotate(Vector3f.YP.rotationDegrees(225.0F));
            matrices.rotate(Vector3f.ZP.rotationDegrees(-90.0F));
            matrices.translate(0.9,0,0);
            matrices.scale(1.5F,1.5F,1.5F);
            RenderUtil.renderModel(stack, stack, matrices, renderBuffer, light, overlay);
            matrices.pop();
            return;
        }
        RenderUtil.renderModel(SpecialModels.SPAS_12_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        RenderUtil.renderModel(SpecialModels.SPAS_12_PUMP.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always push
        matrices.push();

        matrices.translate(0,0,0.35);
        RenderUtil.renderModel(SpecialModels.SPAS_12_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.translate(0,0,-0.35);
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());

        // SHOTGUN ANIMATION, USE ONLY FOR PUMP MODE

        /*if (cooldownOg != 0 && cooldownOg < 0.66 || !Gun.hasAmmo(stack)) {
            double cooldownOgTmp = cooldownOg * 1.46;
            if (Gun.hasAmmo(stack)) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.200f * (-4.5 * Math.pow(cooldownOgTmp - 0.5, 2) + 1.0));
            } else if (!Gun.hasAmmo(stack)) {
                if (cooldownOg > 0.5) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.200f * (-4.5 * Math.pow(cooldownOgTmp - 0.5, 2) + 1.0));
                }
                else {
                    matrices.translate(0, 0, 0.200f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                }
            }
        }*/
        matrices.translate(0,0,0.035);
        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.250f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.250f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else
            {
                matrices.translate(0, 0, 0.250f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            }
        }
        //matrices.translate(0.00, 0.0, 0.085);
        RenderUtil.renderModel(SpecialModels.SPAS_12_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always pop
        matrices.pop();
    }
}
