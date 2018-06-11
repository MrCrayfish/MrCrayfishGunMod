package com.mrcrayfish.guns.proxy;

import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.event.GunHandler;
import com.mrcrayfish.guns.client.event.ReloadHandler;
import com.mrcrayfish.guns.client.event.RenderEvents;
import com.mrcrayfish.guns.client.render.entity.RenderProjectile;
import com.mrcrayfish.guns.entity.EntityProjectile;
import com.mrcrayfish.guns.init.RegistrationHandler;
import com.mrcrayfish.guns.item.ItemGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.awt.*;

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
			if(item instanceof ItemGun)
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
}
