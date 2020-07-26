package com.mrcrayfish.guns.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Author: MrCrayfish
 */
public interface IHeadshotBox<T extends Entity>
{
    AxisAlignedBB getHeadshotBox(T entity);
}
