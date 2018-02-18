package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GunRenderEvent
{
	private static final ResourceLocation SCOPE_OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/scope_long_overlay.png");

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
		if(!mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.thirdPersonView == 0)
		{
			ItemStack heldItem = mc.player.getHeldItemMainhand();
			if(heldItem.getItem() instanceof ItemGun)
			{
				ItemStack scope = Gun.getScope(heldItem);

				ItemGun gun = (ItemGun) heldItem.getItem();
				if(isZooming(Minecraft.getMinecraft().player))
				{
					mc.gameSettings.smoothCamera = gun.getGun().modules.zoom.smooth;
					float newFov = gun.getGun().modules.zoom.fovModifier - 0.1F;
					if(scope != null)
					{
						ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
						if(scopeType != null)
						{
							newFov -= scopeType.getAdditionalZoom();
							mc.gameSettings.smoothCamera = gun.getGun().modules.attachments.scope.smooth;
							if(scopeType == ItemScope.Type.LONG)
							{
								mc.gameSettings.smoothCamera = true;
							}
						}
					}
					event.setNewfov(newFov);
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
		realProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0 : event.getPartialTicks())) / ZOOM_TICKS;
		if(realProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onRenderOverlay(RenderSpecificHandEvent event)
	{
		ItemStack heldItem = event.getItemStack();

		if(!(heldItem.getItem() instanceof ItemGun))
			return;

		//Cancel it because we are doing our own custom render
		event.setCanceled(true);

		ItemStack scope = Gun.getScope(heldItem);
		ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);

		if(scopeType != null && scopeType == ItemScope.Type.LONG && realProgress == 1.0)
			return;

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

					if(gun.modules.attachments.scope == null || scope == null)
					{
						zOffset += gun.modules.zoom.zOffset;
					}
				}

				//TODO standardised scope position
				//gun.modules.zoom.yOffset
				//gun.modules.attachments.scope.yOffset;
				//scopeType.getOffset()

				if(gun.canAttachScope() && scope != null && scopeType != null)
				{
					yOffset -= scopeType.getOffset();
					zOffset -= gun.modules.attachments.scope.zOffset * 0.8 - 0.45;
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

			if(gun.canAttachScope() && scope != null)
			{
				IBakedModel scopeModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(scope);
				GlStateManager.pushMatrix();
				{
					GlStateManager.translate(gun.modules.attachments.scope.xOffset * 0.8, gun.modules.attachments.scope.yOffset * 0.8, gun.modules.attachments.scope.zOffset * 0.8);
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
				IGunModel model = ModelOverrides.getModel(event.getItemStack().getItem());
				if(model != null)
				{
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
	public void onTick(TickEvent.RenderTickEvent event)
	{
		if (event.phase.equals(TickEvent.Phase.START))
			return;

		EntityPlayer player = Minecraft.getMinecraft().player;

		if(player == null)
			return;

		if(Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
			return;

		ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

		if(heldItem.isEmpty() || !(heldItem.getItem() instanceof ItemGun))
			return;

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

		if(scope != null)
		{
			ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
			if(scopeType != null && scopeType == ItemScope.Type.LONG && realProgress == 1.0)
			{
				Minecraft mc = Minecraft.getMinecraft();
				mc.getTextureManager().bindTexture(SCOPE_OVERLAY);
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableDepth();

				ScaledResolution scaledResolution = new ScaledResolution(mc);

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
				buffer.pos(0, scaledResolution.getScaledHeight(), 0).tex(0, 1).endVertex();
				buffer.pos(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 0).tex(1, 1).endVertex();
				buffer.pos(scaledResolution.getScaledWidth(), 0, 0).tex(1, 0).endVertex();
				buffer.pos(0, 0, 0).tex(0, 0).endVertex();
				tessellator.draw();

				GlStateManager.disableAlpha();
				GlStateManager.disableBlend();
				GlStateManager.enableDepth();
			}
		}
	}

	@SubscribeEvent
	public void onRenderHeldItem(RenderItemEvent.Held.Pre event)
	{
		ItemStack heldItem = event.getItem();
		if(heldItem.getItem() instanceof ItemGun)
		{
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			if(gun.general.gripType == Gun.GripType.TWO_HANDED)
			{
				GlStateManager.translate(0, 0, 0.05);
				float invertRealProgress = (float) (1.0 - this.realProgress);
				GlStateManager.rotate(25F * invertRealProgress, 0, 0, 1);
				GlStateManager.rotate(25F * invertRealProgress + (float) (this.realProgress * -20F), 0, 1, 0);
				GlStateManager.rotate(25F * invertRealProgress, 1, 0, 0);
			}
			RenderUtil.applyTransformType(heldItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
			if(ModelOverrides.hasModel(heldItem.getItem()))
			{
				IGunModel model = ModelOverrides.getModel(heldItem.getItem());
				model.registerPieces();
				model.render(event.getPartialTicks(), ItemCameraTransforms.TransformType.NONE);
			}
			else
			{
				Minecraft.getMinecraft().getItemRenderer().renderItemSide(event.getEntity(), heldItem, ItemCameraTransforms.TransformType.NONE, event.getHandSide() == EnumHandSide.LEFT);
			}
			this.renderAttachments(heldItem);
			event.setCanceled(true);

		}
	}

	@SubscribeEvent
	public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
	{
		event.setCanceled(this.renderGun(event.getItem(), event.getTransformType()));
	}

	@SubscribeEvent
	public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
	{
		event.setCanceled(this.renderGun(event.getItem(), event.getTransformType()));
	}

	@SubscribeEvent
	public void onSetupAngles(ModelPlayerEvent.SetupAngles.Post event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();
		if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
		{
			ModelPlayer model = event.getModelPlayer();
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			switch(gun.general.gripType)
			{
				case ONE_HANDED:
					copyModelAngles(model.bipedHead, model.bipedRightArm);
					model.bipedRightArm.rotateAngleX += Math.toRadians(-65F);
					break;
				case TWO_HANDED:
					copyModelAngles(model.bipedHead, model.bipedRightArm);
					copyModelAngles(model.bipedHead, model.bipedLeftArm);
					model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-55F + this.realProgress * -35F);
					model.bipedRightArm.rotateAngleY = (float) Math.toRadians(-45F + this.realProgress * -20F);
					model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-50F + this.realProgress * -40F);
					model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(-10F + this.realProgress * 0F);
					break;
			}
			copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
			copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();
		if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
		{
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			if(gun.general.gripType == Gun.GripType.TWO_HANDED)
			{
				player.prevRenderYawOffset = event.getEntity().prevRotationYaw + 25F + (float) this.realProgress * 20F;
				player.renderYawOffset = event.getEntity().rotationYaw + 25F + (float) this.realProgress * 20F;
			}
		}
	}

	private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
	{
		dest.rotateAngleX = source.rotateAngleX;
		dest.rotateAngleY = source.rotateAngleY;
		dest.rotateAngleZ = source.rotateAngleZ;
	}

	private boolean renderGun(ItemStack stack, ItemCameraTransforms.TransformType transformType)
	{
		if(stack.getItem() instanceof ItemGun)
		{
			GlStateManager.pushMatrix();
			RenderUtil.applyTransformType(stack, transformType);
			RenderUtil.renderModel(stack);
			this.renderAttachments(stack);
			GlStateManager.popMatrix();
			return true;
		}
		return false;
	}

	private void renderAttachments(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemGun)
		{
			Gun gun = ((ItemGun) stack.getItem()).getGun();
			NBTTagCompound itemTag = stack.getTagCompound();
			if(itemTag != null)
			{
				if(gun.canAttachScope() && itemTag.hasKey("attachments", Constants.NBT.TAG_COMPOUND))
				{
					NBTTagCompound attachment = itemTag.getCompoundTag("attachments");
					if(attachment.hasKey("scope", Constants.NBT.TAG_COMPOUND))
					{
						GlStateManager.pushMatrix();
						{
							ItemStack scope = new ItemStack(attachment.getCompoundTag("scope"));
							GlStateManager.translate(gun.modules.attachments.scope.xOffset, gun.modules.attachments.scope.yOffset, gun.modules.attachments.scope.zOffset);
							RenderUtil.renderModel(scope);
						}
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}
}
