package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit() 
	{
		super.preInit();

		MinecraftForge.EVENT_BUS.register(new RenderEvents());
		MinecraftForge.EVENT_BUS.register(new GunHandler());
		MinecraftForge.EVENT_BUS.register(new ReloadHandler());
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, RenderProjectile::new);
		KeyBinds.register();
	}

	@Override
	public void showMuzzleFlash()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.rotationPitch -= 0.4f;
		RenderEvents.drawFlash = true;
	}
}
