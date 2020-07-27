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

import javax.annotation.Nullable;
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
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            if(entity.isChild())
            {
                return new AxisAlignedBB(headBox.minX * 0.75, headBox.minY * 0.5, headBox.minZ * 0.75, headBox.maxX * 0.75, headBox.maxY * 0.55, headBox.maxZ * 0.75);
            }
            return headBox;
        });

        registerHeadshotBox(EntityType.ZOMBIE_PIGMAN, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            if(entity.isChild())
            {
                return new AxisAlignedBB(headBox.minX * 0.75, headBox.minY * 0.5, headBox.minZ * 0.75, headBox.maxX * 0.75, headBox.maxY * 0.55, headBox.maxZ * 0.75);
            }
            return headBox;
        });

        /* Husk */
        registerHeadshotBox(EntityType.HUSK, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            if(entity.isChild())
            {
                return new AxisAlignedBB(headBox.minX * 0.75, headBox.minY * 0.5, headBox.minZ * 0.75, headBox.maxX * 0.75, headBox.maxY * 0.55, headBox.maxZ * 0.75);
            }
            return headBox;
        });

        /* Skeleton */
        registerHeadshotBox(EntityType.SKELETON, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            return headBox;
        });

        /* Skeleton */
        registerHeadshotBox(EntityType.STRAY, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            return headBox;
        });

        /* Creeper */
        registerHeadshotBox(EntityType.CREEPER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 18 * 0.0625, 0);
            return headBox;
        });

        /* Spider */
        registerHeadshotBox(EntityType.SPIDER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 5 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(7 * 0.0625));
            return headBox;
        });

        /* Drowned */
        registerHeadshotBox(EntityType.DROWNED, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 24 * 0.0625, 0);
            return headBox;
        });

        /* Villager */
        registerHeadshotBox(EntityType.VILLAGER, (entity) -> {
            if(entity.isChild()) return null;
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Zombie Villager */
        registerHeadshotBox(EntityType.ZOMBIE_VILLAGER, (entity) -> {
            if(entity.isChild()) return null;
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Vindicator */
        registerHeadshotBox(EntityType.VINDICATOR, (entity) -> {
            if(entity.isChild()) return null;
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Evoker */
        registerHeadshotBox(EntityType.EVOKER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Pillager */
        registerHeadshotBox(EntityType.PILLAGER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Illusioner */
        registerHeadshotBox(EntityType.ILLUSIONER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Wandering Trader */
        registerHeadshotBox(EntityType.WANDERING_TRADER, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Witch */
        registerHeadshotBox(EntityType.WITCH, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 9 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 23 * 0.0625, 0);
            return headBox;
        });

        /* Sheep */
        registerHeadshotBox(EntityType.SHEEP, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3.75 * 0.0625, 0, -3.75 * 0.0625, 3.75 * 0.0625, 8 * 0.0625, 3.75 * 0.0625);
            headBox = headBox.offset(0, 15 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(9.5 * 0.0625));
            return headBox;
        });

        /* Chicken */
        registerHeadshotBox(EntityType.CHICKEN, (entity) -> {
            if(entity.isChild()) return null;
            AxisAlignedBB headBox = new AxisAlignedBB(-2 * 0.0625, 0, -2 * 0.0625, 2 * 0.0625, 6 * 0.0625, 2 * 0.0625);
            headBox = headBox.offset(0, 9 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(5 * 0.0625));
            return headBox;
        });

        /* Cow */
        registerHeadshotBox(EntityType.COW, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3.75 * 0.0625, 0, -3.75 * 0.0625, 3.75 * 0.0625, 8 * 0.0625, 3.75 * 0.0625);
            headBox = headBox.offset(0, 16 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10.5 * 0.0625));
            return headBox;
        });

        /* Mooshroom */
        registerHeadshotBox(EntityType.MOOSHROOM, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3.75 * 0.0625, 0, -3.75 * 0.0625, 3.75 * 0.0625, 8 * 0.0625, 3.75 * 0.0625);
            headBox = headBox.offset(0, 16 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10.5 * 0.0625));
            return headBox;
        });

        /* Pig */
        registerHeadshotBox(EntityType.PIG, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 8 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10 * 0.0625));
            return headBox;
        });

        /* Horse */
        registerHeadshotBox(EntityType.HORSE, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-5 * 0.0625, 0, -5 * 0.0625, 5 * 0.0625, 10 * 0.0625, 5 * 0.0625);
            headBox = headBox.offset(0, 26 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(16 * 0.0625));
            return headBox;
        });

        /* Skeleton Horse */
        registerHeadshotBox(EntityType.SKELETON_HORSE, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-5 * 0.0625, 0, -5 * 0.0625, 5 * 0.0625, 10 * 0.0625, 5 * 0.0625);
            headBox = headBox.offset(0, 26 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(16 * 0.0625));
            return headBox;
        });

        /* Donkey */
        registerHeadshotBox(EntityType.DONKEY, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3.75 * 0.0625, 0, -3.75 * 0.0625, 3.75 * 0.0625, 8 * 0.0625, 3.75 * 0.0625);
            headBox = headBox.offset(0, 20 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(13 * 0.0625));
            return headBox;
        });

        /* Mule */
        registerHeadshotBox(EntityType.MULE, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3.75 * 0.0625, 0, -3.75 * 0.0625, 3.75 * 0.0625, 8 * 0.0625, 3.75 * 0.0625);
            headBox = headBox.offset(0, 21 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(14 * 0.0625));
            return headBox;
        });

        /* Llama */
        registerHeadshotBox(EntityType.LLAMA, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 26 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10 * 0.0625));
            return headBox;
        });

        /* Trader Llama */
        registerHeadshotBox(EntityType.TRADER_LLAMA, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 26 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10 * 0.0625));
            return headBox;
        });

        /* Polar Bear */
        registerHeadshotBox(EntityType.POLAR_BEAR, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4.5 * 0.0625, 0, -4.5 * 0.0625, 4.5 * 0.0625, 9 * 0.0625, 4.5 * 0.0625);
            headBox = headBox.offset(0, 12 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(20 * 0.0625));
            return headBox;
        });

        /* Snow Golem */
        registerHeadshotBox(EntityType.SNOW_GOLEM, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-5 * 0.0625, 0, -5 * 0.0625, 5 * 0.0625, 10 * 0.0625, 5 * 0.0625);
            headBox = headBox.offset(0, 20.5 * 0.0625, 0);
            return headBox;
        });

        /* Turtle */
        registerHeadshotBox(EntityType.TURTLE, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-3 * 0.0625, 0, -3 * 0.0625, 3 * 0.0625, 5 * 0.0625, 3 * 0.0625);
            headBox = headBox.offset(0, 1 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(10 * 0.0625));
            return headBox;
        });

        /* Iron Golem */
        registerHeadshotBox(EntityType.IRON_GOLEM, (entity) -> {
            AxisAlignedBB headBox = new AxisAlignedBB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 10 * 0.0625, 4 * 0.0625);
            headBox = headBox.offset(0, 33 * 0.0625, 0);
            headBox = headBox.offset(Vec3d.fromPitchYaw(0.0F, entity.renderYawOffset).normalize().scale(3.5 * 0.0625));
            return headBox;
        });
    }

    public static <T extends Entity> void registerHeadshotBox(EntityType<T> type, IHeadshotBox<T> headshotBox)
    {
        headshotBoxes.putIfAbsent(type, headshotBox);
    }

    @Nullable
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
