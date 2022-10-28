package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.render.animation.Vector45AnimationController;
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
import org.lwjgl.system.CallbackI;

/*
 * Because the revolver has a rotating chamber, we need to render it in a
 * different way than normal items. In this case we are overriding the model.
 */

/**
 * Author: Timeless Development, and associates.
 */
public class vector45_animation implements IOverrideModel {

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
        Vector45AnimationController controller = Vector45AnimationController.getInstance();
        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.VECTOR45_BODY.getModel(), Vector45AnimationController.INDEX_BODY, transformType, matrices);
            if(Gun.getScope(stack) == null)
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_SIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_LIGHT_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.TACTICAL_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_TACTICAL_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_HEAVY_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            }

            if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_SILENCER.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_COMP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_BRAKE.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else
            {
                //RenderUtil.renderModel(SpecialModels.AR15_HELLMOUTH_MUZZLE.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            else if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem()))
            {
                RenderUtil.renderModel(SpecialModels.VECTOR45_LGRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
            RenderUtil.renderModel(SpecialModels.VECTOR45_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.VECTOR45_BODY.getModel(), Vector45AnimationController.INDEX_MAGAZINE, transformType, matrices);
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0) {
                RenderUtil.renderModel(SpecialModels.VECTOR45_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            } else {
                RenderUtil.renderModel(SpecialModels.VECTOR45_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
            }
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.VECTOR45_BODY.getModel(), Vector45AnimationController.INDEX_BOLT, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.VECTOR45_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();

        matrices.push();
        {
            controller.applySpecialModelTransform(SpecialModels.VECTOR45_BODY.getModel(), Vector45AnimationController.INDEX_HANDLE, transformType, matrices);
            RenderUtil.renderModel(SpecialModels.VECTOR45_HANDLE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        matrices.pop();
        PlayerHandAnimation.render(controller,transformType,matrices,renderBuffer,light);
    }
}
