package com.mrcrayfish.guns.event;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.mrcrayfish.guns.item.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
	
	private float originalFov;
	
	@SubscribeEvent
	public void renderOverlay(KeyInputEvent event)
	{
		this.canZoom = GuiScreen.isAltKeyDown();
		if(this.canZoom) originalFov = Minecraft.getMinecraft().gameSettings.fovSetting;
	}
	
	@SubscribeEvent
	public void renderOverlay(TickEvent event)
	{
		if(event.phase == Phase.START)
		{
			if(canZoom)
			{
				Minecraft.getMinecraft().player.prevCameraYaw = 0F;
				Minecraft.getMinecraft().player.cameraYaw = 0F;
				
				if(zoomProgress < ZOOM_TICKS)
				{
					zoomProgress++;
					Minecraft.getMinecraft().gameSettings.fovSetting -= 1.5F;
				}
			}
			else
			{
				if(zoomProgress > 0)
				{
					zoomProgress--;
					Minecraft.getMinecraft().gameSettings.fovSetting += 1.5F;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void renderOverlay(RenderSpecificHandEvent event)
	{
		this.lastZoomProgress = this.zoomProgress;
		double progress = (this.zoomProgress + (zoomProgress == 0 || zoomProgress == ZOOM_TICKS ?  0 : event.getPartialTicks())) / ZOOM_TICKS;
		if(progress > 0)
		{
			if(event.getHand() == EnumHand.MAIN_HAND)
			{
				ItemStack stack = event.getItemStack();
				if(stack.getItem() instanceof ItemGun)
				{
					ItemGun gun = (ItemGun) stack.getItem();
					GlStateManager.translate(-0.685 * progress, gun.getGun().display.zoomYOffset * progress, -0.5 * progress);
				}
			}
			else
			{	
				GlStateManager.translate(0.0, -2 * progress, 0.0);
			}
		}
	}
}
