package com.mrcrayfish.guns.common.headshot;

import com.mrcrayfish.guns.interfaces.IHeadshotBox;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class BasicHeadshotBox<T extends LivingEntity> implements IHeadshotBox<T>
{
    protected double headWidth;
    protected double headHeight;
    protected double headYOffset;

    public BasicHeadshotBox(double headSize, double headYOffset)
    {
        this.headWidth = headSize;
        this.headHeight = headSize;
        this.headYOffset = headYOffset;
    }

    public BasicHeadshotBox(double headWidth, double headHeight, double headYOffset)
    {
        this.headWidth = headWidth;
        this.headHeight = headHeight;
        this.headYOffset = headYOffset;
    }

    @Nullable
    @Override
    public AABB getHeadshotBox(T entity)
    {
        double halfWidth = this.headWidth / 2.0;
        AABB headBox = new AABB(-halfWidth * 0.0625, 0, -halfWidth * 0.0625, halfWidth * 0.0625, this.headHeight * 0.0625, halfWidth * 0.0625);
        headBox = headBox.move(0, this.headYOffset * 0.0625, 0);
        return headBox;
    }
}
