package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModel;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.animation.MK14AnimationController;
import com.tac.guns.client.render.animation.module.AnimationMeta;
import com.tac.guns.client.render.animation.module.GunAnimationController;
import com.tac.guns.client.render.animation.module.PlayerHandAnimation;
import com.tac.guns.client.render.gun.IOverrideModel;
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

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class mk14_animation implements IOverrideModel {

    private final SpecialModel MK14_BODY = new SpecialModel("mk14");
    private final SpecialModel BOLT = new SpecialModel("mk14_bolt");
    private final SpecialModel BOLT_HANDLE = new SpecialModel("mk14_bolt_handle");
    private final SpecialModel STANDARD_MAG = new SpecialModel("mk14_standard_mag");
    private final SpecialModel EXTENDED_MAG = new SpecialModel("mk14_extended_mag");
    private final SpecialModel T_GRIP = new SpecialModel("mk14_tac_grip");
    private final SpecialModel L_GRIP = new SpecialModel("mk14_light_grip");
    private final SpecialModel SCOPE_MOUNT = new SpecialModel("mk14_mount");

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        MK14AnimationController controller = MK14AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(MK14_BODY.getModel(), MK14AnimationController.INDEX_BODY, transformType, matrices);
            if (Gun.getScope(stack) != null) {
                RenderUtil.renderModel(SCOPE_MOUNT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(L_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);

            } else if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(T_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(L_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);

            } else if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
                RenderUtil.renderModel(T_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            RenderUtil.renderModel(MK14_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);

        } matrices.pop();


        matrices.push();
        {
            controller.applySpecialModelTransform(MK14_BODY.getModel(), MK14AnimationController.INDEX_BOLT, transformType, matrices);
            Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        

            AnimationMeta reloadEmpty = controller.getAnimationFromLabel(GunAnimationController.AnimationLabel.RELOAD_EMPTY);
            boolean shouldOffset = reloadEmpty != null && reloadEmpty.equals(controller.getPreviousAnimation()) && controller.isAnimationRunning();
            if(Gun.hasAmmo(stack) || shouldOffset)
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
            RenderUtil.renderModel(BOLT_HANDLE.getModel(), stack, matrices, renderBuffer, light, overlay);

            if(Gun.hasAmmo(stack)|| shouldOffset)
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
            RenderUtil.renderModel(BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        } matrices.pop();


        matrices.push();
        {
            controller.applySpecialModelTransform(MK14_BODY.getModel(), MK14AnimationController.INDEX_MAGAZINE, transformType, matrices);

            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0) {
                RenderUtil.renderModel(EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        } matrices.pop();

        matrices.push();
        {
            matrices.translate(0, 0, 0.2175f);
            PlayerHandAnimation.render(controller, transformType, matrices, renderBuffer, light);
        }matrices.pop();
    }
}
