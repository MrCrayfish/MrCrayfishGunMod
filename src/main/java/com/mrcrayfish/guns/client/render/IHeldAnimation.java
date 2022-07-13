package com.mrcrayfish.guns.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public interface IHeldAnimation
{
    /**
     * Allows for modifications of the player model. This is where arms should be aligned to hold
     * the weapon correctly. This also gives you access to the current aiming progress.
     *  @param model an get of the player model
     * @param head
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     */
    @OnlyIn(Dist.CLIENT)
    default void applyPlayerModelRotation(Player player, ModelPart rightArm, ModelPart leftArm, ModelPart head, InteractionHand hand, float aimProgress) {}

    /**
     * Allows for transformations of the player model. This is where the entire player model can
     * be rotated and translated to suit the current weapon.
     *
     * @param player the player holding the weapon
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     * @param poseStack the current matrix stack
     * @param buffer a render type buffer get
     */
    @OnlyIn(Dist.CLIENT)
    default void applyPlayerPreRender(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer) {}

    /**
     * Allows for transformations of the weapon before rendering. This is where rotations can be
     * applied to the item for animating aiming.
     *
     * @param hand the hand which is currently being used
     * @param aimProgress the current animation progress of looking down the weapons sight
     * @param poseStack the current matrix stack
     * @param buffer a render type buffer get
     */
    @OnlyIn(Dist.CLIENT)
    default void applyHeldItemTransforms(Player player, InteractionHand hand, float aimProgress, PoseStack poseStack, MultiBufferSource buffer) {}

    /**
     *  @param player
     * @param hand
     * @param stack
     * @param poseStack
     * @param buffer
     * @param light
     * @param partialTicks
     */
    default void renderFirstPersonArms(Player player, HumanoidArm hand, ItemStack stack, PoseStack poseStack, MultiBufferSource buffer, int light, float partialTicks) {}

    /**
     *
     * @param player
     * @param model
     * @param stack
     * @param partialTicks
     */
    default boolean applyOffhandTransforms(Player player, PlayerModel model, ItemStack stack, PoseStack poseStack, float partialTicks)
    {
        return false;
    }

    /**
     * If the sprinting animation should be applied in first person for the grip type this held
     * animation is applied to.
     *
     * @return can apply sprinting animation
     */
    default boolean canApplySprintingAnimation()
    {
        return true;
    }

    /**
     * @return <code>true</code> if any items in the offhand should be rendered
     */
    default boolean canRenderOffhandItem()
    {
        return false;
    }

    /**
     * Copies the rotations from one {@link ModelPart} get to another
     *
     * @param source the model renderer to grab the rotations from
     * @param dest   the model renderer to apply the rotations to
     */
    @OnlyIn(Dist.CLIENT)
    static void copyModelAngles(ModelPart source, ModelPart dest)
    {
        dest.xRot = source.xRot;
        dest.yRot = source.yRot;
        dest.zRot = source.zRot;
    }

    /**
     *
     * @return
     */
    default double getFallSwayZOffset()
    {
        return 0.35;
    }
}
