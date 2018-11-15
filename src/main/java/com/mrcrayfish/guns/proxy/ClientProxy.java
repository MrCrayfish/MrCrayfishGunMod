package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.client.render.entity.RenderGrenade;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityGrenade;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.ItemColored;
import com.mrcrayfish.guns.object.ServerGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.awt.*;
import java.util.Map;

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
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, RenderGrenade::new);
		KeyBinds.register();
	}

	@Override
	public void init()
	{
		super.init();
		IItemColor color = (stack, index) ->
		{
			if(index == 0 && stack.hasTagCompound() && stack.getTagCompound().hasKey("color", Constants.NBT.TAG_INT))
			{
				return stack.getTagCompound().getInteger("color");
			}
			return Color.decode("#66FFFFFF").getRGB();
		};
		RegistrationHandler.Items.getItems().forEach(item ->
		{
			if(item instanceof ItemColored)
			{
				Minecraft.getMinecraft().getItemColors().registerItemColorHandler(color, item);
			}
		});
	}

	@Override
	public void showMuzzleFlash()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		player.rotationPitch -= 0.4f;
		RenderEvents.drawFlash = true;
	}

	@Override
	public void playClientSound(SoundEvent sound)
	{
		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
	}

	@Override
	public void playClientSound(double posX, double posY, double posZ, SoundEvent event, SoundCategory category, float volume, float pitch)
	{
		Minecraft.getMinecraft().addScheduledTask(() ->
		{
			ISound sound = new PositionedSoundRecord(event.getSoundName(), category, volume, pitch, false, 0, ISound.AttenuationType.NONE, 0, 0, 0);
			Minecraft.getMinecraft().getSoundHandler().playSound(sound);
		});
	}

	@Override
	public void syncServerGunProperties(Map<String, ServerGun> properties)
	{
		GunConfig.ID_TO_GUN.forEach((s, gun) -> gun.serverGun = properties.get(s));
	}

	@SubscribeEvent
	public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
	{
		GunConfig.ID_TO_GUN.forEach((s, gun) -> gun.serverGun = null);
	}
}
