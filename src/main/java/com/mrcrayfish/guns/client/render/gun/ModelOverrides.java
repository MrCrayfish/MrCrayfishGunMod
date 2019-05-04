package com.mrcrayfish.guns.client.render.gun;

import com.google.common.collect.ImmutableMap;
import com.mrcrayfish.guns.client.render.gun.model.ModelStandard;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Author: MrCrayfish
 */
public class ModelOverrides
{
    private static final Map<Item, Map<Integer, IOverrideModel>> MODEL_MAP = new HashMap<>();

    public static void register(ItemStack stack, IOverrideModel model)
    {
        if(!MODEL_MAP.containsKey(stack.getItem()))
        {
            MODEL_MAP.put(stack.getItem(), new HashMap<>());
        }
        MODEL_MAP.get(stack.getItem()).put(stack.getMetadata(), model);
    }

    public static boolean hasModel(ItemStack stack)
    {
        return MODEL_MAP.containsKey(stack.getItem()) && MODEL_MAP.get(stack.getItem()).containsKey(stack.getMetadata());
    }

    @Nullable
    public static IOverrideModel getModel(ItemStack stack)
    {
        Map<Integer, IOverrideModel> map = MODEL_MAP.get(stack.getItem());
        if(map != null)
        {
            return map.get(stack.getMetadata());
        }
        return null;
    }

    public static Map<Item, Map<Integer, IOverrideModel>> getModelMap()
    {
        return ImmutableMap.copyOf(MODEL_MAP);
    }
}
