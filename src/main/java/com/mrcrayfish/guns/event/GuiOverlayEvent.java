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
	private boolean canZoom = false;
	private int zoomProgress;
	private int lastZoomProgress;
	
	@SubscribeEvent
	public void renderOverlay(KeyInputEvent event)
	{
		this.canZoom = GuiScreen.isAltKeyDown();
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
				
				if(zoomProgress < 20)
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
		double progress = (this.lastZoomProgress + ((this.zoomProgress - this.lastZoomProgress) * event.getPartialTicks())) / 20.0;
		if(event.getHand() == EnumHand.MAIN_HAND && zoomProgress != 0)
		{
			ItemStack stack = event.getItemStack();
			if(stack.getItem() instanceof ItemGun)
			{
				ItemGun gun = (ItemGun) stack.getItem();
				GlStateManager.translate(-0.685 * progress, gun.getGun().display.zoomYOffset * progress, -0.5);
			}
		}
		else if(zoomProgress != 0)
		{	
			GlStateManager.translate(0.0, -2 * progress, 0.0);
		}
	}
}
