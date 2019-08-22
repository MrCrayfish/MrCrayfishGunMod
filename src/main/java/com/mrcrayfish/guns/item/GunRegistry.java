package com.mrcrayfish.guns.item;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: MrCrayfish
 */
public class GunRegistry
{
    private static final Type GUN_TYPE = new TypeToken<Gun>(){}.getType();

    private static GunRegistry instance = null;

    public static GunRegistry getInstance()
    {
        if(instance == null)
        {
            instance = new GunRegistry();
        }
        return instance;
    }

    private final Map<ResourceLocation, ItemGun> GUNS = new HashMap<>();

    void register(ItemGun gun)
    {
        GUNS.put(Objects.requireNonNull(gun.getRegistryName()), gun);
        loadProperties(gun);
    }

    @Nullable
    public ItemGun getGun(ResourceLocation id)
    {
        return GUNS.get(id);
    }

    public Map<ResourceLocation, ItemGun> getGuns()
    {
        return ImmutableMap.copyOf(GUNS);
    }

    private static void loadProperties(ItemGun itemGun)
    {
        ResourceLocation id = Objects.requireNonNull(itemGun.getRegistryName());

        File configFolder = new File(Minecraft.getMinecraft().gameDir, "config/" + id.getNamespace() + "/guns/");
        configFolder.mkdirs();

        String assetsFile = String.format("/assets/%s/guns/%s.json", id.getNamespace(), id.getPath());
        Reader reader = new InputStreamReader(GunRegistry.class.getResourceAsStream(assetsFile));
        Gson gson = new Gson();
        Gun gun = gson.fromJson(reader, GUN_TYPE);

        try
        {
            validateFields(gun);
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch(InvalidObjectException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Missing property in gun '" + gun.id + "'. Refer to exception above");
        }

        File gunFile = new File(configFolder, id.getPath() + ".json");
        if(!gunFile.exists())
        {
            writeGunToFile(gun, gunFile);
        }

        try
        {
            reader = new InputStreamReader(new FileInputStream(gunFile));
            JsonElement parent = new JsonParser().parse(reader);
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(GUN_TYPE, new Gun.Deserializer(gun));
            gson = builder.create();
            gun = gson.fromJson(parent, GUN_TYPE);
            itemGun.setGun(gun);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static void writeGunToFile(Gun gun, File gunFile)
    {
        try
        {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            builder.registerTypeAdapter(GUN_TYPE, new Gun.Serializer());
            Gson gson = builder.create();
            OutputStream os = new FileOutputStream(gunFile);
            IOUtils.write(gson.toJson(gun), os, StandardCharsets.UTF_8);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private static <T> boolean validateFields(@Nonnull T t) throws IllegalAccessException, InvalidObjectException
    {
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields)
        {
            if(field.getDeclaredAnnotation(Gun.Ignored.class) != null || field.getDeclaredAnnotation(Gun.Optional.class) != null)
                continue;

            if(field.get(t) == null)
            {
                throw new InvalidObjectException("Missing required property: " + field.getName());
            }

            if(!field.getType().isPrimitive() && field.getType() != String.class && !field.getType().isEnum())
            {
                return validateFields(field.get(t));
            }
        }
        return true;
    }
}
