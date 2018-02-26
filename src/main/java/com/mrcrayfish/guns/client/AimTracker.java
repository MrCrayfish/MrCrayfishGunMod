package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.event.CommonEvents;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Author: MrCrayfish
 */
public class AimTracker
{
    private static final double MAX_AIM = 14;

    private int currentAim;
    private int previousAim;

    public void handleAiming(EntityPlayer player)
    {
        previousAim = currentAim;
        if(player.getDataManager().get(CommonEvents.AIMING))
        {
            if(currentAim < MAX_AIM)
            {
                ++currentAim;
            }
        }
        else
        {
            if(currentAim > 0)
            {
                currentAim--;
            }
        }
    }

    public boolean isAiming()
    {
        return currentAim != 0 || previousAim != 0;
    }

    public float getNormalProgress(float partialTicks)
    {
        System.out.println((previousAim + (currentAim - previousAim) * (previousAim == 0 || previousAim == MAX_AIM ? 0 : partialTicks)) / (float) MAX_AIM);
        return (previousAim + (currentAim - previousAim) * (previousAim == 0 || previousAim == MAX_AIM ? 0 : partialTicks)) / (float) MAX_AIM;
    }
}
