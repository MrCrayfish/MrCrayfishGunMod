package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: MrCrayfish
 */
public class GrenadeLauncherModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, int overlay)
    {
        RenderUtil.renderModel(SpecialModels.GRENADE_LAUNCHER_BASE.getModel(), stack, matrixStack, renderTypeBuffer, light, overlay);
        
        if(entity.equals(Minecraft.getInstance().player))
        {
            matrixStack.push();
            matrixStack.translate(0, -5.8 * 0.0625, 0);
            CooldownTracker tracker = Minecraft.getInstance().player.getCooldownTracker();
            float cooldown = tracker.getCooldown(stack.getItem(), Minecraft.getInstance().getRenderPartialTicks());
            cooldown = (float) easeInOutBack(cooldown);
            matrixStack.rotate(Vector3f.ZN.rotationDegrees(45F * cooldown));
            matrixStack.translate(0, 5.8 * 0.0625, 0);
            RenderUtil.renderModel(SpecialModels.GRENADE_LAUNCHER_CYLINDER.getModel(), stack, matrixStack, renderTypeBuffer, light, overlay);
            matrixStack.pop();
        }
    }

    /**
     * Easing function based on code from https://easings.net/#easeInOutBack
     */
    private double easeInOutBack(double x)
    {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;
        return (x < 0.5 ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
    }
}
