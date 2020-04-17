package com.mrcrayfish.guns.client.render;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Hand;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(PlayerModel model, Hand hand, float aimProgress) {}

    public void applyPlayerPreRender(PlayerEntity player, Hand hand, float aimProgress) {}

    public void applyHeldItemTransforms(Hand hand, float aimProgress) {}
}
