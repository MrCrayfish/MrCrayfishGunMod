package com.mrcrayfish.guns.object;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Author: MrCrayfish
 */
public class EntityResult
{
    public Entity entity;
    public Vec3d hitVec;

    public EntityResult(Entity entity, Vec3d hitVec)
    {
        this.entity = entity;
        this.hitVec = hitVec;
    }
}
