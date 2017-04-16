package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.item.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class GuiOverlayEvent 
{
	private static final double ZOOM_TICKS = 8;
	
	private boolean canZoom = false;
	private int zoomProgress;
	private int lastZoomProgress;
	private double realProgress;
	private float originalFov;

	@SubscribeEvent
	public void onFovUpdate(FOVUpdateEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.player.getHeldItemMainhand() != null)
		{
			Item item = mc.player.getHeldItemMainhand().getItem();
			if(item instanceof ItemGun)
			{
				ItemGun gun = (ItemGun) item;
				if(canZoom)
				{
					mc.gameSettings.smoothCamera = gun.getGun().display.zoomSmooth;
					event.setNewfov(gun.getGun().display.zoomFovModifier);
				}
				else
				{
					mc.gameSettings.smoothCamera = false;
					event.setNewfov(1.0F);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.player.getHeldItemMainhand() != null)
		{
			ItemStack stack = mc.player.getHeldItemMainhand();
			canZoom = GuiScreen.isAltKeyDown() && stack.getItem() instanceof ItemGun;
		}
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		lastZoomProgress = zoomProgress;
		
		if(Minecraft.getMinecraft().player != null && !(Minecraft.getMinecraft().player.getHeldItemMainhand() != null && Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() instanceof ItemGun))
		{
			canZoom = false;
		}
		
		if(canZoom)
		{
			Minecraft.getMinecraft().player.prevCameraYaw = 0.02F;
			Minecraft.getMinecraft().player.cameraYaw = 0.02F;
			
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
	
	@SubscribeEvent
	public void onRenderOverlay(RenderSpecificHandEvent event)
	{
		realProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0 : event.getPartialTicks())) / ZOOM_TICKS;

		if(realProgress > 0)
		{
			if(event.getItemStack() != null && event.getItemStack().getItem() instanceof ItemGun)
			{
				ItemGun gun = (ItemGun) event.getItemStack().getItem();

				if(event.getHand() == EnumHand.MAIN_HAND)
				{
					GlStateManager.translate(-0.3415 * realProgress, gun.getGun().display.zoomYOffset * realProgress, gun.getGun().display.zoomZOffset * realProgress);
				}
			}
			if(event.getHand() == EnumHand.OFF_HAND)
			{	
				GlStateManager.translate(0.0, -2 * realProgress, 0.0);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.RightClickItem event)
	{
		if(event.getItemStack().getItem() instanceof ItemGun)
		{
			switch(event.getHand())
			{
			case MAIN_HAND:
				ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, Minecraft.getMinecraft().getItemRenderer(), 1F, "equippedProgressMainHand", "field_187469_f");
				ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, Minecraft.getMinecraft().getItemRenderer(), 1F, "prevEquippedProgressMainHand", "field_187470_g");
				break;
			case OFF_HAND:
				ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, Minecraft.getMinecraft().getItemRenderer(), 1F, "equippedProgressOffHand", "field_187471_h");
				ObfuscationReflectionHelper.setPrivateValue(ItemRenderer.class, Minecraft.getMinecraft().getItemRenderer(), 1F, "prevEquippedProgressOffHand", "field_187472_i");
				break;
			default:
				break;
			
			}
		}
	}
}
