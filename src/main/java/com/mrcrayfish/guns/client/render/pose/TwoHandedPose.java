package com.mrcrayfish.guns.client.render.pose;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.client.AimPose;
import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.LimbPose;
import com.mrcrayfish.guns.client.render.HeldAnimation;
import com.mrcrayfish.guns.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class TwoHandedPose implements HeldAnimation
{
    private AimPose upPose;
    private AimPose forwardPose;
    private AimPose downPose;

    public TwoHandedPose()
    {
        this.initPose();
    }

    private void initPose()
    {
        this.upPose = new AimPose();
        this.upPose.getIdle()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(60F, 0F, 10F))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-120F)
                .setRotationAngleY(-55F)
                .setRotationPointX(-5)
                .setRotationPointY(3)
                .setRotationPointZ(0))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-160F)
                .setRotationAngleY(-20F)
                .setRotationAngleZ(-30F)
                .setRotationPointZ(-1));
        this.upPose.getAiming()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(40F, 0F, 30F))
            .setItemTranslate(new Vector3f(-1, 0, 0))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-140F)
                .setRotationAngleY(-55F)
                .setRotationPointX(-5)
                .setRotationPointY(3)
                .setRotationPointZ(0))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-170F)
                .setRotationAngleY(-20F)
                .setRotationAngleZ(-35F)
                .setRotationPointY(1)
                .setRotationPointZ(0));

        this.forwardPose = new AimPose();
        this.forwardPose.getIdle()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(30F, -11F, 0F))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-60F)
                .setRotationAngleY(-55F)
                .setRotationAngleZ(0F)
                .setRotationPointX(-5)
                .setRotationPointY(2)
                .setRotationPointZ(1))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-65F)
                .setRotationAngleY(-10F)
                .setRotationAngleZ(5F)
                .setRotationPointZ(-1));
        this.forwardPose.getAiming()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(5F, -21F, 0F))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-85F)
                .setRotationAngleY(-65F)
                .setRotationAngleZ(0F)
                .setRotationPointX(-5)
                .setRotationPointY(2))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-90F)
                .setRotationAngleY(-15F)
                .setRotationAngleZ(0F)
                .setRotationPointY(2)
                .setRotationPointZ(0));

        this.downPose = new AimPose();
        this.downPose.getIdle()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(-15F, -5F, 0F))
            .setItemTranslate(new Vector3f(0, -0.5F, 0.5F))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-30F)
                .setRotationAngleY(-65F)
                .setRotationAngleZ(0F)
                .setRotationPointX(-5)
                .setRotationPointY(2))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-5F)
                .setRotationAngleY(-20F)
                .setRotationAngleZ(20F)
                .setRotationPointY(2)
                .setRotationPointZ(0));
        this.downPose.getAiming()
            .setRenderYawOffset(45F)
            .setItemRotation(new Vector3f(-20F, -5F, -10F))
            .setItemTranslate(new Vector3f(0, -0.5F, 1F))
            .setRightArm(new LimbPose()
                .setRotationAngleX(-30F)
                .setRotationAngleY(-65F)
                .setRotationAngleZ(0F)
                .setRotationPointX(-5)
                .setRotationPointY(1))
            .setLeftArm(new LimbPose()
                .setRotationAngleX(-10F)
                .setRotationAngleY(-20F)
                .setRotationAngleZ(30F)
                .setRotationPointY(2)
                .setRotationPointZ(0));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerModelRotation(PlayerEntity player, PlayerModel model, Hand hand, float aimProgress)
    {
        initPose();

        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        ModelRenderer mainArm = right ? model.bipedRightArm : model.bipedLeftArm;
        ModelRenderer secondaryArm = right ? model.bipedLeftArm : model.bipedRightArm;

        if(Minecraft.getInstance().getRenderViewEntity() == player && Minecraft.getInstance().gameSettings.thirdPersonView == 0)
        {
            mainArm.rotateAngleX = 0;
            mainArm.rotateAngleY = 0;
            mainArm.rotateAngleZ = 0;
            return;
        }

        if(Config.CLIENT.display.oldAnimations.get())
        {
            mainArm.rotateAngleX = (float) Math.toRadians(-55F + aimProgress * -30F);
            mainArm.rotateAngleY = (float) Math.toRadians((-45F + aimProgress * -20F) * (right ? 1F : -1F));
            secondaryArm.rotateAngleX = (float) Math.toRadians(-42F + aimProgress * -48F);
            secondaryArm.rotateAngleY = (float) Math.toRadians((-15F + aimProgress * 5F) * (right ? 1F : -1F));
        }
        else
        {
            float angle = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), player.prevRotationPitch, player.rotationPitch) / 90F;
            float angleAbs = Math.abs(angle);
            AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
            this.applyAimPose(targetPose, mainArm, secondaryArm, angleAbs, aimProgress);
            model.bipedHead.rotateAngleX = (float) Math.toRadians(angle > 0.0 ? angle * 70F : angle * 90F);
        }
    }

    private void applyAimPose(AimPose targetPose, ModelRenderer rightArm, ModelRenderer leftArm, float partial, float zoom)
    {
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getRightArm(), targetPose.getAiming().getRightArm(), this.forwardPose.getIdle().getRightArm(), this.forwardPose.getAiming().getRightArm(), rightArm, partial, zoom);
        this.applyLimbPoseToModelRenderer(targetPose.getIdle().getLeftArm(), targetPose.getAiming().getLeftArm(), this.forwardPose.getIdle().getLeftArm(), this.forwardPose.getAiming().getLeftArm(), leftArm, partial, zoom);
    }

    private void applyLimbPoseToModelRenderer(LimbPose targetIdlePose, LimbPose targetAimingPose, LimbPose idlePose, LimbPose aimingPose, ModelRenderer renderer, float partial, float zoom)
    {
        renderer.rotateAngleX = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleX(), targetAimingPose.getRotationAngleX(), idlePose.getRotationAngleX(), aimingPose.getRotationAngleX(), renderer.rotateAngleX, partial, zoom));
        renderer.rotateAngleY = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleY(), targetAimingPose.getRotationAngleY(), idlePose.getRotationAngleY(), aimingPose.getRotationAngleY(), renderer.rotateAngleY, partial, zoom));
        renderer.rotateAngleZ = (float) Math.toRadians(this.getValue(targetIdlePose.getRotationAngleZ(), targetAimingPose.getRotationAngleZ(), idlePose.getRotationAngleZ(), aimingPose.getRotationAngleZ(), renderer.rotateAngleZ, partial, zoom));
        renderer.rotationPointX = this.getValue(targetIdlePose.getRotationPointX(), targetAimingPose.getRotationPointX(), idlePose.getRotationPointX(), aimingPose.getRotationPointX(), renderer.rotationPointX, partial, zoom);
        renderer.rotationPointY = this.getValue(targetIdlePose.getRotationPointY(), targetAimingPose.getRotationPointY(), idlePose.getRotationPointY(), aimingPose.getRotationPointY(), renderer.rotationPointY, partial, zoom);
        renderer.rotationPointZ = this.getValue(targetIdlePose.getRotationPointZ(), targetAimingPose.getRotationPointZ(), idlePose.getRotationPointZ(), aimingPose.getRotationPointZ(), renderer.rotationPointZ, partial, zoom);
    }

    private float getValue(@Nullable Float t1, @Nullable Float t2, Float s1, Float s2, Float def, float partial, float zoom)
    {
        float start = t1 != null && s1 != null ? s1 + (t1 - s1) * partial : (s1 != null ? s1 : def);
        float end = t2 != null && s2 != null ? s2 + (t2 - s2) * partial : (s2 != null ? s2 : def);
        return MathHelper.lerp(zoom, start, end);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
        float angle = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), player.prevRotationPitch, player.rotationPitch) / 90F;
        float angleAbs = Math.abs(angle);
        AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;
        float rightOffset = this.getValue(targetPose.getIdle().getRenderYawOffset(), targetPose.getAiming().getRenderYawOffset(), this.forwardPose.getIdle().getRenderYawOffset(), this.forwardPose.getAiming().getRenderYawOffset(), 0F, angleAbs, aimProgress);
        player.prevRenderYawOffset = player.prevRotationYaw + rightOffset;
        player.renderYawOffset = player.rotationYaw + rightOffset;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void applyHeldItemTransforms(PlayerEntity player, Hand hand, float aimProgress, MatrixStack matrixStack, IRenderTypeBuffer buffer)
    {
        if(hand == Hand.MAIN_HAND)
        {
            boolean right = Minecraft.getInstance().gameSettings.mainHand == HandSide.RIGHT ? hand == Hand.MAIN_HAND : hand == Hand.OFF_HAND;
            matrixStack.translate(0, 0, 0.05);

            float angle = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), player.prevRotationPitch, player.rotationPitch) / 90F;
            float angleAbs = Math.abs(angle);
            AimPose targetPose = angle > 0.0 ? this.downPose : this.upPose;

            float translateX = this.getValue(targetPose.getIdle().getItemTranslate().getX(), targetPose.getAiming().getItemTranslate().getX(), this.forwardPose.getIdle().getItemTranslate().getX(), this.forwardPose.getAiming().getItemTranslate().getX(), 0F, angleAbs, aimProgress);
            float translateY = this.getValue(targetPose.getIdle().getItemTranslate().getY(), targetPose.getAiming().getItemTranslate().getY(), this.forwardPose.getIdle().getItemTranslate().getY(), this.forwardPose.getAiming().getItemTranslate().getY(), 0F, angleAbs, aimProgress);
            float translateZ = this.getValue(targetPose.getIdle().getItemTranslate().getZ(), targetPose.getAiming().getItemTranslate().getZ(), this.forwardPose.getIdle().getItemTranslate().getZ(), this.forwardPose.getAiming().getItemTranslate().getZ(), 0F, angleAbs, aimProgress);
            matrixStack.translate(translateX * 0.0625, translateY * 0.0625, translateZ * 0.0625);

            float rotateX = this.getValue(targetPose.getIdle().getItemRotation().getX(), targetPose.getAiming().getItemRotation().getX(), this.forwardPose.getIdle().getItemRotation().getX(), this.forwardPose.getAiming().getItemRotation().getX(), 0F, angleAbs, aimProgress);
            float rotateY = this.getValue(targetPose.getIdle().getItemRotation().getY(), targetPose.getAiming().getItemRotation().getY(), this.forwardPose.getIdle().getItemRotation().getY(), this.forwardPose.getAiming().getItemRotation().getY(), 0F, angleAbs, aimProgress);
            float rotateZ = this.getValue(targetPose.getIdle().getItemRotation().getZ(), targetPose.getAiming().getItemRotation().getZ(), this.forwardPose.getIdle().getItemRotation().getZ(), this.forwardPose.getAiming().getItemRotation().getZ(), 0F, angleAbs, aimProgress);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(rotateX));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(rotateY));
            matrixStack.rotate(Vector3f.ZP.rotationDegrees(rotateZ));

            //matrixStack.rotate(Vector3f.XP.rotationDegrees(60F * invertRealProgress + aimProgress * 5F));
            //matrixStack.rotate(Vector3f.YP.rotationDegrees((0F * invertRealProgress + aimProgress * -20F) * (right ? 1F : -1F)));
            //matrixStack.rotate(Vector3f.ZP.rotationDegrees((35F + aimProgress * -20F)));
            //matrixStack.rotate(Vector3f.XP.rotationDegrees(30F * invertRealProgress + aimProgress * 5F));
            //matrixStack.rotate(Vector3f.YP.rotationDegrees((-10F * invertRealProgress + aimProgress * -20F) * (right ? 1F : -1F)));
        }
    }

    @Override
    public void renderFirstPersonArms(ClientPlayerEntity player, HandSide hand, ItemStack stack, MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, float partialTicks)
    {
        matrixStack.translate(0, 0, -1);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180F));

        matrixStack.push();

        float reloadProgress = ClientHandler.getGunRenderer().getReloadProgress(partialTicks);
        matrixStack.translate(0, -reloadProgress * 2, 0);

        int side = hand.opposite() == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(6 * side * 0.0625, -0.585, -0.5);

        if(Minecraft.getInstance().player.getSkinType().equals("slim") && hand.opposite() == HandSide.LEFT)
        {
            matrixStack.translate(0.03125F * -side, 0, 0);
        }

        matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(15F * -side));
        matrixStack.rotate(Vector3f.ZP.rotationDegrees(15F * -side));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-35F));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(player, hand.opposite(), matrixStack, buffer, light);

        matrixStack.pop();

        double centerOffset = 2.5;
        if(Minecraft.getInstance().player.getSkinType().equals("slim"))
        {
            centerOffset += hand == HandSide.RIGHT ? 0.2 : 0.8;
        }
        centerOffset = hand == HandSide.RIGHT ? -centerOffset : centerOffset;
        matrixStack.translate(centerOffset * 0.0625, -0.4, -0.975);

        matrixStack.rotate(Vector3f.XP.rotationDegrees(80F));
        matrixStack.scale(0.5F, 0.5F, 0.5F);

        RenderUtil.renderFirstPersonArm(player, hand, matrixStack, buffer, light);
    }
}
