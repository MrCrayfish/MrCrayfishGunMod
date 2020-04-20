package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.annotation.Ignored;
import com.mrcrayfish.guns.annotation.Optional;
import com.mrcrayfish.guns.item.GunItem;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Author: MrCrayfish
 */
public class NetworkGunManager extends ReloadListener<Map<GunItem, Gun>>
{
    private static final Type RESOURCE_LOCATION_TYPE = new TypeToken<ResourceLocation>() {}.getType();
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(RESOURCE_LOCATION_TYPE, new Gun.ResourceLocationDeserializer());
        return builder.create();
    });
    private Map<ResourceLocation, Gun> registeredGuns = new HashMap<>();

    public NetworkGunManager()
    {
        //super(GSON_INSTANCE, "guns");
    }

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
                    if(gun != null && this.isValidObject(gun))
                    {
                        map.put((GunItem) item, gun);
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


        /*for(ResourceLocation location : resourceManager.getAllResourceLocations("guns", (p_223379_0_) -> p_223379_0_.endsWith(".json")))
        {
            String s = location.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(location.getNamespace(), s.substring(i, s.length() - JSON_EXTENSION_LENGTH));

            try(IResource iresource = resourceManagerIn.getResource(location); InputStream inputstream = iresource.getInputStream(); Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));)
            {
                JsonObject jsonobject = JSONUtils.fromJson(this.gson, reader, JsonObject.class);
                if(jsonobject != null)
                {
                    JsonObject jsonobject1 = map.put(resourcelocation1, jsonobject);
                    if(jsonobject1 != null)
                    {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + resourcelocation1);
                    }
                }
                else
                {
                    LOGGER.error("Couldn't load data file {} from {} as it's null or empty", resourcelocation1, location);
                }
            }
            catch(IllegalArgumentException | IOException | JsonParseException jsonparseexception)
            {
                LOGGER.error("Couldn't parse data file {} from {}", resourcelocation1, location, jsonparseexception);
            }
        }*/

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

    public void writeRegisteredGuns(PacketBuffer buffer)
    {
        buffer.writeVarInt(this.registeredGuns.size());
        this.registeredGuns.forEach((id, gun) ->
        {
            buffer.writeResourceLocation(id);
            buffer.writeCompoundTag(gun.serializeNBT());
        });
    }

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

    public boolean updateRegisteredGuns(ImmutableMap<ResourceLocation, Gun> map)
    {
        this.registeredGuns = map;
        for(Map.Entry<ResourceLocation, Gun> entry : map.entrySet())
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

    public Map<ResourceLocation, Gun> getRegisteredGuns()
    {
        return this.registeredGuns;
    }

    /**
     * Validates that the deserialized object's required fields are not null. This is an abstracted
     * method and can be used for validating any deserialized object.
     * @param t the object to validate
     * @param <T> any type
     * @return
     * @throws IllegalAccessException
     * @throws InvalidObjectException
     */
    private <T> boolean isValidObject(@Nonnull T t) throws IllegalAccessException, InvalidObjectException
    {
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields)
        {
            if(field.getDeclaredAnnotation(Ignored.class) != null || field.getDeclaredAnnotation(Optional.class) != null)
            {
                continue;
            }

            if(field.get(t) == null)
            {
                throw new InvalidObjectException("Missing required property: " + field.getName());
            }

            if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
            {
                return isValidObject(field.get(t));
            }
        }
        return true;
    }
}
