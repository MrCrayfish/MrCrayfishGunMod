package com.tac.guns.interfaces;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

/**
 * This interface used to create hit box definitions of an entity's head. This has to be registered with
 * {@link com.tac.guns.common.BoundingBoxManager#registerHeadshotBox(EntityType, IHeadshotBox)}
 * in order for projectiles to be able to perform a headshot on the given entity.
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public interface IHeadshotBox<T extends Entity>
{
    /**
     * Gets a bounding box of the given entity's head in the world. This method can either return an
     * axis aligned box or null for no hit box.
     *
     * @param entity the entity to create a headshot box from
     * @return an axis aligned box of the entity's head
     */
    @Nullable
    AxisAlignedBB getHeadshotBox(T entity);
}
