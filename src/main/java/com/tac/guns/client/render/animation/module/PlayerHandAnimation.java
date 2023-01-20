package com.tac.guns.client.render.animation.module;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.handler.GunRenderingHandler;
import com.tac.guns.client.handler.command.ObjectRenderEditor;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Vector;

@OnlyIn(Dist.CLIENT)
public class PlayerHandAnimation {
    public static void render(GunAnimationController controller, ItemCameraTransforms.TransformType transformType, MatrixStack matrices, IRenderTypeBuffer renderBuffer, int light){
        if(!transformType.isFirstPerson()) return;
        matrices.push();
        {
            controller.applyRightHandTransform(matrices);
            RenderUtil.renderFirstPersonArm(Minecraft.getInstance().player, HandSide.RIGHT, matrices, renderBuffer, light);
        }
        matrices.pop();
        matrices.push();
        {
            if(GunRenderingHandler.get() != null)
            {
                /*if(ObjectRenderEditor.get() != null && ObjectRenderEditor.get().GetFromElements(1) != null)
                {
                    element = ObjectRenderEditor.get().GetFromElements(1);
                }*/

                //When performing a tactical sprint, apply additional actions
                if(GunRenderingHandler.get().wSpeed > 0.094f) {
                    ObjectRenderEditor.RENDER_Element element =
                            new ObjectRenderEditor.RENDER_Element(0,0,0.25f,0);
                    float transition = GunRenderingHandler.get().sOT;

                    float result = GunRenderingHandler.get().sprintDynamicsHSSLeftHand.update(0.15f, transition);
                    //Reverse the left arm rotation
                    matrices.rotate(Vector3f.XP.rotationDegrees(-90F * result));
                    matrices.rotate(Vector3f.ZP.rotationDegrees(25f * result));
                    matrices.translate(-1.2 * /*leftHanded **/ result ,
                            -0.8 * result ,
                            0);
                }
            }
            controller.applyLeftHandTransform(matrices);
            RenderUtil.renderFirstPersonArm(Minecraft.getInstance().player, HandSide.LEFT, matrices, renderBuffer, light);
        }
        matrices.pop();
    }
}
