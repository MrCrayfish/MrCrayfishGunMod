package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.item.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class GuiOverlayEvent 
{
	private static final double ZOOM_TICKS = 30;
	
	private boolean canZoom = false;
	private int zoomProgress;
	private int lastZoomProgress;
	private double realProgress;
	private float originalFov;

	@SubscribeEvent
	public void onFovUpdate(FOVUpdateEvent event)
	{
		Item item = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem();
		if(item instanceof ItemGun)
		{
			ItemGun gun = (ItemGun) item;
			if(canZoom)
			{
				Minecraft.getMinecraft().gameSettings.smoothCamera = gun.getGun().display.zoomSmooth;
				event.setNewfov(gun.getGun().display.zoomFovModifier);
			}
			else
			{
				Minecraft.getMinecraft().gameSettings.smoothCamera = false;
				event.setNewfov(1.0F);
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		ItemStack stack = Minecraft.getMinecraft().player.getHeldItemMainhand();
		canZoom = GuiScreen.isAltKeyDown() && stack.getItem() instanceof ItemGun;
	}
	
	@SubscribeEvent
	public void onTick(TickEvent event)
	{
		if(event.phase == Phase.START)
		{
			if(Minecraft.getMinecraft().player != null && !(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemGun))
			{
				canZoom = false;
			}
			
			if(canZoom)
			{
				Minecraft.getMinecraft().player.prevCameraYaw = 0.0F;
				Minecraft.getMinecraft().player.cameraYaw = 0.0F;
				
				if(zoomProgress < ZOOM_TICKS)
				{
					zoomProgress++;
				}
			}
			else
			{
				if(zoomProgress > 0)
				{
					zoomProgress--;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderOverlay(RenderSpecificHandEvent event)
	{
		lastZoomProgress = zoomProgress;
		
		realProgress = (zoomProgress + (zoomProgress == 0 || zoomProgress == ZOOM_TICKS ?  0 : event.getPartialTicks())) / ZOOM_TICKS;

		if(realProgress > 0)
		{
			System.out.println(realProgress + " " + event.getPartialTicks());
			if(event.getItemStack().getItem() instanceof ItemGun)
			{
				ItemGun gun = (ItemGun) event.getItemStack().getItem();

				if(event.getHand() == EnumHand.MAIN_HAND)
				{
					GlStateManager.translate(-0.6861 * realProgress, gun.getGun().display.zoomYOffset * realProgress, gun.getGun().display.zoomZOffset * realProgress);
				}
			}
			if(event.getHand() == EnumHand.OFF_HAND)
			{	
				GlStateManager.translate(0.0, -2 * realProgress, 0.0);
			}
		}
	}
}
