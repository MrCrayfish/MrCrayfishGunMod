package com.tac.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.Config;
import com.tac.guns.client.SpecialModels;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.ReloadHandler;
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
 * Author: Mr. Pineapple
 */
public class m1911_animation implements IOverrideModel {

    //The render method, similar to what is in DartEntity. We can render the item
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light, int overlay)
    {
        /*if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GUI)*//* && Config.CLIENT.quality.reducedGuiWeaponQuality.get()*//*)
        {
            matrices.push();
            //matrices.rotate(Vector3f.XP.rotationDegrees(-60.0F));
            //matrices.rotate(Vector3f.YP.rotationDegrees(225.0F));
            //matrices.rotate(Vector3f.ZP.rotationDegrees(-90.0F));
            //matrices.translate(0.9,0,0);
            matrices.scale(1.5F,1.5F,1.5F);
            RenderUtil.renderModel(SpecialModels.M1911_LOD.getModel(), stack, matrices, renderBuffer, light, overlay);
            matrices.pop();
            return;
        }
        if(ModelOverrides.hasModel(stack) && transformType.equals(ItemCameraTransforms.TransformType.GROUND)*//* && Config.CLIENT.quality.reducedGuiWeaponQuality.get()*//*)
        {
            matrices.push();
            RenderUtil.renderModel(SpecialModels.M1911_LOD.getModel(), stack, matrices, renderBuffer, light, overlay);
            matrices.pop();
            return;
        }*/
        CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker(); // getCooldownTracker();
        float cooldownOg = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks()); // getRenderPartialTicks()); // getCooldown(stack.getItem(), Minecraft.getInstance().getFrameTime());
        if(Gun.getAttachment(IAttachment.Type.PISTOL_BARREL,stack).getItem() == ModItems.PISTOL_SILENCER.get())
        {
            matrices.push();
            matrices.translate(0,0,-0.0475);
            RenderUtil.renderModel(SpecialModels.M1911_SUPPRESSOR.getModel(), stack, matrices, renderBuffer, light, overlay);
            matrices.translate(0,0,0.0475);
            matrices.pop();
        }
        // if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), entity) > 0)
        if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.OVER_CAPACITY.get(), stack) > 0)
        {
            RenderUtil.renderModel(SpecialModels.M1911_LONG_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }
        else
        {
            RenderUtil.renderModel(SpecialModels.M1911_STANDARD_MAG.getModel(), stack, matrices, renderBuffer, light, overlay);
        }

        RenderUtil.renderModel(SpecialModels.M1911.getModel(), stack, matrices, renderBuffer, light, overlay);

        //Always push
        matrices.push(); // push();

        if(Gun.hasAmmo(stack))
        {
            // Math provided by Bomb787 on GitHub and Curseforge!!!
            matrices.translate(0, 0, 0.240f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
            GunRenderingHandler.get().opticMovement = 0.240f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0);
        }
        else if(!Gun.hasAmmo(stack))
        {
            if(cooldownOg > 0.5){
                // Math provided by Bomb787 on GitHub and Curseforge!!!
                matrices.translate(0, 0, 0.240f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.240f * (-4.5 * Math.pow(cooldownOg-0.5, 2) + 1.0);
            }
            else
            {
                matrices.translate(0, 0, 0.260f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0));
                GunRenderingHandler.get().opticMovement = 0.260f * (-4.5 * Math.pow(0.5-0.5, 2) + 1.0);
            }
        }
            matrices.translate(0.00, 0.0, -0.008);
            RenderUtil.renderModel(SpecialModels.M1911_SLIDE.getModel(), stack, matrices, renderBuffer, light, overlay);

            //Always pop
            matrices.pop();
    }

     

    //TODO comments
}
