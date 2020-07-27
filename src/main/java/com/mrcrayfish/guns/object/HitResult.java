package com.mrcrayfish.guns.object;

import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class HitResult
{
    private Vec3d hitPos;
    private boolean headshot;

    public HitResult(@Nullable Vec3d hitPos, boolean headshot)
    {
        this.hitPos = hitPos;
        this.headshot = headshot;
    }

    public Optional<Vec3d> getHitPos()
    {
        return Optional.ofNullable(this.hitPos);
    }

    public boolean isHeadshot()
    {
        return this.headshot;
    }
}
