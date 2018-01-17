package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.client.render.entity.RenderExtendedEntityItem;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.event.GunRenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy implements IProxy
{
	@Override
	public void preInit() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectile.class, RenderProjectile::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityItem.class, manager -> new RenderExtendedEntityItem(manager, Minecraft.getMinecraft().getRenderItem()));
	}
	
	@Override
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new GunRenderEvent());
	}

	@Override
	public void spawnParticle(EnumParticleTypes type, boolean ignoreRange, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		//World world = Minecraft.getMinecraft().world;
	}
}
