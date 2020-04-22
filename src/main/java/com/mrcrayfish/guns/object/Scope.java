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
    private float yawSensitivity;
    private float pitchSensitivity;
    private boolean stable;
    private boolean smoothCamera;

    private Scope(float additionalZoom, double centerOffset)
    {
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
    }

    /**
     *
     * @return
     */
    public Scope stabilise()
    {
        this.stable = true;
        return this;
    }

    /**
     *
     * @return
     */
    public Scope smoothCamera()
    {
        this.smoothCamera = true;
        return this;
    }

    /**
     * Gets the amount of additional zoom (or reduced fov) this scope provides
     *
     * @return the scopes additional zoom
     */
    public float getAdditionalZoom()
    {
        return this.additionalZoom;
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
        return this.centerOffset;
    }

    /**
     *
     * @return
     */
    public float getYawSensitivity()
    {
        return this.yawSensitivity;
    }

    /**
     *
     * @return
     */
    public float getPitchSensitivity()
    {
        return this.pitchSensitivity;
    }

    public boolean isStable()
    {
        return this.stable;
    }

    public static Scope create(float additionalZoom, double centerOffset)
    {
        return new Scope(additionalZoom, centerOffset);
    }

    public static Scope create(float additionalZoom, double centerOffset, float yawSensitivity, float pitchSensitivity)
    {
        return new Scope(additionalZoom, centerOffset);
    }
}
