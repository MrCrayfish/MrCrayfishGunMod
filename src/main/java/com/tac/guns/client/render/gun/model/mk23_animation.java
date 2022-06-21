package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
import com.tac.guns.util.GunEnchantmentHelper;
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
public class mk23_animation implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        GunItem gunItem = ((GunItem) stack.getItem());
        if(Gun.getAttachment(IAttachment.Type.PISTOL_BARREL,stack).getItem() == ModItems.PISTOL_SILENCER.get())
        {
            RenderUtil.renderModel(SpecialModels.MK23_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
        {
            RenderUtil.renderModel(SpecialModels.MK23_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.MK23_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        RenderUtil.renderModel(SpecialModels.MK23.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always push
        matrices.push();

        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
        float slideDist = 0.225f;
        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            if(GunEnchantmentHelper.getRate(stack, gunItem.getGun()) <= 1 && cooldownOg != 0)
            {
                matrices.translate(0, 0, slideDist * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.185f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, slideDist * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = slideDist * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            }
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, slideDist * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = slideDist * (-4.5 * Math.pow(cooldownOg - 0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, slideDist * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = slideDist * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
        }
        matrices.translate(0, 0, 0.025f);
        RenderUtil.renderModel(SpecialModels.MK23_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always pop
        matrices.pop();
    }
     

    //TODO comments
}
