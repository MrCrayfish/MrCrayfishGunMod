package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.headshot.BasicHeadshotBox;
import com.mrcrayfish.guns.common.headshot.ChildHeadshotBox;
import com.mrcrayfish.guns.common.headshot.NoChildHeadshotBox;
import com.mrcrayfish.guns.common.headshot.NoChildRotatedHeadshotBox;
import com.mrcrayfish.guns.common.headshot.RotatedHeadshotBox;
import com.mrcrayfish.guns.interfaces.IHeadshotBox;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Author: MrCrayfish
 */
public class BoundingBoxManager
{
    private static Map<EntityType<?>, IHeadshotBox<?>> headshotBoxes = new HashMap<>();
    private static WeakHashMap<Player, LinkedList<AABB>> playerBoxes = new WeakHashMap<>();

    static
    {
        /* Player */
        registerHeadshotBox(EntityType.PLAYER, (entity) -> {
            AABB headBox = new AABB(-4 * 0.0625, 0, -4 * 0.0625, 4 * 0.0625, 8 * 0.0625, 4 * 0.0625);
            double scale = 30.0 / 32.0;
            if(entity.isSwimming())
            {
                headBox = headBox.move(0, 3 * 0.0625, 0);
                Vec3 pos = Vec3.directionFromRotation(entity.getXRot(), entity.yBodyRot).normalize().scale(0.8);
                headBox = headBox.move(pos);
            }
            else
            {
                headBox = headBox.move(0, entity.isShiftKeyDown() ? 20 * 0.0625 : 24 * 0.0625, 0);
            }
            return new AABB(headBox.minX * scale, headBox.minY * scale, headBox.minZ * scale, headBox.maxX * scale, headBox.maxY * scale, headBox.maxZ * scale);
        });

        registerHeadshotBox(EntityType.ZOMBIE, new ChildHeadshotBox<>(8.0, 24.0, 0.75, 0.5));
        registerHeadshotBox(EntityType.ZOMBIFIED_PIGLIN, new ChildHeadshotBox<>(8.0, 24.0, 0.75, 0.5));
        registerHeadshotBox(EntityType.HUSK, new ChildHeadshotBox<>(8.0, 24.0, 0.75, 0.5));
        registerHeadshotBox(EntityType.SKELETON, new BasicHeadshotBox<>(8.0, 24.0));
        registerHeadshotBox(EntityType.STRAY, new BasicHeadshotBox<>(8.0, 24.0));
        registerHeadshotBox(EntityType.CREEPER, new BasicHeadshotBox<>(8.0, 18.0));
        registerHeadshotBox(EntityType.SPIDER, new RotatedHeadshotBox<>(8.0, 5.0, 7.0, false, true));
        registerHeadshotBox(EntityType.DROWNED, new BasicHeadshotBox<>(8.0, 24.0));
        registerHeadshotBox(EntityType.VILLAGER, new NoChildHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.ZOMBIE_VILLAGER, new NoChildHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.VINDICATOR, new NoChildHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.EVOKER, new BasicHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.PILLAGER, new BasicHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.ILLUSIONER, new BasicHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.WANDERING_TRADER, new BasicHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.WITCH, new BasicHeadshotBox<>(8.0, 9.0, 23.0));
        registerHeadshotBox(EntityType.SHEEP, new RotatedHeadshotBox<>(7.5, 8.0, 15.0, 9.5, false, true));
        registerHeadshotBox(EntityType.CHICKEN, new NoChildRotatedHeadshotBox<>(4.0, 6.0, 9.0, 5.0, false, true));
        registerHeadshotBox(EntityType.COW, new NoChildRotatedHeadshotBox<>(7.5, 8.0, 16.0, 10.5, false, true));
        registerHeadshotBox(EntityType.MOOSHROOM, new NoChildRotatedHeadshotBox<>(7.5, 8.0, 16.0, 10.5, false, true));
        registerHeadshotBox(EntityType.PIG, new NoChildRotatedHeadshotBox<>(8.0, 8.0, 10, false, true));
        registerHeadshotBox(EntityType.HORSE, new RotatedHeadshotBox<>(10.0, 26.0, 16.0, false, true));
        registerHeadshotBox(EntityType.SKELETON_HORSE, new RotatedHeadshotBox<>(10.0, 26.0, 16.0, false, true));
        registerHeadshotBox(EntityType.DONKEY, new RotatedHeadshotBox<>(7.5, 8.0, 20.0, 13.0, false, true));
        registerHeadshotBox(EntityType.MULE, new RotatedHeadshotBox<>(7.5, 8.0, 21.0, 14.0, false, true));
        registerHeadshotBox(EntityType.LLAMA, new RotatedHeadshotBox<>(8.0, 26.0, 10.0, false, true));
        registerHeadshotBox(EntityType.TRADER_LLAMA, new RotatedHeadshotBox<>(8.0, 26.0, 10.0, false, true));
        registerHeadshotBox(EntityType.POLAR_BEAR, new RotatedHeadshotBox<>(9.0, 12.0, 20.0, false, true));
        registerHeadshotBox(EntityType.SNOW_GOLEM, new BasicHeadshotBox<>(10.0, 20.5));
        registerHeadshotBox(EntityType.TURTLE, new RotatedHeadshotBox<>(6.0, 5.0, 1.0, 10.0, false, true));
        registerHeadshotBox(EntityType.IRON_GOLEM, new RotatedHeadshotBox<>(8.0, 10.0, 33.0, 3.5, false, true));
        registerHeadshotBox(EntityType.PHANTOM, new RotatedHeadshotBox<>(6.0, 3.0, 1.5, 6.5, true, true));
        registerHeadshotBox(EntityType.HOGLIN, new RotatedHeadshotBox<>(14.0, 16.0, 7.0, 19.0, false, true));
        registerHeadshotBox(EntityType.ZOGLIN, new RotatedHeadshotBox<>(14.0, 16.0, 7.0, 19.0, false, true));
        registerHeadshotBox(EntityType.PIGLIN, new ChildHeadshotBox<>(8.0, 24.0, 0.75, 0.5));
    }

    /**
     * Registers a headshot box for the specified entity type.
     *
     * @param type        the entity type
     * @param headshotBox a {@link IHeadshotBox} get
     * @param <T>         a type that extends {@link LivingEntity}
     */
    public static <T extends LivingEntity> void registerHeadshotBox(EntityType<T> type, IHeadshotBox<T> headshotBox)
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
        if(!Config.COMMON.gameplay.improvedHitboxes.get())
            return;

        if(event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END)
        {
            if(event.player.isSpectator())
            {
                playerBoxes.remove(event.player);
                return;
            }
            LinkedList<AABB> boxes = playerBoxes.computeIfAbsent(event.player, player -> new LinkedList<>());
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
        playerBoxes.remove(event.getPlayer());
    }

    public static AABB getBoundingBox(Player entity, int ping)
    {
        if(playerBoxes.containsKey(entity))
        {
            LinkedList<AABB> boxes = playerBoxes.get(entity);
            int index = Mth.clamp(ping, 0, boxes.size() - 1);
            return boxes.get(index);
        }
        return entity.getBoundingBox();
    }
}
