package com.mrcrayfish.guns.client.render.crosshair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrcrayfish.guns.client.handler.CrosshairHandler;
import com.mrcrayfish.guns.interfaces.IResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

/**
 * Author: MrCrayfish
 */
public abstract class Crosshair implements IResourceLocation
{
    public static final Crosshair DEFAULT = new Crosshair(new ResourceLocation("default")) {};

    static
    {
        CrosshairHandler.get().register(DEFAULT);
    }

    private ResourceLocation id;

    /**
     * The default constructor for crosshairs
     *
     * @param id the id for the crosshair
     */
    protected Crosshair(ResourceLocation id)
    {
        this.id = id;
    }

    /**
     * Renders the crosshair to the screen. If implementing, positioning is not initially set to
     * the center of the screen. Use windowWidth and windowHeight for calculating the center. It
     * should be considered that the player may not be in a world.
     *
     * @param mc           a minecraft instance
     * @param stack        the current matrix stack
     * @param windowWidth  the scaled width of the window
     * @param windowHeight the scaled height of the window
     * @param partialTicks
     */
    public void render(Minecraft mc, PoseStack stack, int windowWidth, int windowHeight, float partialTicks) {}

    /**
     * Ticks the crosshair for any logic
     */
    public void tick() {}

    /**
     * Called when the held gun is fired
     */
    public void onGunFired() {}

    /**
     * Gets the id of the crosshair
     */
    @Override

    public final ResourceLocation getLocation()
    {
        return this.id;
    }

    /**
     * Test for default crosshair (aka normal minecraft crosshair)
     */
    public final boolean isDefault()
    {
        return this == DEFAULT;
    }
}
