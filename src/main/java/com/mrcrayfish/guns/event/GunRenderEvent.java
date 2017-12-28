package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.render.model.OverrideModelPlayer;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class GunRenderEvent
{
	private static final double ZOOM_TICKS = 8;
	public static boolean drawFlash = false;

	private int zoomProgress;
	private int lastZoomProgress;
	private double realProgress;

	private boolean setupThirdPerson = false;
	private boolean setupPlayerRender = false;

	private ItemStack flash = null;

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
					mc.gameSettings.smoothCamera = gun.getGun().modules.zoom.smooth;
					event.setNewfov(gun.getGun().modules.zoom.fovModifier - 0.1F);
				}
				else
				{
					mc.gameSettings.smoothCamera = false;
				}
			}
		}
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
				IGunModel model = ModelOverrides.getModel(heldItem.getItem());
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

		if(!(heldItem.getItem() instanceof ItemGun)) return;

		event.setCanceled(true);

		ItemStack scope = null;
		if(heldItem.hasTagCompound())
		{
			if(heldItem.getTagCompound().hasKey("attachments", Constants.NBT.TAG_COMPOUND))
			{
				NBTTagCompound attachment = heldItem.getTagCompound().getCompoundTag("attachments");
				if(attachment.hasKey("scope", Constants.NBT.TAG_COMPOUND))
				{
					scope = new ItemStack(attachment.getCompoundTag("scope"));
				}
			}
		}

		if(realProgress > 0)
		{
			ItemGun itemGun = (ItemGun) event.getItemStack().getItem();
			if(event.getHand() == EnumHand.MAIN_HAND)
			{
				double xOffset = 0.0;
				double yOffset = 0.0;
				double zOffset = 0.0;

				Gun gun = itemGun.getGun();
				if(gun.modules.zoom != null)
				{
					xOffset += gun.modules.zoom.xOffset;
					yOffset += gun.modules.zoom.yOffset;
					zOffset += gun.modules.zoom.zOffset;
				}

				if(gun.modules.attachments.scope != null && scope != null)
				{
					yOffset -= 0.1;
					zOffset += gun.modules.attachments.scope.zOffset - 0.05;
				}

				GlStateManager.translate((-0.3415 + xOffset) * realProgress, yOffset * realProgress, zOffset * realProgress);
			}

			if(event.getHand() == EnumHand.OFF_HAND)
			{	
				GlStateManager.translate(0.0, -0.5 * realProgress, 0.0);
			}
		}

		if(event.getHand() == EnumHand.MAIN_HAND)
		{
			//GlStateManager.translate(0, -(event.getEquipProgress() / 2.0F), 0);
			GlStateManager.translate(0.56F, -0.52F, -0.72F);

			Gun gun = ((ItemGun) event.getItemStack().getItem()).getGun();

			if(gun.modules != null && gun.modules.attachments != null && gun.modules.attachments.scope != null && scope != null)
			{
				IBakedModel scopeModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(ModGuns.PARTS, 1, 3));
				GlStateManager.pushMatrix();
				{

					GlStateManager.translate(gun.modules.attachments.scope.xOffset, gun.modules.attachments.scope.yOffset, gun.modules.attachments.scope.zOffset);
					RenderUtil.renderModel(scopeModel, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
				}
				GlStateManager.popMatrix();
			}

			if(drawFlash)
			{
				if(flash == null) flash = new ItemStack(ModGuns.PARTS, 1, 2);
				IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(flash);
				GlStateManager.pushMatrix();
				{
					GlStateManager.disableLighting();
					GlStateManager.translate(gun.display.flash.xOffset, gun.display.flash.yOffset, gun.display.flash.zOffset);
					RenderUtil.renderModel(model, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
					GlStateManager.enableLighting();
				}
				GlStateManager.popMatrix();
				drawFlash = false;
			}

			GlStateManager.pushMatrix();
			{
				if(ModelOverrides.hasModel(event.getItemStack().getItem()))
				{
					IGunModel model = ModelOverrides.getModel(event.getItemStack().getItem());
					model.registerPieces();
					model.render(event.getPartialTicks(), ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
				}
				else
				{
					Minecraft.getMinecraft().getRenderItem().renderItem(event.getItemStack(), ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
				}
			}
			GlStateManager.popMatrix();
		}
	}

	private boolean isZooming(EntityPlayer player)
	{
		if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if(stack.getItem() instanceof ItemGun)
			{
				Gun gun = ((ItemGun) stack.getItem()).getGun();
				return gun.modules != null && gun.modules.zoom != null && GuiScreen.isAltKeyDown();
			}
		}
		return false;
	}

	@SubscribeEvent
	public void onRenderHand(RenderPlayerEvent.Pre event)
	{
		if(!setupThirdPerson)
		{
			RenderPlayer renderer = event.getRenderer();
			Field field = ReflectionHelper.findField(RenderLivingBase.class, ObfuscationReflectionHelper.remapFieldNames(RenderLivingBase.class.getName(), "field_177097_h"));
			if(field != null) try
			{
				List<LayerRenderer<EntityLivingBase>> layers = (List<LayerRenderer<EntityLivingBase>>) field.get(renderer);
				layers.removeIf(layerRenderer -> layerRenderer instanceof LayerHeldItem);
				layers.add(new LayerCustomHeldItem(event.getRenderer()));
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
			}
			setupThirdPerson = true;
		}

		if(!setupPlayerRender)
		{
			//ObfuscationReflectionHelper.getPrivateValue() TODO: what was I doing?
			Map<String, RenderPlayer> skinMap = ReflectionHelper.getPrivateValue(RenderManager.class, Minecraft.getMinecraft().getRenderManager(), "skinMap");
			if(skinMap != null)
			{
				ReflectionHelper.setPrivateValue(RenderLivingBase.class, skinMap.get("default"), new OverrideModelPlayer(0.0F, false), "mainModel");
				ReflectionHelper.setPrivateValue(RenderLivingBase.class, skinMap.get("slim"), new OverrideModelPlayer(0.0F, true), "mainModel");
			}
			setupPlayerRender = true;
		}

		//EntityPlayer player = (EntityPlayer) event.getEntity(); TODO: What was I doing here too?
		//player.prevRenderYawOffset = player.rotationYaw;
		//player.renderYawOffset = player.rotationYaw;
	}

	@SideOnly(Side.CLIENT)
	public static class LayerCustomHeldItem implements LayerRenderer<EntityLivingBase>
	{
		private final RenderLivingBase<?> livingEntityRenderer;

		private LayerCustomHeldItem(RenderLivingBase<?> livingEntityRendererIn)
		{
			this.livingEntityRenderer = livingEntityRendererIn;
		}

		@Override
		public void doRenderLayer(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			boolean isRightHanded = entity.getPrimaryHand() == EnumHandSide.RIGHT;
			ItemStack leftStack = isRightHanded ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
			ItemStack rightStack = isRightHanded ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();

			if (!leftStack.isEmpty() || !rightStack.isEmpty())
			{
				GlStateManager.pushMatrix();

				if (this.livingEntityRenderer.getMainModel().isChild)
				{
					GlStateManager.translate(0.0F, 0.75F, 0.0F);
					GlStateManager.scale(0.5F, 0.5F, 0.5F);
				}

				this.renderHeldItem(entity, rightStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, partialTicks);
				this.renderHeldItem(entity, leftStack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, partialTicks);
				GlStateManager.popMatrix();
			}
		}

		private void renderHeldItem(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide, float partialTicks)
		{
			if (!stack.isEmpty())
			{
				GlStateManager.pushMatrix();
				{
					if(entity.isSneaking())
					{
						GlStateManager.translate(0.0F, 0.2F, 0.0F);
					}
					this.translateToHand(handSide);

					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

					boolean isLeftHanded = handSide == EnumHandSide.LEFT;
					GlStateManager.translate((float) (isLeftHanded ? -1 : 1) / 16.0F, 0.125F, -0.625F);

					if(stack.getItem() instanceof ItemGun && ModelOverrides.hasModel(stack.getItem()))
					{
						IGunModel model = ModelOverrides.getModel(stack.getItem());
						model.registerPieces();
						model.render(partialTicks, transformType);
					}
					else
					{
						Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, transformType, isLeftHanded);
					}
				}
				GlStateManager.popMatrix();
			}
		}

		protected void translateToHand(EnumHandSide p_191361_1_)
		{
			((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(0.0625F, p_191361_1_);
		}

		public boolean shouldCombineTextures()
		{
			return false;
		}
	}
}
