package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.init.ModSyncedDataKeys;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.MessageAim;
import com.mrcrayfish.obfuscate.common.data.SyncedPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
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
        AimTracker tracker = get(player);
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
    private static AimTracker get(PlayerEntity player)
    {
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING) && !AIMING_MAP.containsKey(player.getUniqueID()))
        {
            AIMING_MAP.put(player.getUniqueID(), new AimTracker());
        }
        return AIMING_MAP.get(player.getUniqueID());
    }

    public static float getAimProgress(PlayerEntity player, float partialTicks)
    {
        AimTracker tracker = get(player);
        if(tracker != null)
        {
            return tracker.getNormalProgress(partialTicks);
        }
        return 0F;
    }

    private void handleAiming(PlayerEntity player)
    {
        this.previousAim = this.currentAim;
        if(SyncedPlayerData.instance().get(player, ModSyncedDataKeys.AIMING))
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

        if(ClientHandler.isAiming())
        {
            if(!aiming)
            {
                SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, true);
                PacketHandler.getPlayChannel().sendToServer(new MessageAim(true));
                aiming = true;
            }
        }
        else if(aiming)
        {
            SyncedPlayerData.instance().set(player, ModSyncedDataKeys.AIMING, false);
            PacketHandler.getPlayChannel().sendToServer(new MessageAim(false));
            aiming = false;
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientPlayerNetworkEvent.LoggedOutEvent event)
    {
        AIMING_MAP.clear();
    }
}
