package com.mrcrayfish.guns.object;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Author: MrCrayfish
 */
public class EntityResult
{
    public Entity entity;
    public Vector3d hitVec;
    public boolean headshot;

    public EntityResult(Entity entity, Vector3d hitVec, boolean headshot)
    {
        this.entity = entity;
        this.hitVec = hitVec;
        this.headshot = headshot;
    }
}
