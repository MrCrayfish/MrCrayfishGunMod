package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

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
        if(!event.player.world.isRemote)
            return;

        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
        {
            TRACKER_MAP.remove(event.player.getUniqueID());
        });
    }
}
