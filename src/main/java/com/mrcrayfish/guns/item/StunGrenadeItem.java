package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.EntityThrowableGrenade;
import com.mrcrayfish.guns.entity.EntityThrowableStunGrenade;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class StunGrenadeItem extends GrenadeItem
{
    public StunGrenadeItem(Item.Properties properties, int maxCookTime)
    {
        super(properties, maxCookTime);
    }

    @Override
    public EntityThrowableGrenade create(World world, PlayerEntity player, int timeLeft)
    {
        return new EntityThrowableStunGrenade(world, player, this.maxCookTime);
    }

    @Override
    public boolean shouldRenderIndicator()
    {
        return false;
    }
}
