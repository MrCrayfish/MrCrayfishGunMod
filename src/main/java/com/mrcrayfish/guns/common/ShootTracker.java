package com.mrcrayfish.guns.common;

import com.google.common.collect.Maps;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.object.Gun;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.mrcrayfish.guns.util.GunModifierHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

/**
 * A simple class to track and control weapon cooldowns
 * <p>
 * Author: MrCrayfish
 */
public class ShootTracker
{
    private final Map<Item, Pair<Long, Integer>> cooldownMap = Maps.newHashMap();

    /**
     * Puts a cooldown for the specified gun item. This stores the time it was fired and the rate
     * of the weapon to determine when it's allowed to fire again.
     *
     * @param item        a gun item
     * @param modifiedGun the modified gun instance of the specified gun
     */
    public void putCooldown(ItemStack weapon, GunItem item, Gun modifiedGun)
    {
        int rate = GunEnchantmentHelper.getRate(weapon, modifiedGun);
        rate = GunModifierHelper.getModifiedRate(weapon, rate);
        this.cooldownMap.put(item, Pair.of(Util.milliTime(), rate * 50));
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
            return Util.milliTime() - pair.getLeft() < pair.getRight() - 50;
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
            return pair.getRight() - (Util.milliTime() - pair.getLeft());
        }
        return 0;
    }
}
