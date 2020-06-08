package com.mrcrayfish.guns.object;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.render.HeldAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public enum GripType
{
    @SerializedName("one_handed")
    ONE_HANDED(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer arm = right ? model.bipedRightArm : model.bipedLeftArm;
            copyModelAngles(model.bipedHead, arm);
            arm.rotateAngleX += Math.toRadians(-70F);
        }
    }, true),
    @SerializedName("two_handed")
    TWO_HANDED(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

            copyModelAngles(model.bipedHead, mainArm);
            copyModelAngles(model.bipedHead, secondaryArm);
            mainArm.rotateAngleX = (float) Math.toRadians(-55F + aimProgress * -30F);
            mainArm.rotateAngleY = (float) Math.toRadians((-45F + aimProgress * -20F) * (right ? 1F : -1F));
            secondaryArm.rotateAngleX = (float) Math.toRadians(-42F + aimProgress * -48F);
            secondaryArm.rotateAngleY = (float) Math.toRadians((-15F + aimProgress * 5F) * (right ? 1F : -1F));
        }

        @Override
        public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + (right ? 25F : -25F) + aimProgress * (right ? 20F : -20F);
            player.renderYawOffset = player.rotationYaw + (right ? 25F : -25F) + aimProgress * (right ? 20F : -20F);
        }

        @Override
        public void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            if(hand == Hand.MAIN_HAND)
            {
                boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
                matrixStack.translate(0, 0, 0.05);
                float invertRealProgress = 1.0F - aimProgress;
                matrixStack.rotate(Vector3f.ZP.rotationDegrees((25F * invertRealProgress) * (right ? 1F : -1F)));
                matrixStack.rotate(Vector3f.YP.rotationDegrees((30F * invertRealProgress + aimProgress * -20F) * (right ? 1F : -1F)));
                matrixStack.rotate(Vector3f.XP.rotationDegrees(25F * invertRealProgress + aimProgress * 5F));
            }
        }
    }, false),
    @SerializedName("chain_gun")
    CHAIN_GUN(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(PlayerModel model, Hand hand, float aimProgress)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
            ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

            mainArm.rotateAngleX = (float) Math.toRadians(-15F);
            mainArm.rotateAngleY = (float) Math.toRadians(-45F) * (right ? 1F : -1F);
            mainArm.rotateAngleZ = (float) Math.toRadians(0F);

            secondaryArm.rotateAngleX = (float) Math.toRadians(-45F);
            secondaryArm.rotateAngleY = (float) Math.toRadians(30F) * (right ? 1F : -1F);
            secondaryArm.rotateAngleZ = (float) Math.toRadians(0F);
        }

        @Override
        public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            player.prevRenderYawOffset = player.prevRotationYaw + 45F * (right ? 1F : -1F);
            player.renderYawOffset = player.rotationYaw + 45F * (right ? 1F : -1F);
        }

        @Override
        public void applyHeldItemTransforms(Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
        {
            if(hand == Hand.OFF_HAND)
            {
                matrixStack.translate(0, -10 * 0.0625F, 0);
                matrixStack.translate(0, 0, -2 * 0.0625F);
            }
        }
    }, false);

    private final HeldAnimation heldAnimation;
    private final boolean renderOffhand;

    GripType(HeldAnimation heldAnimation, boolean renderOffhand)
    {
        this.heldAnimation = heldAnimation;
        this.renderOffhand = renderOffhand;
    }

    public HeldAnimation getHeldAnimation()
    {
        return heldAnimation;
    }

    public boolean canRenderOffhand()
    {
        return renderOffhand;
    }

    @OnlyIn(Dist.CLIENT)
    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }
}
