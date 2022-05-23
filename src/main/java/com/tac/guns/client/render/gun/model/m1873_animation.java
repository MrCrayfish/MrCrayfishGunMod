package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.GunMod;
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
import org.apache.logging.log4j.Level;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Mr. Pineapple
 */
public class m1873_animation implements IOverrideModel {

    //The render method, similar to what is in DartEntity. We can render the item
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {
        //The render method, similar to what is in DartEntity. We can render the item
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
        RenderUtil.renderModel(SpecialModels.M1873.getModel(), stack, matrices, renderBuffer, light, overlay);

        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldown = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());

        matrices.push();
        matrices.translate(0, 0.4375, -0.1875);
        if(cooldown < 0.64)
        {
            matrices.translate(0, -5.8 * 0.0625, 0); //-2
            //GunMod.LOGGER.log(Level.FATAL, cooldown);
            //cooldown = (float) easeInOutBack(cooldown);

            if (cooldown < 0.74) {
                matrices.rotate(Vector3f.ZN.rotationDegrees(-45F * (cooldown * 1.74F))); //.74
                matrices.translate(-0.15 * (cooldown * 1.74F) * 0.0625, 0, 0); //-2
            }
            matrices.translate(0, 5.8 * 0.0625, 0);
        }
        RenderUtil.renderModel(SpecialModels.M1873_CYLINDER.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();

        matrices.push();
        //cooldown = 0.95F;
        matrices.translate(0, 0.3525, -0.0405); //-0.1875
        //if(cooldown != 0)
        //{
            matrices.translate(0, -5.8 * 0.0625, 0);
            matrices.translate(0, -5.15 * (cooldown) * 0.0625, 3.2725 * (cooldown) * 0.0625); //-2 -4.025 * (cooldown) * 0.0625 ----------Adjustable----------
            matrices.rotate(Vector3f.XP.rotationDegrees(-90F * (cooldown))); // * 1.74F
            matrices.translate(0, 5.8 * 0.0625, 0);
        //}
        RenderUtil.renderModel(SpecialModels.M1873_HAMMER.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();
    }
    private double easeInOutBack(double x) {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return (x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    }
     
    //TODO comments
}
