package com.mrcrayfish.guns.client.render;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

/**
 * Author: MrCrayfish
 */
public class HeldAnimation
{
    public void applyPlayerModelRotation(ModelPlayer model, EnumHand hand, float aimProgress) {}

    public void applyPlayerPreRender(EntityPlayer player, EnumHand hand, float aimProgress) {}

    public void applyHeldItemTransforms(EnumHand hand, float aimProgress) {}
}
