package com.mrcrayfish.guns.object;

import com.mrcrayfish.guns.interfaces.IGunModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Author: MrCrayfish
 */
public class Scope extends Attachment
{
    private float additionalZoom;
    private double centerOffset;
    private boolean stable;
    private double viewFinderOffset;

    private Scope(float additionalZoom, double centerOffset, IGunModifier ... modifier)
    {
        super(modifier);
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
    }

    /**
     * Sets the offset distance from the camera to the view finder
     *
     * @param offset the view finder offset
     * @return this scope instance
     */
    public Scope viewFinderOffset(double offset)
    {
        this.viewFinderOffset = offset;
        return this;
    }

    /**
     * Gets the amount of additional zoom (or reduced fov) this scope provides
     *
     * @return the scopes additional zoom
     */
    @OnlyIn(Dist.CLIENT)
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
    public boolean isStable()
    {
        return this.stable;
    }

    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffset()
    {
        return this.viewFinderOffset;
    }

    public static Scope create(float additionalZoom, double centerOffset, IGunModifier ... modifier)
    {
        return new Scope(additionalZoom, centerOffset, modifier);
    }
}
