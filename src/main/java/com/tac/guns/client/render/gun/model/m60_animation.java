package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.animation.M60AnimationController;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
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
 * Author: Timeless Development, and associates.
 */
public class m60_animation implements IOverrideModel {

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
        M60AnimationController controller = M60AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M60.getModel(), M60AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) == null) {
                RenderUtil.renderModel(SpecialModels.M60_UNFOLDED_SIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.M60_FOLDED_SIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.M60.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M60.getModel(), M60AnimationController.INDEX_MAGAZINE, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M60_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M60.getModel(), M60AnimationController.INDEX_CHAIN, transformType, matrices);
            if(controller.isAnimationRunning(GunAnimationController.AnimationLabel.RELOAD_EMPTY)
                    || Gun.hasAmmo(stack)) {
                RenderUtil.renderModel(SpecialModels.M60_BULLET_CHAIN.getModel(), stack, matrices, renderBuffer, light, overlay);

            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M60.getModel(), M60AnimationController.INDEX_CAPS, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M60_CAPS.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.M60_HANDLE.getModel(), M60AnimationController.INDEX_HANDLE, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.M60_HANDLE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
