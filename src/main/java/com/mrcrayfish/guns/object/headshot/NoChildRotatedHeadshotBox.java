package com.mrcrayfish.guns.object.headshot;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class NoChildRotatedHeadshotBox<T extends LivingEntity> extends RotatedHeadshotBox<T>
{
    public NoChildRotatedHeadshotBox(double headSize, double headYOffset, double headZOffset, boolean rotatePitch, boolean rotateYaw)
    {
        super(headSize, headYOffset, headZOffset, rotatePitch, rotateYaw);
    }

    public NoChildRotatedHeadshotBox(double headWidth, double headHeight, double headYOffset, double headZOffset, boolean rotatePitch, boolean rotateYaw)
    {
        super(headWidth, headHeight, headYOffset, headZOffset, rotatePitch, rotateYaw);
    }

    @Nullable
    @Override
    public AxisAlignedBB getHeadshotBox(T entity)
    {
        if(entity.isChild()) return null;
        return super.getHeadshotBox(entity);
    }
}
