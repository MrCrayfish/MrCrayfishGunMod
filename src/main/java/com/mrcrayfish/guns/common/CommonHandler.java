package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class CommonHandler
{
    /**
     * A custom implementation of the cooldown tracker in order to provide the best experience for
     * players. On servers, Minecraft's cooldown tracker is sent to the client but the latency creates
     * an awkward experience as the cooldown applies to the item after the packet has traveled to the
     * server then back to the client. To fix this and still apply security, we just handle the
     * cooldown tracker quietly and not send cooldown packet back to client. The cooldown is still
     * applied on the client in {@link GunItem#onItemRightClick} and {@link GunItem#onUsingTick}.
     */
    private static final Map<UUID, ShootTracker> SHOOT_TRACKER_MAP = new HashMap<>();

    /**
     * Gets the cooldown tracker for the specified player UUID.
     *
     * @param uuid the player's uuid
     * @return a cooldown tracker get
     */
    public static ShootTracker getShootTracker(UUID uuid)
    {
        if(!SHOOT_TRACKER_MAP.containsKey(uuid))
        {
            SHOOT_TRACKER_MAP.put(uuid, new ShootTracker());
        }
        return SHOOT_TRACKER_MAP.get(uuid);
    }
}
