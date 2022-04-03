package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.render.IHeldAnimation;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public class OneHandedPose implements IHeldAnimation
{
    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        ModelRenderer arm = right ? model.bipedRightArm : model.bipedLeftArm;
        IHeldAnimation.copyModelAngles(model.bipedHead, arm);
        arm.rotateAngleX += Math.toRadians(-70F);
    }

    @Override
    public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
    {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

        IBakedModel model = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(stack, player.world, player);
        float translateX = model.getItemCameraTransforms().firstperson_right.translation.getX();
        float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getX();
        int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(translateX * side, 0, -translateZ);

        boolean slim = player.getSkinType().equals("slim");
        float armWidth = slim ? 3.0F : 4.0F;

        matrixStack.scale(0.5F, 0.5F, 0.5F);
        matrixStack.translate(-4.0 * 0.0625 * side, 0, 0);
        matrixStack.translate(-(armWidth / 2.0) * 0.0625 * side, 0, 0);

        matrixStack.translate(0, 0.15, -1.3125);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(75F));

        RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
    }

    @Override
    public boolean applyOffhandTransforms(PlayerEntity player, PlayerModel model, ItemStack stack, MatrixStack matrixStack, float partialTicks)
    {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));

        if(player.isCrouching())
        {
            matrixStack.translate(-4.5 * 0.0625, -15 * 0.0625, -4 * 0.0625);
        }
        else if(!player.getItemStackFromSlot(EquipmentSlotType.LEGS).isEmpty())
        {
            matrixStack.translate(-4.0 * 0.0625, -13 * 0.0625, 1 * 0.0625);
        }
        else
        {
            matrixStack.translate(-3.5 * 0.0625, -13 * 0.0625, 1 * 0.0625);
        }

        matrixStack.rotate(Vector3f.YP.rotationDegrees(90F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(75F));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees((float) (Math.toDegrees(model.bipedRightLeg.rotateAngleX) / 10F)));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        return true;
    }

    @Override
    public boolean canApplySprintingAnimation()
    {
        return false;
    }

    @Override
    public boolean canRenderOffhandItem()
    {
        return true;
    }
}
