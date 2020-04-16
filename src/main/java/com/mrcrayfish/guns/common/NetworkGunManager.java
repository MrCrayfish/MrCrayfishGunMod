package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class NetworkGunManager extends JsonReloadListener
{
    private static final Gson GSON_INSTANCE = new GsonBuilder().create();
    private Map<ResourceLocation, Gun> registeredGuns = ImmutableMap.of();

    public NetworkGunManager()
    {
        super(GSON_INSTANCE, "guns");
    }

    public void write(PacketBuffer buffer)
    {

    }

    public void read(PacketBuffer buffer)
    {

    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objects, IResourceManager resourceManager, IProfiler profiler)
    {

    }
}
