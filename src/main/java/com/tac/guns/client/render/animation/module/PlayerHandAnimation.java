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
            controller.applyLeftHandTransform(matrices);

            if(GunRenderingHandler.get() != null)
            {
                Vector3f result =
                        GunRenderingHandler.get().sprintDynamicsHSSLeftHand.update(0.15f,
                                new Vector3f((float) (-0.25 * /*leftHanded **/ GunRenderingHandler.get().sOT), (float) (-0.1 * GunRenderingHandler.get().sOT), 35F /** leftHanded*/ * GunRenderingHandler.get().sOT));
                ObjectRenderEditor.RENDER_Element element =
                        new ObjectRenderEditor.RENDER_Element(0,0,0.25f,0);
                /*if(ObjectRenderEditor.get() != null && ObjectRenderEditor.get().GetFromElements(1) != null)
                {
                    element = ObjectRenderEditor.get().GetFromElements(1);
                }*/

                if(GunRenderingHandler.get().wSpeed > 0.09) {
                    float transition = GunRenderingHandler.get().sOT;
                    matrices.translate(result.getX() + ((1.35515f+0.205f+element.getxMod()) * transition),
                            result.getY() - ((0.875f+element.getyMod()) * transition),
                            element.getzMod()*transition);
                }
            }
            RenderUtil.renderFirstPersonArm(Minecraft.getInstance().player, HandSide.LEFT, matrices, renderBuffer, light);
        }
        matrices.pop();
    }
}
