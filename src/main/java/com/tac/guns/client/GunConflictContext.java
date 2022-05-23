package com.tac.guns.client;

import com.tac.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public enum GunConflictContext implements IKeyConflictContext
{
    IN_GAME_HOLDING_WEAPON
    {
        @Override
        public boolean isActive()
        {
            return !KeyConflictContext.GUI.isActive() && Minecraft.getInstance().player != null && Minecraft.getInstance().player.getHeldItemMainhand().getItem() instanceof GunItem;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return this == other;
        }
    }
}
