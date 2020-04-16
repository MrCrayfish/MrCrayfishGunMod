package com.mrcrayfish.guns;

import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.common.NetworkGunManager;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityMissile;
import com.mrcrayfish.guns.common.CommonEvents;
import com.mrcrayfish.guns.init.ModItems;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.proxy.ClientProxy;
import com.mrcrayfish.guns.proxy.CommonProxy;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class GunMod
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    public static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static final ItemGroup GROUP = new ItemGroup(Reference.MOD_ID)
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.PISTOL);
        }
    };

    public GunMod()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.serverSpec);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event)
    {
        //ModCrafting.register(); //TODO convert to datapack
        //ModEntities.register();
        ModSyncedDataKeys.register();
        AmmoRegistry.getInstance().registerProjectileFactory(ModItems.GRENADE, EntityGrenade::new);
        AmmoRegistry.getInstance().registerProjectileFactory(ModItems.MISSILE, EntityMissile::new);
        PacketHandler.init();
        MinecraftForge.EVENT_BUS.register(new CommonEvents());
        //TODO load
    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        ClientHandler.setup();
    }

    public void onServerStart(FMLServerAboutToStartEvent event)
    {
        NetworkGunManager manager = new NetworkGunManager();
        PROXY.setGunPropertiesManager(manager);
        event.getServer().getResourceManager().addReloadListener(manager);

        //TODO convert to config value
        /*GameRules rules = event.getServer().getWorld(DimensionType.OVERWORLD).getGameRules();
        if(!rules.getBoolean("gunGriefing"))
        {
            rules.addGameRule("gunGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        }*/
    }
}
