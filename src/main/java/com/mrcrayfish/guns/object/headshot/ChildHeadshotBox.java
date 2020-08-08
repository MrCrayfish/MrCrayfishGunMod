package com.mrcrayfish.guns.object.headshot;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class ChildHeadshotBox<T extends LivingEntity> extends BasicHeadshotBox<T>
{
    private double childHeadScale;
    private double headYOffsetScale;

    public ChildHeadshotBox(double headSize, double headYOffset, double childHeadScale, double headYOffsetScale)
    {
        super(headSize, headYOffset);
        this.childHeadScale = childHeadScale;
        this.headYOffsetScale = headYOffsetScale;
    }

    public ChildHeadshotBox(double headWidth, double headHeight, double headYOffset, double childHeadScale, double headYOffsetScale)
    {
        super(headWidth, headHeight, headYOffset);
        this.childHeadScale = childHeadScale;
        this.headYOffsetScale = headYOffsetScale;
    }

    @Nullable
    @Override
    public AxisAlignedBB getHeadshotBox(T entity)
    {
        AxisAlignedBB headBox = super.getHeadshotBox(entity);
        if(headBox != null && entity.isChild())
        {
            return new AxisAlignedBB(headBox.minX * this.childHeadScale, headBox.minY * this.headYOffsetScale, headBox.minZ * this.childHeadScale, headBox.maxX * this.childHeadScale, headBox.maxY * (this.headYOffsetScale + 0.065), headBox.maxZ * this.childHeadScale);
        }
        return headBox;
    }
}
