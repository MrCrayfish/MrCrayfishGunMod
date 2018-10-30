package com.mrcrayfish.guns.event;

import com.mrcrayfish.guns.item.ItemGun;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Author: MrCrayfish
 */
public class GunFireEvent extends Event
{
    private EntityPlayer player;
    private ItemGun item;

    public GunFireEvent(EntityPlayer player, ItemGun item)
    {
        this.player = player;
        this.item = item;
    }

    public EntityPlayer getPlayer()
    {
        return player;
    }

    public ItemGun getItem()
    {
        return item;
    }
}
