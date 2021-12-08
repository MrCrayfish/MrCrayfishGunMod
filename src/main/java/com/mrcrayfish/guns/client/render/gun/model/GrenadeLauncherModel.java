package com.mrcrayfish.guns.client.render.gun.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.SpecialModels;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import com.mojang.math.Vector3f;

/**
 * Author: MrCrayfish
 */
public class GrenadeLauncherModel implements IOverrideModel
{
    @Override
    public void render(float partialTicks, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity entity, PoseStack poseStack, MultiBufferSource source, int light, int overlay)
    {
        RenderUtil.renderItemWithoutTransforms(SpecialModels.GRENADE_LAUNCHER_BASE.getModel(), stack, parent, poseStack, source, light, overlay);

        if(entity.equals(Minecraft.getInstance().player))
        {
            poseStack.pushPose();
            poseStack.translate(0, -5.8 * 0.0625, 0);
            ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
            float cooldown = tracker.getCooldownPercent(stack.getItem(), Minecraft.getInstance().getFrameTime());
            cooldown = (float) easeInOutBack(cooldown);
            poseStack.mulPose(Vector3f.ZN.rotationDegrees(45F * cooldown));
            poseStack.translate(0, 5.8 * 0.0625, 0);
            RenderUtil.renderItemWithoutTransforms(SpecialModels.GRENADE_LAUNCHER_CYLINDER.getModel(), stack, parent, poseStack, source, light, overlay);
            poseStack.popPose();
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
