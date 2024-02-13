package com.mrcrayfish.guns.client;

import com.mrcrayfish.controllable.client.binding.IBindingContext;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * Author: MrCrayfish
 */
public enum GunConflictContext implements IBindingContext
{
    IN_GAME_HOLDING_WEAPON
    {
        @Override
        public boolean isActive()
        {
            return !KeyConflictContext.GUI.isActive() && Minecraft.getInstance().player != null && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof GunItem;
        }

        @Override
        public boolean conflicts(IBindingContext other)
        {
            return this == other;
        }
    }
}
