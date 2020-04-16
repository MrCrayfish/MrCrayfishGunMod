package com.mrcrayfish.guns.client.render;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EnumHand;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(ModelPlayer model, EnumHand hand, float aimProgress) {}

    public void applyPlayerPreRender(PlayerEntity player, EnumHand hand, float aimProgress) {}

    public void applyHeldItemTransforms(EnumHand hand, float aimProgress) {}
}
