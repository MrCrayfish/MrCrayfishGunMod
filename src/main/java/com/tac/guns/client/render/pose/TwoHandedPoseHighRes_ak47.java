package com.tac.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.animation.GunAnimationController;
import com.tac.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/**
 * Author: ClumsyAlien, codebase and design based off Mr.Crayfish's class concept
 */
public class TwoHandedPoseHighRes_ak47 extends TwoHandedPose {
	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
	{

		matrixStack.push();
		GunAnimationController controller = GunAnimationController.fromItem(stack.getItem());
		if(controller != null) controller.applyLeftHandTransform(stack, player, matrixStack);
		matrixStack.translate(0, 0, -1);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

		//float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks, stack);
		//extraMatrixStack.translate(reloadProgress * 1.25, -reloadProgress, -reloadProgress * 1.5);


		/*int side = hand.opposite() == HandSide.RIGHT ? -1 : 1;
		double sideFloat = hand.opposite() == HandSide.RIGHT ? -1F : -1;
		double x = hand.opposite() == HandSide.RIGHT ? 1 : 1;
		double y = hand.opposite() == HandSide.RIGHT ? -0.905 : -1.015;
		double z = hand.opposite() == HandSide.RIGHT ? 0.04 : -0.04;
		matrixStack.translate(8.5 * sideFloat * 0.0625, y, z);*/
		int side = hand.opposite() == HandSide.RIGHT ? -1 : 1;
		double translationSide = hand.opposite() == HandSide.RIGHT ? -1 : -1;
		//matrixStack.translate(8.5 * translationSide * 0.0625, -1.015, -0.04);
		matrixStack.translate(6.875 * translationSide * 0.0625, -1.015, -0.04);

		if (Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT) {
			matrixStack.translate(0.03125F * -side, 0, 0);
		}


		/*matrixStack.rotate(Vector3f.XP.rotationDegrees(side == -1 ? 80F : 80F));*/
		matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * side));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * side));
		matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
		matrixStack.scale(1.0F, 1.0F, 1.0F);

		RenderUtil.renderFirstPersonArm(player, hand.opposite(), matrixStack, buffer, light);
		matrixStack.pop();

		matrixStack.push();
		if(controller != null) controller.applyRightHandTransform(stack, player, matrixStack);
		matrixStack.translate(0, 0, -1);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

		double centerOffset = 2.5;
		if (Minecraft.getInstance().player.getSkinType().equals("slim")) {
			centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
		}
		/*int side = hand.opposite() == HandSide.RIGHT ? -1 : 1;
		double translationSide = hand.opposite() == HandSide.RIGHT ? -1 : -1;
		*/
		centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset*-10.5;
		matrixStack.translate(centerOffset * 0.0135, -0.745, -1.075);

		matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
		matrixStack.scale(1F, 1F, 1F);
		RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
		matrixStack.pop();
	}
}
/*

package com.tac.guns.client.render.pose;

		import com.mojang.blaze3d.matrix.MatrixStack;
		import com.tac.guns.client.render.animation.GunAnimationController;
		import com.tac.guns.client.util.RenderUtil;
		import net.minecraft.client.Minecraft;
		import net.minecraft.client.entity.player.ClientPlayerEntity;
		import net.minecraft.client.renderer.IRenderTypeBuffer;
		import net.minecraft.item.ItemStack;
		import net.minecraft.util.HandSide;
		import net.minecraft.util.math.vector.Vector3f;


*/
/**
 * Author: ClumsyAlien, codebase and design based off Mr.Crayfish's class concept
 *//*

public class TwoHandedPoseHighRes_ak47 extends TwoHandedPose {
	@Override
	public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks) {

		matrixStack.push();
		GunAnimationController controller = GunAnimationController.fromItem(stack.getItem());
		if(controller != null) controller.applyLeftHandTransform(stack, player, matrixStack);
		matrixStack.translate(0, 0, -1);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

		//float reloadProgress = ReloadHandler.get().getReloadProgress(partialTicks, stack);
		//extraMatrixStack.translate(reloadProgress * 1.25, -reloadProgress, -reloadProgress * 1.5);

		float sideFloat = hand.opposite() == HandSide.RIGHT ? -0.25F : -1;
		int side = hand.opposite() == HandSide.RIGHT ? -1 : 1;
		*/
/*if(sideFloat == 1)
			matrixStack.translate(3.5 * sideFloat * 0.0625, 0, 0);*//*


		double y = sideFloat == -0.25 ? -0.905 : -1.015;
		double z = sideFloat == -0.25 ? 0.04 : -0.04;
		matrixStack.translate(8.5 * sideFloat * 0.0625, y, z);

		if (Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT) {
			matrixStack.translate(0.03125F * -sideFloat, 0, 0);
		}

		matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * -side));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * -side));
		matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
		matrixStack.scale(1.0F, 1.0F, 1.0F);

		RenderUtil.renderFirstPersonArm(player, hand.opposite(), matrixStack, buffer, light);
		matrixStack.pop();

		matrixStack.push();
		if(controller != null) controller.applyRightHandTransform(stack, player, matrixStack);
		matrixStack.translate(0, 0, -1);
		matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

		double centerOffset = 2.5;
		if (Minecraft.getInstance().player.getSkinType().equals("slim")) {
			centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
		}
		centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
		matrixStack.translate(centerOffset * 0.0405, -0.745, -1.075);

		matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
		matrixStack.scale(1F, 1F, 1F);
		RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
		matrixStack.pop();

	}
}
*/
