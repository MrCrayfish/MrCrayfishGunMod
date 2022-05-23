package com.tac.guns.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.annotation.Validator;
import com.tac.guns.item.GunItem;
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
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class NetworkGunManager extends ReloadListener<Map<GunItem, Gun>>
{
    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        builder.registerTypeAdapter(GripType.class, JsonDeserializers.GRIP_TYPE);
        return builder.create();
    });

    private static List<GunItem> clientRegisteredGuns = new ArrayList<>();
    private static NetworkGunManager instance;

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
                        GunMod.LOGGER.error("Couldn't load data file {} as it is missing or malformed. Using default gun data", resourceLocation);
                        map.put((GunItem) item, new Gun());
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
            item.setGun(new Supplier(gun));
        });
        this.registeredGuns = builder.build();
    }

    /**
     * Writes all registered guns into the provided packet buffer
     *
     * @param buffer a packet buffer get
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
     * @param buffer a packet buffer get
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
        clientRegisteredGuns.clear();
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
                ((GunItem) item).setGun(new Supplier(entry.getValue()));
                clientRegisteredGuns.add((GunItem) item);
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
     * Gets a list of all the guns registered on the client side. Note, this is an immutable list.
     *
     * @return a map of guns registered on the client
     */
    public static List<GunItem> getClientRegisteredGuns()
    {
        return ImmutableList.copyOf(clientRegisteredGuns);
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event)
    {
        NetworkGunManager.instance = null;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event)
    {
        NetworkGunManager networkGunManager = new NetworkGunManager();
        event.addListener(networkGunManager);
        NetworkGunManager.instance = networkGunManager;
    }

    /**
     * Gets the network gun manager. This will be null if the client isn't running an integrated
     * server or the client is connected to a dedicated server.
     *
     * @return the network gun manager
     */
    @Nullable
    public static NetworkGunManager get()
    {
        return instance;
    }

    public interface IGunProvider
    {
        ImmutableMap<ResourceLocation, Gun> getRegisteredGuns();

        ImmutableMap<ResourceLocation, CustomGun> getCustomGuns();
    }

    /**
     * A simple wrapper for a gun object to pass to GunItem. This is to indicate to developers that
     * Gun instances shouldn't be changed on GunItems as they are controlled by NetworkGunManager.
     * Changes to gun properties should be made through the JSON file.
     */
    public static class Supplier
    {
        private Gun gun;

        private Supplier(Gun gun)
        {
            this.gun = gun;
        }

        public Gun getGun()
        {
            return this.gun;
        }
    }
}
