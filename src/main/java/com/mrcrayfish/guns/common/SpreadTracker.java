package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.item.ItemGun;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class SpreadTracker
{
    private final Map<ItemGun, Pair<MutableLong, MutableInt>> SPREAD_TRACKER_MAP = new HashMap<>();

    public void update(ItemGun item)
    {
        Pair<MutableLong, MutableInt> entry = SPREAD_TRACKER_MAP.computeIfAbsent(item, gun -> Pair.of(new MutableLong(-1), new MutableInt()));
        MutableLong lastFire = entry.getLeft();
        if(lastFire.getValue() != -1)
        {
            MutableInt spreadCount = entry.getRight();
            long deltaTime = System.currentTimeMillis() - lastFire.getValue();
            if(deltaTime < 300) //Make config option
            {
                if(spreadCount.getValue() < 10) //Make config option
                {
                    spreadCount.increment();
                }
            }
            else
            {
                spreadCount.setValue(0);
            }
        }
        lastFire.setValue(System.currentTimeMillis());
    }

    public float getSpread(ItemGun item)
    {
        Pair<MutableLong, MutableInt> entry = SPREAD_TRACKER_MAP.get(item);
        if(entry != null)
        {
            return entry.getRight().getValue() / 10F;
        }
        return 0F;
    }
}
