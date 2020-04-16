package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class SpreadHandler
{
    private static final Map<UUID, SpreadTracker> TRACKER_MAP = new HashMap<>();

    public static SpreadTracker getSpreadTracker(UUID uuid)
    {
        return TRACKER_MAP.computeIfAbsent(uuid, uuid1 -> new SpreadTracker());
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if(!event.getPlayer().world.isRemote) //TODO probably dont need this
            return;

        MinecraftServer server = event.getPlayer().getServer();
        if(server != null)
        {
            server.execute(() -> TRACKER_MAP.remove(event.getPlayer().getUniqueID()));
        }
    }
}
