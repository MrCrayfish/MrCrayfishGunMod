package com.mrcrayfish.guns.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;

/**
 * Author: MrCrayfish
 */
public class GunItemStackRenderer extends BlockEntityWithoutLevelRenderer
{
    public GunItemStackRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack poseStack, MultiBufferSource source, int light, int overlay)
    {
        // Hack to remove transforms created by ItemRenderer#render
        poseStack.popPose();

        poseStack.pushPose();
        {
            Minecraft mc = Minecraft.getInstance();
            if(transform == ItemTransforms.TransformType.GROUND)
            {
                GunRenderingHandler.get().applyWeaponScale(stack, poseStack);
            }
            GunRenderingHandler.get().renderWeapon(mc.player, stack, transform, poseStack, source, light, Minecraft.getInstance().getDeltaFrameTime());
        }
        poseStack.popPose();

        // Push the stack again since we popped the pose prior
        poseStack.pushPose();
    }
}
