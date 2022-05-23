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

/*
    Here is a more complicated piece of the mod, the custom Pose

    Much of this entire file is from the Original One Handed Pose from the CGM mod, and is only slightly modified in some very specific values, the one comment is the only part I have changed
*/
public class OneHandedPoseHighRes_m1911 extends OneHandedPose {
	@Override
	public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks) {
        /*
            For changing the single hand Pose animation ( Where will the hand be rendered, used to display the hand / hands on screen in a certain location, matching the current held gun)
            We would need to change the location of our hand by adjusting the values of [x,y,z](1), changing the final rotation of the rendered hand(2), and changing the scale of the rendered hand (3).
        */
		
		matrixStack.translate(-0.151, -0.335, 0.70);
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
		matrixStack.rotate(Vector3f.XP.rotationDegrees(82.5F)); // (2)^
		matrixStack.scale(1.4F, 1.4F, 1.4F); // (3)^
		
		RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light); // Finally render our hand with the params we've set
	}
}
