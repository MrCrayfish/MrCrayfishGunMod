package com.mrcrayfish.guns.object;

import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class HitResult
{
    private Vector3d hitPos;
    private boolean headshot;

    public HitResult(@Nullable Vector3d hitPos, boolean headshot)
    {
        this.hitPos = hitPos;
        this.headshot = headshot;
    }

    public Optional<Vector3d> getHitPos()
    {
        return Optional.ofNullable(this.hitPos);
    }

    public boolean isHeadshot()
    {
        return this.headshot;
    }
}
