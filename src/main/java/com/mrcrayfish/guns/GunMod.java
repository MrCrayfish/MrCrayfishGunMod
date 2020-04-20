package com.mrcrayfish.guns;

import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityMissile;
import com.mrcrayfish.guns.init.*;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.network.PacketHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(Reference.MOD_ID)
public class GunMod
{
    public static boolean controllableLoaded = false;
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.PISTOL.get());
        }
    };

    private static NetworkGunManager manager;

    public GunMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.REGISTER.register(bus);
        ModContainers.REGISTER.register(bus);
        ModEntities.REGISTER.register(bus);
        ModItems.REGISTER.register(bus);
        ModPotions.REGISTER.register(bus);
        ModSounds.REGISTER.register(bus);
        ModTileEntities.REGISTER.register(bus);
        ModSyncedDataKeys.register();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        controllableLoaded = ModList.get().isLoaded("controllable");
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        //ModCrafting.register(); //TODO convert to datapack
        //ModEntities.register();
        AmmoRegistry.getInstance().registerProjectileFactory(ModItems.GRENADE.get(), (worldIn, entity, item, modifiedGun) -> new EntityGrenade(ModEntities.GRENADE.get(), worldIn, entity, item, modifiedGun));
        AmmoRegistry.getInstance().registerProjectileFactory(ModItems.MISSILE.get(), (worldIn, entity, item, modifiedGun) -> new EntityMissile(ModEntities.MISSILE.get(), worldIn, entity, item, modifiedGun));
        PacketHandler.init();
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        ClientHandler.setup();
    }

    @SubscribeEvent
    public void onServerStart(FMLServerAboutToStartEvent event)
    {
        NetworkGunManager manager = new NetworkGunManager();
        event.getServer().getResourceManager().addReloadListener(manager);
        GunMod.manager = manager;
    }

    @SubscribeEvent
    public void onServerStart(FMLServerStoppedEvent event)
    {
        GunMod.manager = null;
    }

    /**
     * Gets the network gun manager. This will be null if the client isn't running an integrated
     * server or the client is connected to a dedicated server.
     *
     * @return the network gun manager
     */
    @Nullable
    public static NetworkGunManager getNetworkGunManager()
    {
        return manager;
    }
}
