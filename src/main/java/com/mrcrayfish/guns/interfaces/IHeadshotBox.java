package com.mrcrayfish.guns.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public interface IHeadshotBox<T extends Entity>
{
    @Nullable
    AxisAlignedBB getHeadshotBox(T entity);
}
