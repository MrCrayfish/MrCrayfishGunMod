package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ShootingHandler;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.GunItem;
import com.tac.guns.item.attachment.IAttachment;
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
public class m92fs_animation implements IOverrideModel {

    //The render method, similar to what is in DartEntity. We can render the item
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        
        /*if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
        {
            RenderUtil.renderModel(SpecialModels.M92FS_EXTENDED_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.M92FS_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }*/

        RenderUtil.renderModel(SpecialModels.M92FS.getModel(), stack, matrices, renderBuffer, light, overlay);
        if(Gun.getAttachment(IAttachment.Type.PISTOL_BARREL,stack).getItem() == ModItems.PISTOL_SILENCER.get())
        {
            RenderUtil.renderModel(SpecialModels.M92FS_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
            //Always push
            matrices.push();

            Gun gun = ((GunItem) stack.getItem()).getGun();
        float cooldownOg = ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate()) < 0 ? 1 : ShootingHandler.get().getshootMsGap() / ShootingHandler.calcShootTickGap(gun.getGeneral().getRate());
        
             

        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.235f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            GunRenderingHandler.get().opticMovement = 0.235f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0);
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.235f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.235f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, 0.235f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.235f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
        }

        matrices.translate(0.00, 0.0, -0.0745);
        RenderUtil.renderModel(SpecialModels.M92FS_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

            //Always pop
            matrices.pop();
    }

     

    //TODO comments
}
