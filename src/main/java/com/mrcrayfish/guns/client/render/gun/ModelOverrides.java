package com.mrcrayfish.guns.client.render.gun;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ModelOverrides
{
    private static final Map<Item, IOverrideModel> MODEL_MAP = new HashMap<>();

    public static void register(ItemStack stack, IOverrideModel model)
    {
        MODEL_MAP.putIfAbsent(stack.getItem(), model);
    }

    public static boolean hasModel(ItemStack stack)
    {
        return MODEL_MAP.containsKey(stack.getItem());
    }

    @Nullable
    public static IOverrideModel getModel(ItemStack stack)
    {
        return MODEL_MAP.get(stack.getItem());
    }

    public static Map<Item, IOverrideModel> getModelMap()
    {
        return ImmutableMap.copyOf(MODEL_MAP);
    }
}
