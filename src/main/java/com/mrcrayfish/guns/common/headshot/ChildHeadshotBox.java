package com.mrcrayfish.guns.common.headshot;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

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
    public AABB getHeadshotBox(T entity)
    {
        AABB headBox = super.getHeadshotBox(entity);
        if(headBox != null && entity.isBaby())
        {
            return new AABB(headBox.minX * this.childHeadScale, headBox.minY * this.headYOffsetScale, headBox.minZ * this.childHeadScale, headBox.maxX * this.childHeadScale, headBox.maxY * (this.headYOffsetScale + 0.065), headBox.maxZ * this.childHeadScale);
        }
        return headBox;
    }
}
