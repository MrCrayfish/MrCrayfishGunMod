package com.tac.guns.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tac.guns.GunMod;
import com.tac.guns.Reference;
import com.tac.guns.annotation.Validator;
import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class NetworkRigManager extends ReloadListener<Map<ArmorRigItem, Rig>>
{

    private static final Gson GSON_INSTANCE = Util.make(() -> {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ResourceLocation.class, JsonDeserializers.RESOURCE_LOCATION);
        return builder.create();
    });

    private static List<ArmorRigItem> clientRegisteredrigs = new ArrayList<>();
    private static NetworkRigManager instance;
    private Map<ResourceLocation, Rig> registeredRigs = new HashMap<>();

    public HashSet<UUID> Ids = new HashSet<>();
    @Override
    protected Map<ArmorRigItem, Rig> prepare(IResourceManager resourceManager, IProfiler profiler)
    {
        Map<ArmorRigItem, Rig> map = Maps.newHashMap();
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof ArmorRigItem).forEach(item ->
        {
            ResourceLocation id = item.getRegistryName();
            if(id != null)
            {
                ResourceLocation resourceLocation = new ResourceLocation(String.format("%s:rigs/%s.json", id.getNamespace(), id.getPath()));
                try(IResource resource = resourceManager.getResource(resourceLocation); InputStream is = resource.getInputStream();
                    Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)))
                {
                    Rig rig = JSONUtils.fromJson(GSON_INSTANCE, reader, Rig.class);
                    if(rig != null && Validator.isValidObject(rig))
                    {
                        map.put((ArmorRigItem) item, rig);
                    }
                    else
                    {
                        GunMod.LOGGER.error("Couldn't load data file {} as it is missing or malformed. Using default rig data", resourceLocation);
                        map.put((ArmorRigItem) item, new Rig());
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
    protected void apply(Map<ArmorRigItem, Rig> objects, IResourceManager resourceManager, IProfiler profiler)
    {
        ImmutableMap.Builder<ResourceLocation, Rig> builder = ImmutableMap.builder();
        objects.forEach((item, rig) -> {
            Validate.notNull(item.getRegistryName());
            builder.put(item.getRegistryName(), rig);
            item.setRig(new Supplier(rig));
        });
        this.registeredRigs = builder.build();
    }

    /**
     * Writes all registered rigs into the provided packet buffer
     *
     * @param buffer a packet buffer get
     */
    public void writeRegisteredRigs(PacketBuffer buffer)
    {
        buffer.writeVarInt(this.registeredRigs.size());
        this.registeredRigs.forEach((id, rig) -> {
            buffer.writeResourceLocation(id);
            buffer.writeCompoundTag(rig.serializeNBT());
        });
    }

    /**
     * Reads all registered rigs from the provided packet buffer
     *
     * @param buffer a packet buffer get
     * @return a map of registered rigs from the server
     */
    public static ImmutableMap<ResourceLocation, Rig> readRegisteredRigs(PacketBuffer buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, Rig> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                Rig rig = Rig.create(buffer.readCompoundTag());
                builder.put(id, rig);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    /**
     * Updates registered rigs from data provided by the server
     *
     * @param message an update rigs message
     * @return true if all registered rigs were able to update their corresponding gun item
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean updateRegisteredRigs(IRigProvider message)
    {
        clientRegisteredrigs.clear();
        Map<ResourceLocation, Rig> registeredrigs = message.getRegisteredRigs();
        if(registeredrigs != null)
        {
            for(Map.Entry<ResourceLocation, Rig> entry : registeredrigs.entrySet())
            {
                Item item = ForgeRegistries.ITEMS.getValue(entry.getKey());
                if(!(item instanceof ArmorRigItem))
                {
                    return false;
                }
                ((ArmorRigItem) item).setRig(new Supplier(entry.getValue()));
                clientRegisteredrigs.add((ArmorRigItem) item);
            }
            return true;
        }
        return false;
    }

    /**
     * Gets a map of all the registered rigs objects. Note, this is an immutable map.
     *
     * @return a map of registered gun objects
     */
    public Map<ResourceLocation, Rig> getRegisteredRigs()
    {
        return this.registeredRigs;
    }

    /**
     * Gets a list of all the rigs registered on the client side. Note, this is an immutable list.
     *
     * @return a map of rigs registered on the client
     */
    public static List<ArmorRigItem> getClientRegisteredRigs()
    {
        return ImmutableList.copyOf(clientRegisteredrigs);
    }

    @SubscribeEvent
    public static void onServerStopped(FMLServerStoppedEvent event)
    {
        NetworkRigManager.instance = null;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event)
    {
        NetworkRigManager networkRigManager = new NetworkRigManager();
        event.addListener(networkRigManager);
        NetworkRigManager.instance = networkRigManager;
    }

    /**
     * Gets the network gun manager. This will be null if the client isn't running an integrated
     * server or the client is connected to a dedicated server.
     *
     * @return the network gun manager
     */
    @Nullable
    public static NetworkRigManager get()
    {
        return instance;
    }

    public interface IRigProvider
    {
        ImmutableMap<ResourceLocation, Rig> getRegisteredRigs();

        ImmutableMap<ResourceLocation, CustomRig> getCustomRigs();
    }

    /**
     * A simple wrapper for a gun object to pass to GunItem. This is to indicate to developers that
     * Gun instances shouldn't be changed on GunItems as they are controlled by NetworkGunManager.
     * Changes to gun properties should be made through the JSON file.
     */
    public static class Supplier
    {
        private Rig rig;

        private Supplier(Rig rig)
        {
            this.rig = rig;
        }

        public Rig getRig()
        {
            return this.rig;
        }
    }
}
