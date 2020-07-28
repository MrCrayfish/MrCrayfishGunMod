package com.mrcrayfish.guns.util;

import com.mrcrayfish.guns.object.EntityResult;
import net.minecraft.util.math.EntityRayTraceResult;

/**
 * Author: MrCrayfish
 */
public class ExtendedEntityRayTraceResult extends EntityRayTraceResult
{
    private final boolean headshot;

    public ExtendedEntityRayTraceResult(EntityResult result)
    {
        super(result.entity, result.hitVec);
        this.headshot = result.headshot;
    }

    public boolean isHeadshot()
    {
        return this.headshot;
    }
}
