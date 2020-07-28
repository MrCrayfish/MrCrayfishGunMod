package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.annotation.Validator;
import com.mrcrayfish.guns.object.CustomGun;
import com.mrcrayfish.guns.object.GripType;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class CustomGunLoader extends JsonReloadListener
{
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        builder.registerTypeAdapter(ItemStack.class, JsonDeserializers.ITEM_STACK);
        builder.registerTypeAdapter(GripType.class, JsonDeserializers.GRIP_TYPE);
        return builder.create();
    });

    private Map<ResourceLocation, CustomGun> customGunMap = new HashMap<>();

    public CustomGunLoader()
    {
        super(GSON_INSTANCE, "custom_guns");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objects, IResourceManager manager, IProfiler profiler)
    {
        ImmutableMap.Builder<ResourceLocation, CustomGun> builder = ImmutableMap.builder();
        objects.forEach((resourceLocation, object) ->
        {
            try
            {
                CustomGun customGun = GSON_INSTANCE.fromJson(object, CustomGun.class);
                if(customGun != null && Validator.isValidObject(customGun))
                {
                    builder.put(resourceLocation, customGun);
                }
                else
                {
                    GunMod.LOGGER.error("Couldn't load data file {} as it is missing or malformed", resourceLocation);
                }
            }
            catch(InvalidObjectException e)
            {
                GunMod.LOGGER.error("Missing required properties for {}", resourceLocation);
                e.printStackTrace();
            }
            catch(IllegalAccessException e)
            {
                e.printStackTrace();
            }
        });
        this.customGunMap = builder.build();
    }

    /**
     * Writes all custom guns into the provided packet buffer
     *
     * @param buffer a packet buffer instance
     */
    public void writeCustomGuns(PacketBuffer buffer)
    {
        buffer.writeVarInt(this.customGunMap.size());
        this.customGunMap.forEach((id, gun) -> {
            buffer.writeResourceLocation(id);
            buffer.writeCompoundTag(gun.serializeNBT());
        });
    }

    /**
     * Reads all registered guns from the provided packet buffer
     *
     * @param buffer a packet buffer instance
     * @return a map of registered guns from the server
     */
    public static ImmutableMap<ResourceLocation, CustomGun> readCustomGuns(PacketBuffer buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, CustomGun> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                CustomGun customGun = new CustomGun();
                customGun.deserializeNBT(buffer.readCompoundTag());
                builder.put(id, customGun);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }
}
