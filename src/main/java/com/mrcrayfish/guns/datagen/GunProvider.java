package com.mrcrayfish.guns.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.common.Gun;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Author: MrCrayfish
 */
public abstract class GunProvider implements DataProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    private final DataGenerator generator;
    private final Map<ResourceLocation, Gun> gunMap = new HashMap<>();

    protected GunProvider(DataGenerator generator)
    {
        this.generator = generator;
    }

    protected abstract void registerGuns();

    protected final void addGun(ResourceLocation id, Gun gun)
    {
        this.gunMap.put(id, gun);
    }

    @Override
    public void run(HashCache cache)
    {
        this.gunMap.clear();
        this.registerGuns();
        this.gunMap.forEach((id, gun) ->
        {
            Path path = this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/guns/" + id.getPath() + ".json");
            try
            {
                JsonObject object = gun.toJsonObject();
                String rawJson = GSON.toJson(object);
                String hash = SHA1.hashUnencodedChars(rawJson).toString();
                if(!Objects.equals(cache.getHash(path), hash) || !Files.exists(path))
                {
                    Files.createDirectories(path.getParent());
                    try(BufferedWriter writer = Files.newBufferedWriter(path))
                    {
                        writer.write(rawJson);
                    }
                }
                cache.putNew(path, hash);
            }
            catch(IOException e)
            {
                LOGGER.error("Couldn't save trades to {}", path, e);
            }
        });
    }

    @Override
    public String getName()
    {
        return "Guns: " + Reference.MOD_ID;
    }
}
