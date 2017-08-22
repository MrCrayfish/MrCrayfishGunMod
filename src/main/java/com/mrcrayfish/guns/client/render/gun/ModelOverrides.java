package com.mrcrayfish.guns.client.render.gun;

import com.mrcrayfish.guns.client.render.gun.model.ModelStandard;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ModelOverrides
{
    private static final Map<ResourceLocation, IGunModel> MODEL_MAP = new HashMap<>();

    public static void register(@Nonnull ResourceLocation resource)
    {
        register(resource, new ModelStandard(resource));
    }

    public static void register(@Nonnull ResourceLocation resource, @Nonnull IGunModel model)
    {
        MODEL_MAP.put(resource, model);
    }

    @Nullable
    public static IGunModel getModel(ResourceLocation resource)
    {
        return MODEL_MAP.get(resource);
    }
}
