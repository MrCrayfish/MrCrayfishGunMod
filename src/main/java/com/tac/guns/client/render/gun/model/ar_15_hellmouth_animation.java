package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.ShootingHandler;
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
public class ar_15_hellmouth_animation implements IOverrideModel {

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        if (Gun.getScope(stack) == null)
        {
            RenderUtil.renderModel(SpecialModels.AR_15_FSU.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.AR_15_FS.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.LIGHT_STOCK.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_LIGHT_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            RenderUtil.renderModel(SpecialModels.AR_15_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.TACTICAL_STOCK.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_TACTICAL_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
            RenderUtil.renderModel(SpecialModels.AR_15_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else if(Gun.getAttachment(IAttachment.Type.STOCK, stack).getItem() == ModItems.WEIGHTED_STOCK.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_HEAVY_STOCK.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) == 3)
        {
            RenderUtil.renderModel(SpecialModels.AR_15_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.AR_15_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_COMPENSATOR.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_COMPENSATOR.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else if(Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.MUZZLE_BRAKE.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_BRAKE.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.AR_15_DEFAULT_BARREL.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_TAC_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else if(Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_LIGHT_GRIP.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        /*if(Gun.getAttachment(IAttachment.Type.SIDE_RAIL, stack).getItem() == ModItems.STANDARD_FLASHLIGHT.orElse(ItemStack.EMPTY.getItem()))
        {
            RenderUtil.renderModel(SpecialModels.AR_15_CQB_STANDARD_FLASHLIGHT.getModel(), stack, matrices, renderBuffer, light, overlay);
        }*/

        RenderUtil.renderModel(SpecialModels.AR_15_BODY.getModel(), stack, matrices, renderBuffer, light, overlay);

        matrices.push();
        Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());

        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            }
            else
            {
                matrices.translate(0, 0, 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
            }
        }

        RenderUtil.renderModel(SpecialModels.AR_15_BOLT.getModel(), stack, matrices, renderBuffer, light, overlay);
        matrices.pop();
    }
}
