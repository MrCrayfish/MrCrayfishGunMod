package com.mrcrayfish.guns.object;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public class Scope
{
    private float additionalZoom;
    private double centerOffset;

    private Scope(float additionalZoom, double centerOffset)
    {
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
    }

    /**
     * Gets the amount of additional zoom (or reduced fov) this scope provides
     *
     * @return the scopes additional zoom
     */
    public float getAdditionalZoom()
    {
        return additionalZoom;
    }

    /**
     * Gets the offset to the center of the scope. Used to render scope cross hair exactly in the
     * middle of the screen.
     *
     * @return the scope center offset
     */
    @OnlyIn(Dist.CLIENT)
    public double getCenterOffset()
    {
        return centerOffset;
    }

    public static Scope create(float additionalZoom, double centerOffset)
    {
        return new Scope(additionalZoom, centerOffset);
    }
}
