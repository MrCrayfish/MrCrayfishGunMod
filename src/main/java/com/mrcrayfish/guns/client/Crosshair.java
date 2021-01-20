package com.mrcrayfish.guns.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcrayfish.guns.client.event.CrosshairHandler;
import com.mrcrayfish.guns.interfaces.IResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

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

    protected Crosshair(ResourceLocation id)
    {
        this.id = id;
    }

    public void render(Minecraft mc, MatrixStack stack, int windowWidth, int windowHeight) {}

    @Override
    public final ResourceLocation getLocation()
    {
        return this.id;
    }

    public final boolean isDefault()
    {
        return this == DEFAULT;
    }
}
