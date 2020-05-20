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
    public StunGrenadeItem(Item.Properties properties)
    {
        super(properties);
    }

    @Override
    public EntityThrowableGrenade create(World world, PlayerEntity player)
    {
        return new EntityThrowableStunGrenade(world, player);
    }
}
