package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.event.GuiOverlayEvent;
import com.mrcrayfish.guns.init.ModGuns;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void preInit() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, new IRenderFactory<EntityProjectile>() 
		{
			@Override
			public Render<EntityProjectile> createRenderFor(RenderManager manager) 
			{
				return new RenderProjectile(manager);
			}
		});
	}
	
	@Override
	public void init()
	{
		ModGuns.registerRenders();
		
		MinecraftForge.EVENT_BUS.register(new GuiOverlayEvent());
	}
}
