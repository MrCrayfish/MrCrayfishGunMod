package com.mrcrayfish.guns;

import com.mrcrayfish.guns.common.GuiHandler;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityMissile;
import com.mrcrayfish.guns.init.*;
import com.mrcrayfish.guns.item.AmmoRegistry;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.proxy.CommonProxy;
import com.mrcrayfish.guns.recipe.RecipeAttachment;
import com.mrcrayfish.guns.recipe.RecipeColorItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.GameRules;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

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
		ModTileEntities.register();
        ModPotions.register();
		PacketHandler.init();

		RegistrationHandler.Recipes.add(new RecipeAttachment());
		RegistrationHandler.Recipes.add(new RecipeColorItem());

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModCrafting.register();
		ModEntities.register();

		AmmoRegistry.getInstance().registerProjectileFactory(ModGuns.GRENADE, EntityGrenade::new);
		AmmoRegistry.getInstance().registerProjectileFactory(ModGuns.MISSILE, EntityMissile::new);

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
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
