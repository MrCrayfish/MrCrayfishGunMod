package com.mrcrayfish.guns.common;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * Author: MrCrayfish
 */
public class BoundingBoxTracker
{
    private static Map<UUID, LinkedList<AxisAlignedBB>> playerBoxes = new HashMap<>();

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
        {
            if(event.player.isSpectator())
            {
                playerBoxes.remove(event.player.getUniqueID());
                return;
            }
            LinkedList<AxisAlignedBB> boxes = playerBoxes.computeIfAbsent(event.player.getUniqueID(), uuid -> new LinkedList<>());
            boxes.addFirst(event.player.getBoundingBox());
            if(boxes.size() > 20)
            {
                boxes.removeLast();
            }
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        playerBoxes.remove(event.getPlayer().getUniqueID());
    }

    public static AxisAlignedBB getBoundingBox(Entity entity, int ping)
    {
        if(playerBoxes.containsKey(entity.getUniqueID()))
        {
            LinkedList<AxisAlignedBB> boxes = playerBoxes.get(entity.getUniqueID());
            int index = MathHelper.clamp(ping, 0, boxes.size() - 1);
            return boxes.get(index);
        }
        return entity.getBoundingBox();
    }
}
