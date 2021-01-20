package com.mrcrayfish.guns.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.client.ControllerType;
import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.client.Crosshair;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class AimingHandler
{
    private static AimingHandler instance;

    public static AimingHandler get()
    {
        if(instance == null)
        {
            instance = new AimingHandler();
        }
        return instance;
    }

    private static final double MAX_AIM_PROGRESS = 4;
    private final Map<UUID, AimTracker> aimingMap = new HashMap<>();
    private double adsProgress;
    private double lastAdsProgress;
    private double normalisedAdsProgress;
    private boolean aiming = false;

    private AimingHandler() {}

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            tracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
            if(!tracker.isAiming())
            {
                this.aimingMap.remove(player.getUniqueID());
            }
        }
    }

    @Nullable
    private AimTracker getAimTracker(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) && !this.aimingMap.containsKey(player.getUniqueID()))
        {
            this.aimingMap.put(player.getUniqueID(), new AimTracker());
        }
        return this.aimingMap.get(player.getUniqueID());
    }

    public float getAimProgress(PlayerEntity player, float partialTicks)
    {
        AimTracker tracker = this.getAimTracker(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        updateAimProgress();

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(isAiming())
        {
            if(!this.aiming)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageAim(true));
                this.aiming = true;
            }
        }
        else if(this.aiming)
        {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageAim(false));
            this.aiming = false;
        }
    }

    @SubscribeEvent
    public void onFovUpdate(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null && !mc.player.getHeldItemMainhand().isEmpty() && mc.gameSettings.getPointOfView() == PointOfView.FIRST_PERSON)
        {
            ItemStack heldItem = mc.player.getHeldItemMainhand();
            if(heldItem.getItem() instanceof GunItem)
            {
                GunItem gunItem = (GunItem) heldItem.getItem();
                if(AimingHandler.get().isAiming() && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
                {
                    Gun modifiedGun = gunItem.getModifiedGun(heldItem);
                    if(modifiedGun.getModules().getZoom() != null)
                    {
                        float newFov = modifiedGun.getModules().getZoom().getFovModifier();
                        Scope scope = Gun.getScope(heldItem);
                        if(scope != null)
                        {
                            newFov -= scope.getAdditionalZoom();
                        }
                        event.setNewfov(newFov + (1.0F - newFov) * (1.0F - (float) this.normalisedAdsProgress));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.aimingMap.clear();
    }

    /**
     * Prevents the crosshair from rendering when aiming down sight
     */
    @SubscribeEvent(receiveCanceled = true)
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        this.normalisedAdsProgress = (this.lastAdsProgress + (this.adsProgress - this.lastAdsProgress) * (this.lastAdsProgress == 0 || this.lastAdsProgress == MAX_AIM_PROGRESS ? 0.0 : event.getPartialTicks())) / MAX_AIM_PROGRESS;
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if(this.normalisedAdsProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (crosshair == null || crosshair.isDefault()))
        {
            event.setCanceled(true);
        }
    }

    private void updateAimProgress()
    {
        this.lastAdsProgress = this.adsProgress;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(isAiming() && !SyncedPlayerData.instance().get(player, ModSyncedDataKeys.RELOADING))
        {
            if(this.adsProgress < MAX_AIM_PROGRESS)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.adsProgress += speed;
                if(this.adsProgress > MAX_AIM_PROGRESS)
                {
                    this.adsProgress = (int) MAX_AIM_PROGRESS;
                }
            }
        }
        else
        {
            if(this.adsProgress > 0)
            {
                ItemStack weapon = player.getHeldItem(Hand.MAIN_HAND);
                double speed = GunEnchantmentHelper.getAimDownSightSpeed(weapon);
                speed = GunModifierHelper.getModifiedAimDownSightSpeed(weapon, speed);
                this.adsProgress -= speed;
                if(this.adsProgress < 0)
                {
                    this.adsProgress = 0;
                }
            }
        }
    }

    public boolean isAiming()
    {
        Minecraft mc = Minecraft.getInstance();
        if(!mc.isGameFocused())
            return false;

        if(mc.player == null)
            return false;

        if(mc.player.isSpectator())
            return false;

        if(mc.currentScreen != null)
            return false;

        ItemStack heldItem = mc.player.getHeldItemMainhand();
        if(!(heldItem.getItem() instanceof GunItem))
            return false;

        Gun gun = ((GunItem) heldItem.getItem()).getModifiedGun(heldItem);
        if(gun.getModules().getZoom() == null)
            return false;

        if(this.adsProgress == 0 && isLookingAtInteractableBlock())
            return false;

        boolean zooming = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if(GunMod.controllableLoaded)
        {
            Controller controller = Controllable.getController();
            if(controller != null)
            {
                zooming |= controller.getLTriggerValue() >= 0.5;
            }
        }

        return zooming;
    }

    public boolean isLookingAtInteractableBlock()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.objectMouseOver != null && mc.world != null)
        {
            if(mc.objectMouseOver instanceof BlockRayTraceResult)
            {
                BlockRayTraceResult result = (BlockRayTraceResult) mc.objectMouseOver;
                BlockState state = mc.world.getBlockState(result.getPos());
                Block block = state.getBlock();
                return block instanceof ContainerBlock || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE || block == ModBlocks.WORKBENCH.get();
            }
            else if(mc.objectMouseOver instanceof EntityRayTraceResult)
            {
                EntityRayTraceResult result = (EntityRayTraceResult) mc.objectMouseOver;
                return result.getEntity() instanceof ItemFrameEntity;
            }
        }
        return false;
    }

    public double getNormalisedAdsProgress()
    {
        return this.normalisedAdsProgress;
    }

    public static class AimTracker
    {
        private int currentAim;
        private int previousAim;

        private void handleAiming(PlayerEntity player, ItemStack heldItem)
        {
            this.previousAim = this.currentAim;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING))
            {
                if(this.currentAim < MAX_AIM_PROGRESS)
                {
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim += speed;
                    if(this.currentAim > MAX_AIM_PROGRESS)
                    {
                        this.currentAim = (int) MAX_AIM_PROGRESS;
                    }
                }
            }
            else
            {
                if(this.currentAim > 0)
                {
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim -= speed;
                    if(this.currentAim < 0)
                    {
                        this.currentAim = 0;
                    }
                }
            }
        }

        public boolean isAiming()
        {
            return this.currentAim != 0 || this.previousAim != 0;
        }

        public float getNormalProgress(float partialTicks)
        {
            return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM_PROGRESS ? 0 : partialTicks)) / (float) MAX_AIM_PROGRESS;
        }
    }
}
