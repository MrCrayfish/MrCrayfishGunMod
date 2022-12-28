package com.tac.guns.client.handler;

import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import com.tac.guns.client.InputHandler;
import com.tac.guns.client.render.crosshair.Crosshair;
import com.tac.guns.common.Rig;
import com.tac.guns.init.ModSyncedDataKeys;
import com.tac.guns.item.ArmorPlateItem;
import com.tac.guns.item.IArmorPlate;
import com.tac.guns.item.TransitionalTypes.wearables.ArmorRigItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageArmorRepair;
import com.tac.guns.util.WearableHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class ArmorInteractionHandler
{
    private static ArmorInteractionHandler instance;

    public static ArmorInteractionHandler get()
    {
        if(instance == null)
        {
            instance = new ArmorInteractionHandler();
        }
        return instance;
    }

    private static final double MAX_AIM_PROGRESS = 4;
    // TODO: Only commented, since we may need to track players per client for future third person animation ... private final Map<PlayerEntity, AimTracker> aimingMap = new WeakHashMap<>();
    private double normalisedRepairProgress;
    private boolean repairing = false;
    private int repairTime = -1;
    private int prevRepairTime = 0;
	private ArmorInteractionHandler()
	{
		InputHandler.ARMOR_REPAIRING.addPressCallback( () -> {
			final Minecraft mc = Minecraft.getInstance();
			if(mc.player != null && WearableHelper.PlayerWornRig(mc.player) != null && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(mc.player))) {
                this.repairing = true;
                this.repairTime = ((ArmorRigItem) WearableHelper.PlayerWornRig(mc.player).getItem()).getRig().getRepair().getTicksToRepair();// Replace with enchantment checker
            }
		} );
	}


    public float getRepairProgress(float partialTicks, PlayerEntity player) {
        return this.repairTime != 0 ? ((this.prevRepairTime + ((this.repairTime - this.prevRepairTime) * partialTicks)) / (float) ((ArmorRigItem)WearableHelper.PlayerWornRig(player).getItem()).getRig().getRepair().getTicksToRepair()) : 1F;
    }

    /*public float getRepairProgress(float partialTicks, ItemStack stack) {
        return this.repairTime != 0 ? (this.repairTime - this.prevRepairTime) * partialTicks : 1F;
    }*/

    /*@SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;
        *//*if(!this.aiming)
            ScopeJitterHandler.getInstance().resetBreathingTickBuffer();*//*
        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if(tracker != null) {
            tracker.handleAiming(player, player.getHeldItem(Hand.MAIN_HAND));
            if (!tracker.isAiming()) {
                this.aimingMap.remove(player);
            }
        }
        if (this.repairing)
            player.setSprinting(false);
    }*/

    /*@Nullable
    private AimTracker getAimTracker(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.QREPAIRING))
        {
            this.aimingMap.put(player, new AimTracker());
        }
    }*/

    /*public float getAimProgress(PlayerEntity player, float partialTicks)
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
    }*/

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;

        this.prevRepairTime = this.repairTime;
        if(InputHandler.ARMOR_REPAIRING.down && this.repairTime > 0)
            this.repairTime--;
        else if (this.repairTime == 0)
        {
            PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(true, true));
            this.repairTime = -1;
            return;
        }

        if(InputHandler.AIM_HOLD.down)
        {
            if(!this.repairing)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.QREPAIRING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(true, false));
                this.repairing = true;
            }
        }
        else if(this.repairing && !InputHandler.AIM_HOLD.down)
        {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.QREPAIRING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageArmorRepair(false, false));
            this.repairing = false;
        }
    }

    /**
     * I think was supposed to be used to replace current crosshair with a repair crosshair, disable for now
     */
    //@SubscribeEvent(receiveCanceled = true)
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        //this.normalisedRepairProgress = this.localTracker.getNormalProgress(event.getPartialTicks());
        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
        if(this.repairing && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS && (crosshair == null || crosshair.isDefault()))
        {
            event.setCanceled(true);
        }
    }

    public boolean isRepairing()
    {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null)
            return false;

        if(mc.player.isSpectator())
            return false;

        if(mc.currentScreen != null)
            return false;

        if(WearableHelper.PlayerWornRig(mc.player) == null)
            return false;
        Rig rig = ((ArmorRigItem)WearableHelper.PlayerWornRig(mc.player).getItem()).getRig();
        return this.repairTime != 0 && mc.player.getHeldItemMainhand().getItem().getRegistryName().equals(rig.getRepair().getItem()) && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(mc.player));
    }

    public double getNormalisedRepairProgress()
    {
        return this.normalisedRepairProgress;
    }


}