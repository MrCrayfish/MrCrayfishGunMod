package com.mrcrayfish.guns;

import org.apache.logging.log4j.Logger;

import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.model.ModelChainGun;
import com.mrcrayfish.guns.common.GuiHandler;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityGrenadeStun;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModCrafting;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModPotions;
import com.mrcrayfish.guns.init.ModSounds;
import com.mrcrayfish.guns.init.ModTileEntities;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION, acceptedMinecraftVersions = Reference.MC_VERSION, dependencies = Reference.DEPENDENCIES)
public class MrCrayfishGunMod
{
	@Mod.Instance
	public static MrCrayfishGunMod instance;

	@SidedProxy(clientSide = Reference.PROXY_CLIENT, serverSide = Reference.PROXY_SERVER)
	public static CommonProxy proxy;

	public static final CreativeTabs GUN_TAB = new TabGun();

	public static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		ModBlocks.register();
		ModGuns.register();
		ModSounds.register();
		ModCrafting.register();
		ModTileEntities.register();
        ModPotions.register();

		PacketHandler.init();

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		EntityRegistry.registerModEntity(new ResourceLocation("cgm:projectile"), EntityProjectile.class, "cgmProjectile", 0, this, 64, 80, true);
		EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade"), EntityGrenade.class, "cgm.grenade", 1, this, 64, 80, true);
		EntityRegistry.registerModEntity(new ResourceLocation("cgm:grenade_stun"), EntityGrenadeStun.class, "cgm.grenade_stun", 2, this, 64, 80, true);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		proxy.init();
	}

	@EventHandler
	public void init(FMLLoadCompleteEvent event)
	{
		if (event.getSide() == Side.CLIENT)
		{
			ModelOverrides.register(new ResourceLocation("cgm:chain_gun"), new ModelChainGun());
		}
	}

	@EventHandler
	public void onServerStart(FMLServerStartedEvent event)
	{
		GameRules rules = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0).getGameRules();
		if (!rules.hasRule("gunGriefing"))
		{
			rules.addGameRule("gunGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
		}
	}
}
