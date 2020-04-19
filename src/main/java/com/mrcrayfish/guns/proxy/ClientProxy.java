package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.client.event.SoundEvents;
import com.mrcrayfish.guns.item.ColoredItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientProxy extends CommonProxy
{
	public static boolean controllableLoaded = false;

	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new GunHandler());
		MinecraftForge.EVENT_BUS.register(new ReloadHandler());

		//RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, RenderProjectile::new);
		//RenderingRegistry.registerEntityRenderingHandler(EntityThrowableGrenade.class, RenderGrenade::new);
		//RenderingRegistry.registerEntityRenderingHandler(EntityThrowableStunGrenade.class, RenderGrenade::new);

		KeyBinds.register();

		SoundEvents.initReflection();


	}

	@Override
	@SuppressWarnings({"ConstantConditions"})
	public void init()
	{
		super.init();
		IItemColor color = (stack, index) ->
		{
			if(index == 0 && stack.hasTag() && stack.getTag().contains("Color", Constants.NBT.TAG_INT))
			{
				return stack.getTag().getInt("Color");
			}
			return -1;
		};
		ForgeRegistries.ITEMS.forEach(item ->
		{
			if(item instanceof ColoredItem)
			{
				Minecraft.getInstance().getItemColors().register(color, item);
			}
		});

		/*ModelOverrides.register(new ItemStack(ModItems.MINI_GUN), new ModelChainGun());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.SMALL.ordinal()), new ModelShortScope());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.MEDIUM.ordinal()), new ModelMediumScope());
		ModelOverrides.register(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.LONG.ordinal()), new ModelLongScope());

		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.PISTOL), new DisplayProperty(0.0F, 0.55F, -0.25F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SHOTGUN), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.RIFLE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.GRENADE_LAUNCHER), new DisplayProperty(0.0F, 0.55F, -0.1F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.BAZOOKA), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.5F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.MINI_GUN), new DisplayProperty(0.0F, 0.55F, 0.1F, 0.0F, 0.0F, 0.0F, 2.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.ASSAULT_RIFLE), new DisplayProperty(0.0F, 0.55F, -0.15F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.BASIC_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.ADVANCED_AMMO), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SHELL), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.MISSILE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 2.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.STUN_GRENADE), new DisplayProperty(0.0F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, 3.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.SMALL.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.MEDIUM.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SCOPES, 1, ScopeItem.Type.LONG.ordinal()), new DisplayProperty(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 4.0F));
		WorkbenchScreen.addDisplayProperty(new ItemStack(ModItems.SILENCER), new DisplayProperty(0.0F, 0.25F, 0.5F, 0.0F, 0.0F, 0.0F, 1.5F));*/
	}

	@Override
	public void postInit()
	{
		super.postInit();
		//ModelOverrides.getModelMap().forEach((item, map) -> map.values().forEach(IOverrideModel::init));
	}

	/*@SubscribeEvent
	public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		GunRegistry.getInstance().getGuns().forEach((location, gun) -> gun.getGun().serverGun = null);
	}*/
}
