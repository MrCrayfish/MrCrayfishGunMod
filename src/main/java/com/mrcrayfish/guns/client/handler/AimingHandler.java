package com.mrcrayfish.guns.client.handler;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.client.render.crosshair.Crosshair;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.init.ModBlocks;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.item.attachment.impl.Scope;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.WeakHashMap;

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

    private static final double MAX_AIM_PROGRESS = 5;
    private final AimTracker localTracker = new AimTracker();
    private final Map<PlayerEntity, AimTracker> aimingMap = new WeakHashMap<>();
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
                this.aimingMap.remove(player);
            }
        }
    }

    @Nullable
    private AimTracker getAimTracker(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) && !this.aimingMap.containsKey(player))
        {
            this.aimingMap.put(player, new AimTracker());
        }
        return this.aimingMap.get(player);
    }

    public float getAimProgress(PlayerEntity player, float partialTicks)
    {
        if(player.isUser())
        {
            return (float) this.localTracker.getNormalProgress(partialTicks);
        }

        AimTracker tracker = this.getAimTracker(player);
        if(tracker != null)
        {
            return (float) tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(this.isAiming())
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

        this.localTracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
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
                if(AimingHandler.get().getNormalisedAdsProgress() != 0 && !SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
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
        this.normalisedAdsProgress = this.localTracker.getNormalProgress(event.getPartialTicks());
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if(this.normalisedAdsProgress > 0 && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (crosshair == null || crosshair.isDefault()))
        {
            event.setCanceled(true);
        }
    }

    public boolean isAiming()
    {
        Minecraft mc = Minecraft.getInstance();
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

        if(!this.localTracker.isAiming() && this.isLookingAtInteractableBlock())
            return false;

        if(SyncedPlayerData.instance().get(mc.player, ModSyncedDataKeys.RELOADING))
            return false;

        boolean zooming = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
        if(GunMod.controllableLoaded)
        {
            zooming |= ControllerHandler.isAiming();
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
                // Forge should add a tag for intractable blocks so modders can know which blocks can be interacted with :)
                return block instanceof ContainerBlock || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE || block == ModBlocks.WORKBENCH.get() || BlockTags.DOORS.contains(block) || BlockTags.TRAPDOORS.contains(block) || Tags.Blocks.CHESTS.contains(block) || Tags.Blocks.FENCE_GATES.contains(block);
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

    public class AimTracker
    {
        private double currentAim;
        private double previousAim;

        private void handleAiming(PlayerEntity player, ItemStack heldItem)
        {
            this.previousAim = this.currentAim;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) || (player.isUser() && AimingHandler.this.isAiming()))
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

        public double getNormalProgress(float partialTicks)
        {
            return MathHelper.clamp((this.previousAim + (this.currentAim - this.previousAim) * partialTicks) / MAX_AIM_PROGRESS, 0.0, 1.0);
        }
    }
}
