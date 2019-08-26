package com.mrcrayfish.guns.client.event;

import com.mrcrayfish.guns.GunConfig;
import com.mrcrayfish.guns.ItemStackUtil;
import com.mrcrayfish.guns.MrCrayfishGunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.render.gun.IOverrideModel;
import com.mrcrayfish.guns.client.render.gun.ModelOverrides;
import com.mrcrayfish.guns.client.util.RenderUtil;
import com.mrcrayfish.guns.event.CommonEvents;
import com.mrcrayfish.guns.init.ModGuns;
import com.mrcrayfish.guns.init.ModPotions;
import com.mrcrayfish.guns.item.*;
import com.mrcrayfish.guns.object.Bullet;
import com.mrcrayfish.guns.object.GripType;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.*;

public class RenderEvents
{
    private static final ResourceLocation SCOPE_OVERLAY = new ResourceLocation(Reference.MOD_ID, "textures/scope_long_overlay.png");
    private static final ResourceLocation WHITE_GRADIENT = new ResourceLocation(Reference.MOD_ID, "textures/effect/white_gradient.png");
    private static final Map<UUID, CooldownTracker> COOLDOWN_TRACKER_MAP = new HashMap<>();
    private static final double ZOOM_TICKS = 4;
    public static boolean drawFlash = false;
    public static int screenTextureId = -1;
    public static boolean shadersEnabled;

    private int zoomProgress;
    private int lastZoomProgress;
    public double normalZoomProgress;
    public double recoilNormal;
    public double recoilAngle;

    public boolean playAnimation;
    private int startTick;
    private int reloadTimer;
    private int prevReloadTimer;
    private boolean lastSmoothCamera = Minecraft.getMinecraft().gameSettings.smoothCamera;

    private ItemStack flash = null;

    private List<Bullet> bullets = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase == TickEvent.Phase.START && event.player.world.isRemote)
        {
            getCooldownTracker(event.player.getUniqueID()).tick();
        }
    }

    @SubscribeEvent
    public void onKeyPressedEvent(KeyInputEvent event)
    {
        if(Minecraft.getMinecraft().gameSettings.keyBindSmoothCamera.isPressed())
        {
            Minecraft.getMinecraft().gameSettings.smoothCamera = !Minecraft.getMinecraft().gameSettings.smoothCamera;
            lastSmoothCamera = Minecraft.getMinecraft().gameSettings.smoothCamera;
        }
    }

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
                if(isZooming(Minecraft.getMinecraft().player) && !mc.player.getDataManager().get(CommonEvents.RELOADING))
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
                    event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (zoomProgress / (float) ZOOM_TICKS)));
                }
                else
                {
                    mc.gameSettings.smoothCamera = lastSmoothCamera;
                }
            }
            else
            {
                mc.gameSettings.smoothCamera = lastSmoothCamera;
            }
        }
        else
        {
            mc.gameSettings.smoothCamera = lastSmoothCamera;
        }
    }

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        lastZoomProgress = zoomProgress;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(isZooming(player) && !player.getDataManager().get(CommonEvents.RELOADING))
        {
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
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.END)
            return;

        prevReloadTimer = reloadTimer;

        EntityPlayer player = Minecraft.getMinecraft().player;
        if(isZooming(player) && !player.getDataManager().get(CommonEvents.RELOADING))
        {
            Minecraft.getMinecraft().player.prevCameraYaw = 0.0075F;
            Minecraft.getMinecraft().player.cameraYaw = 0.0075F;
        }

        if(player != null)
        {
            ItemStack heldItem = player.getHeldItemMainhand();
            if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
            {
                IOverrideModel model = ModelOverrides.getModel(heldItem);
                if(model != null)
                {
                    model.tick(player);
                }
            }

            if(player.getDataManager().get(CommonEvents.RELOADING))
            {
                if(startTick == -1)
                {
                    startTick = player.ticksExisted + 5;
                }

                if(reloadTimer < 5)
                {
                    reloadTimer++;
                }
            }
            else
            {
                playAnimation = false;

                if(startTick != -1)
                {
                    startTick = -1;
                }

                if(reloadTimer > 0)
                {
                    reloadTimer -= 1;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        normalZoomProgress = (lastZoomProgress + (zoomProgress - lastZoomProgress) * (lastZoomProgress == 0 || lastZoomProgress == ZOOM_TICKS ? 0.0 : event.getPartialTicks())) / ZOOM_TICKS;
        if(normalZoomProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderSpecificHandEvent event)
    {
        boolean right = Minecraft.getMinecraft().gameSettings.mainHand == EnumHandSide.RIGHT ? event.getHand() == EnumHand.MAIN_HAND : event.getHand() == EnumHand.OFF_HAND;
        ItemStack heldItem = event.getItemStack();

        if(event.getHand() == EnumHand.OFF_HAND)
        {
            ItemStack mainHandStack = Minecraft.getMinecraft().player.getHeldItemMainhand();
            if(mainHandStack.getItem() instanceof ItemGun)
            {
                if(((ItemGun) mainHandStack.getItem()).getGun().general.gripType != GripType.ONE_HANDED)
                {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if(!(heldItem.getItem() instanceof ItemGun))
            return;

        // Cancel it because we are doing our own custom render
        event.setCanceled(true);

        // Ignores rendering the gun if the grip type doesn't allow it to be render in the offhand
        if(event.getHand() == EnumHand.OFF_HAND)
        {
            return;
        }

        ItemStack scope = Gun.getScope(heldItem);
        ItemScope.Type scopeType = ItemScope.Type.getFromStack(scope);
        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(heldItem);
        float scaleX = model.getItemCameraTransforms().firstperson_right.scale.getX();
        float scaleY = model.getItemCameraTransforms().firstperson_right.scale.getY();
        float scaleZ = model.getItemCameraTransforms().firstperson_right.scale.getZ();
        float translateY = model.getItemCameraTransforms().firstperson_right.translation.getY() * scaleY;
        float translateZ = model.getItemCameraTransforms().firstperson_right.translation.getZ() * scaleZ;

        GlStateManager.pushMatrix();
        if(normalZoomProgress > 0)
        {
            ItemGun itemGun = (ItemGun) heldItem.getItem();
            if(event.getHand() == EnumHand.MAIN_HAND)
            {
                double xOffset = 0.0;
                double yOffset = 0.0;
                double zOffset = 0.0;

                Gun gun = itemGun.getGun();
                if(gun.canAttachType(IAttachment.Type.SCOPE) && scope != null && scopeType != null)
                {
                    Gun.ScaledPositioned scaledPos = gun.modules.attachments.scope;
                    xOffset -= scaledPos.xOffset * 0.0625 * scaleX;
                    yOffset -= scaledPos.yOffset * 0.0625 * scaleY - translateY + scopeType.getHeightToCenter() * scaleY * 0.0625 * scaledPos.scale;
                    zOffset -= scaledPos.zOffset * 0.0625 * scaleZ - translateZ - 0.35;
                }
                else if(gun.modules.zoom != null)
                {
                    xOffset -= gun.modules.zoom.xOffset * 0.0625 * scaleX;
                    yOffset -= gun.modules.zoom.yOffset * 0.0625 * scaleY - translateY;
                    zOffset -= gun.modules.zoom.zOffset * 0.0625 * scaleZ - translateZ;
                }
                double renderOffset = right ? -0.3415 : -0.3775;
                GlStateManager.translate((renderOffset + xOffset) * normalZoomProgress * (right ? 1F : -1F), yOffset * normalZoomProgress, zOffset * normalZoomProgress);
            }
            else
            {
                GlStateManager.translate(0, -1 * normalZoomProgress, 0);
            }
        }

        GlStateManager.translate(0, -event.getEquipProgress(), 0);

        EnumHandSide hand = right ? EnumHandSide.RIGHT : EnumHandSide.LEFT;

        /* Renders the reload arm. Will only render if actually reloading. This is applied before
         * any recoil or reload rotations as the animations would be borked if applied after. */
        renderReloadArm(heldItem, hand);

        /* Translate the item position based on the hand side */
        GlStateManager.translate(0.56F - (right ? 0.0F : 0.72F), -0.56F, -0.72F);

        /* Applies recoil and reload rotations */
        this.applyRecoil(heldItem.getItem(), ((ItemGun) heldItem.getItem()).getGun());
        this.applyReload(event.getPartialTicks());

        /* Render offhand arm so it is holding the weapon. Only applies if it's a two handed weapon */
        GlStateManager.pushMatrix();
        GlStateManager.translate(-(0.56F - (right ? 0.0F : 0.72F)), 0.56F, 0.72F);
        renderHeldArm(heldItem, hand, event.getPartialTicks());
        GlStateManager.popMatrix();

        /* Renders the weapon */
        this.renderWeapon(Minecraft.getMinecraft().player, heldItem, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, event.getPartialTicks());

        GlStateManager.popMatrix();
    }

    private void applyReload(float partialTicks)
    {
        float reloadProgress = (prevReloadTimer + (reloadTimer - prevReloadTimer) * partialTicks) / 5F;
        GlStateManager.translate(0, 0.35 * reloadProgress, 0);
        GlStateManager.translate(0, 0, -0.1 * reloadProgress);
        GlStateManager.rotate(45F * reloadProgress, 1, 0, 0);
    }

    private void applyRecoil(Item item, Gun gun)
    {
        CooldownTracker tracker = getCooldownTracker(Minecraft.getMinecraft().player.getUniqueID());
        float cooldown = tracker.getCooldown(item, Minecraft.getMinecraft().getRenderPartialTicks());
        cooldown = cooldown >= gun.general.recoilDurationOffset  ? (cooldown - gun.general.recoilDurationOffset) / (1.0F - gun.general.recoilDurationOffset) : 0.0F;

        if(cooldown >= 0.8)
        {
            float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            recoilNormal = 1 - (--amount) * amount * amount * amount;
        }
        else
        {
            float amount = (cooldown / 0.8F);
            recoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
        }

        recoilAngle = gun.general.recoilAngle;

        GlStateManager.translate(0, 0, gun.general.recoilKick * 0.0625 * recoilNormal);
        GlStateManager.translate(0, 0, 0.35);
        GlStateManager.rotate((float) (gun.general.recoilAngle * recoilNormal), 1, 0, 0);
        GlStateManager.translate(0, 0, -0.35);
    }

    private boolean isZooming(EntityPlayer player)
    {
        if(player != null && player.getHeldItemMainhand() != ItemStack.EMPTY)
        {
            ItemStack stack = player.getHeldItemMainhand();
            if(stack.getItem() instanceof ItemGun)
            {
                Gun gun = ((ItemGun) stack.getItem()).getGun();
                return gun.modules != null && gun.modules.zoom != null && MrCrayfishGunMod.proxy.isZooming();
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.RenderTickEvent event)
    {
        if(event.phase.equals(TickEvent.Phase.START))
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if(!mc.inGameHasFocus)
            return;

        EntityPlayer player = mc.player;
        if(player == null)
            return;

        if(Minecraft.getMinecraft().gameSettings.thirdPersonView != 0)
            return;

        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if(heldItem.isEmpty() || !(heldItem.getItem() instanceof ItemGun))
            return;

        ScaledResolution scaledResolution = new ScaledResolution(mc);

        if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
        {
            ItemScope.Type scopeType = ItemScope.Type.getFromStack(Gun.getAttachment(IAttachment.Type.SCOPE, heldItem));
            if(scopeType == ItemScope.Type.LONG && normalZoomProgress == 1.0)
            {
                if(true) return;

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
            if(!gun.general.auto)
            {
                float coolDown = player.getCooldownTracker().getCooldown(heldItem.getItem(), event.renderTickTime);
                if(coolDown > 0.0F)
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

        if(heldItem.getItem() instanceof ItemGun)
        {
            event.setCanceled(true);
            Gun gun = ((ItemGun) heldItem.getItem()).getGun();
            gun.general.gripType.getHeldAnimation().applyHeldItemTransforms(hand, entity instanceof EntityPlayer ? GunHandler.getAimProgress((EntityPlayer) entity, event.getPartialTicks()) : 0f);
            if(hand == EnumHand.MAIN_HAND)
            {
                this.renderWeapon(entity, heldItem, event.getTransformType(), event.getPartialTicks());
            }
        }

        if(hand == EnumHand.OFF_HAND)
        {
            ItemStack mainHandStack = entity.getHeldItemMainhand();
            if(!mainHandStack.isEmpty() && mainHandStack.getItem() instanceof ItemGun)
            {
                Gun mainHandGun = ((ItemGun) mainHandStack.getItem()).getGun();
                if(!mainHandGun.general.gripType.canRenderOffhand())
                {
                    event.setCanceled(true);
                }
                else if(heldItem.getItem() instanceof ItemGun)
                {
                    Gun gun = ((ItemGun) heldItem.getItem()).getGun();
                    if(gun.general.gripType.canRenderOffhand())
                    {
                        this.renderWeapon(entity, heldItem, event.getTransformType(), event.getPartialTicks());
                    }
                }
            }
        }
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
            gun.general.gripType.getHeldAnimation().applyPlayerModelRotation(model, EnumHand.MAIN_HAND, GunHandler.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialTicks()));
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
            gun.general.gripType.getHeldAnimation().applyPlayerPreRender(player, EnumHand.MAIN_HAND, GunHandler.getAimProgress((EntityPlayer) event.getEntity(), event.getPartialRenderTick()));
        }
    }

    @SubscribeEvent
    public void onModelRender(ModelPlayerEvent.Render.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack offHandStack = player.getHeldItemOffhand();
        if(offHandStack.getItem() instanceof ItemGun)
        {
            switch(player.getPrimaryHand().opposite())
            {
                case LEFT:
                    event.getModelPlayer().leftArmPose = ModelBiped.ArmPose.EMPTY;
                    break;
                case RIGHT:
                    event.getModelPlayer().rightArmPose = ModelBiped.ArmPose.EMPTY;
                    break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderPlayer(ModelPlayerEvent.Render.Post event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemOffhand();
        if(!heldItem.isEmpty() && heldItem.getItem() instanceof ItemGun)
        {
            Gun gun = ((ItemGun) heldItem.getItem()).getGun();
            if(!gun.general.gripType.canRenderOffhand())
            {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180F, 0, 1, 0);
                GlStateManager.rotate(180F, 0, 0, 1);
                if(player.isSneaking())
                {
                    GlStateManager.translate(0 * 0.0625, -7 * 0.0625, -5 * 0.0625);
                    GlStateManager.rotate(30F, 1, 0, 0);
                }
                else
                {
                    GlStateManager.translate(0 * 0.0625, -5 * 0.0625, -2.75 * 0.0625);
                }
                GlStateManager.rotate(-45F, 0, 0, 1);
                GlStateManager.scale(0.5, 0.5, 0.5);
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, event.getPartialTicks());
                GlStateManager.popMatrix();
            }
            else
            {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180F, 0, 1, 0);
                GlStateManager.rotate(180F, 0, 0, 1);
                if(player.isSneaking())
                {
                    GlStateManager.translate(-4.5 * 0.0625, -15 * 0.0625, -4 * 0.0625);
                }
                else
                {
                    GlStateManager.translate(-4.5 * 0.0625, -13 * 0.0625, 1 * 0.0625);
                }
                GlStateManager.rotate(90F, 0, 1, 0);
                GlStateManager.rotate(75F, 0, 0, 1);
                GlStateManager.rotate((float) Math.toDegrees(event.getModelPlayer().bipedRightLeg.rotateAngleX) / 10F, 0, 0, 1);
                GlStateManager.scale(0.5, 0.5, 0.5);
                this.renderWeapon(player, heldItem, ItemCameraTransforms.TransformType.FIXED, event.getPartialTicks());
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getPartialTicks()));
    }

    @SubscribeEvent
    public void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        event.setCanceled(this.renderWeapon(mc.player, event.getItem(), event.getTransformType(), event.getPartialTicks()));
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
    }

    private boolean renderWeapon(EntityLivingBase entity, ItemStack stack, ItemCameraTransforms.TransformType transformType, float partialTicks)
    {
        if(stack.getItem() instanceof ItemGun)
        {
            GlStateManager.pushMatrix();

            ItemStack model = ItemStack.EMPTY;
            if(stack.getTagCompound() != null)
            {
                if(stack.getTagCompound().hasKey("model", Constants.NBT.TAG_COMPOUND))
                {
                    model = new ItemStack(stack.getTagCompound().getCompoundTag("model"));
                    model.setTagCompound(stack.getTagCompound().copy());
                }
            }

            RenderUtil.applyTransformType(model.isEmpty() ? stack : model, transformType);
            this.renderGun(entity, transformType, model.isEmpty() ? stack : model, partialTicks);
            this.renderAttachments(entity, transformType, stack, partialTicks);

            if(transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
            {
                this.renderMuzzleFlash(stack);
            }

            GlStateManager.popMatrix();
            return true;
        }
        return false;
    }

    private void renderGun(EntityLivingBase entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, float partialTicks)
    {
        if(ModelOverrides.hasModel(stack))
        {
            IOverrideModel model = ModelOverrides.getModel(stack);
            if(model != null)
            {
                model.render(partialTicks, transformType, stack, ItemStack.EMPTY, entity);
            }
        }
        else
        {
            RenderUtil.renderModel(stack);
        }
    }

    private void renderAttachments(EntityLivingBase entity, ItemCameraTransforms.TransformType transformType, ItemStack stack, float partialTicks)
    {
        if(stack.getItem() instanceof ItemGun)
        {
            Gun gun = ((ItemGun) stack.getItem()).getGun();
            NBTTagCompound gunTag = ItemStackUtil.createTagCompound(stack);
            NBTTagCompound attachments = gunTag.getCompoundTag("attachments");
            for(String attachmentKey : attachments.getKeySet())
            {
                IAttachment.Type type = IAttachment.Type.getType(attachmentKey);
                if(gun.canAttachType(type))
                {
                    ItemStack attachmentStack = Gun.getAttachment(type, stack);
                    if(!attachmentStack.isEmpty())
                    {
                        GlStateManager.pushMatrix();
                        {
                            Gun.ScaledPositioned positioned = gun.getAttachmentPosition(type);
                            if(positioned != null)
                            {
                                double displayX = positioned.xOffset * 0.0625;
                                double displayY = positioned.yOffset * 0.0625;
                                double displayZ = positioned.zOffset * 0.0625;
                                GlStateManager.translate(displayX, displayY, displayZ);
                                GlStateManager.translate(0, -0.5, 0);
                                GlStateManager.scale(positioned.scale, positioned.scale, positioned.scale);

                                IOverrideModel model = ModelOverrides.getModel(attachmentStack);
                                if(model != null)
                                {
                                    model.render(partialTicks, transformType, attachmentStack, stack, entity);
                                }
                                else
                                {
                                    RenderUtil.renderModel(attachmentStack, stack);
                                }
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
        if(drawFlash)
        {
            if(flash == null)
            {
                flash = new ItemStack(ModGuns.PARTS, 1, 2);
            }
            IBakedModel flashModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(flash);
            GlStateManager.pushMatrix();
            {
                GlStateManager.disableLighting();
                GlStateManager.translate(gun.display.flash.xOffset * 0.0625, gun.display.flash.yOffset * 0.0625, gun.display.flash.zOffset * 0.0625);

                if(!Gun.getAttachment(IAttachment.Type.BARREL, weapon).isEmpty())
                {
                    Gun.ScaledPositioned positioned = gun.getAttachmentPosition(IAttachment.Type.BARREL);
                    if(positioned != null)
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

    private void renderHeldArm(ItemStack stack, EnumHandSide hand, float partialTicks)
    {
        GlStateManager.pushMatrix();

        Gun gun = ((ItemGun) stack.getItem()).getModifiedGun(stack);
        if(gun.general.gripType == GripType.TWO_HANDED)
        {
            GlStateManager.translate(0, 0, -1);
            GlStateManager.rotate(180F, 0, 1, 0);

            float reloadProgress = (prevReloadTimer + (reloadTimer - prevReloadTimer) * partialTicks) / 5F;
            GlStateManager.translate(0, -reloadProgress * 2, 0);

            int side = hand.opposite() == EnumHandSide.RIGHT ? 1 : -1;
            GlStateManager.translate(6.5 * side * 0.0625, -0.55, -0.5625);

            if(Minecraft.getMinecraft().player.getSkinType().equals("slim") && hand.opposite() == EnumHandSide.LEFT)
            {
                GlStateManager.translate(0.03125F * -side, 0, 0);
            }

            GlStateManager.rotate(90F, 1, 0, 0);
            GlStateManager.rotate(15F * -side, 0, 1, 0);
            GlStateManager.rotate(15F * -side, 0, 0, 1);
            GlStateManager.rotate(-35F, 1, 0, 0);

            GlStateManager.scale(0.5, 0.5, 0.5);
            this.renderArm(hand.opposite(), 0.0625F);
        }
        else if(gun.general.gripType == GripType.ONE_HANDED)
        {
            GlStateManager.translate(0, 0, -1);
            GlStateManager.rotate(180F, 0, 1, 0);

            double centerOffset = 2.5;
            if(Minecraft.getMinecraft().player.getSkinType().equals("slim"))
            {
                centerOffset += hand == EnumHandSide.RIGHT ? 0.2 : 0.8;
            }
            centerOffset = hand == EnumHandSide.RIGHT ? -centerOffset : centerOffset;
            GlStateManager.translate(centerOffset * 0.0625, -0.45, -1.0);

            GlStateManager.rotate(75F, 1, 0, 0);

            GlStateManager.scale(0.5, 0.5, 0.5);
            this.renderArm(hand, 0.0625F);
        }
        GlStateManager.popMatrix();
    }

    private void renderReloadArm(ItemStack stack, EnumHandSide hand)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if(mc.player.ticksExisted < startTick || reloadTimer != 5)
            return;

        Gun gun = ((ItemGun) stack.getItem()).getModifiedGun(stack);

        ItemAmmo ammo = AmmoRegistry.getInstance().getAmmo(gun.projectile.item);
        if(ammo == null)
            return;

        GlStateManager.pushMatrix();

        float reload = ((mc.player.ticksExisted - startTick + mc.getRenderPartialTicks()) % 10F) / 10F;
        float percent = 1.0F - reload;
        if(percent >= 0.5F)
        {
            percent = 1.0F - percent;
        }
        percent *= 2F;
        percent = percent < 0.5 ? 2 * percent * percent : -1 + (4 - 2 * percent) * percent;

        int side = hand.opposite() == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(-2.75 * side * 0.0625, -0.5625, -0.5625);
        GlStateManager.rotate(180F, 0, 1, 0);
        GlStateManager.translate(0, -0.35 * (1.0 - percent), 0);

        GlStateManager.translate(side * 1 * 0.0625, 0, 0);
        GlStateManager.rotate(90F, 1, 0, 0);
        GlStateManager.rotate(35F * -side, 0, 1, 0);
        GlStateManager.rotate(-75F * percent, 1, 0, 0);

        GlStateManager.scale(0.5, 0.5, 0.5);
        this.renderArm(hand.opposite(), 0.0625F);
        if(reload < 0.5F)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-side * 5 * 0.0625, 15 * 0.0625, -1 * 0.0625);
            GlStateManager.rotate(180F, 1, 0, 0);
            GlStateManager.scale(0.75, 0.75, 0.75);
            RenderUtil.renderModel(new ItemStack(ammo), ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }

    private void renderArm(EnumHandSide hand, float scale)
    {
        AbstractClientPlayer abstractClientPlayer = Minecraft.getMinecraft().player;
        Minecraft.getMinecraft().getTextureManager().bindTexture(abstractClientPlayer.getLocationSkin());
        RenderPlayer renderPlayer = (RenderPlayer) Minecraft.getMinecraft().getRenderManager().<AbstractClientPlayer>getEntityRenderObject(abstractClientPlayer);

        GlStateManager.disableCull();

        if(hand == EnumHandSide.RIGHT)
        {
            renderPlayer.getMainModel().bipedRightArm.rotateAngleX = 0F;
            renderPlayer.getMainModel().bipedRightArm.rotateAngleY = 0F;
            renderPlayer.getMainModel().bipedRightArm.rotateAngleZ = 0F;
            renderPlayer.getMainModel().bipedRightArm.render(scale);
        }
        else
        {
            renderPlayer.getMainModel().bipedLeftArm.rotateAngleX = 0F;
            renderPlayer.getMainModel().bipedLeftArm.rotateAngleY = 0F;
            renderPlayer.getMainModel().bipedLeftArm.rotateAngleZ = 0F;
            renderPlayer.getMainModel().bipedLeftArm.render(scale);
        }

        GlStateManager.enableCull();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void blindPlayer(RenderGameOverlayEvent.Pre event)
    {
        if(event.getType() != ElementType.ALL)
            return;

        PotionEffect effect = Minecraft.getMinecraft().player.getActivePotionEffect(ModPotions.BLINDED);
        if(effect != null)
        {
            // Render white screen-filling overlay at full alpha effect when duration is above threshold
            // When below threshold, fade to full transparency as duration approaches 0
            float percent = Math.min((effect.getDuration() / (float) GunConfig.SERVER.stunGrenades.blind.alphaFadeThresholdSynced), 1);
            Gui.drawRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, ((int) (percent * GunConfig.SERVER.stunGrenades.blind.alphaOverlaySynced + 0.5) << 24) | 16777215);
        }
    }

    @SubscribeEvent
    public void onRenderLastWorld(RenderWorldLastEvent event)
    {
        if(FMLClientHandler.instance().hasOptifine())
        {
            try
            {
                Class<?> clazz = Class.forName("net.optifine.shaders.Shaders");
                if(clazz != null)
                {
                    Field field = clazz.getDeclaredField("activeProgramID");
                    int activeProgramID = (int) field.get(null);
                    shadersEnabled = activeProgramID != 0;
                }
            }
            catch(ClassNotFoundException | NoSuchFieldException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        if(!shadersEnabled)
        {
            if(screenTextureId == -1)
            {
                screenTextureId = GlStateManager.generateTexture();
            }
            Minecraft mc = Minecraft.getMinecraft();
            GlStateManager.bindTexture(screenTextureId);
            GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 0, 0, mc.displayWidth, mc.displayHeight, 0);
        }
        else if(screenTextureId != -1)
        {
            GlStateManager.deleteTexture(screenTextureId);
            screenTextureId = -1;
        }
    }

    public static CooldownTracker getCooldownTracker(UUID uuid)
    {
        if(!COOLDOWN_TRACKER_MAP.containsKey(uuid))
        {
            COOLDOWN_TRACKER_MAP.put(uuid, new CooldownTracker());
        }
        return COOLDOWN_TRACKER_MAP.get(uuid);
    }

    @SubscribeEvent
    public void onRenderBullets(RenderWorldLastEvent event)
    {
        for(Bullet bullet : bullets)
        {
            this.renderBullet(bullet, event.getPartialTicks());
        }
    }

    private void renderBullet(Bullet bullet, float partialTicks)
    {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if(entity == null || bullet.isFinished() || bullet.getProjectile() == null)
            return;

        double doubleX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double doubleY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double doubleZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();

        GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

        double bulletX = bullet.getPosX() + bullet.getMotionX() * partialTicks;
        double bulletY = bullet.getPosY() + bullet.getMotionY() * partialTicks;
        double bulletZ = bullet.getPosZ() + bullet.getMotionZ() * partialTicks;
        GlStateManager.translate(bulletX, bulletY, bulletZ);

        GlStateManager.rotate(bullet.getRotationYaw(), 0, 1, 0);
        GlStateManager.rotate(-bullet.getRotationPitch() + 90, 1, 0, 0);
        GlStateManager.rotate((bullet.getProjectile().ticksExisted + partialTicks) * (float) 50, 0, 1, 0);

        {
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            Minecraft.getMinecraft().getTextureManager().bindTexture(WHITE_GRADIENT);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            Vec3d motionVec = new Vec3d(bullet.getMotionX(), bullet.getMotionY(), bullet.getMotionZ());
            double length = motionVec.length() / 2.0;

            buffer.pos(0, 0, -0.05).tex(0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(0, 0, 0.05).tex(1, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(0, -length, 0.05).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(0, -length, -0.05).tex(0, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();

            buffer.pos(-0.05, 0, 0).tex(0, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(0.05, 0, 0).tex(1, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(0.05, -length, 0).tex(1, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            buffer.pos(-0.05, -length, 0).tex(0, 1).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();

            tessellator.draw();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GlStateManager.popAttrib();
        }

        GlStateManager.scale(0.275, 0.275, 0.275);

        GlStateManager.translate(-0.5, -0.5, -0.5);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(bullet.getProjectile().getItem());
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
        for(EnumFacing enumfacing : EnumFacing.values())
        {
            this.renderQuads(buffer, model.getQuads(null, enumfacing, 0L));
        }
        this.renderQuads(buffer, model.getQuads(null, null, 0L));
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    private void renderQuads(BufferBuilder buffer, List<BakedQuad> quads)
    {
        int i = 0;
        for(int j = quads.size(); i < j; ++i)
        {
            BakedQuad quad = quads.get(i);
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(buffer, quad, -1);
        }
    }

    @SubscribeEvent
    public void onTickBullets(TickEvent.ClientTickEvent event)
    {
        if(event.phase == TickEvent.Phase.END)
        {
            bullets.forEach(bullet -> bullet.tick(Minecraft.getMinecraft().world));
            bullets.removeIf(Bullet::isFinished);
        }
    }

    public void addBullet(Bullet bullet)
    {
        this.bullets.add(bullet);
    }
}
