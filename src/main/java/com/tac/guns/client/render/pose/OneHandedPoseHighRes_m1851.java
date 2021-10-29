package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Author: ClumsyAlien, codebase and design based off Mr.Crayfish's class concept
 */
public class OneHandedPoseHighRes_m1851 extends OneHandedPose {
	@Override
	public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks) {
		matrixStack.translate(-0.161, -0.27, 0.85);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));
		
		double centerOffset = 2.5;
		if (Minecraft.getInstance().player.getSkinType().equals("slim")) {
			centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
		}
		centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
		if (Minecraft.getInstance().player.getSkinType().equals("slim")) {
			matrixStack.translate(centerOffset * 0.0755, -0.45, 1.0); // (1)^
		} else {
			matrixStack.translate(centerOffset * 0.0625, -0.45, 1.0); // (1)^
		}
		
		matrixStack.rotate(Vector3f.XP.rotationDegrees(90F));
		matrixStack.scale(1.5F, 1.5F, 1.5F);
		
		RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
	}
}
