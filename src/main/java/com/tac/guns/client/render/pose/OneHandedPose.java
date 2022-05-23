package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.IHeldAnimation;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
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
 * Author: Forked from MrCrayfish, continued by Timeless devs
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
        arm.rotateAngleX += Math.toRadians(-90F);
    }

    @Override
    public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
    {
        matrixStack.translate(0, 0, -1);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

        double centerOffset = 2.5;
        if(Minecraft.getInstance().player.getSkinType().equals("slim"))
        {
            centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
        }
        centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
        matrixStack.translate(centerOffset * 0.0625, -0.45, -1.0);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(75F));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

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
