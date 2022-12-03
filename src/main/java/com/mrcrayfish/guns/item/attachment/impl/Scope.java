package com.mrcrayfish.guns.item.attachment.impl;

import com.mrcrayfish.guns.common.properties.SightAnimation;
import com.mrcrayfish.guns.debug.Debug;
import com.mrcrayfish.guns.debug.IDebugWidget;
import com.mrcrayfish.guns.debug.IEditorMenu;
import com.mrcrayfish.guns.debug.client.screen.EditorScreen;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugButton;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugSlider;
import com.mrcrayfish.guns.debug.client.screen.widget.DebugToggle;
import com.mrcrayfish.guns.interfaces.IGunModifier;
import net.minecraft.ChatFormatting;
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
    protected float additionalZoom;
    protected double centerOffset;
    protected boolean stable;
    protected double viewFinderOffset;
    protected double viewportFov;
    protected SightAnimation sightAnimation;

    private Scope() {}

    private Scope(float additionalZoom, double centerOffset, double viewportFov, IGunModifier... modifier)
    {
        super(modifier);
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
        this.viewportFov = viewportFov;
        this.sightAnimation = SightAnimation.DEFAULT;
    }

    private Scope(float additionalZoom, double centerOffset, boolean stable, double viewFinderOffset, double viewportFov, SightAnimation sightAnimation, IGunModifier... modifiers)
    {
        super(modifiers);
        this.additionalZoom = additionalZoom;
        this.centerOffset = centerOffset;
        this.stable = stable;
        this.viewFinderOffset = viewFinderOffset;
        this.viewportFov = viewportFov;
        this.sightAnimation = sightAnimation;
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
     * @return this scope get
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
    public double getCenterOffset()
    {
        return this.centerOffset;
    }

    /**
     * @return If this scope can be stabilised
     */
    public boolean isStable()
    {
        return this.stable;
    }

    /**
     * @return The view finder offset of this scope
     */
    public double getViewFinderOffset()
    {
        return this.viewFinderOffset;
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
        widgets.add(Pair.of(Component.literal("Additional Zoom"), () -> new DebugSlider(0.0, 0.5, this.additionalZoom, 0.05, 3, value -> this.additionalZoom = value.floatValue())));
        widgets.add(Pair.of(Component.literal("Center Offset"), () -> new DebugSlider(0.0, 4.0, this.centerOffset, 0.025, 4, value -> this.centerOffset = value)));
        widgets.add(Pair.of(Component.literal("View Finder Offset"), () -> new DebugSlider(0.0, 5.0, this.viewFinderOffset, 0.05, 3, value -> this.viewFinderOffset = value)));
        widgets.add(Pair.of(Component.literal("Aim FOV"), () -> new DebugSlider(1.0, 100.0, this.viewportFov, 1.0, 4, value -> this.viewportFov = value)));
        widgets.add(Pair.of(Component.literal("Sight Animations"), () -> new DebugButton(Component.literal("Edit"), btn -> {
            Minecraft.getInstance().setScreen(new EditorScreen(Minecraft.getInstance().screen, this.sightAnimation));
        })));
    }

    public Scope copy()
    {
        Scope scope = new Scope();
        scope.additionalZoom = this.additionalZoom;
        scope.centerOffset = this.centerOffset;
        scope.stable = this.stable;
        scope.viewFinderOffset = this.viewFinderOffset;
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
        private float additionalZoom = 0.0F;
        private double centerOffset = 0.0;
        private boolean stable = false;
        private double viewFinderOffset = 0.0;
        private double viewportFov = 10.0;
        private SightAnimation sightAnimation = SightAnimation.DEFAULT;
        private IGunModifier[] modifiers = new IGunModifier[]{};

        private Builder() {}

        public Builder additionalZoom(float additionalZoom)
        {
            this.additionalZoom = additionalZoom;
            return this;
        }

        public Builder centerOffset(double centerOffset)
        {
            this.centerOffset = centerOffset;
            return this;
        }

        public Builder stable(boolean stable)
        {
            this.stable = stable;
            return this;
        }

        public Builder viewFinderOffset(double viewFinderOffset)
        {
            this.viewFinderOffset = viewFinderOffset;
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

        public Builder sightAnimation(SightAnimation.Builder builder)
        {
            this.sightAnimation = builder.build();
            return this;
        }

        public Scope build()
        {
            return new Scope(this.additionalZoom, this.centerOffset, this.stable, this.viewFinderOffset, this.viewportFov, this.sightAnimation, this.modifiers);
        }
    }
}
