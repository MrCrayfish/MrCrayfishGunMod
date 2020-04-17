package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.GunMod;
import com.mrcrayfish.guns.common.CommonEvents;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class AimTracker
{
    private static final Map<UUID, AimTracker> AIMING_MAP = new HashMap<>();

    private static boolean aiming = false;
    private static final double MAX_AIM = 8;

    private int currentAim;
    private int previousAim;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = event.player;
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            tracker.handleAiming(player);
            if(!tracker.isAiming())
            {
                AIMING_MAP.remove(player.getUniqueID());
            }
        }
    }

    @Nullable
    private static AimTracker getAimTracker(PlayerEntity player)
    {
        if(player.getDataManager().get(CommonEvents.AIMING) && !AIMING_MAP.containsKey(player.getUniqueID()))
        {
            AIMING_MAP.put(player.getUniqueID(), new AimTracker());
        }
        return AIMING_MAP.get(player.getUniqueID());
    }

    public static float getAimProgress(PlayerEntity player, float partialTicks)
    {
        AimTracker tracker = getAimTracker(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    public void handleAiming(PlayerEntity player)
    {
        this.previousAim = this.currentAim;
        if(player.getDataManager().get(CommonEvents.AIMING))
        {
            if(this.currentAim < MAX_AIM)
            {
                this.currentAim++;
            }
        }
        else
        {
            if(this.currentAim > 0)
            {
                this.currentAim--;
            }
        }
    }

    public boolean isAiming()
    {
        return this.currentAim != 0 || this.previousAim != 0;
    }

    public float getNormalProgress(float partialTicks)
    {
        return (this.previousAim + (this.currentAim - this.previousAim) * (this.previousAim == 0 || this.previousAim == MAX_AIM ? 0 : partialTicks)) / (float) MAX_AIM;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
            return;

        PlayerEntity player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if(GunMod.PROXY.isZooming())
        {
            if(!aiming)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);
                PacketHandler.INSTANCE.sendToServer(new MessageAim(true));
                aiming = true;
            }
        }
        else if(aiming)
        {
            Minecraft.getInstance().player.getDataManager().set(CommonEvents.AIMING, false);
            PacketHandler.INSTANCE.sendToServer(new MessageAim(false));
            aiming = false;
        }
    }
}
