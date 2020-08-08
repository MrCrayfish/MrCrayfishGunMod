package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.interfaces.IGunModifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * An attachment class related to scopes. Scopes need to at least specify the additional zoom (or fov)
 * they provide and the y-offset to the center of the scope for them to render correctly. Use
 * {@link #create(float, double, IGunModifier...)} to create an instance.
 * <p>
 * Author: MrCrayfish
 */
public class Scope extends Attachment
{
    private float additionalZoom;
    private double centerOffset;
    private boolean stable;
    private double viewFinderOffset;

    private Scope(float additionalZoom, double centerOffset, IGunModifier... modifier)
    {
        super(modifier);
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
    }

    /**
     * Marks this scope to allow it to be stabilised while using a controller. This is essentially
     * holding your breath while looking down the sight.
     */
    public void stabilise()
    {
        this.stable = true;
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
     * @return If this scope can be stabilised
     */
    @OnlyIn(Dist.CLIENT)
    public boolean isStable()
    {
        return this.stable;
    }

    /**
     * @return The view finder offset of this scope
     */
    @OnlyIn(Dist.CLIENT)
    public double getViewFinderOffset()
    {
        return this.viewFinderOffset;
    }

    /**
     * Creates a scope instance
     *
     * @param additionalZoom the additional zoom this scope provides
     * @param centerOffset   the length to the center of the view finder from the base of the scope model in pixels
     * @param modifiers      an array of gun modifiers
     * @return a scope instance
     */
    public static Scope create(float additionalZoom, double centerOffset, IGunModifier... modifiers)
    {
        return new Scope(additionalZoom, centerOffset, modifiers);
    }
}
