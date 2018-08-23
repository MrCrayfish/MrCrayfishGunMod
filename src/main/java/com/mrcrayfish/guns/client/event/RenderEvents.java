package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.KeyBinds;
import com.mrcrayfish.guns.client.render.gun.IGunModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.item.IAttachment;
import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.item.ItemScope;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RenderEvents
{
	private static final ResourceLocation SCOPE_OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/scope_long_overlay.png");

	private static final double ZOOM_TICKS = 8;
	public static boolean drawFlash = false;

	private int zoomProgress;
	private int lastZoomProgress;
	private double normalZoomProgress;

	private int reloadTimer;
	private int prevReloadTimer;
	private boolean lastSmoothCamera = Minecraft.getMinecraft().gameSettings.smoothCamera;

	private ItemStack flash = null;

	@SubscribeEvent
	public void onKeyPressedEvent(KeyInputEvent event)
	{
		if (Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera.isPressed())
		{
			Minecraft.getMinecraft().gameSettings.smoothCamera = !Minecraft.getMinecraft().gameSettings.smoothCamera;
			lastSmoothCamera = Minecraft.getMinecraft().gameSettings.smoothCamera;
		}
	}

	@SubscribeEvent
	public void onFovUpdate(FOVUpdateEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.thirdPersonView == 0)
		{
			ItemStack heldItem = mc.player.getHeldItemMainhand();
			if (heldItem.getItem() instanceof ItemGun)
			{
				ItemStack scope = Gun.getScope(heldItem);

				ItemGun gun = (ItemGun) heldItem.getItem();
				if (isZooming(Minecraft.getMinecraft().player))
				{
					mc.gameSettings.smoothCamera = gun.getGun().modules.zoom.smooth;
					float newFov = gun.getGun().modules.zoom.fovModifier - 0.1F;
					if (scope != null)
					{
						ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
						if (scopeType != null)
						{
							newFov -= scopeType.getAdditionalZoom();
							mc.gameSettings.smoothCamera = gun.getGun().modules.attachments.scope.smooth;
							if (scopeType == ItemScope.Type.LONG)
							{
								mc.gameSettings.smoothCamera = true;
							}
						}
					}
					event.setNewfov(newFov);
				} else
				{
					mc.gameSettings.smoothCamera = lastSmoothCamera;
				}
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event)
	{
		lastZoomProgress = zoomProgress;
		prevReloadTimer = reloadTimer;

		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player != null && isZooming(player) && !player.getDataManager().get(CommonEvents.RELOADING))
		{
			Minecraft.getMinecraft().player.prevCameraYaw = 0.0075F;
			Minecraft.getMinecraft().player.cameraYaw = 0.0075F;

			if (zoomProgress < ZOOM_TICKS)
			{
				zoomProgress++;
			}
		} else
		{
			if (zoomProgress > 0)
			{
				zoomProgress--;
			}
		}

		if (player != null)
		{
			ItemStack heldItem = player.getHeldItemMainhand();
			if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
			{
				IGunModel model = ModelOverrides.getModel(heldItem.getItem());
				if (model != null)
					model.tick();
			}

			if (player.getDataManager().get(CommonEvents.RELOADING))
			{
				if (reloadTimer < 10)
				{
					reloadTimer++;
				}
			}
			else if (reloadTimer > 0)
			{
				reloadTimer -= 2;
				if (reloadTimer < 0)
				{
					reloadTimer = 0;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event)
	{
		normalZoomProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0 : event.getPartialTicks())) / ZOOM_TICKS;
		if (normalZoomProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onRenderOverlay(RenderSpecificHandEvent event)
	{
		boolean right = Minecraft.getMinecraft().gameSettings.mainHand == EnumHandSide.RIGHT ? event.getHand() == EnumHand.MAIN_HAND : event.getHand() == EnumHand.OFF_HAND;
		ItemStack heldItem = event.getItemStack();
		if (!(heldItem.getItem() instanceof ItemGun))
			return;

		// Cancel it because we are doing our own custom render
		event.setCanceled(true);

		ItemStack mainHand = Minecraft.getMinecraft().player.getHeldItemMainhand();
		if (mainHand.getItem() instanceof ItemGun)
		{
			if (event.getHand() != EnumHand.MAIN_HAND && !((ItemGun) mainHand.getItem()).getGun().general.gripType.canRenderOffhand())
				return;
		}

		ItemStack scope = Gun.getScope(heldItem);
		ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
		if (scopeType != null && scopeType == ItemScope.Type.LONG && normalZoomProgress == 1.0)
			return;

		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(heldItem);
		float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
		float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
		float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
		float translateY = model.getItemCameraTransforms().firstperson_right.translation.getY() * scaleY;
		float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ() * scaleZ;

		GlStateManager.pushMatrix();
		if (normalZoomProgress > 0)
		{
			ItemGun itemGun = (ItemGun) heldItem.getItem();
			if (event.getHand() == EnumHand.MAIN_HAND)
			{
				double xOffset = 0.0;
				double yOffset = 0.0;
				double zOffset = 0.0;

				Gun gun = itemGun.getGun();
				if (gun.canAttachType(IAttachment.Type.SCOPE) && scope != null && scopeType != null)
				{
					xOffset -= gun.modules.attachments.scope.xOffset * 0.0625 * scaleX;
					yOffset -= gun.modules.attachments.scope.yOffset * 0.0625 * scaleY - translateY + scopeType.getHeightToCenter() * scaleY * 0.0625;
					zOffset -= gun.modules.attachments.scope.zOffset * 0.0625 * scaleZ - translateZ - 0.45;
				} else if (gun.modules.zoom != null)
				{
					xOffset -= gun.modules.zoom.xOffset * 0.0625 * scaleX;
					yOffset -= gun.modules.zoom.yOffset * 0.0625 * scaleY - translateY;
					zOffset -= gun.modules.zoom.zOffset * 0.0625 * scaleZ - translateZ;
				}
				double renderOffset = right ? -0.3415 : -0.3775;
				GlStateManager.translate((renderOffset + xOffset) * normalZoomProgress * (right ? 1F : -1F), yOffset * normalZoomProgress, zOffset * normalZoomProgress);
			} else
			{
				GlStateManager.translate(0, -1 * normalZoomProgress, 0);
			}
		}

		/*
		 * if(Minecraft.getMinecraft().player.getDataManager().get(CommonEvents.RELOADING)) { GlStateManager.pushMatrix(); { renderArmFirstPerson(EnumHandSide.LEFT, event.getPartialTicks()); } GlStateManager.popMatrix(); }
		 */
		
		GlStateManager.translate(0, -event.getEquipProgress(), 0);

		if (right)
		{
			GlStateManager.translate(0.56F, -0.56F, -0.72F);
		} else
		{
			GlStateManager.translate(0.56F - 0.72, -0.56F, -0.75F);
		}

		float reloadProgress = (prevReloadTimer + (reloadTimer - prevReloadTimer) * event.getPartialTicks()) / 10F;

		GlStateManager.translate(0, 0.35 * reloadProgress, 0);
		GlStateManager.translate(0, 0, -0.1 * reloadProgress);
		GlStateManager.rotate(45F * reloadProgress, 1, 0, 0);

		this.renderWeapon(heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, event.getPartialTicks());

		GlStateManager.popMatrix();
	}

	private boolean isZooming(EntityPlayer player)
	{
		if (player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
		{
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem() instanceof ItemGun)
			{
				Gun gun = ((ItemGun) stack.getItem()).getGun();
				return gun.modules != null && gun.modules.zoom != null && KeyBinds.KEY_AIM.isKeyDown();
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
		if (player == null)
			return;

		if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
			return;

		ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
		if (heldItem.isEmpty() || !(heldItem.getItem() instanceof ItemGun))
			return;

		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledResolution = new ScaledResolution(mc);

		if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
		{
			ItemScope.Type scopeType = ItemScope.Type.getFromStack(Gun.getAttachment(IAttachment.Type.SCOPE, heldItem));
			if (scopeType != null && scopeType == ItemScope.Type.LONG && normalZoomProgress == 1.0)
			{
				mc.getTextureManager().bindTexture(SCOPE_OVERLAY);
				GlStateManager.color(1.0F, 1.0F, 1.0F);
				GlStateManager.enableBlend();
				GlStateManager.enableAlpha();
				GlStateManager.disableDepth();

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

			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			if (!gun.general.auto)
			{
				float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
				if (coolDown > 0.0F)
				{
					double scale = 3;
					int i = (int) ((scaledResolution.getScaledHeight() / 2 - 7 - 60) / scale);
					int j = (int) Math.ceil((scaledResolution.getScaledWidth() / 2 - 8 * scale) / scale);

					GlStateManager.enableAlpha();
					mc.getTextureManager().bindTexture(Gui.ICONS);

					GlStateManager.pushMatrix();
					{
						GlStateManager.scale(scale, scale, scale);
						int progress = (int) Math.ceil((coolDown + 0.05) * 17.0F) - 1;
						GuiScreen.drawModalRectWithCustomSizedTexture(j, i, 36, 94, 16, 4, 256, 256);
						GuiScreen.drawModalRectWithCustomSizedTexture(j, i, 52, 94, progress, 4, 256, 256);
					}
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderHeldItem(RenderItemEvent.Held.Pre event)
	{
		EnumHand hand = Minecraft.getMinecraft().gameSettings.mainHand == EnumHandSide.RIGHT ? event.getHandSide() == EnumHandSide.RIGHT ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND : event.getHandSide() == EnumHandSide.LEFT ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
		EntityLivingBase entity = event.getEntity();
		ItemStack heldItem = entity.getHeldItem(hand);

		if (heldItem.getItem() instanceof ItemGun)
		{
			event.setCanceled(true);
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			gun.general.gripType.getHeldAnimation().applyHeldItemTransforms(hand, entity instanceof EntityPlayer ? GunHandler.getAimProgress((EntityPlayer) entity, event.getPartialTicks()) : 0f);
			if (hand == EnumHand.MAIN_HAND)
			{
				this.renderWeapon(heldItem, event.getTransformType(), event.getPartialTicks());
			} else
			{
				ItemStack mainHand = entity.getHeldItemMainhand();
				if (mainHand.getItem() instanceof ItemGun)
				{
					Gun mainhandGun = ((ItemGun) mainHand.getItem()).getGun();
					if (mainhandGun.general.gripType.canRenderOffhand())
					{
						this.renderWeapon(heldItem, event.getTransformType(), event.getPartialTicks());
					}
				} else
				{
					this.renderWeapon(heldItem, event.getTransformType(), event.getPartialTicks());
				}
			}
			return;
		}
	}

	@SubscribeEvent
	public void onSetupAngles(ModelPlayerEvent.SetupAngles.Post event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();
		if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
		{
			ModelPlayer model = event.getModelPlayer();
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			gun.general.gripType.getHeldAnimation().applyPlayerModelRotation(model, EnumHand.MAIN_HAND, GunHandler.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialTicks()));
			copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
			copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
			return;
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack heldItem = player.getHeldItemMainhand();
		if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
		{
			Gun gun = ((ItemGun) heldItem.getItem()).getGun();
			gun.general.gripType.getHeldAnimation().applyPlayerPreRender(player, EnumHand.MAIN_HAND, GunHandler.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialRenderTick()));
			return;
		}
	}

	@SubscribeEvent
	public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
	{
		event.setCanceled(this.renderWeapon(event.getItem(), event.getTransformType(), event.getPartialTicks()));
	}

	@SubscribeEvent
	public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
	{
		event.setCanceled(this.renderWeapon(event.getItem(), event.getTransformType(), event.getPartialTicks()));
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
	}

	private boolean renderWeapon(ItemStack stack, ItemCameraTransforms.TransformType transformType, float partialTicks)
	{
		if (stack.getItem() instanceof ItemGun)
		{
			GlStateManager.pushMatrix();
			RenderUtil.applyTransformType(stack, transformType);
			this.renderGun(stack, partialTicks);
			this.renderAttachments(stack);

			if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
			{
				this.renderMuzzleFlash(stack);
			}

			GlStateManager.popMatrix();
			return true;
		}
		return false;
	}

	private void renderGun(ItemStack stack, float partialTicks)
	{
		if (ModelOverrides.hasModel(stack.getItem()))
		{
			IGunModel model = ModelOverrides.getModel(stack.getItem());
			if (model != null)
			{
				model.registerPieces();
				model.render(partialTicks, ItemCameraTransforms.TransformType.NONE, stack);
			}
		} else
		{
			RenderUtil.renderModel(stack);
		}
	}

	private void renderAttachments(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemGun)
		{
			Gun gun = ((ItemGun) stack.getItem()).getGun();
			NBTTagCompound gunTag = ItemStackUtil.createTagCompound(stack);
			NBTTagCompound attachments = gunTag.getCompoundTag("attachments");
			for (String attachmentKey : attachments.getKeySet())
			{
				IAttachment.Type type = IAttachment.Type.getType(attachmentKey);
				if (gun.canAttachType(type))
				{
					ItemStack attachmentStack = Gun.getAttachment(type, stack);
					if (!attachmentStack.isEmpty())
					{
						GlStateManager.pushMatrix();
						{
							Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
							if (positioned != null)
							{
								double displayX = positioned.xOffset * 0.0625;
								double displayY = positioned.yOffset * 0.0625;
								double displayZ = positioned.zOffset * 0.0625;
								GlStateManager.translate(displayX, displayY, displayZ);
								GlStateManager.scale(positioned.scale, positioned.scale, positioned.scale);
								RenderUtil.renderModel(attachmentStack);
							}
						}
						GlStateManager.popMatrix();
					}
				}
			}
		}
	}

	private void renderMuzzleFlash(ItemStack weapon)
	{
		Gun gun = ((ItemGun) weapon.getItem()).getGun();
		if (drawFlash)
		{
			if (flash == null)
				flash = new ItemStack(ModGuns.PARTS, 1, 2);
			IBakedModel flashModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(flash);
			GlStateManager.pushMatrix();
			{
				GlStateManager.disableLighting();
				GlStateManager.translate(gun.display.flash.xOffset * 0.0625, gun.display.flash.yOffset * 0.0625, gun.display.flash.zOffset * 0.0625);

				if (!Gun.getAttachment(IAttachment.Type.BARREL, weapon).isEmpty())
				{
					Gun.ScaledPositioned positioned = gun.getAttachmentPosition(IAttachment.Type.BARREL);
					if (positioned != null)
					{
						GlStateManager.translate(0, 0, (gun.display.flash.zOffset - positioned.zOffset) * 0.0625 - positioned.scale);
						GlStateManager.scale(0.5, 0.5, 0);
					}
				}

				Minecraft.getMinecraft().entityRenderer.disableLightmap();
				RenderUtil.renderModel(flashModel, weapon);
				Minecraft.getMinecraft().entityRenderer.enableLightmap();
				GlStateManager.enableLighting();
			}
			GlStateManager.popMatrix();
			drawFlash = false;
		}
	}

	private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
	{
		dest.rotateAngleX = source.rotateAngleX;
		dest.rotateAngleY = source.rotateAngleY;
		dest.rotateAngleZ = source.rotateAngleZ;
	}

	private void renderArmFirstPerson(float equipProgress, float swingProgress, EnumHandSide hand)
	{
		GlStateManager.pushMatrix();
		boolean flag = hand != EnumHandSide.LEFT;
		float f = flag ? 1.0F : -1.0F;
		float f1 = MathHelper.sqrt(swingProgress);
		float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
		float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
		float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
		GlStateManager.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equipProgress * -0.6F, f4 + -0.71999997F);
		GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
		float f5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
		float f6 = MathHelper.sin(f1 * (float) Math.PI);
		GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
		AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
		Minecraft.getMinecraft().getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
		GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
		GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
		RenderPlayer renderplayer = (RenderPlayer) Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
		GlStateManager.disableCull();

		if (flag)
		{
			renderplayer.renderRightArm(abstractclientplayer);
		} else
		{
			renderplayer.renderLeftArm(abstractclientplayer);
		}

		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}
