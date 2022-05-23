package com.tac.guns.item.TransitionalTypes.grenades;

import com.tac.guns.entity.ThrowableGrenadeEntity;
import com.tac.guns.entity.specifics.BaseballGrenadeEntity;
import com.tac.guns.entity.specifics.LightGrenadeEntity;
import com.tac.guns.item.GrenadeItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class BaseballGrenadeItem extends GrenadeItem
{
    private float power;
    public BaseballGrenadeItem(Properties properties, int maxCookTime, float power, float speed)
    {
        super(properties, maxCookTime, power, speed);
        this.power = power;
    }

    public ThrowableGrenadeEntity create(World world, LivingEntity entity, int timeLeft)
    {
        return new BaseballGrenadeEntity(world, entity, timeLeft, this.power); // Current ThrowableGrenadeEntity is perfect for impact 1/31/2022
    }

    public boolean canCook()
    {
        return true;
    }

    protected void onThrown(World world, ThrowableGrenadeEntity entity)
    {
    }
}
