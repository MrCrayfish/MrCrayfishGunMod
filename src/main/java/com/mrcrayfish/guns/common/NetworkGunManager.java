package com.mrcrayfish.guns.common;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mrcrayfish.framework.api.data.login.ILoginData;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.annotation.Validator;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageUpdateGuns;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class NetworkGunManager extends SimplePreparableReloadListener<Map<GunItem, Gun>>
{
    private static final int FILE_TYPE_LENGTH_VALUE = ".json".length();
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
    protected Map<GunItem, Gun> prepare(ResourceManager manager, ProfilerFiller profiler)
    {
        Map<GunItem, Gun> map = new HashMap<>();
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof GunItem).forEach(item ->
        {
            ResourceLocation id = item.getRegistryName();
            if(id != null)
            {
                List<ResourceLocation> resources = new ArrayList<>(manager.listResources("guns", (fileName) -> fileName.endsWith(id.getPath() + ".json")));
                resources.sort((r1, r2) -> {
                    if(r1.getNamespace().equals(r2.getNamespace())) return 0;
                    return r2.getNamespace().equals(Reference.MOD_ID) ? 1 : -1;
                });
                resources.forEach(resource ->
                {
                    String path = resource.getPath().substring(0, resource.getPath().length() - FILE_TYPE_LENGTH_VALUE);
                    String[] splitPath = path.split("/");

                    // Makes sure the file name matches exactly with the id of the gun
                    if(!id.getPath().equals(splitPath[splitPath.length - 1]))
                        return;

                    try(InputStream inputstream = manager.getResource(resource).getInputStream(); Reader reader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8)))
                    {
                        Gun gun = GsonHelper.fromJson(GSON_INSTANCE, reader, Gun.class);
                        if(gun != null && Validator.isValidObject(gun))
                        {
                            map.put((GunItem) item, gun);
                        }
                        else
                        {
                            GunMod.LOGGER.error("Couldn't load data file {} as it is missing or malformed. Using default gun data", resource);
                            map.putIfAbsent((GunItem) item, new Gun());
                        }
                    }
                    catch(InvalidObjectException e)
                    {
                        GunMod.LOGGER.error("Missing required properties for {}", resource);
                        e.printStackTrace();
                    }
                    catch(IOException e)
                    {
                        GunMod.LOGGER.error("Couldn't parse data file {}", resource);
                    }
                    catch(IllegalAccessException e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        });
        return map;
    }

    @Override
    protected void apply(Map<GunItem, Gun> objects, ResourceManager resourceManager, ProfilerFiller profiler)
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
    public void writeRegisteredGuns(FriendlyByteBuf buffer)
    {
        buffer.writeVarInt(this.registeredGuns.size());
        this.registeredGuns.forEach((id, gun) -> {
            buffer.writeResourceLocation(id);
            buffer.writeNbt(gun.serializeNBT());
        });
}

    /**
     * Reads all registered guns from the provided packet buffer
     *
     * @param buffer a packet buffer get
     * @return a map of registered guns from the server
     */
    public static ImmutableMap<ResourceLocation, Gun> readRegisteredGuns(FriendlyByteBuf buffer)
    {
        int size = buffer.readVarInt();
        if(size > 0)
        {
            ImmutableMap.Builder<ResourceLocation, Gun> builder = ImmutableMap.builder();
            for(int i = 0; i < size; i++)
            {
                ResourceLocation id = buffer.readResourceLocation();
                Gun gun = Gun.create(buffer.readNbt());
                builder.put(id, gun);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    public static boolean updateRegisteredGuns(MessageUpdateGuns message)
    {
        return updateRegisteredGuns(message.getRegisteredGuns());
    }

    /**
     * Updates registered guns from data provided by the server
     *
     * @return true if all registered guns were able to update their corresponding gun item
     */
    private static boolean updateRegisteredGuns(Map<ResourceLocation, Gun> registeredGuns)
    {
        clientRegisteredGuns.clear();
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
    public static void onServerStopped(ServerStoppedEvent event)
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

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event)
    {
        if(event.getPlayer() == null)
        {
            PacketHandler.getPlayChannel().send(PacketDistributor.ALL.noArg(), new MessageUpdateGuns());
        }
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

    public static class LoginData implements ILoginData
    {
        @Override
        public void writeData(FriendlyByteBuf buffer)
        {
            Validate.notNull(NetworkGunManager.get());
            NetworkGunManager.get().writeRegisteredGuns(buffer);
        }

        @Override
        public Optional<String> readData(FriendlyByteBuf buffer)
        {
            Map<ResourceLocation, Gun> registeredGuns = NetworkGunManager.readRegisteredGuns(buffer);
            NetworkGunManager.updateRegisteredGuns(registeredGuns);
            return Optional.empty();
        }
    }
}
