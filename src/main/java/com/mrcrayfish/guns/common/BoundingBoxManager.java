package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.interfaces.IHeadshotBox;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
public class BoundingBoxManager
{
    private static Map<EntityType<?>, IHeadshotBox<?>> headshotBoxes = new HashMap<>();
    private static Map<UUID, LinkedList<AxisAlignedBB>> playerBoxes = new HashMap<>();

    static
    {
        /* Player */
        registerHeadshotBox(EntityType.PLAYER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            double scale = 30.0 / 32.0;
            if(entity.isSwimming())
            {
                Vec3d pos = Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(0.8);
                headBox = headBox.offset(pos);
            }
            else
            {
                headBox = headBox.offset(0, entity.isSneaking() ? 20 * 0.0625 : 24 * 0.0625, 0);
            }
            return new AxisAlignedBB(headBox.minX * scale, headBox.minY * scale, headBox.minZ * scale, headBox.maxX * scale, headBox.maxY * scale, headBox.maxZ * scale);
        });

        /* Zombie */
        registerHeadshotBox(EntityType.ZOMBIE, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, entity.isSneaking() ? 20 * 0.0625 : 24 * 0.0625, 0);
            if(entity.isChild())
            {
                return new AxisAlignedBB(headBox.minX * 0.75, headBox.minY * 0.5, headBox.minZ * 0.75, headBox.maxX * 0.75, headBox.maxY * 0.55, headBox.maxZ * 0.75);
            }
            return headBox;
        });

        /* Skeleton */
        registerHeadshotBox(EntityType.SKELETON, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, entity.isSneaking() ? 20 * 0.0625 : 24 * 0.0625, 0);
            return headBox;
        });
    }

    public static <T extends Entity> void registerHeadshotBox(EntityType<T> type, IHeadshotBox<T> headshotBox)
    {
        headshotBoxes.putIfAbsent(type, headshotBox);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> IHeadshotBox<T> getHeadshotBoxes(EntityType<T> type)
    {
        return (IHeadshotBox<T>) headshotBoxes.get(type);
    }

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
