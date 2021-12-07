package com.mrcrayfish.guns.common.headshot;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class NoChildHeadshotBox<T extends LivingEntity> extends BasicHeadshotBox<T>
{
    public NoChildHeadshotBox(double headSize, double headYOffset)
    {
        super(headSize, headYOffset);
    }

    public NoChildHeadshotBox(double headWidth, double headHeight, double headYOffset)
    {
        super(headWidth, headHeight, headYOffset);
    }

    @Nullable
    @Override
    public AABB getHeadshotBox(T entity)
    {
        if(entity.isBaby()) return null;
        return super.getHeadshotBox(entity);
    }
}
