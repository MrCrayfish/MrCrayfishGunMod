package com.mrcrayfish.guns.common;

import com.google.common.collect.Maps;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.Util;
import net.minecraft.world.item.context.UseOnContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A simple class to track and control weapon cooldowns
 * <p>
 * Author: MrCrayfish
 */
public class ShootTracker
{
    /**
     * A custom implementation of the cooldown tracker in order to provide the best experience for
     * players. On servers, Minecraft's cooldown tracker is sent to the client but the latency creates
     * an awkward experience as the cooldown applies to the item after the packet has traveled to the
     * server then back to the client. To fix this and still apply security, we just handle the
     * cooldown tracker quietly and not send cooldown packet back to client. The cooldown is still
     * applied on the client in {@link GunItem#onItemUseFirst(ItemStack, UseOnContext)} and {@link GunItem#onUsingTick}.
     */
    private static final Map<Player, ShootTracker> SHOOT_TRACKER_MAP = new WeakHashMap<>();

    private final Map<Item, Pair<Long, Integer>> cooldownMap = Maps.newHashMap();

    /**
     * Gets the cooldown tracker for the specified player UUID.
     *
     * @param player the player instance
     * @return a cooldown tracker get
     */
    public static ShootTracker getShootTracker(Player player)
    {
        return SHOOT_TRACKER_MAP.computeIfAbsent(player, player1 -> new ShootTracker());
    }

    /**
     * Puts a cooldown for the specified gun item. This stores the time it was fired and the rate
     * of the weapon to determine when it's allowed to fire again.
     *
     * @param item        a gun item
     * @param modifiedGun the modified gun get of the specified gun
     */
    public void putCooldown(ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        int rate = GunEnchantmentHelper.getRate(weapon, modifiedGun);
        rate = GunModifierHelper.getModifiedRate(weapon, rate);
        this.cooldownMap.put(item, Pair.of(Util.getMillis(), rate * 50));
    }

    /**
     * Checks if the specified item has an active cooldown. If a cooldown is active, it means that
     * the weapon can not be fired until it has finished. This method provides leeway as sometimes a
     * weapon is ready to fire but the cooldown is not completely finished, rather it's in the last
     * 50 milliseconds or 1 game tick.
     *
     * @param item a gun item
     * @return if the specified gun item has an active cooldown
     */
    public boolean hasCooldown(GunItem item)
    {
        Pair<Long, Integer> pair = this.cooldownMap.get(item);
        if(pair != null)
        {
            /* Give a 50 millisecond leeway as most of the time the cooldown has finished, just not exactly to the millisecond */
            return Util.getMillis() - pair.getLeft() < pair.getRight() - 50;
        }
        return false;
    }

    /**
     * Gets the remaining milliseconds before the weapon is allowed to shoot again. This doesn't
     * take into account the leeway given in {@link #hasCooldown(GunItem)}.
     *
     * @param item a gun item
     * @return the remaining time in milliseconds
     */
    public long getRemaining(GunItem item)
    {
        Pair<Long, Integer> pair = this.cooldownMap.get(item);
        if(pair != null)
        {
            return pair.getRight() - (Util.getMillis() - pair.getLeft());
        }
        return 0;
    }
}
