package com.mrcrayfish.guns.util.math;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.util.math.EntityRayTraceResult;

/**
 * Author: MrCrayfish
 */
public class ExtendedEntityRayTraceResult extends EntityRayTraceResult
{
    private final boolean headshot;

    public ExtendedEntityRayTraceResult(ProjectileEntity.EntityResult result)
    {
        super(result.getEntity(), result.getHitPos());
        this.headshot = result.isHeadshot();
    }

    public boolean isHeadshot()
    {
        return this.headshot;
    }
}
