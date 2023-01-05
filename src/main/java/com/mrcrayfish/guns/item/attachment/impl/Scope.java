package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.common.properties.SightAnimation;
import com.mrcrayfish.guns.debug.IDebugWidget;
import com.mrcrayfish.guns.debug.IEditorMenu;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugButton;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugSlider;
import com.mrcrayfish.guns.interfaces.IGunModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Supplier;

/**
 * An attachment class related to scopes. Scopes need to at least specify the additional zoom (or fov)
 * they provide and the y-offset to the center of the scope for them to render correctly. Use
 * {@link #create(float, double, IGunModifier...)} to create an get.
 * <p>
 * Author: MrCrayfish
 */
public class Scope extends Attachment implements IEditorMenu
{
    protected float aimFovModifier;
    protected float additionalZoom;
    protected double reticleOffset;
    protected boolean stable;
    protected double viewFinderDist;
    protected double viewportFov;
    protected SightAnimation sightAnimation;

    private Scope() {}

    private Scope(float additionalZoom, double reticleOffset, double viewportFov, IGunModifier... modifier)
    {
        super(modifier);
        this.aimFovModifier = 1.0F;
        this.additionalZoom = additionalZoom;
        this.reticleOffset = reticleOffset;
        this.viewportFov = viewportFov;
        this.sightAnimation = SightAnimation.DEFAULT;
    }

    private Scope(float aimFovModifier, float additionalZoom, double reticleOffset, boolean stable, double viewFinderDist, double viewportFov, SightAnimation sightAnimation, IGunModifier... modifiers)
    {
        super(modifiers);
        this.aimFovModifier = aimFovModifier;
        this.additionalZoom = additionalZoom;
        this.reticleOffset = reticleOffset;
        this.stable = stable;
        this.viewFinderDist = viewFinderDist;
        this.viewportFov = viewportFov;
        this.sightAnimation = sightAnimation;
    }

    /**
     * Marks this scope to allow it to be stabilised while using a controller. This is essentially
     * holding your breath while looking down the sight.
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public void stabilise()
    {
        this.stable = true;
    }

    /**
     * Deprecated: Use meta files instead
     * <p>
     * Sets the offset distance from the camera to the view finder
     *
     * @param offset the view finder offset
     * @return this scope get
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public Scope viewFinderOffset(double offset)
    {
        this.viewFinderDist = offset;
        return this;
    }

    public float getFovModifier()
    {
        return this.aimFovModifier;
    }

    /**
     * Deprecated: Use {@link #getFovModifier()}
     * <p>
     * Gets the amount of additional zoom (or reduced fov) this scope provides
     *
     * @return the scopes additional zoom
     */
    @Deprecated(since = "1.2.9", forRemoval = true)
    public float getAdditionalZoom()
    {
        return this.additionalZoom;
    }

    /**
     * Deprecated: Use meta files instead
     * <p>
     * Gets the offset to the center of the scope. Used to render scope cross hair exactly in the
     * middle of the screen.
     *
     * @return the scope center offset
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public double getCenterOffset()
    {
        return this.reticleOffset;
    }

    /**
     * Deprecated: Use meta files instead
     * <p>
     * Gets the offset need to translate the gun model so the reticle of the scope aligns with the
     * center of the screen.
     *
     * @return the reticle offset
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public double getReticleOffset()
    {
        return this.reticleOffset;
    }

    /**
     * @return If this scope can be stabilised
     */
    public boolean isStable()
    {
        return this.stable;
    }

    /**
     * Deprecated: Use meta files instead
     * @return The view finder offset of this scope
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public double getViewFinderOffset()
    {
        return this.viewFinderDist;
    }

    /**
     * @return The distance to offset camera from the center of the scope model.
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public double getViewFinderDistance()
    {
        return this.viewFinderDist;
    }

    /**
     * @return The FOV of the first person viewport when aiming
     */
    public double getViewportFov()
    {
        return this.viewportFov;
    }

    public SightAnimation getSightAnimation()
    {
        return this.sightAnimation;
    }

    @Override
    public Component getEditorLabel()
    {
        return Component.literal("Scope");
    }

    @Override
    public void getEditorWidgets(List<Pair<Component, Supplier<IDebugWidget>>> widgets)
    {
        widgets.add(Pair.of(Component.literal("Aim FOV Modifier"), () -> new DebugSlider(0.0, 1.0, this.aimFovModifier, 0.05, 3, value -> this.aimFovModifier = value.floatValue())));
        widgets.add(Pair.of(Component.literal("Zoom (Legacy)"), () -> new DebugSlider(0.0, 0.5, this.additionalZoom, 0.05, 3, value -> this.additionalZoom = value.floatValue())));
        widgets.add(Pair.of(Component.literal("Reticle Offset"), () -> new DebugSlider(0.0, 4.0, this.reticleOffset, 0.025, 4, value -> this.reticleOffset = value)));
        widgets.add(Pair.of(Component.literal("View Finder Distance"), () -> new DebugSlider(0.0, 5.0, this.viewFinderDist, 0.05, 3, value -> this.viewFinderDist = value)));
        widgets.add(Pair.of(Component.literal("Viewport FOV"), () -> new DebugSlider(1.0, 100.0, this.viewportFov, 1.0, 4, value -> this.viewportFov = value)));
        widgets.add(Pair.of(Component.literal("Sight Animations"), () -> new DebugButton(Component.literal("Edit"), btn -> {
            Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, this.sightAnimation));
        })));
    }

    public Scope copy()
    {
        Scope scope = new Scope();
        scope.aimFovModifier = this.aimFovModifier;
        scope.additionalZoom = this.additionalZoom;
        scope.reticleOffset = this.reticleOffset;
        scope.stable = this.stable;
        scope.viewFinderDist = this.viewFinderDist;
        scope.viewportFov = this.viewportFov;
        scope.sightAnimation = this.sightAnimation.copy();
        return scope;
    }

    /**
     * Deprecated: Use the builder instead.
     * <p>
     * Creates a scope. This method is now deprecated.
     *
     * @param additionalZoom the additional zoom this scope provides
     * @param centerOffset   the length to the center of the view finder from the base of the scope model in pixels
     * @param modifiers      an array of gun modifiers
     * @return a scope get
     */
    @Deprecated(since = "1.3.0", forRemoval = true)
    public static Scope create(float additionalZoom, double centerOffset, IGunModifier... modifiers)
    {
        // -1 to indicate that it should use the default fov
        return new Scope(additionalZoom, centerOffset, -1.0, modifiers);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private float aimFovModifier = 1.0F;
        private float additionalZoom = 0.0F;
        private double reticleOffset = 0.0;
        private boolean stable = false;
        private double viewFinderDist = 0.0;
        private double viewportFov = 10.0;
        private SightAnimation sightAnimation = SightAnimation.DEFAULT;
        private IGunModifier[] modifiers = new IGunModifier[]{};

        private Builder() {}

        public Builder aimFovModifier(float fovModifier)
        {
            this.aimFovModifier = fovModifier;
            return this;
        }

        /**
         * Deprecated: Use {@link #aimFovModifier(float)} ()}
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder additionalZoom(float additionalZoom)
        {
            this.additionalZoom = additionalZoom;
            return this;
        }

        /**
         * Deprecated: Use meta files instead
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder centerOffset(double centerOffset)
        {
            this.reticleOffset = centerOffset;
            return this;
        }

        /**
         * Deprecated: Use meta files instead
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder reticleOffset(double reticleOffset)
        {
            this.reticleOffset = reticleOffset;
            return this;
        }

        public Builder stable(boolean stable)
        {
            this.stable = stable;
            return this;
        }

        /**
         * Deprecated: Use meta files instead
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder viewFinderOffset(double viewFinderOffset)
        {
            this.viewFinderDist = viewFinderOffset;
            return this;
        }

        /**
         * Deprecated: Use meta files instead
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder viewFinderDistance(double viewFinderDist)
        {
            this.viewFinderDist = viewFinderDist;
            return this;
        }

        public Builder viewportFov(double viewportFov)
        {
            this.viewportFov = viewportFov;
            return this;
        }

        public Builder modifiers(IGunModifier... modifiers)
        {
            this.modifiers = modifiers;
            return this;
        }

        /**
         * Deprecated: Use meta files instead
         */
        @Deprecated(since = "1.3.0", forRemoval = true)
        public Builder sightAnimation(SightAnimation.Builder builder)
        {
            this.sightAnimation = builder.build();
            return this;
        }

        public Scope build()
        {
            return new Scope(this.aimFovModifier, this.additionalZoom, this.reticleOffset, this.stable, this.viewFinderDist, this.viewportFov, this.sightAnimation, this.modifiers);
        }
    }
}
