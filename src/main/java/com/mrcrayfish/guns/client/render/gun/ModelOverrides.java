package com.mrcrayfish.guns.client.render.gun;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.client.render.gun.model.ModelStandard;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ModelOverrides
{
    private static final Map<Item, IOverrideModel> MODEL_MAP = new HashMap<>();

    public static void register(Item item)
    {
        register(item, new ModelStandard(item));
    }

    public static void register(Item item, IOverrideModel model)
    {
        MODEL_MAP.put(item, model);
    }

    public static boolean hasModel(Item item)
    {
        return MODEL_MAP.containsKey(item);
    }

    @Nullable
    public static IOverrideModel getModel(Item item)
    {
        return MODEL_MAP.get(item);
    }

    public static Map<Item, IOverrideModel> getModelMap()
    {
        return ImmutableMap.copyOf(MODEL_MAP);
    }
}
