package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.animation.Glock17AnimationController;
import com.tac.guns.client.render.animation.STI2011AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.attachment.IAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.vector.Vector3f;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class sti2011_animation implements IOverrideModel {

    //The render method, similar to what is in DartEntity. We can render the item
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay) {


        STI2011AnimationController controller = STI2011AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.STI2011_BODY.getModel(),STI2011AnimationController.INDEX_BODY,transformType,matrices);
            if (Gun.getAttachment(IAttachment.Type.PISTOL_BARREL, stack).getItem() == ModItems.PISTOL_SILENCER.get()) {
                matrices.translate(0, 0, -0.1825);
                RenderUtil.renderModel(SpecialModels.STI2011_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
                matrices.translate(0, 0, 0.1825);
            }

            RenderUtil.renderModel(SpecialModels.STI2011_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.STI2011_BODY.getModel(),STI2011AnimationController.INDEX_MAG,transformType,matrices);
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0) {
                RenderUtil.renderModel(SpecialModels.STI2011_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.STI2011_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {

            controller.applySpecialModelTransform(SpecialModels.STI2011_BODY.getModel(),STI2011AnimationController.INDEX_SLIDE,transformType,matrices);
            CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
            float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());

            AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();

            matrices.translate(0.00, 0.0, 0.035); // Issues with the slide starting out further forward, seems to be ~ a 0.035 movement
            if (Gun.hasAmmo(stack) || shouldOffset) {
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.260f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.260f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            } else if (!Gun.hasAmmo(stack)) {
                if (cooldownOg > 0.5) {
                    // Math provided by Bomb787 on GitHub and Curseforge!!!
                    matrices.translate(0, 0, 0.260f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                    GunRenderingHandler.get().opticMovement = 0.260f * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
                } else {
                    matrices.translate(0, 0, 0.260f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0));
                    GunRenderingHandler.get().opticMovement = 0.260f * (-4.5 * Math.pow(0.5 - 0.5, 2) + 1.0);
                }
            }
            matrices.translate(0.00, 0.0, -0.008);
            RenderUtil.renderModel(SpecialModels.STI2011_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
