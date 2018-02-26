package com.mrcrayfish.guns.event;

import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MrCrayfish
 */
public class CommonEvents
{
    public static final DataParameter<Boolean> AIMING = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BOOLEAN);

    @SubscribeEvent
    public void onPlayerInit(EntityLivingInitEvent event)
    {
        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            event.getEntityLiving().getDataManager().register(AIMING, false);
        }
    }
}
