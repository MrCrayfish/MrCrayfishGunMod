package com.mrcrayfish.guns.item;

import com.mrcrayfish.guns.entity.EntityThrowableGrenade;
import com.mrcrayfish.guns.entity.EntityThrowableStunGrenade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
public class ItemStunGrenade extends ItemGrenade
{
    public ItemStunGrenade(ResourceLocation id)
    {
        super(id);
    }

    @Override
    public EntityThrowableGrenade create(World world, EntityPlayer player)
    {
        return new EntityThrowableStunGrenade(world, player);
    }
}
