package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.animation.Ak47AnimationController;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.attachment.IAttachment;
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
public class type81_x_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
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
        Ak47AnimationController controller = Ak47AnimationController.getInstance();
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker(); // getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks()); // getRenderPartialTicks()); // getCooldown(stack.getItem(), Minecraft.getInstance().getFrameTime());
        matrices.push();
        {
            //controller.applySpecialModelTransform(SpecialModels.TYPE81_X.getModel(),Ak47AnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getScope(stack) != null) {
                RenderUtil.renderModel(SpecialModels.TYPE81_X_MOUNT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.TYPE81_X.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        //Always push
        matrices.push();
        {
            //controller.applySpecialModelTransform(SpecialModels.AK47.getModel(), Ak47AnimationController.INDEX_BOLT, transformType, matrices);

            /*//We're getting the cooldown tracker for the item - items like the sword, ender pearl, and chorus fruit all have this too.
            CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
            float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());*/

            // Math provided by Bomb787 on GitHub and Curseforge!!!
            //matrices.translate(0, 0, 0.190f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1));
            if(Gun.hasAmmo(stack))
            {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else if(!Gun.hasAmmo(stack))
            {
                if(cooldownOg > 0.5){
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
                }
                else
                {
                    matrices.translate(0, 0, 0.280f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                }
            }
            matrices.translate(0, 0, 0.025F);
            RenderUtil.renderModel(SpecialModels.TYPE81_X_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        //Always pop
        matrices.pop();
/*
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.AK47.getModel(), Ak47AnimationController.INDEX_MAGAZINE, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.AK47_MAGAZINE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();*/
    }

     

    //TODO comments
}
