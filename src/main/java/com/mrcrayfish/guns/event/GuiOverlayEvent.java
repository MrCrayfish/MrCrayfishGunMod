package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.render.gun.model.ModelChainGun;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class GuiOverlayEvent 
{
	private static final double ZOOM_TICKS = 8;

	private int zoomProgress;
	private int lastZoomProgress;
	private double realProgress;
	private float lastEqiupProgress = 0F;

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
				if(isZooming(Minecraft.getMinecraft().player))
				{
					mc.gameSettings.smoothCamera = gun.getGun().display.zoomSmooth;
					event.setNewfov(gun.getGun().display.zoomFovModifier);
				}
				else
				{
					mc.gameSettings.smoothCamera = false;
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		lastZoomProgress = zoomProgress;

		if(isZooming(Minecraft.getMinecraft().player))
		{
			Minecraft.getMinecraft().player.prevCameraYaw = 0.0075F;
			Minecraft.getMinecraft().player.cameraYaw = 0.0075F;
			
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

		if(Minecraft.getMinecraft().player != null)
		{
			ItemStack heldItem = Minecraft.getMinecraft().player.getHeldItemMainhand();
			if(heldItem != null && heldItem.getItem() instanceof ItemGun)
			{
				ResourceLocation resource = Item.REGISTRY.getNameForObject(heldItem.getItem());
				IGunModel model = ModelOverrides.getModel(resource);
				if(model != null) model.tick();
			}
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event)
	{
		if(realProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onRenderOverlay(RenderSpecificHandEvent event)
	{
		realProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0 : event.getPartialTicks())) / ZOOM_TICKS;

		ItemStack heldItem = event.getItemStack();
		boolean hasGun = heldItem != null && heldItem.getItem() instanceof ItemGun;

		if(realProgress > 0)
		{
			if(hasGun)
			{
				ItemGun gun = (ItemGun) event.getItemStack().getItem();

				if(event.getHand() == EnumHand.MAIN_HAND)
				{
					GlStateManager.translate((-0.3415 + gun.getGun().display.zoomXOffset) * realProgress, gun.getGun().display.zoomYOffset * realProgress, gun.getGun().display.zoomZOffset * realProgress);
				}
			}
			if(event.getHand() == EnumHand.OFF_HAND)
			{	
				GlStateManager.translate(0.0, -0.5 * realProgress, 0.0);
			}
		}

		if(hasGun && event.getHand() == EnumHand.MAIN_HAND)
		{
			event.setCanceled(true);

			Gun gun = ((ItemGun) event.getItemStack().getItem()).getGun();
			if(drawFlash)
			{
				if(flash == null) flash = new ItemStack(ModGuns.parts, 1, 2);
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(flash);
				GlStateManager.pushMatrix();
				{
					GlStateManager.disableLighting();
					GlStateManager.translate(0.56F, -0.52F, -0.72F);
					GlStateManager.translate(gun.display.flashXOffset, gun.display.flashYOffset, gun.display.flashZOffset);
					RenderUtil.renderModel(model);
					GlStateManager.enableLighting();
				}
				GlStateManager.popMatrix();
				drawFlash = false;
			}

			GlStateManager.pushMatrix();
			{
				GlStateManager.translate(0.56F, -0.52F, -0.72F);

				if(Math.abs(event.getEquipProgress() - lastEqiupProgress) < 0.2F)
				{
					GlStateManager.translate(0, -(event.getEquipProgress() / 2.0F), 0);
					lastEqiupProgress = event.getEquipProgress();
				}

				ResourceLocation resource = Item.REGISTRY.getNameForObject(event.getItemStack().getItem());
				IGunModel model = ModelOverrides.getModel(resource);
				if(model != null)
				{
					model.registerPieces();
					model.render(event.getPartialTicks());
				} else
				{
					Minecraft.getMinecraft().getRenderItem().renderItem(event.getItemStack(), ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
				}
			}
			GlStateManager.popMatrix();
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

	private boolean isZooming(EntityPlayer player)
	{
		if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() instanceof ItemGun)
			{
				ItemGun item = (ItemGun) stack.getItem();
				return item.getGun().display.canZoom && GuiScreen.isAltKeyDown();
			}
		}
		return false;
	}

	private static void drawRectWithTexture(double x, double y, double z, float u, float v, int width, int height, float textureWidth, float textureHeight)
	{
		float scale = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldrenderer = tessellator.getBuffer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double)x, (double)(y + height), z).tex((double)(u * scale), (double)(v + textureHeight) * scale).endVertex();
		worldrenderer.pos((double)(x + width), (double)(y + height), z).tex((double)(u + textureWidth) * scale, (double)(v + textureHeight) * scale).endVertex();
		worldrenderer.pos((double)(x + width), (double)y, z).tex((double)(u + textureWidth) * scale, (double)(v * scale)).endVertex();
		worldrenderer.pos((double)x, (double)y, z).tex((double)(u * scale), (double)(v * scale)).endVertex();
		tessellator.draw();
	}

	private void drawFlash()
	{
		GlStateManager.pushMatrix();
		GlStateManager.enableTexture2D();
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		GlStateManager.scale(-0.025F, -0.025F, 0.025F);
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.enableTexture2D();

		drawRectWithTexture(0,0, 0, 0, 0, 256, 256, 256, 256);

		GlStateManager.depthMask(true);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
}
