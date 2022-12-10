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
		InputHandler.ARMOR_REPAIRING.addPressCallBack( () -> {
			final Minecraft mc = Minecraft.getInstance();
			if(
				mc.player != null
                        && WearableHelper.PlayerWornRig(mc.player) != null
				 && !WearableHelper.isFullDurability(WearableHelper.PlayerWornRig(mc.player))
			) this.repairing = true; this.repairTime = ((ArmorRigItem)WearableHelper.PlayerWornRig(mc.player).getItem()).getRig().getRepair().getTicksToRepair();// Replace with enchantment checker
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

    /*@SubscribeEvent
    public void onClientTick(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        this.aimingMap.clear();
    }*/

    /**
     * Prevents the crosshair from rendering when aiming down sight
     */
    @SubscribeEvent(receiveCanceled = true)
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


    // TODO: Use for interacting with upgrade or repair station of some sort for extra functionality
    /*public boolean isLookingAtInteractableBlock()
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
                *//*if(block == ModBlocks.UPGRADE_BENCH.get())
                    return false;*//*
                return block instanceof ContainerBlock || block.hasTileEntity(state) || block == Blocks.CRAFTING_TABLE || block == ModBlocks.WORKBENCH.get() || *//* ||*//* BlockTags.DOORS.contains(block) || BlockTags.TRAPDOORS.contains(block) || Tags.Blocks.CHESTS.contains(block) || Tags.Blocks.FENCE_GATES.contains(block);
            }
            else if(mc.objectMouseOver instanceof EntityRayTraceResult)
            {
                EntityRayTraceResult result = (EntityRayTraceResult) mc.objectMouseOver;
                return result.getEntity() instanceof ItemFrameEntity;
            }
        }
        return false;
    }*/

    public double getNormalisedRepairProgress()
    {
        return this.normalisedRepairProgress;
    }

    // TODO: Just remove? Tracking can be made simply for armor quick repair
    /*public class AimTracker
    {
        private double currentAim;
        private double previousAim;
        private double amplifier = 0.8;

        private void handleAiming(PlayerEntity player, ItemStack heldItem)
        {
            this.previousAim = this.currentAim;
            double vAmplifier = 0.1;
            if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) || (player.isUser() && ArmorInteractionHandler.this.isAiming()))
            {
                if(this.amplifier < 1.3)
                {
                    amplifier += vAmplifier;
                }
                if(this.currentAim < MAX_AIM_PROGRESS)
                {
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim += speed * amplifier;
                    if(this.currentAim > MAX_AIM_PROGRESS)
                    {
                        amplifier = 0.5;
                        this.currentAim = (int) MAX_AIM_PROGRESS;
                    }
                }
            }
            else
            {
                if(this.currentAim > 0)
                {
                    if(this.amplifier < 1.3)
                    {
                        amplifier += vAmplifier;
                    }
                    double speed = GunEnchantmentHelper.getAimDownSightSpeed(heldItem);
                    speed = GunModifierHelper.getModifiedAimDownSightSpeed(heldItem, speed);
                    this.currentAim -= speed * amplifier;
                    if(this.currentAim < 0)
                    {
                        amplifier = 0.5;
                        this.currentAim = 0;
                    }
                }else amplifier = 0.8;
            }
        }

        public boolean isAiming()
        {
            return this.currentAim != 0 || this.previousAim != 0;
        }

        public double getNormalProgress(float partialTicks)
        {
            return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM_PROGRESS ? 0 : partialTicks)) / (float) MAX_AIM_PROGRESS;
        }
    }*/
    //public double getNormalProgress(float partialTicks)
    //{
    //    return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM_PROGRESS ? 0 : partialTicks)) / (float) MAX_AIM_PROGRESS;
    //}
}