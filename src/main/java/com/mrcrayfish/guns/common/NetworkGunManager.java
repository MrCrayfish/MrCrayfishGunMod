package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.annotation.Ignored;
import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.annotation.Validator;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.HandshakeMessages;
import com.mrcrayfish.guns.network.message.MessageUpdateGuns;
import com.mrcrayfish.guns.object.CustomGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class NetworkGunManager extends ReloadListener<Map<GunItem, Gun>>
{
    private static final Type RESOURCE_LOCATION_TYPE = new TypeToken<ResourceLocation>(){}.getType();
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(RESOURCE_LOCATION_TYPE, new ResourceLocationDeserializer());
        return builder.create();
    });

    /**
     * A fallback gun object for any weapons that haven't added a json yet.
     */
    static final Gun FALLBACK_GUN = Util.make(() -> {
        Gun gun = new Gun();
        gun.projectile.item = new ResourceLocation("cgm:basic_ammo");
        return gun;
    });

    private Map<ResourceLocation, Gun> registeredGuns = new HashMap<>();

    @Override
    protected Map<GunItem, Gun> prepare(IResourceManager resourceManager, IProfiler profiler)
    {
        Map<GunItem, Gun> map = Maps.newHashMap();
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof GunItem).forEach(item ->
        {
            ResourceLocation id = item.getRegistryName();
            if(id != null)
            {
                ResourceLocation resourceLocation = new ResourceLocation(String.format("%s:guns/%s.json", id.getNamespace(), id.getPath()));
                try(IResource resource = resourceManager.getResource(resourceLocation); InputStream is = resource.getInputStream(); Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
                {
                    Gun gun = JSONUtils.fromJson(GSON_INSTANCE, reader, Gun.class);
                    if(gun != null && Validator.isValidObject(gun))
                    {
                        map.put((GunItem) item, gun);
                    }
                    else
                    {
                        GunMod.LOGGER.error("Couldn't load data file {} as it is missing or malformed. Using fallback gun data", resourceLocation);
                        map.put((GunItem) item, FALLBACK_GUN);
                    }
                }
                catch(InvalidObjectException e)
                {
                    GunMod.LOGGER.error("Missing required properties for {}", resourceLocation);
                    e.printStackTrace();
                }
                catch(IOException e)
                {
                    GunMod.LOGGER.error("Couldn't parse data file {}", resourceLocation);
                }
                catch(IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return map;
    }

    @Override
    protected void apply(Map<GunItem, Gun> objects, IResourceManager resourceManager, IProfiler profiler)
    {
        ImmutableMap.Builder<ResourceLocation, Gun> builder = ImmutableMap.builder();
        objects.forEach((item, gun) -> {
            Validate.notNull(item.getRegistryName());
            builder.put(item.getRegistryName(), gun);
            item.setGun(gun);
        });
        this.registeredGuns = builder.build();
    }

    /**
     * Writes all registered guns into the provided packet buffer
     *
     * @param buffer a packet buffer instance
     */
    public void writeRegisteredGuns(PacketBuffer buffer)
    {
        buffer.writeVarInt(this.registeredGuns.size());
        this.registeredGuns.forEach((id, gun) -> {
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
    public static ImmutableMap<ResourceLocation, Gun> readRegisteredGuns(PacketBuffer buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, Gun> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                Gun gun = Gun.create(buffer.readCompoundTag());
                builder.put(id, gun);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    /**
     * Updates registered guns from data provided by the server
     *
     * @param message an update guns message
     * @return true if all registered guns were able to update their corresponding gun item
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean updateRegisteredGuns(IGunProvider message)
    {
        Map<ResourceLocation, Gun> registeredGuns = message.getRegisteredGuns();
        if(registeredGuns != null)
        {
            for(Map.Entry<ResourceLocation, Gun> entry : registeredGuns.entrySet())
            {
                Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
                if(!(item instanceof GunItem))
                {
                    return false;
                }
                ((GunItem) item).setGun(entry.getValue());
            }
            return true;
        }
        return false;
    }

    /**
     * Gets a map of all the registered guns objects. Note, this is an immutable map.
     *
     * @return a map of registered gun objects
     */
    public Map<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    /**
     * A simple deserializer for resource locations. A more simplified version than the serializer
     * provided in {@code net.minecraft.util.ResourceLocation}
     */
    private static class ResourceLocationDeserializer implements JsonDeserializer<ResourceLocation>
    {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            return new ResourceLocation(json.getAsString());
        }
    }

    public interface IGunProvider
    {
        ImmutableMap<ResourceLocation, Gun> getRegisteredGuns();

        ImmutableMap<ResourceLocation, CustomGun> getCustomGuns();
    }
}
